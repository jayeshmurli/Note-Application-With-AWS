package com.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.model.Credentials;
import com.restapi.model.User;
import com.restapi.services.RegisterService;

@RestController
public class RegisterUserController {

	@Autowired
	private RegisterService registerService;

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public User registerUser(@RequestBody Credentials credentials) {
		return this.registerService.registerUser(credentials);
	}
}
