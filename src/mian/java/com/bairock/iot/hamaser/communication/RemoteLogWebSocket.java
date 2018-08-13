package com.bairock.iot.hamaser.communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/remoteLog")
public class RemoteLogWebSocket {

	public static String filter = "";
	
	private static List<RemoteLogWebSocket> listWebSocket = new ArrayList<>();
	
	private Session session;
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		this.session = session;
		listWebSocket.add(this);
	}

	@OnClose
	public void onClose() {
		closeWebSocket();
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		filter = message;
	}

	@OnError
	public void onError(Session session, Throwable thr) {
		closeWebSocket();
	}

	private void closeWebSocket() {
		listWebSocket.remove(this);
		session = null;
	}

	public void sendMessage(String message) {
		if (null != session) {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void sendMessageToAll(String message) {
		if(!message.contains("H")) {
			//心跳信息不过滤
			if(!filter.isEmpty()) {
				//通过过滤器过滤信息
				if(!message.contains(filter)) {
					return;
				}
			}
		}
		
		for(RemoteLogWebSocket rw : listWebSocket) {
			rw.sendMessage(message);
		}
	}
}
