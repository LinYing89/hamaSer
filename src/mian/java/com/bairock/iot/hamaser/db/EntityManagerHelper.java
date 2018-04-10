package com.bairock.iot.hamaser.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerHelper {

	public static EntityManager getEntityManager() {
		EntityManagerFactory em = Persistence.createEntityManagerFactory("intelDev");
		return em.createEntityManager();
	}
}
