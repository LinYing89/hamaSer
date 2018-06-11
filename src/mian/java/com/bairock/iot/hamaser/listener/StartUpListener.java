package com.bairock.iot.hamaser.listener;

import java.io.FileInputStream;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;

import com.bairock.iot.hamaser.communication.MyDevChannelBridge;
import com.bairock.iot.hamaser.communication.MyOnPadDisconnectedListener;
import com.bairock.iot.hamaser.communication.PadChannelBridgeHelper;
import com.bairock.iot.hamaser.communication.PadServer;
import com.bairock.iot.hamaser.communication.UpDownloadServer;
import com.bairock.iot.intelDev.communication.DevChannelBridgeHelper;
import com.bairock.iot.intelDev.communication.DevServer;
import com.bairock.iot.intelDev.user.IntelDevHelper;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

public class StartUpListener implements ServletContextListener {

	private static EntityManagerFactory em = null;
	public static String SERVER_IP;
	public static String DOWNLOAD_PATH;
	private UpDownloadServer upDownloadServer;
	private PadServer padServer;
	private DevServer devServer;

	public void contextDestroyed(ServletContextEvent sce) {
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
		upDownloadServer.close();
		padServer.close();
		devServer.close();
	}

	public void contextInitialized(ServletContextEvent sce) {
		em = Persistence.createEntityManagerFactory("intelDev");
		DevChannelBridgeHelper.DEV_CHANNELBRIDGE_NAME = MyDevChannelBridge.class.getName();

		try {
			// ∂¡»°≈‰÷√Œƒº˛
			Properties properties = new Properties();
			properties.loadFromXML(new FileInputStream(sce.getServletContext().getRealPath("/WEB-INF/config.xml")));
			SERVER_IP = properties.get("serverIp").toString();
			PadServer.PORT = Integer.parseInt(properties.get("padPort").toString());
			DevServer.PORT = Integer.parseInt(properties.get("devPort").toString());
			UpDownloadServer.PORT = Integer.parseInt(properties.get("upDownloadPort").toString());
			DOWNLOAD_PATH = properties.get("downloadPath").toString();
			System.out.println("padPort:" + PadServer.PORT + " devPort:" + DevServer.PORT + " loadPort:" + UpDownloadServer.PORT);
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
}
