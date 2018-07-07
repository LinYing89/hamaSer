package com.bairock.iot.hamaser.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.bairock.iot.hamaser.listener.SessionHelper;
import com.bairock.iot.hamaser.listener.StartUpListener;
import com.bairock.iot.intelDev.device.DevHaveChild;
import com.bairock.iot.intelDev.device.Device;
import com.bairock.iot.intelDev.device.DeviceAssistent;
import com.bairock.iot.intelDev.device.alarm.AlarmInfo;
import com.bairock.iot.intelDev.linkage.Effect;
import com.bairock.iot.intelDev.linkage.Linkage;
import com.bairock.iot.intelDev.linkage.LinkageCondition;
import com.bairock.iot.intelDev.linkage.LinkageHolder;
import com.bairock.iot.intelDev.linkage.SubChain;
import com.bairock.iot.intelDev.linkage.loop.LoopDuration;
import com.bairock.iot.intelDev.linkage.loop.ZLoop;
import com.bairock.iot.intelDev.linkage.timing.Timing;
import com.bairock.iot.intelDev.linkage.timing.ZTimer;
import com.bairock.iot.intelDev.user.DevGroup;
import com.bairock.iot.intelDev.user.User;

/**
 * 用户表数据库操作
 * 
 * @author Administrator
 *
 */
public class UserDao {

	private Logger logger = Logger.getLogger(this.getClass().getName()); 
	
	/**
	 * 是否有此用户名
	 * 
	 * @param userName
	 * @return
	 */
	public boolean isHaveByUserName(String userName) {
		boolean res = false;
		User u = findByUserName(userName);
		if(null != u) {
			res = true;
		}

		return res;
	}

	public boolean isFind(String userName, String psd) {
		boolean res = false;
		EntityManager eManager2 = SessionHelper.getEntityManager(userName);
		try {
			eManager2.getTransaction().begin();

			TypedQuery<User> query = eManager2.createQuery("from User u where u.name=:name u.psd=:psd", User.class);
			query.setParameter("name", userName);
			query.setParameter("psd", psd);
			List<User> listUser = query.getResultList();
			if (null != listUser && listUser.size() > 0) {
				res = true;
			}
			eManager2.getTransaction().commit();
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userName:" + userName + "psd:" + psd);
		}
		return res;
	}

	public String findPsdByName(String userName) {
		String res = null;
		EntityManager eManager2 = SessionHelper.getEntityManager(userName);
		try {
			eManager2.getTransaction().begin();

			List<User> listUser = (List<User>) eManager2
					.createQuery("from User u where u.name = " + userName, User.class).getResultList();
			if (null != listUser && listUser.size() > 0) {
				res = listUser.get(0).getPsd();
			}
			eManager2.getTransaction().commit();
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userName:" + userName);
		}
		return res;
	}

	public User findByNameAndPsd(String name, String psd) {
		User user = null;
		EntityManager eManager2 = SessionHelper.getEntityManager(name);
		try {
			eManager2.getTransaction().begin();

			TypedQuery<User> query;
			String sql = null;
			if(psd.equals("auto")) {
				sql = "from User as u where u.name=:name";
				query = eManager2.createQuery(sql, User.class);
				query.setParameter("name", name);
			}else {
				sql = "from User as u where u.name=:name and u.psd=:psd";
				query = eManager2.createQuery(sql, User.class);
				query.setParameter("name", name);
				query.setParameter("psd", psd);
			}
			user = query.getSingleResult();
			user.getListDevGroup();
			eManager2.getTransaction().commit();
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userName:" + name + "psd:" + psd);
		}
		return user;
	}

	public String findDevGroupPetName(String userName, String devGroupName, String devGroupPsd) {
		String res = null;
		EntityManager eManager2 = SessionHelper.getEntityManager(userName);
		try {
			eManager2.getTransaction().begin();

			TypedQuery<User> query = eManager2.createQuery("from User as u where u.name=:name", User.class);
			query.setParameter("name", userName);
			List<User> listUser = query.getResultList();
			if (null != listUser && listUser.size() > 0) {
				User user = listUser.get(0);
				for (DevGroup dg : user.getListDevGroup()) {
					if (dg.getName().equals(devGroupName) && dg.getPsd().equals(devGroupPsd)) {
						res = dg.getPetName();
						break;
					}
				}
			}
			eManager2.getTransaction().commit();
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userName:" + userName + "devGroupName:" + devGroupName + "devGroupPsd:" + devGroupPsd);
		}
		return res;
	}
	
