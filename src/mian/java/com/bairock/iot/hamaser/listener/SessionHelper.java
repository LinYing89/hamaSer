package com.bairock.iot.hamaser.listener;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.user.DevGroup;
import com.bairock.iot.intelDev.user.User;

public class SessionHelper {

	public static final String USER = "user";
	public static final String USER_NAME = "userName";
	public static final String DEV_GROUP = "devGroup";
	public static final String DEV_GROUP_NAME = "devGroupName";
	
	public static final String ENTITY_MANAGER = "entityManager";
	
	public static List<HttpSession> LIST_SESSION = new ArrayList<HttpSession>();
	
	/**
	 * 获取用户
	 * @param name 用户名
	 * @return
	 */
	public static User getUser(String name){
		for(HttpSession session : LIST_SESSION){
			User user = (User) session.getAttribute(USER);
			if(null != user){
				if(user.getName().equals(name)){
					return user;
				}
			}
		}
		return null;
	}
	
	public static EntityManager getEntityManager(String userName){
		EntityManager em = null;
		for(HttpSession session : LIST_SESSION){
			User user = (User) session.getAttribute(USER);
			if(null != user){
				if(user.getName().equals(userName)){
					em = (EntityManager) session.getAttribute(ENTITY_MANAGER);
					if(null == em) {
						em = StartUpListener.getEntityManager();
						session.setAttribute(ENTITY_MANAGER, em);
					}
					return em;
				}
			}
		}
		if(null == em) {
			em = StartUpListener.getEntityManager();
		}
		return em;
	}
	
	/**
	 * 获取组
	 * @param userName 用户名称
	 * @param groupName 组名
	 * @return
	 */
	public static DevGroup getDevGroup(String userName, String groupName){
		for(HttpSession session : LIST_SESSION){
			DevGroup group = (DevGroup) session.getAttribute(DEV_GROUP);
			if(null != group){
				String snUserName = (String) session.getAttribute(USER_NAME);
				if(null != snUserName){
					if(snUserName.equals(userName) && group.getName().equals(groupName)){
						return group;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取用户的指定设备
	 * @param devCoding 设备编码
	 * @param userName
	 * @param devGroupName
	 * @return
	 */
	public static Device getDevice(String devCoding, String userName, String devGroupName){
		Device device = null;
		DevGroup group = getDevGroup(userName, devGroupName);
		if(null != group){
    		Device myDev = group.findDeviceWithCoding(devCoding);
    		if(null != myDev){
    			device = myDev;
    		}
    	}
		return device;
	}
	
	/**
	 * 组的session中只保存组(group)和用户员名(userName)
	 * 用户的session中保存用户(user)，组(group)和用户名(userName)
	 * @param userName
	 * @param groupname
	 * @param groupPsd
	 * @return
	 */
	public static List<HttpSession> getMySession(String userName, String groupname){
		List<HttpSession> listSession = new ArrayList<HttpSession>();
		for(HttpSession session : LIST_SESSION){
			DevGroup devGroup = (DevGroup) session.getAttribute(DEV_GROUP);
			if(null != devGroup){
				String uName = (String) session.getAttribute(USER_NAME);
				if(null != uName){
					if(uName.equals(userName) && devGroup.getName().equals(groupname)){
						listSession.add(session);
					}
				}
			}
		}
		return listSession;
	}
	
	public static List<HttpSession> getUserSession(String userName){
		List<HttpSession> listSession = new ArrayList<>();
		for(HttpSession session : LIST_SESSION){
			String uName = (String) session.getAttribute(USER_NAME);
			if(null != uName){
				if(uName.equals(userName)){
					listSession.add(session);
				}
			}
		}
		return listSession;
	}
}
