package com.restapi.controllers;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/", method = { RequestMethod.GET })
	public String showDate() {
		return new Date().toLocaleString();
	}
}
