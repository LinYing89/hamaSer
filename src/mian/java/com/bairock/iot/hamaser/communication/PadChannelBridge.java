package com.bairock.iot.hamaser.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bairock.iot.hamaser.dao.DevGroupDao;
import com.bairock.iot.hamaser.dao.DeviceDao;
import com.bairock.iot.hamaser.dao.UserDao;
import com.bairock.iot.hamaser.listener.SessionHelper;
import com.bairock.iot.intelDev.communication.DevChannelBridgeHelper;
import com.bairock.iot.intelDev.device.CtrlModel;
import com.bairock.iot.intelDev.device.DevStateHelper;
import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.device.Gear;
import com.bairock.iot.intelDev.device.OrderHelper;
import com.bairock.iot.intelDev.device.devcollect.DevCollect;
import com.bairock.iot.intelDev.device.devcollect.Pressure;
import com.bairock.iot.intelDev.device.devswitch.DevSwitch;
import com.bairock.iot.intelDev.user.DevGroup;
import com.bairock.iot.intelDev.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class PadChannelBridge {

	public static ChannelGroup channelGroup = new DefaultChannelGroup("client", GlobalEventExecutor.INSTANCE);

	private Channel channel;

	private String userName;
	private String groupName;
	private String channelId;
	// the channel have no response count,0 if have response
	private int noReponse;

	private StringBuilder sbReceived = new StringBuilder();
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public void channelReceived(String msg) {
		noReponse = 0;
		sbReceived.append(msg);
		if(judgeMsgFormate(sbReceived.toString())){
			displayMsg(sbReceived.toString());
			sbReceived.setLength(0);
		}else if(sbReceived.length() >= 5000) {
			sbReceived.setLength(0);
		}
	}
	
	public boolean judgeMsgFormate(String msg) {
		boolean formatOk = false;
		int len = msg.length();
		if (len < 3 || 
				(!msg.endsWith("#") &&!(msg.substring(len - 3, len - 2)).equals(OrderHelper.SUFFIX))) {
			formatOk = false;
		} else {
			formatOk = true;
		}
		return formatOk;
	}

	public String getHeart() {
		String heart;
		//
		if (userName == null || groupName == null) {
			//涓嶇煡閬撶敤鎴峰悕鎴栫粍鍚�
			heart = OrderHelper.getOrderMsg("h2");
		} else {
			if(haveWebPage()) {
				//鍚屾璁惧淇℃伅
				heart = OrderHelper.getOrderMsg("h1");
			}else {
				//涓嶅悓姝ヨ澶囦俊鎭�
				heart = OrderHelper.getOrderMsg("h3");
			}
		}
		return heart;
	}
	
	public void sendHeart() {
		sendMessage(getHeart());
	}
	
	private void displayMsg(String msg) {
		//System.out.println("PadChannelBridge displayMsg " + msg);
		try {
			if (null == msg) {
				return;
			}
			String[] arryMsg = msg.split("\\$");
			for (String str : arryMsg) {
				if (!str.isEmpty()) {
					analysisMsg(str);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void analysisMsg(String msg) {
		if (msg.startsWith("UN")) {
			// 鐢ㄦ埛鍚嶇粍鍚�
			if (msg.length() <= 3) {
				return;
			}
			String userMsg = msg.substring(2, msg.indexOf("#"));
			String[] arryMsg = userMsg.split(":");
			if (arryMsg.length != 2) {
				return;
			}
			userName = arryMsg[0];
			groupName = arryMsg[1];
		} else if (msg.startsWith("S")) {
			// 璁剧疆鍛戒护锛屽锛歋B10001:a1:ip,port#00
			int index = msg.indexOf(":");
			if (index < 0 || index + 3 > msg.length()) {
				return;
			}
			String type = msg.substring(index + 1, index + 2);
			if (type.equals("a")) {
				// 璁剧疆璁惧妯″紡
				String coding = msg.substring(1, index);
				String model = msg.substring(index + 2, index + 3);
				String ip = msg.substring(index + 4, msg.indexOf("#"));
				System.out.println("msg:" + msg);
				System.out.println("ip:" + ip);
				// 璁剧疆璁惧妯″紡
				if(model.equals("1")) {
					ObjectMapper mapper = new ObjectMapper();
					try {
						String json = ip.substring(ip.indexOf(":") + 1);
						System.out.println("json:" + json);
						User user = mapper.readValue(json, User.class);
						Device device1 = user.getListDevGroup().get(0).getListDevice().get(0);
						UserDao userDao = new UserDao();
						User userDb = userDao.findUserInit(user.getName(), user.getListDevGroup().get(0).getName());
						DevGroup groupDb = userDb.getListDevGroup().get(0);
						Device devDb = groupDb.findDeviceWithCoding(device1.getCoding());
						if(devDb == null) {
							groupDb.addDevice(device1);
							DevGroupDao dd = new DevGroupDao();
							dd.update(groupDb);
						}else {
							Device.copyDeviceExceptId(devDb, device1);
							DeviceDao devDao = new DeviceDao();
							devDao.update(devDb);
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
					sendMessage(OrderHelper.getOrderMsg("I" + coding + ":a1"));
				}else {
					DevChannelBridgeHelper.getIns().sendDevOrder(coding, "$" + msg);
				}
			}
		} else if (msg.startsWith("C")) {
			if (!msg.contains("#") || !msg.contains(":")) {
				return;
			}
			String cutMsg = msg.substring(1, msg.indexOf("#"));
			int index = cutMsg.indexOf(":") + 1;
			String coding = cutMsg.substring(0, index - 1);
			// 鎺у埗鍛戒护鍙戦�佺粰杩滅▼妯″紡鐨勮澶�
			DevChannelBridgeHelper.getIns().sendDevOrder(coding, "$" + msg, this.userName, this.groupName);
		}  else if (msg.startsWith("ogr")) {
			// TODO
		} else if (msg.startsWith("ogs")) {
			// TODO
		} else {
			// like IB30006:707#5C
			if (msg.startsWith("I")) {
				if (userName != null && groupName != null) {
					sendToOtherClient(msg);
				}
				analysisIMsg(msg);
			}
		}
	}

	private void analysisIMsg(String msg) {
		if (!msg.contains("#")) {
			return;
		}
		
		String cutMsg = msg.substring(1, msg.indexOf("#"));
		int index = cutMsg.indexOf(":");
		DevGroup group = SessionHelper.getDevGroup(userName, groupName);
		if (null != group) {
			String coding = cutMsg.substring(0, index);
			if(coding.contains("_")) {
				//鏄瓙璁惧缂栫爜
				coding = coding.substring(0, coding.indexOf("_"));
			}
			Device dev = group.findDeviceWithCoding(coding);
			if(null == dev) {
				return;
			}
			setDevCtrlModel(dev);
			String state = cutMsg.substring(index + 1);
			if (state.startsWith("9")) {
				// 鎸′綅
				if(!(dev instanceof DevSwitch)) {
					//鍙湁寮�鍏崇被璁惧鏈夋尅浣�
					return;
				}
				String devSubCode = cutMsg.substring(cutMsg.lastIndexOf("_") + 1, index);
				String stateHead = cutMsg.substring(index + 2);
				Device subDev = ((DevSwitch)dev).getSubDevBySc(devSubCode);
				if(null == subDev) {
					return;
				}
				subDev.setGear(Enum.valueOf(Gear.class, stateHead));
				//sendDeviceGear(subDev, userName, groupName);
			} else if (state.startsWith("2")) {
				// 鏄惁姝ｅ父
				dev.setDevStateId(DevStateHelper.DS_YI_CHANG);
				//sendNormalMessage("1", dev, userName, groupName, false);
			} else {
				// 璁惧鐘舵��
				dev.handle(state);
				//sendAffectMessage("1", dev, userName, groupName);
			}
		}
	}

	private void setDevCtrlModel(Device device) {
		if (null == device) {
			return;
		}
		if (device.getCtrlModel() != CtrlModel.LOCAL) {
			// 浠巔ad鍙戣繃鏉ョ殑涓�瀹氭槸鏈湴妯″紡
			device.setCtrlModel(CtrlModel.LOCAL);
			//sendDeviceCtrlModel(device, userName, groupName);
		}
	}
	
	public Channel getChannel() {
		if (null == channel) {
			if (null == channelId) {
				return null;
			}

			for (Channel c : channelGroup) {
				if (c.id().asShortText().equals(channelId)) {
					channel = c;
					return channel;
				}
			}
		}
		return channel;
	}

	public static void sendDeviceGear(String msg, String userName, String groupName) {
		List<GroupWebSocket> listMyGroup = GroupWebSocketHelper.getMyListGroupWebSocket(userName, groupName);
		for (GroupWebSocket gws : listMyGroup) {
			sendGroupMessage(msg, gws);
		}
	}

	public static void sendDeviceGear(Device dev, String userName, String groupName) {
		List<GroupWebSocket> listMyGroup = GroupWebSocketHelper.getMyListGroupWebSocket(userName, groupName);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		map.put("jsonId", 5);
		map.put("devCoding", dev.getCoding());
		map.put("gear", dev.getGear().toString());
		try {
			String json = mapper.writeValueAsString(map);
			if (null != json) {
				for (GroupWebSocket gws : listMyGroup) {
					sendGroupMessage(json, gws);
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendDeviceCtrlModel(Device dev, String userName, String groupName) {
		List<GroupWebSocket> listMyGroup = GroupWebSocketHelper.getMyListGroupWebSocket(userName, groupName);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		map.put("jsonId", 4);
		map.put("devCoding", dev.getCoding());
		map.put("ctrlModel", dev.getCtrlModel().toString());
		try {
			String json = mapper.writeValueAsString(map);
			if (null != json) {
				for (GroupWebSocket gws : listMyGroup) {
					sendGroupMessage(json, gws);
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public static void sendAffectMessage(String source, Device dev, String userName, String groupName) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<GroupWebSocket> listMyGroup = GroupWebSocketHelper.getMyListGroupWebSocket(userName, groupName);
			if (dev instanceof DevSwitch) {
				for (Device dev1 : ((DevSwitch) dev).getListDev()) {
					Map<String, Object> map = new HashMap<>();
					map.put("jsonId", 3);
					map.put("devCoding", dev1.getCoding());
					map.put("state", dev1.getDevState());
					String json = mapper.writeValueAsString(map);
					if (null != json) {
						for (GroupWebSocket gws : listMyGroup) {
							sendGroupMessage(json, gws);
						}
					}
				}
			} else if (dev instanceof DevCollect) {
				if (dev instanceof Pressure) {
					Pressure p = (Pressure) dev;
					Map<String, Object> map = new HashMap<>();
					map.put("jsonId", 6);
					map.put("devCoding", dev.getCoding());
					map.put("value", p.getCollectProperty().getPercent());
					String json = mapper.writeValueAsString(map);
					if (null != json) {
						for (GroupWebSocket gws : listMyGroup) {
							sendGroupMessage(json, gws);
						}
					}
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public static void sendNormalMessage(String source, Device dev, String userName, String groupName, boolean normal) {
		if (dev instanceof DevSwitch) {
			for (Device dev1 : ((DevSwitch) dev).getListDev()) {
				sendDevNormalGroupMsg(dev1, userName, groupName);
			}
		} else if (dev instanceof DevCollect) {
			if (dev instanceof Pressure) {
				sendDevNormalGroupMsg(dev, userName, groupName);
			}
		}
	}

	private static void sendDevNormalGroupMsg(Device dev, String userName, String groupName) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		map.put("jsonId", 3);
		map.put("devCoding", dev.getCoding());
		map.put("state", "4");
		try {
			String json = mapper.writeValueAsString(map);
			if (null != json) {
				sendGroupMessage(json, userName, groupName);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public static void sendGroupMessage(String msg, String userName, String groupName) {
		List<GroupWebSocket> listMyGroup = GroupWebSocketHelper.getMyListGroupWebSocket(userName, groupName);
		for (GroupWebSocket gws : listMyGroup) {
			sendGroupMessage(msg, gws);
		}
	}

	public static void sendGroupMessage(String msg, GroupWebSocket groupWebSocket) {
		if (null != groupWebSocket) {
			groupWebSocket.sendMessage(msg);
		}
	}

	private boolean haveWebPage() {
		List<GroupWebSocket> listMyGroup = GroupWebSocketHelper.getMyListGroupWebSocket(userName, groupName);
		if (listMyGroup.isEmpty()) {
			return false;
		}
		return true;
	}

	public void sendToAllClient(String msg) {
		for (PadChannelBridge pcb : PadChannelBridgeHelper.getIns().getListPadChannelBridge(userName, groupName)) {
			pcb.sendMessage("$" + msg);
		}
	}

	public void sendToOtherClient(String msg) {
		for (PadChannelBridge pcb : PadChannelBridgeHelper.getIns().getListPadChannelBridge(userName, groupName)) {
			if (pcb != this) {
				pcb.sendMessageNotReponse("$" + msg);
			}
		}
	}

	public void sendMessage(String msg) {
		System.out.println("PadChannelBridge sendMessage " + msg);
		if (noReponse > 2) {
			channel.close();
			PadChannelBridgeHelper.getIns().removeBridge(this);
		}else {
			noReponse++;
			getChannel().writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
		}
	}
	
	public void sendMessageNotReponse(String msg) {
		System.out.println("PadChannelBridge sendMessageNotReponse " + msg);
	    getChannel().writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
	}
}
