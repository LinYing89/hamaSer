package com.bairock.iot.hamaser.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bairock.iot.hamaser.dao.UserDao;

public class ClientLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 客户端登录验证
		response.setCharacterEncoding("UTF-8");
		String userName = request.getParameter("name");
		String group = request.getParameter("group");
		String password = request.getParameter("psd");
		UserDao ud = new UserDao();
		String res = ud.findDevGroupPetName(userName, group, password);

		PrintWriter out = response.getWriter();
		String message = "";
		if (null == res) {
			message = "ERROR";
		} else {
			message = "OK:" + res;
		}
		out.println(message);
		out.flush();
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
