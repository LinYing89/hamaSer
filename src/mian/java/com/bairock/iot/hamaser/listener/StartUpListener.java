package com.bairock.iot.hamaser.listener;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;

import com.bairock.iot.hamaser.communication.MyDevChannelBridge;
import com.bairock.iot.hamaser.communication.PadServer;
import com.bairock.iot.hamaser.communication.UpDownloadServer;
import com.bairock.iot.intelDev.communication.DevChannelBridgeHelper;
import com.bairock.iot.intelDev.communication.DevServer;
import com.bairock.iot.intelDev.user.IntelDevHelper;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

public class StartUpListener implements ServletContextListener {

	private static EntityManagerFactory em = null;
	private UpDownloadServer upDownloadServer;
	private PadServer padServer;
	private DevServer devServer;
	
    public void contextDestroyed(ServletContextEvent sce)  { 
    	for(HttpSession s : SessionHelper.LIST_SESSION) {
    		EntityManager em = (EntityManager) s.getAttribute(SessionHelper.ENTITY_MANAGER);
    		if(null != em) {
    			em.close();
    		}
    	}
         em.close();
         AbandonedConnectionCleanupThread.checkedShutdown();
         DevChannelBridgeHelper.getIns().stopSeekDeviceOnLineThread();
         IntelDevHelper.shutDown();
         upDownloadServer.close();
         padServer.close();
         devServer.close();
    }
    
    public void contextInitialized(ServletContextEvent sce)  { 
    	em = Persistence.createEntityManagerFactory("intelDev");
    	DevChannelBridgeHelper.DEV_CHANNELBRIDGE_NAME = MyDevChannelBridge.class.getName();
    	upDownloadServer = new UpDownloadServer();
    	padServer = new PadServer();
    	devServer = new DevServer();
    	PadServer.PORT = 10002;
    	DevServer.PORT = 10003;
    	
    	try {
			upDownloadServer.run();
			padServer.run();
			devServer.run();
			DevChannelBridgeHelper.getIns().startSeekDeviceOnLineThread();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
    public static EntityManagerFactory getEntityManagerFactory() {
    	return em;
    }
    
    public static EntityManager getEntityManager() {
    	return em.createEntityManager();
    }
}
