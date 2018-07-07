package com.bairock.iot.hamaser.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bairock.iot.hamaser.dao.UserDao;
import com.bairock.iot.hamaser.listener.SessionHelper;
import com.bairock.iot.intelDev.device.alarm.AlarmInfo;

/**
 * Servlet implementation class FindGroupAlarmInfo
 */
@WebServlet("/FindGroupAlarmInfo")
public class FindGroupAlarmInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String groupName = request.getParameter(SessionHelper.DEV_GROUP_NAME);
		String userName = request.getParameter(SessionHelper.USER_NAME);
		List<AlarmInfo> list = new UserDao().findGroupAlarmInfo(userName, groupName);
//		ObjectMapper mapper = new ObjectMapper();
//		String alarms = mapper.writeValueAsString(list);
//		request.setAttribute("alarms", alarms);
		request.setAttribute("alarms", list);
		request.getRequestDispatcher("/AlarmInfo.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
