package com.restapi.controllers;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
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
		if (StringUtils.isEmpty(note.getTitle())) {
			errorResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Title", "Please Enter Title");
			return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isEmpty(note.getContent())) {
			errorResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Content", "Please Enter Content");
			return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		return this.noteService.addNewNote(message, note);
	}
	
	@RequestMapping(value = "/note", method = RequestMethod.GET)
	public ResponseEntity<Object> getNote(){
		String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		ApiResponse errorResponse;
		if (message.equals("Username does not exist") || message.equals("Invalid Credentials")) {
			errorResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message, message);
			return new ResponseEntity<Object>(errorResponse, HttpStatus.UNAUTHORIZED);
		}
		
		return this.noteService.getNotes(message);
	}
	
	@RequestMapping(value = "/note/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getNoteById(@PathVariable @NotNull Long id){
		String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		ApiResponse errorResponse;
		if (message.equals("Username does not exist") || message.equals("Invalid Credentials")) {
			errorResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message, message);
			return new ResponseEntity<Object>(errorResponse, HttpStatus.UNAUTHORIZED);
		}
		
		return this.noteService.getNoteById(message, id);
	}
	
	@RequestMapping(value = "/note/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteNote(@PathVariable("id") Long id) 
	{
		String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ApiResponse errorResponse;
		if (message.equals("Username does not exist") || message.equals("Invalid Credentials")) {
			errorResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message, message);
			return new ResponseEntity<Object>(errorResponse, HttpStatus.UNAUTHORIZED);
		}
		return this.noteService.deleteExistingNote(message, id);
	}


    @RequestMapping(value = "/note/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateNote(@RequestBody Note note,@PathVariable("id") Long id)
    {
	   String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	   ApiResponse errorResponse;
	   if (message.equals("Username does not exist") || message.equals("Invalid Credentials")) {
		errorResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message, message);
		return new ResponseEntity<Object>(errorResponse, HttpStatus.UNAUTHORIZED);
	   } 
	   if (StringUtils.isEmpty(note.getTitle())) {
			ApiResponse response = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Title", "Please Enter Title");
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	   }
	   if (StringUtils.isEmpty(note.getContent())) {
			ApiResponse response = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Content", "Please Enter Content");
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	   }
	   return this.noteService.updateExistingNote(note,message, id);
    }
}
