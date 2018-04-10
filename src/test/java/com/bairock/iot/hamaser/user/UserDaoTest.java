package com.bairock.iot.hamaser.user;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bairock.iot.intelDev.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserDaoTest {

	private EntityManagerFactory em;
	
	@Before
	public void setUp() throws Exception {
		em = Persistence.createEntityManagerFactory("intelDev");
	}

	@After
	public void tearDown() throws Exception {
		em.close();
	}

	@Test
	public void test() {
		User user = new User("jack", "a123456", "444894216@qq.com", "15861295673", "hello", new Date());
		
		EntityManager eManager = em.createEntityManager();
		eManager.getTransaction().begin();
		
		eManager.persist(user);
		eManager.getTransaction().commit();
		eManager.close();
		
		EntityManager eManager2 = em.createEntityManager();
		eManager2.getTransaction().begin();
		List<User> listUser = (List<User>) eManager2.createQuery("from User", User.class).getResultList();
		for(User u : listUser){
			String json = getUserJson(u);
			System.out.println("pack: " + getUserJson(u));
			
			User UU = getUserFromJson(json);
			System.out.println("unpack: " + getUserJson(UU));
//			System.out.println("user:"+ u.toString());
		}
		eManager2.getTransaction().commit();
		eManager2.close();
		em.close();
	}
	
	private String getUserJson(User u){
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(u);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	private User getUserFromJson(String json){
		ObjectMapper mapper = new ObjectMapper();
		User user = null;
		try {
			user = mapper.readValue(json, User.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

}
