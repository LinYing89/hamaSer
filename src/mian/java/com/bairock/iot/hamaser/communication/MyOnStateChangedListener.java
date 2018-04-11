package com.bairock.iot.hamaser.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bairock.iot.intelDev.device.CtrlModel;
import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.device.Device.OnStateChangedListener;
import com.bairock.iot.intelDev.device.IStateDev;
import com.bairock.iot.intelDev.device.devcollect.DevCollect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyOnStateChangedListener implements OnStateChangedListener {

	@Override
	public void onStateChanged(Device dev, String stateId) {
		refreshUi(dev);
	}

	@Override
	public void onNormalToAbnormal(Device dev) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAbnormalToNormal(Device dev) {
		// TODO Auto-generated method stub

	}

	private void refreshUi(Device device) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Device superParent = device.findSuperParent();
			List<GroupWebSocket> listMyGroup = GroupWebSocketHelper
					.getMyListGroupWebSocket(superParent.getDevGroup().getUser().getName(), superParent.getDevGroup().getName());
			if (!device.isNormal() && device.getCtrlModel() == CtrlModel.REMOTE) {
				String order = device.createAbnormalOrder();
				PadChannelBridgeHelper.getIns().sendOrder(device.getDevGroup().getUser().getName(),
						device.getDevGroup().getName(), order);
			}
			if (device instanceof IStateDev || device instanceof DevCollect) {
				Map<String, Object> map = new HashMap<>();
				map.put("jsonId", 3);
				map.put("devCoding", device.getCoding());
				map.put("state", device.getDevState());
				String json = mapper.writeValueAsString(map);
				if (null != json) {
					for (GroupWebSocket gws : listMyGroup) {
						GroupWebSocketHelper.sendGroupMessage(json, gws);
					}
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onNoResponse(Device dev) {
		// TODO Auto-generated method stub
		
	}
}
