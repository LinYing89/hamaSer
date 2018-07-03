package com.bairock.iot.hamaser.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.bairock.iot.intelDev.device.alarm.AlarmTrigger;
import com.bairock.iot.intelDev.device.alarm.DevAlarm;
import com.bairock.iot.intelDev.user.DevGroup;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;

public class MyOnAlarmTriggedListener implements DevAlarm.OnAlarmTriggedListener{

	@Override
	public void onAlarmTrigged(AlarmTrigger trigger) {
		pushToTag(trigger, Message.TYPE_NOTIFICATION, 0);
	}
	
	@Override
	public void onAlarmTrigging(AlarmTrigger trigger) {
		pushToTag(trigger, Message.TYPE_MESSAGE, 1);
	}

	@Override
	public void onAlarmTriggedRelieve(AlarmTrigger trigger) {
		pushToTag(trigger, Message.TYPE_MESSAGE, 2);
	}
	
	/**
	 * 
	 * @param trigger
	 * @param messageType
	 * @param triggerStyle 0:报警，1:报警中，2:报警解除
	 */
	private void pushToTag(AlarmTrigger trigger, int messageType, int triggerStyle) {
		List<String> listTag = new ArrayList<>();
		DevGroup dg = trigger.getDevAlarm().findSuperParent().getDevGroup();
		String tag = dg.getUser().getName() + "_" + dg.getName();
		listTag.add(tag);
		
		Message message1 = createMessage(trigger, messageType, triggerStyle);
		
		XingeApp xinge = new XingeApp(2100297444, "b328d1fa35307949d7809b6453f3ac8c");
        JSONObject ret = xinge.pushTags(0, listTag, "OR", message1);
        System.out.println("MyOnAlarmTriggedListener " + ret);
	}
	
	private Message createMessage(AlarmTrigger trigger, int messageType, int triggerStyle) {
		Message message1 = new Message();
        message1.setType(messageType);
        message1.setTitle("报警");
        message1.setContent(trigger.getDevAlarm().getName() + ":" + trigger.getMessage());
        
        if(messageType == Message.TYPE_NOTIFICATION) {
	        ClickAction ca = new ClickAction();
	        ca.setAtyAttrIntentFlag(268435456 | 2097152);
	        message1.setAction(ca);
	        
	        //含义：样式编号0，响铃，震动，不可从通知栏清除，覆盖相同id的通知
	        Style style = new Style(0,1,1,1,1);
	        style.setRingRaw("leida.wav");
	        message1.setStyle(style);
        }else {
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("trigger", triggerStyle);
        	map.put("name", trigger.getDevAlarm().getName());
        	message1.setCustom(map);
        }
        return message1;
	}
	
}
