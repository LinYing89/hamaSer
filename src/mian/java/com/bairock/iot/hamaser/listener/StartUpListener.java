package com.bairock.iot.hamaser.listener;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bairock.iot.hamaser.communication.MyDevChannelBridge;
import com.bairock.iot.hamaser.communication.PadServer;
import com.bairock.iot.hamaser.communication.UpDownloadServer;
import com.bairock.iot.intelDev.communication.DevChannelBridgeHelper;
import com.bairock.iot.intelDev.communication.DevServer;

public class StartUpListener implements ServletContextListener {

	private static EntityManagerFactory em = null;
	private UpDownloadServer upDownloadServer;
	private PadServer padServer;
	private DevServer devServer;
	
    public void contextDestroyed(ServletContextEvent sce)  { 
         em.close();
         upDownloadServer.close();
         padServer.close();
         devServer.close();
    }
    
    public void contextInitialized(ServletContextEvent sce)  { 
    	em = Persistence.createEntityManagerFactory("intelDev");
    	DevChannelBridgeHelper.DEV_CHANNELbRIDGE_NAME = MyDevChannelBridge.class.getName();
    	upDownloadServer = new UpDownloadServer();
    	padServer = new PadServer();
    	devServer = new DevServer();
    	DevServer.PORT = 4049;
    	
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
