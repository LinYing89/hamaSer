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
@WebServlet(description = "download guagua app for android", urlPatterns = { "/DownloadApp" })
public class DownloadApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<AppVersion> listAppVersion = new AppVesionDao().findApps();
		request.setAttribute("listApp", listAppVersion);
		request.getRequestDispatcher("/page/download.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
