package com.restapi.services;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptUtil {
	
	// Method to generate BCrypt encrypted password  
	public String generateEncryptedPassword(String password) {
		
		// Generate a random salt of length 10
		String salt = BCrypt.gensalt(10);
		
		// Generate encrypted password using salt and plaintext password
		String encryptedPassword = BCrypt.hashpw(password, salt);
		
		return encryptedPassword;
	}


	public static boolean verifyPassword (String plainTextPassword, String storedHash)
	{
		boolean passwordVerified = false;
		passwordVerified = BCrypt.checkpw(plainTextPassword, storedHash);
		return passwordVerified;
	}

}
