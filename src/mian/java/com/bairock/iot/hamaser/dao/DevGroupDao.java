package com.bairock.iot.hamaser.dao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.bairock.iot.hamaser.communication.MyOnAlarmTriggedListener;
import com.bairock.iot.hamaser.communication.MyOnCtrlModelChangedListener;
import com.bairock.iot.hamaser.communication.MyOnCurrentValueChangedListener;
import com.bairock.iot.hamaser.communication.MyOnGearChangedListener;
import com.bairock.iot.hamaser.communication.MyOnStateChangedListener;
import com.bairock.iot.hamaser.communication.MyOnValueTriggedListener;
import com.bairock.iot.hamaser.listener.SessionHelper;
import com.bairock.iot.hamaser.listener.StartUpListener;
import com.bairock.iot.intelDev.device.CtrlModel;
import com.bairock.iot.intelDev.device.DevHaveChild;
import com.bairock.iot.intelDev.device.DevStateHelper;
import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.device.Gear;
import com.bairock.iot.intelDev.device.alarm.DevAlarm;
import com.bairock.iot.intelDev.device.devcollect.DevCollect;
import com.bairock.iot.intelDev.user.DevGroup;

public class DevGroupDao {

	private Logger logger = Logger.getLogger(this.getClass().getName()); 
	
	/**
	 * 获取组对象
	 * @param id user id
	 * @param groupName 组名
	 * @return
	 */
	public DevGroup findByUserIdAndGroupName(long id, String groupName) {
		DevGroup group = null;
		EntityManager eManager2 = StartUpListener.getEntityManager();
		try {
			eManager2.getTransaction().begin();

			TypedQuery<DevGroup> query = eManager2
					.createQuery("from DevGroup as u where u.user.id=:user_id and u.name=:name", DevGroup.class);
			query.setParameter("user_id", id);
			query.setParameter("name", groupName);
			group = query.getSingleResult();
			eManager2.getTransaction().commit();
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userId:" + id + "groupName:" + groupName);
		} finally {
			eManager2.close();
		}
		return group;
	}

	/**
	 * 添加组对象
	 * @param devGroup
	 * @return
	 */
	public boolean add(DevGroup devGroup) {
		boolean res = false;
		EntityManager eManager = SessionHelper.getEntityManager(devGroup.getUser().getName());
		try {
			eManager.getTransaction().begin();

			// TypedQuery<DevGroup> query = eManager.createQuery("from DevGroup as u where
			// u.id=:id",
			// DevGroup.class);
			// query.setParameter("id", linkageHolder.getId());
			// List<DevGroup> listUser = query.getResultList();
			// if(null == listUser || listUser.size() == 0) {
			// //no data
			// eManager.persist(linkageHolder);
			// }else {
			// //update
			// }

			eManager.persist(devGroup);
			eManager.getTransaction().commit();
			res = true;
		} catch (Exception e) {
			eManager.getTransaction().rollback();
			e.printStackTrace();
			String userName = "";
			if(null != devGroup.getUser()) {
				userName = devGroup.getUser().getName();
			}
			logger.error(e.getMessage() + " 说明:userName:" + userName + "groupName:" + devGroup.getName());
		}
		return res;
	}

	/**
	 * 删除组对象
	 * @param userName
	 * @param devGroup
	 * @return
	 */
	public boolean delete(String userName, DevGroup devGroup) {
		boolean res = false;
		EntityManager eManager2 = SessionHelper.getEntityManager(userName);
		try {
			eManager2.getTransaction().begin();
			eManager2.remove(eManager2.merge(devGroup));
			eManager2.getTransaction().commit();
			res = true;
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userName:" + userName + "groupName:" + devGroup.getName());
		}
		return res;
	}

	/**
	 * 更新组对象
	 * @param devGroup
	 * @return
	 */
	public boolean update(DevGroup devGroup) {
		String userName = "";
		if(null != devGroup.getUser()) {
			userName = devGroup.getUser().getName();
		}
		logger.error("update group 说明:userName:" + userName + "groupName:" + devGroup.getName());
		
		boolean res = false;
		EntityManager eManager2 = SessionHelper.getEntityManager(devGroup.getUser().getName());
		try {
			eManager2.getTransaction().begin();
			eManager2.merge(devGroup);
			eManager2.getTransaction().commit();
			res = true;
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userName:" + userName + "groupName:" + devGroup.getName());
		}
		return res;
	}

	public static void setDeviceListener(DevGroup group, MyOnStateChangedListener onStateChangedListener,
			MyOnGearChangedListener onGearChangedListener, MyOnCtrlModelChangedListener l3) {
		for (Device dev : group.getListDevice()) {
			setDeviceListener(dev, onStateChangedListener, onGearChangedListener, l3);
		}
	}

	public static void setDeviceListener(Device device, MyOnStateChangedListener onStateChangedListener,
			MyOnGearChangedListener onGearChangedListener, MyOnCtrlModelChangedListener l3) {
		device.setDevStateId(DevStateHelper.DS_YI_CHANG);
		device.setCtrlModel(CtrlModel.UNKNOW);
		device.setGear(Gear.UNKNOW);
		device.setOnStateChanged(onStateChangedListener);
		device.setOnGearChanged(onGearChangedListener);
		device.setOnCtrlModelChanged(l3);
		if (device instanceof DevHaveChild) {
			for (Device device1 : ((DevHaveChild) device).getListDev()) {
				setDeviceListener(device1, onStateChangedListener, onGearChangedListener, l3);
			}
		}
		if(device instanceof DevCollect) {
			DevCollect dc = (DevCollect)device;
			dc.getCollectProperty().addOnCurrentValueChangedListener(new MyOnCurrentValueChangedListener());
			dc.getCollectProperty().setOnValueTriggedListener(new MyOnValueTriggedListener());
			dc.getCollectProperty().initTriggerListener();
		}else if(device instanceof DevAlarm) {
			((DevAlarm)device).addOnAlarmTriggedListener(new MyOnAlarmTriggedListener());
		}
	}
}
