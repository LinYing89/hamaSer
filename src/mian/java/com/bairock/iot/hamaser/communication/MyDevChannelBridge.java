package com.bairock.iot.hamaser.communication;

import com.bairock.iot.hamaser.dao.DevGroupDao;
import com.bairock.iot.hamaser.dao.UserDao;
import com.bairock.iot.hamaser.listener.SessionHelper;
import com.bairock.iot.intelDev.communication.DevChannelBridge;
import com.bairock.iot.intelDev.device.CtrlModel;
import com.bairock.iot.intelDev.device.DevHaveChild;
import com.bairock.iot.intelDev.device.DevStateHelper;
import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.device.OrderHelper;
import com.bairock.iot.intelDev.user.DevGroup;
import com.bairock.iot.intelDev.user.User;

public class MyDevChannelBridge extends DevChannelBridge {

	private int unrecognizableCount = 0;
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
					unrecognizableCount++;
					if(unrecognizableCount >= 2) {
						unrecognizableCount = 0;
						String order = OrderHelper.SET_HEAD + coding + OrderHelper.SEPARATOR + "a3";
						order = OrderHelper.getOrderMsg(order);
						sendOrder(order);
						System.out.println("MyDevChannelBridge set to local" + order);
					}
					return;
				}
				this.userName = userName;
				this.groupName = groupName;
				
				//set device which in session to remote model
				//there not set this device = device in group, because if session is closed, the user info will not release
				Device devInGroup = findDevInGroup(coding);
				if(null != devInGroup) {
					devInGroup.setCtrlModel(CtrlModel.REMOTE);
				}
				
//				dg.addDevice(dev1);
//				user1.addGroup(dg);
				dev.setCtrlModel(CtrlModel.UNKNOW);
				dev.setDevStateId(DevStateHelper.DS_YI_CHANG);
				setDevice(dev);
				DevGroupDao.setDeviceListener(dev, new MyOnStateChangedListener(), new MyOnGearChangedListener(),
						new MyOnCtrlModelChangedListener());
				sendOrder(dev.createInitOrder());
				setDeviceToZhangChang(dev);
				if(null != state) {
					handleState(dev, state);
					PadChannelBridgeHelper.getIns().sendOrder(this.userName,this.groupName, msg);
				}
			}else if(null != coding) {
				unrecognizableCount++;
				if(unrecognizableCount >= 2) {
					unrecognizableCount = 0;
					String order = OrderHelper.SET_HEAD + coding + OrderHelper.SEPARATOR + "a3";
					order = OrderHelper.getOrderMsg(order);
					sendOrder(order);
					System.out.println("MyDevChannelBridge set to local" + order);
					return;
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
	
	private Device findDevInGroup(String devCoding) {
		Device devInGroup = null;
		DevGroup group = SessionHelper.getDevGroup(userName, groupName);
		if (null != group) {
			devInGroup = group.findDeviceWithCoding(devCoding);
		}
		return devInGroup;
	}
	
	private void setDeviceToZhangChang(Device dev) {
		dev.setDevStateId(DevStateHelper.DS_ZHENG_CHANG);
		if(dev instanceof DevHaveChild) {
			for(Device device : ((DevHaveChild)dev).getListDev()) {
				setDeviceToZhangChang(device);
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
