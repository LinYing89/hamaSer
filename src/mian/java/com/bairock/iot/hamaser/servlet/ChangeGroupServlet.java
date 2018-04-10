package com.bairock.iot.hamaser.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bairock.iot.hamaser.communication.MyOnCtrlModelChangedListener;
import com.bairock.iot.hamaser.communication.MyOnGearChangedListener;
import com.bairock.iot.hamaser.communication.MyOnStateChangedListener;
import com.bairock.iot.hamaser.dao.DevGroupDao;
import com.bairock.iot.hamaser.listener.SessionHelper;
import com.bairock.iot.intelDev.user.DevGroup;
import com.bairock.iot.intelDev.user.User;

/**
 * Servlet implementation class ChangeGroupServlet
 */
public class ChangeGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupName = request.getParameter(SessionHelper.DEV_GROUP_NAME);
		System.out.println("ChangeGroupServlet groupName " + groupName);
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		User manager = (User)request.getSession().getAttribute(SessionHelper.USER);
		if(null == manager) {
			return;
		}
		DevGroup group = manager.findDevGroupByName(groupName);
		
		if(null != group){
			MyOnStateChangedListener l1 = new MyOnStateChangedListener();
			MyOnGearChangedListener l2 = new MyOnGearChangedListener();
			MyOnCtrlModelChangedListener l3 = new MyOnCtrlModelChangedListener();
			DevGroupDao.setDeviceListener(group, l1, l2, l3);
			request.getSession().setAttribute(SessionHelper.DEV_GROUP, group);
			response.sendRedirect(request.getContextPath() + "/group.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
