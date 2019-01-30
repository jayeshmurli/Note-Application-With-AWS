package com.restapi.controllers;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.response.ApiResponse;

@RestController
public class HomePageController {

	@RequestMapping(value = "/", method = { RequestMethod.GET })
	public ResponseEntity<Object> showDate(@RequestHeader("Authorization") String bearerToken) {

		String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ApiResponse apiResponse;
		if (message != null && message.contentEquals("User not logged in")) {
			apiResponse = new ApiResponse(HttpStatus.FORBIDDEN, "User not logged in", "");
			return new ResponseEntity<Object>(apiResponse, HttpStatus.FORBIDDEN);
		} else if (message != null && message.contentEquals("Username not entered")) {
			apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Username not entered", "");
			return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
		} else if (message != null && message.contentEquals("Password not entered")) {
			apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Password not entered", "");
			return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
		} else if (message != null && message.contentEquals("Username does not exist")) {
			apiResponse = new ApiResponse(HttpStatus.FORBIDDEN, "Username does not exist", "");
			return new ResponseEntity<Object>(apiResponse, HttpStatus.FORBIDDEN);
		} else if (message != null && message.contentEquals("Invalid Credentials")) {
			apiResponse = new ApiResponse(HttpStatus.FORBIDDEN, "Invalid Credentials", "");
			return new ResponseEntity<Object>(apiResponse, HttpStatus.FORBIDDEN);
		} else {
			apiResponse = new ApiResponse(HttpStatus.OK, new Date().toString(), "Success");
			return new ResponseEntity<Object>(apiResponse, new HttpHeaders(), HttpStatus.OK);
		}

	}
}