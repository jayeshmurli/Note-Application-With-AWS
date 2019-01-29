package com.restapi.controllers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

	@RequestMapping(value = "/", method = { RequestMethod.GET })
	public ResponseEntity<String> showDate(@RequestHeader("Authorization") String bearerToken) 
	{

			String message =(String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (message!=null && message.contentEquals("User not logged in"))
				return new ResponseEntity<String>("User not logged in", HttpStatus.FORBIDDEN); 
			
			else if (message!=null && message.contentEquals("Username not entered"))
				return new ResponseEntity<String>("Username not entered", HttpStatus.BAD_REQUEST); 
			
			else if (message!=null && message.contentEquals("Password not entered"))
				return new ResponseEntity<String>("Password not entered", HttpStatus.BAD_REQUEST); 
			
			else if (message!=null && message.contentEquals("Username does not exist"))
				return new ResponseEntity<String>("Username does not exist", HttpStatus.FORBIDDEN); 
			
			else if (message!=null && message.contentEquals("Invalid Credentials"))
				return new ResponseEntity<String>("Invalid Credentials", HttpStatus.FORBIDDEN);
			
			else
				return new ResponseEntity<String>(new Date().toString(),HttpStatus.OK);

	}
}