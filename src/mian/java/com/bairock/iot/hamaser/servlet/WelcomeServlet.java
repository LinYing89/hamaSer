package com.bairock.iot.hamaser.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bairock.iot.hamaser.dao.UserDao;

/**
 * Servlet implementation class WelcomeServlet
 */
public class WelcomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WelcomeServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean login = false;
		String username = null;
		String ssid = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user")) {
					username = cookie.getValue();
				} else if (cookie.getName().equals("ssid")) {
					ssid = cookie.getValue();
				}
			}
		}
		if (username != null && ssid != null) {
			login = ssid.equals(LoginServlet.calcMD5(username + LoginServlet.KEY));
		}
		if (login) {
			UserDao ud = new UserDao();
			String psd = ud.findPsdByName(username);
			if (null != psd) {
				request.setAttribute("forward", "forward");
				request.setAttribute("name", username);
				request.setAttribute("psd", psd);
				request.getRequestDispatcher("/servlet/LoginServlet").forward(request, response);
			} else {
				response.sendRedirect(request.getContextPath() + "/login.jsp");
			}
		} else {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
