package com.restapi.services;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.restapi.daos.UserDAO;
import com.restapi.model.Credentials;
import com.restapi.model.User;
import com.restapi.response.ApiResponse;

@Service
public class RegisterService {

	@Autowired
	private BCryptUtil bCrptUtil;

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private ValidatorUtil validUtil;
	
	@Autowired
	private LoginService loginService;

	public ResponseEntity<Object> registerUser(Credentials credentials) {
		if(!validUtil.verifyEmail(credentials.getUsername())){
			ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST, "Not an valid email address!", "Not an valid email address");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		else if(loginService.checkIfUserExists(credentials.getUsername())) {
			ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST, "User with same email already exists!", "User with same email already exists");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		else if (!validUtil.verifyPassword(credentials.getPassword())) {
			ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST, "Password must contain minimum 8 characters!", "Password must contain minimum 8 characters");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		else {
			User user = new User(credentials.getUsername(),	this.bCrptUtil.generateEncryptedPassword(credentials.getPassword()));
			this.userDAO.saveUser(user);
		
			return new ResponseEntity<Object>(user, new HttpHeaders(), HttpStatus.OK);
		}
	}
}
