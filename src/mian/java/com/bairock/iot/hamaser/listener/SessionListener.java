package com.bairock.iot.hamaser.listener;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent se)  { 
    	SessionHelper.LIST_SESSION.add(se.getSession());
    }

    public void sessionDestroyed(HttpSessionEvent se)  { 
    	//close EntityManager when the last user session is destroyed
    	String userName = (String) se.getSession().getAttribute(SessionHelper.USER_NAME);
    	if(SessionHelper.getUserSession(userName).size() <= 1) {
    		EntityManager em = (EntityManager) se.getSession().getAttribute(SessionHelper.ENTITY_MANAGER);
    		if(null != em) {
    			em.close();
    		}
    	}
    	
    	SessionHelper.LIST_SESSION.remove(se.getSession());
    }
	
}
