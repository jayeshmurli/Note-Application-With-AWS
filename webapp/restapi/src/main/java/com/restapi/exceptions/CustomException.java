package com.restapi.exceptions;

public class CustomException extends RuntimeException {
	public CustomException(String exception) {
	    super(exception);
	}
}
