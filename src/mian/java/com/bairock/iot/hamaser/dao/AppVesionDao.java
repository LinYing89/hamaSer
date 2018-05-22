package com.bairock.iot.hamaser.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.bairock.iot.hamaser.listener.StartUpListener;
import com.bairock.iot.intelDev.user.AppVersion;

public class AppVesionDao {

	public List<AppVersion> findApps() {
		List<AppVersion> listAppVersion = new ArrayList<>();
		EntityManager eManager2 = StartUpListener.getEntityManager();
		try {
			eManager2.getTransaction().begin();

			TypedQuery<AppVersion> query = eManager2.createQuery("from AppVersion", AppVersion.class);
			listAppVersion = query.getResultList();
			eManager2.getTransaction().commit();
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			eManager2.close();
		}
		return listAppVersion;
	}
}