	public User findByUserName(String userName) {
		User res = null;
		EntityManager eManager2 = SessionHelper.getEntityManager(userName);
		try {
			eManager2.getTransaction().begin();

			TypedQuery<User> query = eManager2.createQuery("from User as u where u.name=:name", User.class);
			query.setParameter("name", userName);
			List<User> listUser = query.getResultList();
			if (null != listUser && listUser.size() > 0) {
				res = listUser.get(0);
			}
			eManager2.getTransaction().commit();
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userName:" + userName);
		}
		return res;
	}
	
	public User findByUserNameInit(String userName) {
		User res = null;
		EntityManager eManager2 = SessionHelper.getEntityManager(userName);
		try {
			eManager2.getTransaction().begin();

			TypedQuery<User> query = eManager2.createQuery("from User as u where u.name=:name", User.class);
			query.setParameter("name", userName);
			List<User> listUser = query.getResultList();
			if (null != listUser && listUser.size() > 0) {
				res = listUser.get(0);
				for(DevGroup dg : res.getListDevGroup()) {
					initGroup(dg);
				}
			}
			eManager2.getTransaction().commit();
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userName:" + userName);
		}
		return res;
	}

	public boolean add(User user) {
		boolean res = false;
		EntityManager eManager2 = SessionHelper.getEntityManager(user.getName());
		try {
			eManager2.getTransaction().begin();
			eManager2.persist(user);
			eManager2.getTransaction().commit();
			res = true;
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			res = false;
			logger.error(e.getMessage() + " 说明:userName:" + user.getName());
		}
		return res;
	}
	
	public User findUserInit(String userName, String groupName) {
		User user = null;
		EntityManager eManager2 = SessionHelper.getEntityManager(userName);
		try {
			eManager2.getTransaction().begin();

			TypedQuery<User> query = eManager2.createQuery("from User as u where u.name=:name",
					User.class);
			query.setParameter("name", userName);
			//User user1 = query.getSingleResult();
			user = query.getSingleResult();
			DevGroup myGroup = null;
			if(null != user) {
				for(DevGroup group : user.getListDevGroup()){
					if(group.getName().equals(groupName)) {
						myGroup = group;
						break;
					}
				}
				initGroup(myGroup);
			}
			eManager2.getTransaction().commit();
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userName:" + userName + "groupName:" + groupName);
		}
		return user;
	}
	
	public Device findDeviceByUserNameGroupNameDevCoding(String userName, String groupName, String devCoding) {
		Device device = null;
		EntityManager eManager2 = SessionHelper.getEntityManager(userName);
		try {
			eManager2.getTransaction().begin();

			TypedQuery<User> query = eManager2.createQuery("from User as u where u.name=:name",
					User.class);
			query.setParameter("name", userName);
			User user1 = query.getSingleResult();
			DevGroup myGroup = null;
			Device devInDb = null;
			if(null != user1) {
				for(DevGroup group : user1.getListDevGroup()){
					if(group.getName().equals(groupName)) {
						myGroup = group;
						for(Device dev : group.getListDevice()) {
							if(dev.getCoding().equals(devCoding)) {
								initDevice(dev);
								devInDb = dev;
								break;
							}
						}
						break;
					}
				}
				//initGroup(myGroup);
			}
			eManager2.getTransaction().commit();
			if(null == myGroup || null == devInDb) {
				return null;
			}
			device = DeviceAssistent.createDeviceByCoding(devInDb.getCoding());
			Device.copyDevice(device, devInDb);
			User user = new User();
			user.setName(user1.getName());
			DevGroup group = new DevGroup();
			group.setName(myGroup.getName());
			user.addGroup(group);
			group.addDevice(device);
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userName:" + userName + "groupName:" + groupName + "devCoding:" + devCoding);
		}
		return device;
	}
	
	
	public Device findDeviceByUserNameGroupNameDevCoding2(String userName, String groupName, String devCoding) {
		Device device = null;
		EntityManager eManager2 = SessionHelper.getEntityManager(userName);
		try {
			eManager2.getTransaction().begin();

			TypedQuery<User> query = eManager2.createQuery("from User as u where u.name=:name",
					User.class);
			query.setParameter("name", userName);
			User user1 = query.getSingleResult();
			Device devInDb = null;
			if(null != user1) {
				for(DevGroup group : user1.getListDevGroup()){
					if(group.getName().equals(groupName)) {
						for(Device dev : group.getListDevice()) {
							if(dev.getCoding().equals(devCoding)) {
								initDevice(dev);
								devInDb = dev;
								break;
							}
						}
						break;
					}
				}
				//initGroup(myGroup);
			}
			eManager2.getTransaction().commit();
			device = devInDb;
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userName:" + userName + "groupName:" + groupName + "devCoding:" + devCoding);
		}
		return device;
	}
	
