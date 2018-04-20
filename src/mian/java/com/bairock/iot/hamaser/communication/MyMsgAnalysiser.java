package com.bairock.iot.hamaser.communication;

import com.bairock.iot.intelDev.communication.MessageAnalysiser;
import com.bairock.iot.intelDev.device.Device;

public class MyMsgAnalysiser extends MessageAnalysiser {

	@Override
	public void deviceFeedback(Device device, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unKnowMsg(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void allMessageEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void singleMessageEnd(Device device, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configDevice(Device device, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configDeviceCtrlModel(Device device, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unKnowDev(Device device, String msg) {
		// TODO Auto-generated method stub
		
	}

}
