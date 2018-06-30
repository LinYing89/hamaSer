package com.bairock.iot.hamaser.communication;

import java.util.ArrayList;
import java.util.List;

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
		pushToTag(trigger);
	}
	
	private void pushToTag(AlarmTrigger trigger) {
		List<String> listTag = new ArrayList<>();
		DevGroup dg = trigger.getDevAlarm().findSuperParent().getDevGroup();
		String tag = dg.getUser().getName() + "_" + dg.getName();
		listTag.add(tag);
		
		Message message1 = createMessage(trigger);
		
		XingeApp xinge = new XingeApp(2100297444, "b328d1fa35307949d7809b6453f3ac8c");
        JSONObject ret = xinge.pushTags(0, listTag, "OR", message1);
        System.out.println("MyOnAlarmTriggedListener " + ret);
	}
	
	private Message createMessage(AlarmTrigger trigger) {
		Message message1 = new Message();
        message1.setType(Message.TYPE_NOTIFICATION);
        message1.setTitle("报警");
        message1.setContent(trigger.getDevAlarm().getName() + ":" + trigger.getMessage());
        
        ClickAction ca = new ClickAction();
        ca.setAtyAttrIntentFlag(268435456 | 2097152);
        message1.setAction(ca);
        
        //含义：样式编号0，响铃，震动，不可从通知栏清除，覆盖相同id的通知
        Style style = new Style(0,1,1,1,1);
        message1.setStyle(style);
        return message1;
	}
	
}
