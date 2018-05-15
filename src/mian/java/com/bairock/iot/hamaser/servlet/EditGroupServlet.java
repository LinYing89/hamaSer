package com.bairock.iot.hamaser.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bairock.iot.hamaser.dao.DevGroupDao;
import com.bairock.iot.hamaser.dao.RegisterGroupBean;
import com.bairock.iot.hamaser.listener.SessionHelper;
import com.bairock.iot.intelDev.user.DevGroup;
import com.bairock.iot.intelDev.user.User;

/**
 * Servlet implementation class EditGroupServlet
 */
public class EditGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RegisterGroupBean formbean = new RegisterGroupBean();
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		User manager = (User) request.getSession().getAttribute(SessionHelper.USER);
		DevGroup selectedGroup = (DevGroup) request.getSession().getAttribute(SessionHelper.DEV_GROUP);
		String selectedGroupName = selectedGroup.getName();
		String groupName = request.getParameter(SessionHelper.DEV_GROUP_NAME);
		String petName = request.getParameter("petName");
		String groupPsd = request.getParameter("psd");
		formbean.setGroupName(groupName);
		formbean.setGroupPsd(groupPsd);
		formbean.setGroupConfirmPsd(groupPsd);
		if (formbean.validate() == false) {
			request.setAttribute("formbean", formbean);
			request.getRequestDispatcher("/page/editGroup.jsp").forward(request, response);
			return;
		}
		try{
			if(!groupName.equals(selectedGroupName)){
				for(DevGroup group : manager.getListDevGroup()) {
					if(groupName.equals(group.getName())) {
						formbean.getErrors().put("groupName", "名称重复");
						request.setAttribute("formbean", formbean);
						request.getRequestDispatcher("/page/editGroup.jsp").forward(request, response);
						return;
					}
				}
			}
			selectedGroup.setName(groupName);
			selectedGroup.setPetName(petName);
			selectedGroup.setPsd(groupPsd);
			DevGroupDao dgd = new DevGroupDao();
			dgd.update(selectedGroup);
			response.sendRedirect(request.getContextPath() + "/groupList.jsp");
		}catch(Exception e){
			e.printStackTrace(); 
			formbean.getErrors().put("groupName", "格式错误");
			request.setAttribute("formbean", formbean);
			request.getRequestDispatcher("/page/editGroup.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
