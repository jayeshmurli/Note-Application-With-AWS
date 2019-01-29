package com.restapi.controllers;

import java.util.Date;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

	@RequestMapping(value = "/", method = { RequestMethod.GET })
	public String showDate(@RequestHeader("Authorization") String bearerToken) 
	{
		try
		{
			String user =(String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (user!=null && !user.equals(""))
			{
				return new Date().toString();
			}
			else
			{
				return "Error";
			}
			
		}
		catch (Exception e) {
			return "Error";
		}

		
	}
}