	public Device findDevByUserNameGroupNameDevCodingUseEManager(EntityManager eManager2, String userName, String groupName, String devCoding) {
		Device device = null;
		try {
			eManager2.getTransaction().begin();

			TypedQuery<User> query = eManager2.createQuery("from User as u where u.name=:name",
					User.class);
			query.setParameter("name", userName);
			User user1 = query.getSingleResult();
			Device devInDb = null;
			if(null != user1) {
				for(DevGroup group : user1.getListDevGroup()){
					if(group.getName().equals(groupName)) {
						for(Device dev : group.getListDevice()) {
							if(dev.getCoding().equals(devCoding)) {
								initDevice(dev);
								devInDb = dev;
								break;
							}
						}
						break;
					}
				}
				//initGroup(myGroup);
			}
			eManager2.getTransaction().commit();
			device = devInDb;
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userName:" + userName + "groupName:" + groupName + "devCoding:" + devCoding);
		}
		return device;
	}
	
	/**
	 * 获取组下的所有报警信息
	 * @param userName 用户名
	 * @param groupName 组名
	 * @return
	 */
	public List<AlarmInfo> findGroupAlarmInfo(String userName, String groupName) {
		List<AlarmInfo> listAlarmInfo = new ArrayList<>();
		EntityManager eManager2 = StartUpListener.getEntityManager();
		try {
			eManager2.getTransaction().begin();

			TypedQuery<User> query = eManager2.createQuery("from User as u where u.name=:name",
					User.class);
			query.setParameter("name", userName);
			User user1 = query.getSingleResult();
			if(null != user1) {
				for(DevGroup group : user1.getListDevGroup()){
					if(group.getName().equals(groupName)) {
						for(Device dev : group.getListDevice()) {
							listAlarmInfo.addAll(findAlarmInfo(dev));
						}
					}
				}
			}
			eManager2.getTransaction().commit();
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
			logger.error(e.getMessage() + " 说明:userName:" + userName + "groupName:" + groupName);
		}finally {
			eManager2.close();
		}
		return listAlarmInfo;
	}
	
	private void initGroup(DevGroup group) {
		if(null == group) {
			return;
		}
		for(Device dev : group.getListDevice()) {
			initDevice(dev);
		}
		for(LinkageHolder ls : group.getListLinkageHolder()) {
			for(Linkage ldv : ls.getListLinkage()) {
				for(Effect effect : ldv.getListEffect()) {
					effect.getDevice();
				}
				if(ldv instanceof SubChain) {
					for(LinkageCondition lc : ((SubChain)ldv).getListCondition()) {
						lc.getDevice();
					}
					if(ldv instanceof ZLoop) {
						for(LoopDuration ld : ((ZLoop)ldv).getListLoopDuration()) {
							ld.getOnKeepTime();
							ld.getOffKeepTime();
						}
					}
				}else if(ldv instanceof Timing) {
					for(ZTimer zTimer : ((Timing)ldv).getListZTimer()) {
						zTimer.getOnTime();
						zTimer.getOffTime();
						zTimer.getWeekHelper();
					}
				}
			}
		}
	}
	
	private List<AlarmInfo> findAlarmInfo(Device device){
		List<AlarmInfo> list = device.getListAlarmInfo();
		if(device instanceof DevHaveChild) {
			for(Device dev : ((DevHaveChild)device).getListDev()) {
				list.addAll(findAlarmInfo(dev));
			}
		}
		return list;
	}
	
	private void initDevice(Device device) {
		if(device instanceof DevHaveChild) {
			for(Device dev : ((DevHaveChild)device).getListDev()) {
				initDevice(dev);
			}
		}
	}
}
