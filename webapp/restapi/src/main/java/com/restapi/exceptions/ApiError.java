package com.restapi.exceptions;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ApiError {
	 
    private HttpStatus status;
    private String message;
    private String error;
 
    
 
    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        this.error = error;
        //System.out.println("Test " + message + " " + error);
    }
    
    public HttpStatus getStatus() {
    	return this.status;
    }
    
    public String getMessage() {
    	return this.message;
    }
    
    public String getError() {
    	return this.error;
    }
}

