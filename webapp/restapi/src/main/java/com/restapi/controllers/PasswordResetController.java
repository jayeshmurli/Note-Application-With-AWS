package com.restapi.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.metrics.StatMetric;
import com.restapi.services.PasswordResetService;

@RestController
public class PasswordResetController 
{
	
	private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);
	
	@Autowired
	StatMetric statMetric;
	
	@Autowired
	PasswordResetService passwordResetService;
	
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public ResponseEntity<Object> registerUser(@Valid @RequestBody  String email) {
		
		logger.info("Resetting password for user");
		statMetric.increementStat("POST /reset");
		
		return this.passwordResetService.sendResetEmail(email);
	}

}
