package com.bairock.iot.hamaser.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bairock.iot.hamaser.listener.StartUpListener;

/**
 * Servlet implementation class Download
 */
@WebServlet("/Download")
public class Download extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String appName = request.getParameter("appName");
		if (null == appName) {
			return;
		}

		appName = new String(appName.getBytes("ISO-8859-1"), "UTF-8");

		boolean debug = false;
		try {
			debug = Boolean.parseBoolean(request.getParameter("debug"));
		} catch (Exception e) {
		}

		String appPath = StartUpListener.getAppPath(debug) + File.separator + appName;
		System.out.println("appPath" + appPath);
		try {
			InputStream in = new FileInputStream(appPath);
			response.setContentType("application/x-download");
			// response.setContentType("application/force-download");
			// response.setContentType("application/x-msdownload");
			response.addHeader("Content-Disposition", "attachment;filename=" + appName);
			OutputStream out = response.getOutputStream();

			try {
				int len = 0;
				byte b[] = new byte[1024];
				while ((len = in.read(b)) > 0) {
					out.write(b, 0, len);
				}
			} finally {
				out.flush();
				in.close();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
