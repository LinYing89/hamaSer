package com.bairock.iot.hamaser.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bairock.iot.hamaser.dao.RegisterFormBean;
import com.bairock.iot.hamaser.dao.UserDao;
import com.bairock.iot.intelDev.user.User;

public class RegisterServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");

		RegisterFormBean formbean = new RegisterFormBean();
		String name = request.getParameter("name");
		String psd = request.getParameter("psd");
		String confirmPsd = request.getParameter("confirmPsd");
		formbean.setName(name);
		formbean.setPsd(psd);
		formbean.setConfirmPsd(confirmPsd);
		if (formbean.validate() == false) {
			request.setAttribute("formbean", formbean);
			// response.sendRedirect(request.getContextPath() + "/register.jsp");
			request.getRequestDispatcher("/register.jsp").forward(request, response);
			return;
		}
		try {
			UserDao ud = new UserDao();
			if (ud.isHaveByUserName(name)) {
				registerFail("Ãû³ÆÖØ¸´", formbean, request, response);
//				formbean.getErrors().put("name", "ç”¨æˆ·åå·²å­˜åœ¨");
//				request.setAttribute("formbean", formbean);
//				request.getRequestDispatcher("/register.jsp").forward(request, response);
				// response.sendRedirect(request.getContextPath() + "/register.jsp");
				return;
			}
			User user = new User();
			user.setName(name);
			user.setPsd(psd);
			user.setRegisterTime(new Date());
			boolean res = ud.add(user);
			if (res) {
				String message = String.format("×¢²á³É¹¦,3Ãëºó×Ô¶¯Ìø×ªµ½µÇÂ¼Ò³Ãæ<meta http-equiv='refresh' content='3;url=%s'/>",
						request.getContextPath() + "/login.jsp");
				request.setAttribute("message", message);
				request.getRequestDispatcher("/message.jsp").forward(request, response);
				// response.sendRedirect(request.getContextPath() + "/message.jsp");
			} else {
				registerFail("×¢²áÊ§°Ü", formbean, request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			registerFail("×¢²áÊ§°Ü", formbean, request, response);
			// formbean.getErrors().put("name", "æ³¨å†Œå¤±è´¥");
			// request.setAttribute("formbean", formbean);
			// request.getRequestDispatcher("/register.jsp").forward(request, response);
			// response.sendRedirect(request.getContextPath() + "/register.jsp");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private void registerFail(String info, RegisterFormBean formbean, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		formbean.getErrors().put("name", info);
		request.setAttribute("formbean", formbean);
		request.getRequestDispatcher("/register.jsp").forward(request, response);
	}

}
