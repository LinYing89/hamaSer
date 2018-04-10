package com.bairock.iot.hamaser.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bairock.iot.hamaser.dao.DevGroupDao;
import com.bairock.iot.hamaser.listener.SessionHelper;
import com.bairock.iot.intelDev.user.DevGroup;
import com.bairock.iot.intelDev.user.User;

public class DeleteGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupName = request.getParameter(SessionHelper.DEV_GROUP_NAME);
		User manager = (User)request.getSession().getAttribute(SessionHelper.USER);
		DevGroup group = manager.findDevGroupByName(groupName);
		if(null != group) {
			manager.removeGroup(group);
			DevGroupDao dao = new DevGroupDao();
			if(dao.delete(manager.getName(), group)) {
				
			}
		}
		response.sendRedirect(request.getContextPath() + "/groupList.jsp");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
