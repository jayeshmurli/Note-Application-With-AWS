package com.restapi.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.restapi.model.User;

@Service
public class UserDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public User saveUser(User user) {
		this.entityManager.persist(user);
		return user;
	}
	
	public String getStoredPasswordFromUser(String email) 
	{
		String hashed_pw = "";
		try
		{
			Query query = this.entityManager.createQuery("SELECT u FROM User u WHERE u.username = ?1");
		    query.setParameter(1, email);
		    List<User> resultList = query.getResultList();
		    hashed_pw = resultList.get(0).getPassword();
			
		}
		catch (Exception e) {
			//System.out.println("caught exception in hashed_pw::");
			hashed_pw=null;
			
		}
		
	    //System.out.println("Returning hashed_pw::"+hashed_pw);
		return hashed_pw;
	}
	
	public int checkIfUserExists(String email) 
	{
		//System.out.println("Email is :"+email);
		int result = 0;
		try
		{
			Query query = this.entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = ?1");
			query.setParameter(1, email);
	        Long resultInLong = (Long) query.getSingleResult();
	        //System.out.println("resultInLong:"+resultInLong);
	        result = Math.toIntExact(resultInLong); 
		}
		catch (Exception e) {
			//System.out.println("Exception in checkIfUserExists:"+e.getMessage());
			result =0;
		}
		
		//System.out.println("Returning count of user::"+result);
		return result;
	}
}
