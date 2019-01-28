package com.restapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.daos.UserDAO;
import com.restapi.model.Credentials;
import com.restapi.model.User;

@Service
public class RegisterService {

	@Autowired
	private BCryptUtil bCrptUtil;

	@Autowired
	private UserDAO userDAO;

	public User registerUser(Credentials credentials) {
		User user = new User(credentials.getUsername(),
				this.bCrptUtil.generateEncryptedPassword(credentials.getPassword()));
		this.userDAO.saveUser(user);
		return user;
	}
}
