package com.bairock.iot.hamaser.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.bairock.iot.hamaser.listener.SessionHelper;
import com.bairock.iot.hamaser.listener.StartUpListener;
import com.bairock.iot.intelDev.device.Device;

public class DeviceDao {

	public Device findByMainCodeIdAndSubCode(String mainCodeId, String subCode) {
		Device device = null;
		EntityManager eManager2 = StartUpListener.getEntityManager();
		try {
			eManager2.getTransaction().begin();

			TypedQuery<Device> query = eManager2
					.createQuery("from Device as u where u.mainCodeId=:mainCodeId and u.subCode=:subCode", Device.class);
			query.setParameter("mainCodeId", mainCodeId);
			query.setParameter("subCode", subCode);
			//device = query.getSingleResult();
			List<Device> listUser = query.getResultList();
			if (null != listUser && listUser.size() > 0) {
				device = listUser.get(0);
			}
			eManager2.getTransaction().commit();
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			eManager2.close();
		}
		return device;
	}
	
	public boolean update(Device device) {
		boolean res = false;
		EntityManager eManager2 = SessionHelper.getEntityManager(device.getDevGroup().getUser().getName());
		try {
			eManager2.getTransaction().begin();
			eManager2.merge(device);
			eManager2.getTransaction().commit();
			res = true;
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
		}
		return res;
	}
	
}
