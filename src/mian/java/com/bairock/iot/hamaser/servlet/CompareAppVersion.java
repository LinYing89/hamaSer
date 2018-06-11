package com.bairock.iot.hamaser.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bairock.iot.hamaser.dao.AppVesionDao;
import com.bairock.iot.intelDev.user.AppVersion;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class CompareAppVersion
 */
@WebServlet("/CompareAppVersion")
public class CompareAppVersion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		
		String strAppVc = request.getParameter("appVc");
		int iAppVc = Integer.parseInt(strAppVc);
		AppVersion appVersion = new AppVesionDao().findLastApp();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		if(null != appVersion && appVersion.getAppVc() > iAppVc) {
			map.put("update", true);
			map.put("appName", appVersion.getAppName());
		}else{
			map.put("update", false);
			map.put("appName", "");
		}
		String json = mapper.writeValueAsString(map);
		if (json != null) {
			PrintWriter out = response.getWriter();
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
