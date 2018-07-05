package com.bairock.iot.hamaser.communication;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.bairock.iot.hamaser.dao.DevGroupDao;
import com.bairock.iot.hamaser.dao.UserDao;
import com.bairock.iot.hamaser.listener.SessionHelper;
import com.bairock.iot.intelDev.user.DevGroup;
import com.bairock.iot.intelDev.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * upload and download channel handler
 * @author Administrator
 *
 */
public class UpDownloadHandler extends ChannelInboundHandlerAdapter{
	
	private StringBuilder sbReadJson;
	private Logger logger = Logger.getLogger(this.getClass().getName()); 
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf m = (ByteBuf)msg;
		try{
			byte[] req = new byte[m.readableBytes()];
			m.readBytes(req);
			String str = new String(req, "GBK");
			logger.info(str);
			if(str.equals("upload")) {
				sbReadJson = new StringBuilder();
			}else if(str.startsWith("download")) {
				String userName = str.substring(str.indexOf(":") + 1, str.lastIndexOf(":"));
				String groupName = str.substring(str.lastIndexOf(":") + 1);
				UserDao userDao = new UserDao();
				User user = userDao.findUserInit(userName, groupName);
				if(null == user || user.getListDevGroup().isEmpty()) {
					return;
				}
				for(DevGroup devGroup : user.getListDevGroup()) {
					if(null != devGroup.getName() && devGroup.getName().equals(groupName)) {
						User u = new User();
						u.setName(user.getName());
						u.setPetName(user.getPetName());
						u.addGroup(devGroup);
						ObjectMapper mapper = new ObjectMapper();
						String json = mapper.writeValueAsString(u);
						if(null != json) {
							ctx.writeAndFlush(Unpooled.copiedBuffer(json.getBytes("GBK")));
							TimeUnit.MILLISECONDS.sleep(200);
							ctx.writeAndFlush(Unpooled.copiedBuffer("OK".getBytes("GBK")));
						}
						break;
					}
				}
				
				
			}else {
				sbReadJson.append(str);
				//upload end
				if(str.endsWith("#")) {
					String json = sbReadJson.toString();
					sbReadJson.setLength(0);
					json = json.substring(0, json.length() - 1);
					if(getUserFromJson(json) != null) {
						ctx.writeAndFlush(Unpooled.copiedBuffer("OK".getBytes("GBK")));
					}else {
						ctx.writeAndFlush(Unpooled.copiedBuffer("ERR".getBytes("GBK")));
					}
				}
			}
		}finally{
			m.release();
//			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}
	
	private User getUserFromJson(String json){
		ObjectMapper mapper = new ObjectMapper();
		User user = null;
		try {
			user = mapper.readValue(json, User.class);
			DevGroup dg = user.getListDevGroup().get(0);
			User u = new UserDao().findByUserName(user.getName());
			DevGroup group = null;
			if(null != u) {
				user.setEmail(u.getEmail());
				user.setId(u.getId());
				user.setPetName(u.getPetName());
				user.setPsd(u.getPsd());
				user.setRegisterTime(u.getRegisterTime());
				user.setTel(u.getTel());
				group = new DevGroupDao().findByUserIdAndGroupName(u.getId(), dg.getName());
				if(null != group) {
					dg.setId(group.getId());
				}
				group = dg;
				//group.removeDeletedDevice();
			}else {
				return null;
			}
			//boolean res = new UserDao().update(user);
			//boolean res = new DevGroupDao().update(group);
			boolean res = new DevGroupDao().update(dg);
			if(res) {
				List<HttpSession> list = SessionHelper.getUserSession(user.getName());
				for(HttpSession hs : list) {
					User u1 = (User)hs.getAttribute(SessionHelper.USER);
					if(null != u1) {
						DevGroup refreshGroup = null;
						for(DevGroup dg1 : u1.getListDevGroup()) {
							if(dg1.getName().equals(group.getName())) {
								refreshGroup = dg1;
								break;
							}
						}
						u1.getListDevGroup().remove(refreshGroup);
						u1.addGroup(group);
					}
					//hs.setAttribute(SessionHelper.USER, user);
				}
				List<HttpSession> list2 = SessionHelper.getMySession(user.getName(), dg.getName());
				for(HttpSession hs : list2) {
					hs.setAttribute(SessionHelper.DEV_GROUP, group);
				}
				MyOnStateChangedListener l1 = new MyOnStateChangedListener();
				MyOnGearChangedListener l2 = new MyOnGearChangedListener();
				MyOnCtrlModelChangedListener l3 = new MyOnCtrlModelChangedListener();
				DevGroupDao.setDeviceListener(dg, l1, l2, l3);
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return user;
	}
}
