package com.restapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.daos.UserDAO;

@Service
public class LoginService 
{
	@Autowired
	private BCryptUtil bCrptUtil;
	
	@Autowired
	private UserDAO userDAO;
	
	public boolean checkUser (String userName, String password)
	{
		//System.out.println("in check user::"+password);
		String hashedPwdFromDB= this.userDAO.getStoredPasswordFromUser(userName); //get stored hash from username from MYSQL
		
		if (hashedPwdFromDB !=null)
		{
			if (bCrptUtil.verifyPassword(password, hashedPwdFromDB))
				return true;
			else 
				return false;
		}
		else
			return false;
		
	}
	
	public boolean checkIfUserExists (String userName )
	{
		int count= this.userDAO.checkIfUserExists(userName);
		if (count>0)
			return true;
		else
			return false;
		
		
	}

}
