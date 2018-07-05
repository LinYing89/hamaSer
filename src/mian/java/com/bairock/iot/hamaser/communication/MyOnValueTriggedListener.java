package com.bairock.iot.hamaser.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.bairock.iot.intelDev.device.CtrlModel;
import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.device.devcollect.CollectProperty;
import com.bairock.iot.intelDev.device.devcollect.ValueTrigger;
import com.bairock.iot.intelDev.user.DevGroup;
import com.bairock.iot.intelDev.user.IntelDevHelper;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;

public class MyOnValueTriggedListener implements CollectProperty.OnValueTriggedListener {

	@Override
	public void onValueTrigged(ValueTrigger trigger, float value) {
		Device dev = trigger.getCollectProperty().getDevCollect();
		dev.addAlarmInfo(IntelDevHelper.createAlarmInfo(dev.getName() + ":" + trigger.getMessage()
				+ " (当前值:" + value + ")"));
		
		if (dev.getCtrlModel() == CtrlModel.REMOTE) {
			//更新数据库
			//MyDevChannelBridge.findBridge(dev).updateDeviceDb();
			
			pushToTag(trigger, value, Message.TYPE_NOTIFICATION, 0);
			pushToTag(trigger, value, Message.TYPE_MESSAGE, 1);
		}
		// pushToAccount(trigger, value);

		// JSONObject ret =
		// xinge.pushSingleDevice("d8d66c26421d53be42148b227fd943a55675d213", message1);
	}

	@Override
	public void onValueTriggedRelieve(ValueTrigger trigger, float value) {
		Device dev = trigger.getCollectProperty().getDevCollect();
		dev.addAlarmInfo(IntelDevHelper.createAlarmInfo(dev.getName() + ":" + "提醒解除"
		+ " (当前值:" + value + ")"));
		
		if (trigger.getCollectProperty().getDevCollect().getCtrlModel() == CtrlModel.REMOTE) {
			//更新数据库
			//MyDevChannelBridge.findBridge(dev).updateDeviceDb();
			
			pushToTag(trigger, value, Message.TYPE_MESSAGE, 2);
		}
	}
	
	@SuppressWarnings("unused")
	private void pushToAccount(ValueTrigger trigger, float value) {
		// Message message1 = createMessage(trigger, value);
		//
		// //XingeApp.pushAccountAndroid(2100297444, "b328d1fa35307949d7809b6453f3ac8c",
		// "test", "test", "test123_1"));
		//
		// DevGroup dg =
		// trigger.getCollectProperty().getDevCollect().findSuperParent().getDevGroup();
		// String assount = dg.getUser().getName() + "_" + dg.getName();
		//
		// XingeApp xinge = new XingeApp(2100297444,
		// "b328d1fa35307949d7809b6453f3ac8c");
		// JSONObject ret = xinge.pushSingleAccount(0, assount, message1);
		// System.out.println("MyOnValueTriggedListener " + ret);
	}

	private void pushToTag(ValueTrigger trigger, float value, int messageType, int triggerStyle) {
		List<String> listTag = new ArrayList<>();
		DevGroup dg = trigger.getCollectProperty().getDevCollect().findSuperParent().getDevGroup();
		String tag = dg.getUser().getName() + "_" + dg.getName();
		listTag.add(tag);

		Message message1 = createMessage(trigger, value, messageType, triggerStyle);

		XingeApp xinge = new XingeApp(2100297444, "b328d1fa35307949d7809b6453f3ac8c");
		JSONObject ret = xinge.pushTags(0, listTag, "OR", message1);
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info(ret);
	}

	private Message createMessage(ValueTrigger trigger, float value, int messageType, int triggerStyle) {
		Message message1 = new Message();
		message1.setType(messageType);
		message1.setTitle(trigger.getCollectProperty().getDevCollect().getName());
		message1.setContent(trigger.getCollectProperty().getDevCollect().getName() + ":" + trigger.getMessage()
				+ " (当前值:" + value + ")");

		if (messageType == Message.TYPE_NOTIFICATION) {
			ClickAction ca = new ClickAction();
			ca.setAtyAttrIntentFlag(268435456 | 2097152);
			message1.setAction(ca);

			Style style = new Style(0, 1, 1, 1, 0);
			message1.setStyle(style);
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("trigger", triggerStyle);
			map.put("name", trigger.getCollectProperty().getDevCollect().getName());
			message1.setCustom(map);
		}
		return message1;
	}

}
