package com.bairock.iot.hamaser.communication;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.bairock.iot.intelDev.device.devcollect.CollectProperty;
import com.bairock.iot.intelDev.device.devcollect.ValueTrigger;
import com.bairock.iot.intelDev.user.DevGroup;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;

public class MyOnValueTriggedListener implements CollectProperty.OnValueTriggedListener{

	@Override
	public void onValueTrigged(ValueTrigger trigger, float value) {
		System.out.println(trigger.getCollectProperty().getDevCollect().getLongCoding() + " msg:" + trigger.getMessage() + " -" + value);
		
		pushToTag(trigger, value);
//		pushToAccount(trigger, value);
        
        //JSONObject ret = xinge.pushSingleDevice("d8d66c26421d53be42148b227fd943a55675d213", message1);
	}
	
	@SuppressWarnings("unused")
	private void pushToAccount(ValueTrigger trigger, float value) {
		Message message1 = createMessage(trigger, value);
        
        //XingeApp.pushAccountAndroid(2100297444, "b328d1fa35307949d7809b6453f3ac8c", "test", "≤‚ ‘", "test123_1"));
        
        DevGroup dg = trigger.getCollectProperty().getDevCollect().findSuperParent().getDevGroup();
        String assount = dg.getUser().getName() + "_" + dg.getName();
        
        XingeApp xinge = new XingeApp(2100297444, "b328d1fa35307949d7809b6453f3ac8c");
        JSONObject ret = xinge.pushSingleAccount(0, assount, message1);
        System.out.println("MyOnValueTriggedListener " + ret);
	}
	
	private void pushToTag(ValueTrigger trigger, float value) {
		List<String> listTag = new ArrayList<>();
		DevGroup dg = trigger.getCollectProperty().getDevCollect().findSuperParent().getDevGroup();
		String tag = dg.getUser().getName() + "_" + dg.getName();
		listTag.add(tag);
		
		Message message1 = createMessage(trigger, value);
		
		XingeApp xinge = new XingeApp(2100297444, "b328d1fa35307949d7809b6453f3ac8c");
        JSONObject ret = xinge.pushTags(0, listTag, "OR", message1);
        System.out.println("MyOnValueTriggedListener " + ret);
	}
	
	private Message createMessage(ValueTrigger trigger, float value) {
		Message message1 = new Message();
        message1.setType(Message.TYPE_NOTIFICATION);
        message1.setTitle(trigger.getCollectProperty().getDevCollect().getName());
        message1.setContent(trigger.getMessage() + " (µ±«∞÷µ:" + value + ")");
        
        ClickAction ca = new ClickAction();
        ca.setAtyAttrIntentFlag(268435456 | 2097152);
        message1.setAction(ca);
        
        Style style = new Style(0,1,1,1,0);
        message1.setStyle(style);
        return message1;
	}
	
}
