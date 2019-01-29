package com.restapi.exceptions;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomRestExceptionHandler {
	
	@ExceptionHandler({ PersistenceException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
	    String errors = "";
	    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
	        errors = errors + violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage();
	    }
	 
	    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
	    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	@ExceptionHandler({ CustomException.class })
	public ResponseEntity<Object> handleCustomException(CustomException ex, WebRequest request) {
	    List<String> errors = new ArrayList<String>();
	    
	    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage());
	    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}
}
