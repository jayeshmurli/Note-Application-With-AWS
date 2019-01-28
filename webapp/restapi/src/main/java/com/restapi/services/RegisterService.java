package com.restapi.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.restapi.model.Credentials;
import com.restapi.model.User;

@Service
public class RegisterService {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public User registerUser(Credentials credentials) {
		User user = new User(credentials.getUsername(), credentials.getPassword());
		this.entityManager.persist(user);
		return user;
	}
}
