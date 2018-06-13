package com.bairock.iot.hamaser.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bairock.iot.hamaser.dao.AppVesionDao;
import com.bairock.iot.intelDev.user.AppVersion;

/**
 * Servlet implementation class DownloadApp
 */
@WebServlet(description = "download guagua app for android", urlPatterns = { "/ShowAppList" })
public class ShowAppList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("UTF-8");
		
		boolean debug = false;
		try {
			debug = Boolean.parseBoolean(request.getParameter("debug"));
		}catch(Exception e) {}
		
		List<AppVersion> listAppVersion = new AppVesionDao().findApps(debug);
		request.setAttribute("listApp", listAppVersion);
		if(debug) {
			request.getRequestDispatcher("/downloadAppDebugList.jsp").forward(request, response);
		}else {
			request.getRequestDispatcher("/downloadAppReleaseList.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
