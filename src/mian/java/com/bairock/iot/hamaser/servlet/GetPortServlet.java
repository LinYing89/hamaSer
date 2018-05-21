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

import com.bairock.iot.hamaser.communication.PadServer;
import com.bairock.iot.hamaser.communication.UpDownloadServer;
import com.bairock.iot.intelDev.communication.DevServer;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class GetPortServlet
 */
@WebServlet(description = "get connect port", urlPatterns = { "/GetPortServlet" })
public class GetPortServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		map.put("padPort", PadServer.PORT);
		map.put("devPort", DevServer.PORT);
		map.put("upDownloadPort", UpDownloadServer.PORT);
		String json = mapper.writeValueAsString(map);
		if (json != null) {
			out.println(json);
			out.flush();
			out.close();
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
