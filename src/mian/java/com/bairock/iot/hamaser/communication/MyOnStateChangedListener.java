package com.bairock.iot.hamaser.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bairock.iot.intelDev.communication.RefreshCollectorValueHelper;
import com.bairock.iot.intelDev.device.CtrlModel;
import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.device.Device.OnStateChangedListener;
import com.bairock.iot.intelDev.device.IStateDev;
import com.bairock.iot.intelDev.device.devcollect.DevCollect;
import com.bairock.iot.intelDev.device.devcollect.DevCollectClimateContainer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyOnStateChangedListener implements OnStateChangedListener {

	@Override
	public void onStateChanged(Device dev, String stateId) {
		refreshUi(dev);
	}

	@Override
	public void onNormalToAbnormal(Device dev) {
		if(dev instanceof DevCollectClimateContainer){
            RefreshCollectorValueHelper.getIns().endRefresh(dev);
        }
//		if(dev instanceof DevCollect || dev instanceof DevCollectSignalContainer){
//            RefreshCollectorValueHelper.getIns().endRefresh(dev);
//        }
	}

	@Override
	public void onAbnormalToNormal(Device dev) {
		
		if(dev instanceof DevCollectClimateContainer){
            RefreshCollectorValueHelper.getIns().RefreshDev(dev);
        }
		
//		boolean canAdd = false;
//		if(dev instanceof DevCollectSignalContainer){
//            canAdd = true;
//        }else if(dev instanceof DevCollect){
//            if(dev.getParent() == null){
//                canAdd = true;
//            }else if(!(dev.getParent() instanceof DevCollectSignalContainer)){
//                canAdd = true;
//            }
//        }
//        if(canAdd){
//            RefreshCollectorValueHelper.getIns().RefreshDev(dev);
//        }
	}

	private void refreshUi(Device device) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Device superParent = device.findSuperParent();
			List<GroupWebSocket> listMyGroup = GroupWebSocketHelper
					.getMyListGroupWebSocket(superParent.getDevGroup().getUser().getName(), superParent.getDevGroup().getName());
			if (!device.isNormal() && device.getCtrlModel() == CtrlModel.REMOTE) {
				String order = device.createAbnormalOrder();
				PadChannelBridgeHelper.getIns().sendOrder(superParent.getDevGroup().getUser().getName(),
						superParent.getDevGroup().getName(), order);
			}
			if (device instanceof IStateDev || device instanceof DevCollect) {
				Map<String, Object> map = new HashMap<>();
				map.put("jsonId", 3);
				map.put("devCoding", device.getLongCoding());
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
