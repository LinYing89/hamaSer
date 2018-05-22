package com.bairock.iot.hamaser.servlet;

import java.io.IOException;
import java.security.MessageDigest;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bairock.iot.hamaser.dao.UserDao;
import com.bairock.iot.hamaser.listener.SessionHelper;
import com.bairock.iot.intelDev.user.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(description = "login", urlPatterns = { "/Login" })
public class LoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String KEY = ":cookie@lygzb.com";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String username = "";
		String password = "";
		String ctrlable = "true";
		String forward = (String) request.getAttribute("forward");
		if ((null != forward && !forward.isEmpty()) && forward.equals("forward")) {
			// 
			username = (String) request.getAttribute("name");
			password = "auto";
		} else {
			username = request.getParameter("name");
			password = request.getParameter("psd");
			if (null == password || password.isEmpty()) {
				password = "a123456";
				ctrlable = "false";
			} else if (null != username && !username.isEmpty()) {
				ctrlable = "true";
				Cookie userCookie = new Cookie("user", username);
				userCookie.setMaxAge(Integer.MAX_VALUE);
				userCookie.setPath(request.getContextPath());
				response.addCookie(userCookie);
			}
			if (null != ctrlable) {
				request.getSession().setAttribute("ctrlable", ctrlable);
			}
		}
		String strAutoLogin = request.getParameter("autoLogin");
		User user = null;
		UserDao ud = new UserDao();
		
		EntityManager em = SessionHelper.getEntityManager(username);
		request.getSession().setAttribute(SessionHelper.ENTITY_MANAGER, em);
		
		user = ud.findByNameAndPsd(username, password);

		if (null == user) {
			request.setAttribute("error", "用户名错误");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}

		request.setAttribute("name", username);
		boolean autoLogin = false;
		if (null != strAutoLogin && !strAutoLogin.isEmpty()) {
			autoLogin = "on".equalsIgnoreCase(strAutoLogin);
		}
		if (autoLogin) {
			// 娣诲姞cookie
			// Cookie userCookie = new Cookie("user", username);
			// userCookie.setMaxAge(3600);
			// userCookie.setPath(request.getContextPath());
			// response.addCookie(userCookie);
			String ssid = calcMD5(username + KEY);
			Cookie ssidCookie = new Cookie("ssid", ssid);
			ssidCookie.setMaxAge(Integer.MAX_VALUE);
			ssidCookie.setPath(request.getContextPath());
			response.addCookie(ssidCookie);
		}
		User sessionuser = SessionHelper.getUser(username);
		if (null == sessionuser) {
			sessionuser = user;
		}
		if (null != sessionuser) {
			request.getSession().setAttribute(SessionHelper.USER, sessionuser);
			request.getSession().setAttribute(SessionHelper.USER_NAME, username);
			response.sendRedirect(request.getContextPath() + "/groupList.jsp");
			// request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
	}

	/**
	 * MD5鍔犲瘑绠楁硶
	 * 
	 * @param ss
	 * @return
	 */
	public static String calcMD5(String ss) {
		String s = ss == null ? "" : ss;
		// 瀛楀吀
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			// 鑾峰彇MD5
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			// 鏇存柊鏁版嵁
			mdTemp.update(strTemp);
			// 鍔犲瘑
			byte[] md = mdTemp.digest();
			int j = md.length;
			// 鏂板瓧绗︿覆鏁扮粍
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
