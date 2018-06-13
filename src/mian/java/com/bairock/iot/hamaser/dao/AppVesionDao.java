package com.bairock.iot.hamaser.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.bairock.iot.hamaser.listener.StartUpListener;
import com.bairock.iot.intelDev.user.AppVersion;

public class AppVesionDao {

	public List<AppVersion> findApps(boolean debug) {
		List<AppVersion> listAppVersion = new ArrayList<>();
		EntityManager eManager2 = StartUpListener.getEntityManager();
		try {
			eManager2.getTransaction().begin();

			TypedQuery<AppVersion> query = eManager2.createQuery("from AppVersion as a where a.debugVersion=:debug", AppVersion.class);
			query.setParameter("debug", debug);
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
	
	public AppVersion findLastApp(boolean debug) {
		AppVersion app = null;
		EntityManager eManager2 = StartUpListener.getEntityManager();
		try {
			eManager2.getTransaction().begin();

			TypedQuery<AppVersion> query = eManager2.createQuery("from AppVersion app where app.appVc = (select max(a.appVc) from AppVersion a) and app.debugVersion=:debug", AppVersion.class);
			query.setParameter("debug", debug);
			app = query.getSingleResult();
			eManager2.getTransaction().commit();
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			eManager2.close();
		}
		return app;
	}
}
