package com.restapi.services;

import org.junit.Test;

import junit.framework.Assert;


import static org.junit.Assert.assertEquals;

public class ValidatorUtilTest {
	
		
	ValidatorUtil validUtil = new ValidatorUtil();

	@Test
	public void testVerifyPassword() {
		boolean validPassword = validUtil.verifyPassword("Password@123");
	   Assert.assertTrue(validPassword);
	}
}
