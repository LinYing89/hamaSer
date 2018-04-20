package com.bairock.iot.hamaser.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.bairock.iot.hamaser.listener.SessionHelper;
import com.bairock.iot.hamaser.listener.StartUpListener;
import com.bairock.iot.intelDev.device.DevHaveChild;
import com.bairock.iot.intelDev.device.Device;
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
		EntityManager eManager2 = StartUpListener.getEntityManager();
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
		} finally {
			eManager2.close();
		}
		return res;
	}

	public String findPsdByName(String userName) {
		String res = null;
		EntityManager eManager2 = StartUpListener.getEntityManager();
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
		} finally {
			eManager2.close();
		}
		return res;
	}

	public User findByNameAndPsd(String name, String psd) {
		User user = null;
		EntityManager eManager2 = SessionHelper.getEntityManager(name);
		try {
			eManager2.getTransaction().begin();

			TypedQuery<User> query = eManager2.createQuery("from User as u where u.name=:name and u.psd=:psd",
					User.class);
			query.setParameter("name", name);
			query.setParameter("psd", psd);
			user = query.getSingleResult();
			user.getListDevGroup();
			eManager2.getTransaction().commit();
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
		}
		return user;
	}

	public String findDevGroupPetName(String userName, String devGroupName, String devGroupPsd) {
		String res = null;
		EntityManager eManager2 = StartUpListener.getEntityManager();
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
		} finally {
			eManager2.close();
		}
		//eManager2.close();
		return res;
	}
	
	public User findByUserName(String userName) {
		User res = null;
		EntityManager eManager2 = StartUpListener.getEntityManager();
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
		} finally {
			eManager2.close();
		}
		return res;
	}
	
	public User findByUserNameInit(String userName) {
		User res = null;
		EntityManager eManager2 = StartUpListener.getEntityManager();
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
		} finally {
			eManager2.close();
		}
		return res;
	}

	public boolean add(User user) {
		boolean res = false;
		EntityManager eManager2 = StartUpListener.getEntityManager();
		try {
			eManager2.getTransaction().begin();
			eManager2.persist(user);
			eManager2.getTransaction().commit();
			res = true;
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			res = false;
		}finally {
			eManager2.close();
		}
		return res;
	}
	
	public User findUserInit(String userName, String groupName) {
		User user = null;
		EntityManager eManager2 = StartUpListener.getEntityManager();
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
//			user = new User();
//			user.setName(user1.getName());
//			user.addGroup(myGroup);
//			user.getListDevGroup().clear();
//			user.addGroup(myGroup);
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
		}
		return user;
	}
	
	public Device findDeviceByUserNameGroupNameDevCoding(String userName, String groupName, String devCoding) {
		Device device = null;
		EntityManager eManager2 = StartUpListener.getEntityManager();
		try {
			eManager2.getTransaction().begin();

			TypedQuery<User> query = eManager2.createQuery("from User as u where u.name=:name",
					User.class);
			query.setParameter("name", userName);
			User user1 = query.getSingleResult();
			DevGroup myGroup = null;
			if(null != user1) {
				for(DevGroup group : user1.getListDevGroup()){
					if(group.getName().equals(groupName)) {
						myGroup = group;
						for(Device dev : group.getListDevice()) {
							if(dev.getCoding().equals(devCoding)) {
								device = dev;
								initDevice(device);
								break;
							}
						}
						break;
					}
				}
				//initGroup(myGroup);
			}
			eManager2.getTransaction().commit();
			if(null == myGroup || null == device) {
				return null;
			}
			User user = new User();
			user.setName(user1.getName());
			DevGroup group = new DevGroup();
			group.setName(myGroup.getName());
			user.addGroup(group);
			group.addDevice(device);
//			user.getListDevGroup().clear();
//			user.addGroup(myGroup);
		} catch (Exception e) {
			eManager2.getTransaction().rollback();
			e.printStackTrace();
		}
		return device;
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
	
	private void initDevice(Device device) {
		if(device instanceof DevHaveChild) {
			for(Device dev : ((DevHaveChild)device).getListDev()) {
				initDevice(dev);
			}
		}
	}
	
//	public boolean update(User user) {
//		boolean res = false;
//		EntityManager eManager2 = SessionHelper.getEntityManager(user.getName());
//		try {
//			eManager2.getTransaction().begin();
//			eManager2.merge(user);
//			eManager2.getTransaction().commit();
//			res = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			eManager2.getTransaction().rollback();
//		} 
//		return res;
//	}
}