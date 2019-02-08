package com.restapi.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.model.Note;
import com.restapi.response.ApiResponse;
import com.restapi.services.NoteService;

@RestController
public class NoteController {
	
	@Autowired
	NoteService noteService;
	
	@RequestMapping(value = "/note", method = RequestMethod.POST)
	public ResponseEntity<Object> addNote(@RequestBody Note note) {
		String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ApiResponse errorResponse;
		if (message.equals("Username does not exist") || message.equals("Invalid Credentials")) {
			errorResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message, message);
			return new ResponseEntity<Object>(errorResponse, HttpStatus.UNAUTHORIZED);
		}
		return this.noteService.addNewNote(message, note);
	}
}
