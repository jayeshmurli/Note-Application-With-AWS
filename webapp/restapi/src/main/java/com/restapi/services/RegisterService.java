package com.restapi.services;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.daos.UserDAO;
import com.restapi.exceptions.CustomException;
import com.restapi.model.Credentials;
import com.restapi.model.User;

@Service
public class RegisterService {

	@Autowired
	private BCryptUtil bCrptUtil;

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private ValidatorUtil validUtil;

	public User registerUser(Credentials credentials) {
		if(!validUtil.verifyEmail(credentials.getUsername())){
			throw new CustomException("Invalid email address format!");
		}
		
		if (!validUtil.verifyPassword(credentials.getPassword())) {
			 throw new CustomException("Password must contain minimum 8 characters.");
		}
		
		User user = new User(credentials.getUsername(),
				this.bCrptUtil.generateEncryptedPassword(credentials.getPassword()));
		try {
			this.userDAO.saveUser(user);
		}
		catch(PersistenceException e) {
			if(e.getMessage().contains("ConstraintViolationException"));
				throw new CustomException("User with email already exists!");
		}
		return user;
	}
}
