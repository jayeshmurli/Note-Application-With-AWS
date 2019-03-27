package com.restapi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.restapi.daos.PasswordResetDAO;
import com.restapi.response.ApiResponse;
import com.restapi.util.ValidatorUtil;

@Service
public class PasswordResetService 
{
	@Autowired
	private PasswordResetDAO passwordResetDAO;
	
	@Autowired
	private ValidatorUtil validUtil;
	
	private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
	
	
	public ResponseEntity<Object> sendResetEmail (String email)
	{
		logger.info("Sending reset email");
		if(!validUtil.verifyEmail(email))
		{
			ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST, "Invalid syntax for this request was provided.", "Not an valid email address");
			logger.error("Email validation failed");
			return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
		}
		else {
			passwordResetDAO.sendEmailToUser(email);
			logger.info("Email successfully sent");
			return new ResponseEntity<Object>(null, HttpStatus.CREATED);
		}
		
	}

}
