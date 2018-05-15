package com.bairock.iot.hamaser.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bairock.iot.hamaser.communication.PadServer;
import com.bairock.iot.hamaser.dao.UserDao;
import com.bairock.iot.intelDev.communication.DevServer;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		if(null == res) {
			map.put("stateCode", 0);
		}else {
			map.put("stateCode", 200);
		}
		map.put("petName", res);
		map.put("padPort", PadServer.PORT);
		map.put("devPort", DevServer.PORT);
		String json = mapper.writeValueAsString(map);
		if(json != null) {
			out.println(json);
			out.flush();
			out.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
