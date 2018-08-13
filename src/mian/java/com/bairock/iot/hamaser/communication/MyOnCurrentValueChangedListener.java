package com.bairock.iot.hamaser.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.device.devcollect.CollectProperty;
import com.bairock.iot.intelDev.device.devcollect.CollectSignalSource;
import com.bairock.iot.intelDev.device.devcollect.DevCollect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyOnCurrentValueChangedListener implements CollectProperty.OnCurrentValueChangedListener {

	@Override
	public void onCurrentValueChanged(DevCollect dev, Float value) {
		Device superParent = dev.findSuperParent();
		List<GroupWebSocket> listMyGroup = GroupWebSocketHelper.getMyListGroupWebSocket(
				superParent.getDevGroup().getUser().getName(), superParent.getDevGroup().getName());

		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("jsonId", 6);
			map.put("devCoding", dev.getLongCoding());
			if(dev.getCollectProperty().getCollectSrc() == CollectSignalSource.SWITCH) {
				if(dev.getCollectProperty().getCurrentValue() != null) {
                    if (dev.getCollectProperty().getCurrentValue() == 1) {
                        map.put("currentValue", "开");
                    } else {
                        map.put("currentValue", "关");
                    }
                }
			}else {
				map.put("currentValue", dev.getCollectProperty().getValueWithSymbol());
			}
			//map.put("percent", dev.getCollectProperty().getPercent());
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
