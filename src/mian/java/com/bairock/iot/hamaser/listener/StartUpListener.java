package com.bairock.iot.hamaser.listener;

import java.io.FileInputStream;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.bairock.iot.hamaser.communication.MyDevChannelBridge;
import com.bairock.iot.hamaser.communication.MyOnPadDisconnectedListener;
import com.bairock.iot.hamaser.communication.PadChannelBridgeHelper;
import com.bairock.iot.hamaser.communication.PadServer;
import com.bairock.iot.hamaser.communication.UpDownloadServer;
import com.bairock.iot.intelDev.communication.DevChannelBridgeHelper;
import com.bairock.iot.intelDev.communication.DevServer;
import com.bairock.iot.intelDev.communication.RefreshCollectorValueHelper;
import com.bairock.iot.intelDev.user.IntelDevHelper;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

public class StartUpListener implements ServletContextListener {

	private static EntityManagerFactory em = null;
	public static String SERVER_IP;
	public static String DOWNLOAD_RELEASE_PATH;
	public static String DOWNLOAD_DEBUG_PATH;
	private UpDownloadServer upDownloadServer;
	private PadServer padServer;
	private DevServer devServer;
	private Logger logger = Logger.getLogger(this.getClass().getName()); 

	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("contextDestroyed");
		DevChannelBridgeHelper.getIns().closeAllBridge();
		
		for (HttpSession s : SessionHelper.LIST_SESSION) {
			EntityManager em = (EntityManager) s.getAttribute(SessionHelper.ENTITY_MANAGER);
			if (null != em) {
				em.close();
			}
		}
		em.close();
		AbandonedConnectionCleanupThread.checkedShutdown();
		DevChannelBridgeHelper.getIns().stopSeekDeviceOnLineThread();
		IntelDevHelper.shutDown();
		RefreshCollectorValueHelper.getIns().isStoped = true;
		upDownloadServer.close();
		padServer.close();
		devServer.close();
	}

	public void contextInitialized(ServletContextEvent sce) {
		logger.info("contextInitialized");
		em = Persistence.createEntityManagerFactory("intelDev");
		DevChannelBridgeHelper.DEV_CHANNELBRIDGE_NAME = MyDevChannelBridge.class.getName();

		try {
			Properties properties = new Properties();
			properties.loadFromXML(new FileInputStream(sce.getServletContext().getRealPath("/WEB-INF/config.xml")));
			SERVER_IP = properties.get("serverIp").toString();
			PadServer.PORT = Integer.parseInt(properties.get("padPort").toString());
			DevServer.PORT = Integer.parseInt(properties.get("devPort").toString());
			UpDownloadServer.PORT = Integer.parseInt(properties.get("upDownloadPort").toString());
			DOWNLOAD_RELEASE_PATH = properties.get("downloadReleasePath").toString();
			DOWNLOAD_DEBUG_PATH = properties.get("downloadDebugPath").toString();
			logger.info("padPort:" + PadServer.PORT + " devPort:" + DevServer.PORT + " loadPort:" + UpDownloadServer.PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			PadChannelBridgeHelper.getIns().setOnPadDisconnectedListener(new MyOnPadDisconnectedListener());
			upDownloadServer = new UpDownloadServer();
			padServer = new PadServer();
			devServer = new DevServer();
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
	
	public static String getAppPath(boolean debug) {
		if(debug) {
			return DOWNLOAD_DEBUG_PATH;
		}else {
			return DOWNLOAD_RELEASE_PATH;
		}
	}
}
