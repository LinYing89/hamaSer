package com.bairock.iot.hamaser.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.device.Device.OnGearChangedListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bairock.iot.intelDev.device.Gear;

public class MyOnGearChangedListener implements OnGearChangedListener {

	@Override
	public void onGearChanged(Device dev, Gear gear) {
		Device superParent = dev.findSuperParent();
		List<GroupWebSocket> listMyGroup = GroupWebSocketHelper
				.getMyListGroupWebSocket(superParent.getDevGroup().getUser().getName(), superParent.getDevGroup().getName());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		map.put("jsonId", 5);
		map.put("devCoding", dev.getCoding());
		map.put("gear", dev.getGear().toString());
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
