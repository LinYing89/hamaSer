package com.bairock.iot.hamaser.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.bairock.iot.hamaser.listener.StartUpListener;
import com.bairock.iot.intelDev.linkage.LinkageHolder;
import com.bairock.iot.intelDev.user.User;

public class LinkageHolderDao {
	
	private EntityManager eManager;
    
	public LinkageHolderDao() {
		eManager = StartUpListener.getEntityManager();
	}
	
    public void add(LinkageHolder linkageHolder, String groupId) {
//    	try {
//    		eManager.getTransaction().begin();
//
//			TypedQuery<LinkageHolder> query = eManager.createQuery("from LinkageHolder as u where u.id=:id",
//					LinkageHolder.class);
//			query.setParameter("id", linkageHolder.getId());
//			List<LinkageHolder> listUser = query.getResultList();
//			if(null == listUser || listUser.size() == 0) {
//				//no data
//				eManager.persist(linkageHolder);
//			}else {
//				//update
//			}
//			if (null != listUser && listUser.size() > 0) {
//				user = listUser.get(0);
//			}
//			eManager.getTransaction().commit();
//		} catch (Exception e) {
//			eManager.getTransaction().rollback();
//			e.printStackTrace();
//		}
//    	
//    	try {
//    		eManager.getTransaction().begin();
//    		eManager.persist(devGroup);
//    		eManager.getTransaction().commit();
//			res = true;
//		} catch (Exception e) {
//			eManager.getTransaction().rollback();
//			e.printStackTrace();
//		} 
    }

}
