package com.bairock.iot.hamaser.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bairock.iot.hamaser.communication.PadChannelBridge.OnPadConnectedListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyOnPadConnectedListener implements OnPadConnectedListener {

	@Override
	public void onPadConnected(String userName, String groupName) {
		if(null == userName || null == groupName) {
			return;
		}
		List<GroupWebSocket> listMyGroup = GroupWebSocketHelper.getMyListGroupWebSocket(userName, groupName);
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		map.put("jsonId", 7);
		map.put("state", 1);
		try {
			String json = mapper.writeValueAsString(map);
			if (null != json) {
				for (GroupWebSocket gws : listMyGroup) {
					GroupWebSocketHelper.sendGroupMessage(json, gws);
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
