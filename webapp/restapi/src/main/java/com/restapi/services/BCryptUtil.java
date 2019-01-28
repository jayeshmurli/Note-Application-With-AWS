package com.restapi.services;

import org.springframework.security.crypto.bcrypt.*;

public class BCryptUtil 
{
	public static boolean verifyPassword (String plainTextPassword, String storedHash)
	{
		boolean passwordVerified = false;
		passwordVerified = BCrypt.checkpw(plainTextPassword, storedHash);
		return passwordVerified;
	}

}
