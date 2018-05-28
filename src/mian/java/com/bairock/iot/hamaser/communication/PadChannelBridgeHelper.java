package com.bairock.iot.hamaser.communication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bairock.iot.intelDev.user.IntelDevHelper;

public class PadChannelBridgeHelper {

	private static PadChannelBridgeHelper ins = new PadChannelBridgeHelper();

	private List<PadChannelBridge> listPadChannelBridge = Collections.synchronizedList(new ArrayList<>());

	private OnPadDisconnectedListener onPadDisconnectedListener;
	
	public static PadChannelBridgeHelper getIns() {
		return ins;
	}

	private PadChannelBridgeHelper() {
		IntelDevHelper.executeThread(new PadHeartThread());
	}

	public List<PadChannelBridge> getListPadChannelBridge(String userName, String groupName) {
		List<PadChannelBridge> list = new ArrayList<>();
		for (PadChannelBridge db : listPadChannelBridge) {
			if (null != db.getUserName() && null != db.getGroupName() && db.getUserName().equals(userName)
					&& db.getGroupName().equals(groupName)) {
				list.add(db);
			}
		}
		return list;
	}

	public void setOnPadDisconnectedListener(OnPadDisconnectedListener onPadDisconnectedListener) {
		this.onPadDisconnectedListener = onPadDisconnectedListener;
	}

	public void sendOrder(String userName, String groupName, String order) {
		List<PadChannelBridge> list = getListPadChannelBridge(userName, groupName);
		for (PadChannelBridge pcb : list) {
			pcb.sendMessageNotReponse(order);
		}
	}

	public void setChannelId(String channelId) {
		boolean result = false;
		List<PadChannelBridge> list = listPadChannelBridge;
		for (PadChannelBridge db : list) {
			if (null == db || db.getChannelId() == null) {
				listPadChannelBridge.remove(db);
				continue;
			}
			if (db.getChannelId().equals(channelId)) {
				result = true;
				break;
			}
		}
		if (!result) {
			addBridge(channelId);
		}
	}

	public void channelReceived(String channelId, String msg) {
		List<PadChannelBridge> list = listPadChannelBridge;
		for (PadChannelBridge db : list) {
			if (null == db || db.getChannelId() == null) {
				listPadChannelBridge.remove(db);
				continue;
			}
			if (db.getChannelId().equals(channelId)) {
				db.channelReceived(msg);
				break;
			}
		}
	}

	private void addBridge(String channelId) {
		PadChannelBridge db = new PadChannelBridge();
		db.setChannelId(channelId);
		db.setOnPadConnectedListener(new MyOnPadConnectedListener());
		listPadChannelBridge.add(db);
	}

	public void removeBridge(PadChannelBridge db) {
		listPadChannelBridge.remove(db);
		if(null != onPadDisconnectedListener && null != db) {
			onPadDisconnectedListener.onPadDisconnected(db.getUserName(), db.getGroupName());
		}
	}

	public void channelUnRegistered(String channelId) {
		List<PadChannelBridge> list = new ArrayList<>(listPadChannelBridge);
		for (PadChannelBridge db : list) {
			if (null == db || db.getChannelId() == null) {
				listPadChannelBridge.remove(db);
				continue;
			}
			if (db.getChannelId().equals(channelId)) {
				removeBridge(db);
			}
		}
	}
	
	public List<PadChannelBridge> findMyPadChannelBridge(String userName, String groupName){
		List<PadChannelBridge> listPb = new ArrayList<>(listPadChannelBridge);
		List<PadChannelBridge> list = new ArrayList<>();
		for (PadChannelBridge db : listPb) {
			if(db.getUserName() != null && db.getGroupName() != null 
					&& db.getUserName().equals(userName) && db.getGroupName().equals(groupName)) {
				list.add(db);
			}
		}
		return list;
	}

	/**
	 * 
	 * @author LinQiang
	 *
	 */
	public class PadHeartThread extends Thread {

		@Override
		public void run() {
			while (!isInterrupted()) {
				try {
					sleep(10000);
					// System.out.println("DevHeartThread begin");
					if (listPadChannelBridge.isEmpty()) {
						continue;
					}

					List<PadChannelBridge> list = new ArrayList<>(listPadChannelBridge);
					// System.out.println("PadChannelBridgeHelper " + list.size());
					for (PadChannelBridge db : list) {
						db.sendHeart();
					}
				} catch (InterruptedException e) {
					// e.printStackTrace();
					break;
				}
			}
		}

	}
	
	public interface OnPadDisconnectedListener {
		void onPadDisconnected(String userName, String groupName);
	}
}
