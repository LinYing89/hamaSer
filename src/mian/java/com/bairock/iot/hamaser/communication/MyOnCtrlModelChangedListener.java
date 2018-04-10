package com.bairock.iot.hamaser.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bairock.iot.intelDev.device.CtrlModel;
import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.device.Device.OnCtrlModelChangedListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyOnCtrlModelChangedListener implements OnCtrlModelChangedListener {

	@Override
	public void onCtrlModelChanged(Device dev, CtrlModel ctrlModel) {
		Device superParent = dev.findSuperParent();
		List<GroupWebSocket> listMyGroup = GroupWebSocketHelper
				.getMyListGroupWebSocket(superParent.getDevGroup().getUser().getName(), superParent.getDevGroup().getName());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		map.put("jsonId", 4);
		map.put("devCoding", dev.getCoding());
		map.put("ctrlModel", dev.getCtrlModel().toString());
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
