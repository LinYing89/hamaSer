package com.bairock.iot.hamaser.communication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bairock.iot.intelDev.communication.DevChannelBridge;
import com.bairock.iot.intelDev.communication.DevChannelBridgeHelper;
import com.bairock.iot.intelDev.device.DevHaveChild;
import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.device.IStateDev;
import com.bairock.iot.intelDev.device.OrderHelper;
import com.bairock.iot.intelDev.device.devcollect.DevCollect;
import com.bairock.iot.intelDev.device.devcollect.DevCollectSignalContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.StringUtils;

@ServerEndpoint(value="/websocket")
public class GroupWebSocket {

	private Session session;
	private String userName = "";
	private String groupName = "";
//	private boolean onLine = false;
//	private boolean first = true;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		this.session = session;
		GroupWebSocketHelper.addGroupWebSocket(this);
	}
	
	@OnClose
	public void onClose() {
		closeWebSocket();
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		analysisMessage(message);
	}
	
	@OnError
	public void onError(Session session, Throwable thr) {
		closeWebSocket();
	}
	
	private void closeWebSocket() {
		GroupWebSocketHelper.removeGroupWebSocket(this);
		session = null;
		userName = null;
		groupName = null;
	}
	
	public void sendMessage(String message){
		if(null != session) {
			try {
				session.getBasicRemote().sendText(message);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void analysisMessage(String message) {
		try {
			if(StringUtils.isNullOrEmpty(message)) {
				return;
			}
			ObjectMapper mapper = new ObjectMapper();
			Map<?, ?> map = mapper.readValue(message.substring(message.indexOf("{")), Map.class);
			int jsonId = (int)map.get("jsonId");
			switch(jsonId) {
			case 1:
				//user name and group name
				userName = (String)map.get("userName");
				groupName = (String)map.get("groupName");
				if(null != userName && null != groupName) {
					sendToCu(userName, groupName, OrderHelper.getOrderMsg("h1"));
					refreshState();
				}
				break;
			case 2:
				//refresh all device state
				if(null == groupName || null == userName) {
					return;
				}
				refreshState();
				break;
			case 3:
				//control order
				String coding = (String)map.get("coding");
				String num = (String)map.get("num");
				String state = (String)map.get("state");
				String order = "C" + coding + ":" + state + num;
				order = OrderHelper.getOrderMsg(order);
				
				//TODO send to remote device
				if(!state.equals("0")) {
					DevChannelBridgeHelper.getIns().sendDevOrder(coding, order, userName, groupName, true);
				}
				//send to pad always, if is remote, pad change gate, if is local, pad change gate and send order to local
				sendToCu(userName, groupName, order);
				break;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendToCu(String userName, String groupName, String order) {
		PadChannelBridgeHelper.getIns().sendOrder(userName, groupName, order);
	}
	
	private void refreshState() {
		sendToCu(userName, groupName, OrderHelper.getOrderMsg("RF"));
		for(DevChannelBridge dcb : DevChannelBridgeHelper.getIns().getListDevChannelBridge()) {
			Device dev = dcb.getDevice();
			if(null != dev && null != dev.getDevGroup() 
					&& null != dev.getDevGroup().getUser()
					&& dev.getDevGroup().getName().equals(groupName)
					&& dev.getDevGroup().getUser().getName().equals(userName)) {
				refreshDevState(dcb.getDevice());
			}
		}
	}
	
	private void refreshDevState(Device dev) {
		if(null == dev) {
			return;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = new HashMap<>();
			map.put("jsonId", 3);
			map.put("devCoding", dev.getCoding());
			map.put("state", dev.getDevState());
			String json = mapper.writeValueAsString(map);
			if (null != json) {
				sendMessage(json);
			}
			
			map = new HashMap<>();
			map.put("jsonId", 4);
			map.put("devCoding", dev.getCoding());
			map.put("ctrlModel", dev.getCtrlModel().toString());
			json = mapper.writeValueAsString(map);
			if (null != json) {
				sendMessage(json);
			}
			
			if(dev instanceof DevCollect) {
				map = new HashMap<>();
				DevCollect dc = (DevCollect)dev;
				map.put("jsonId", 6);
				map.put("devCoding", dev.getCoding());
				map.put("currentValue", dc.getCollectProperty().getCurrentValue());
				map.put("percent", dc.getCollectProperty().getPercent());
				json = mapper.writeValueAsString(map);
				if (null != json) {
					sendMessage(json);
				}
				if(dev.getParent() == null || !(dev.getParent() instanceof DevCollectSignalContainer)) {
					DevChannelBridgeHelper.getIns().sendDevOrder(dev, dev.createInitOrder(), true);
				}
			}else if(!(dev instanceof IStateDev)) {
				DevChannelBridgeHelper.getIns().sendDevOrder(dev, dev.createInitOrder(), true);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(dev instanceof DevHaveChild) {
			for(Device childDev : ((DevHaveChild)dev).getListDev()) {
				refreshDevState(childDev);
			}
		}
		
	}
//	public void isOnLine() {
//		boolean isOnLine = true;
//		if(first) {
//			first = false;
//		}else {
//			isOnLine = onLine;
//		}
//		onLine = false;
//		//for(MsgC)
//	}
}
