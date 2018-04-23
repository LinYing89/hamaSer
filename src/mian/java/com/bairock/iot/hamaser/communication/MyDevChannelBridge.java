package com.bairock.iot.hamaser.communication;

import com.bairock.iot.hamaser.dao.DevGroupDao;
import com.bairock.iot.hamaser.dao.UserDao;
import com.bairock.iot.hamaser.listener.SessionHelper;
import com.bairock.iot.intelDev.communication.DevChannelBridge;
import com.bairock.iot.intelDev.device.CtrlModel;
import com.bairock.iot.intelDev.device.DevHaveChild;
import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.device.OrderHelper;
import com.bairock.iot.intelDev.user.DevGroup;
import com.bairock.iot.intelDev.user.User;

public class MyDevChannelBridge extends DevChannelBridge {

	StringBuilder sb = new StringBuilder();
	
	private String userName;
	private String groupName;
	
	@Override
	public void channelReceived(String msg, User user) {
		System.out.println("MyDevChannelBridge channelReceived: " + msg);
		sb.append(msg);
		if(judgeMsgFormate(sb.toString())){
			analysisReceiveMessage(msg);
			sb.setLength(0);
		}
	}
	
	public boolean judgeMsgFormate(String msg) {
		boolean formatOk = false;
		int len = msg.length();
		if (len < 3 || !(msg.substring(len - 3, len - 2)).equals(OrderHelper.SUFFIX)) {
			formatOk = false;
		} else {
			formatOk = true;
		}
		return formatOk;
	}
	
	public void analysisReceiveMessage(String msg) {
			if (null == msg || !(msg.contains(OrderHelper.PREFIX)) || !(msg.contains(OrderHelper.SUFFIX))) {
				// MessageAnalysiser.listErrMsg.add(msg);
				return;
			}

			String[] arryMsg = msg.split("\\$");
			for (int i = 1; i < arryMsg.length; i++) {
				String data = arryMsg[i];
				analysisSingleMsg(data);
			}
	}
	
	public void analysisSingleMsg(String msg) {
		if(!msg.contains("#")) {
			return;
		}
		String cutMsg = msg.substring(0, msg.indexOf("#"));
		
		String[] msgs = cutMsg.split(":");
		if(msgs.length < 2) {
			return;
		}
		String coding = null;
		String userName = null;
		String groupName = null;
		String state = null;
		for(String str : msgs) {
			if(str.startsWith("I")) {
				coding = str.substring(1);
			}else if(str.startsWith("u")) {
				userName = str.substring(1);
			}else if(str.startsWith("g")) {
				groupName = str.substring(1);
			}else{
				state = str;
			}
		}
		if (null == getDevice()) {
			if(null != coding && null != userName && null != groupName) {
				UserDao userDao = new UserDao();
				Device dev = userDao.findDeviceByUserNameGroupNameDevCoding(userName, groupName, coding);
				if(null == dev) {
					return;
				}
				this.userName = userName;
				this.groupName = groupName;
//				User user1 = new User();
//				user1.setName(userName);
//				DevGroup dg = new DevGroup();
//				dg.setName(groupName);
//				Device dev1 = DeviceAssistent.createDeviceByCoding(coding);
//				if(null == dev1) {
//					return;
//				}
				
				//set device which in session to remote model
				DevGroup group = SessionHelper.getDevGroup(userName, groupName);
				if (null != group) {
					Device devInGroup = group.findDeviceWithCoding(coding);
					if(null != devInGroup && devInGroup.getCtrlModel() != CtrlModel.REMOTE) {
						devInGroup.setCtrlModel(CtrlModel.REMOTE);
					}
				}
				
//				dg.addDevice(dev1);
//				user1.addGroup(dg);
				dev.setCtrlModel(CtrlModel.UNKNOW);
				setDevice(dev);
				DevGroupDao.setDeviceListener(dev, new MyOnStateChangedListener(), new MyOnGearChangedListener(),
						new MyOnCtrlModelChangedListener());
				if(null != state) {
					handleState(dev, state);
					PadChannelBridgeHelper.getIns().sendOrder(this.userName,this.groupName, msg);
				}
			}

		}else {
			if(null != coding) {
				if(!getDevice().getCoding().equals(coding) && getDevice() instanceof DevHaveChild) {
					Device childDev = ((DevHaveChild)getDevice()).findDevByCoding(coding);
					if(null != childDev) {
						handleState(childDev, state);
					}
				}else {
					handleState(getDevice(), state);
				}
				PadChannelBridgeHelper.getIns().sendOrder(this.userName,this.groupName, OrderHelper.PREFIX + msg);
			}
		}
	}
	
	private void handleState(Device dev, String state) {
		if(null == dev || state == null) {
			return;
		}
		if(dev.getCtrlModel() != CtrlModel.REMOTE) {
			dev.setCtrlModel(CtrlModel.REMOTE);
		}
		System.out.println("MyDevChannelBridge " + dev.getCoding() + "_" + state);
		dev.handle(state);
	}
}
