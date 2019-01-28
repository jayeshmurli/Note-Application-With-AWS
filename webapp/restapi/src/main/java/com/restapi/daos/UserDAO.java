package com.restapi.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
}
