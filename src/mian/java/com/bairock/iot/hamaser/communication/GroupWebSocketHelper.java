package com.bairock.iot.hamaser.communication;

import java.util.ArrayList;
import java.util.List;

public class GroupWebSocketHelper {

	private static List<GroupWebSocket> listGroupWebSocket = new ArrayList<>();
	
	public static void addGroupWebSocket(GroupWebSocket groupWebSocket) {
		if(!listGroupWebSocket.contains(groupWebSocket)) {
			listGroupWebSocket.add(groupWebSocket);
		}
	}
	
	public static void removeGroupWebSocket(GroupWebSocket groupWebSocket) {
		listGroupWebSocket.remove(groupWebSocket);
	}
	
	public static List<GroupWebSocket> getMyListGroupWebSocket(String userName, String groupName){
		List<GroupWebSocket> list = new ArrayList<>();
		List<GroupWebSocket> listMyGroup = new ArrayList<>();
		list.addAll(listGroupWebSocket);
		for(GroupWebSocket gs : list) {
			if(gs.getUserName().equals(userName) && gs.getGroupName().equals(groupName)) {
				listMyGroup.add(gs);
			}
		}
		return listMyGroup;
	}
	
//	public static void refreshOnLine(String userName, String groupName) {
//		try {
//			List<GroupWebSocket> listMyGroup = getMyListGroupWebSocket(userName, groupName);
//			for(GroupWebSocket gs : listMyGroup) {
//				gs.isOnLine();
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void sendGroupMessage(String msg, GroupWebSocket groupWebSocket) {
		if (null != groupWebSocket) {
			groupWebSocket.sendMessage(msg);
		}
	}
}
