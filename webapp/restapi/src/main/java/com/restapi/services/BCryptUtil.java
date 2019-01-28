package com.restapi.services;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptUtil {
	
	public String generateEncryptedPassword(String password) {
		
		String salt = BCrypt.gensalt(10);
		String encryptedPassword = BCrypt.hashpw(password, salt);
		
		return encryptedPassword;
	}

}
