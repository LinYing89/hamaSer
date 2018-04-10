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

public class RegisterGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RegisterGroupBean formbean = new RegisterGroupBean();
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		User manager = (User) request.getSession().getAttribute(SessionHelper.USER);
		String groupName = request.getParameter("groupName");
		String petName = request.getParameter("petName");
		System.out.println("RegisterGroupServlet petName: " + petName);
		String groupPsd = request.getParameter("groupPsd");
		String groupConfirmPsd = request.getParameter("groupConfirmPsd");
		formbean.setGroupName(groupName);
		formbean.setGroupPsd(groupPsd);
		formbean.setGroupConfirmPsd(groupConfirmPsd);
		if (formbean.validate() == false) {
			request.setAttribute("formbean", formbean);
			request.getRequestDispatcher("/page/registerGroup.jsp").forward(request, response);
			//System.out.println("RegisterServlet step2 :");
			return;
		}
		try{
			//System.out.println("RegisterServlet step3 :");
			for(DevGroup dg : manager.getListDevGroup()) {
				if(dg.getName().equals(groupName)) {
					formbean.getErrors().put("groupName", "组名已存在");
					request.setAttribute("formbean", formbean);
					request.getRequestDispatcher("/page/registerGroup.jsp").forward(request, response);
					//System.out.println("RegisterServlet step4 :");
					return;
				}
			}
			DevGroup devGroup = new DevGroup();
			devGroup.setName(groupName);
			devGroup.setPetName(petName);
			devGroup.setPsd(groupPsd);
			manager.addGroup(devGroup);
			DevGroupDao dgd = new DevGroupDao();
			if(!dgd.add(devGroup)) {
				manager.removeGroup(devGroup);
				formbean.getErrors().put("name", "注册失败");
				request.setAttribute("formbean", formbean);
				request.getRequestDispatcher("/page/registerGroup.jsp").forward(request, response);
			}
			response.sendRedirect(request.getContextPath() + "/groupList.jsp");
			//request.getRequestDispatcher("/message.jsp").forward(request, response);
		}catch (Exception e) {
			e.printStackTrace(); 
			formbean.getErrors().put("name", "注册失败");
			request.setAttribute("formbean", formbean);
			request.getRequestDispatcher("/page/registerGroup.jsp").forward(request, response);
			//response.sendRedirect(request.getContextPath() + "/register.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
