package com.restapi.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.restapi.daos.NoteDAO;
import com.restapi.daos.UserDAO;
import com.restapi.json.NoteJson;
import com.restapi.model.Note;
import com.restapi.model.User;
import com.restapi.response.ApiResponse;

@Service
public class NoteService {
	@Autowired
	UserDAO userDAO;

	@Autowired
	NoteDAO noteDao;

	public ResponseEntity<Object> addNewNote(String username, Note note) {
		User user = this.userDAO.getUser(username);
		Note newNote = new Note();
		newNote.setContent(note.getContent());
		newNote.setTitle(note.getTitle());
		Date currentDate = new Date();
		newNote.setCreatedOn(currentDate);
		newNote.setLastUpdatedOn(currentDate);
		newNote.setCreatedBy(user);
		user.getNotes().add(newNote);
		newNote = this.noteDao.saveNote(newNote);
		return new ResponseEntity<Object>(new NoteJson(newNote), HttpStatus.CREATED);
	}
	
	public ResponseEntity<Object> getNotes(String username){
		
		User user = this.userDAO.getUser(username);
		List<NoteJson> notes = new ArrayList<NoteJson>();
		for(Note not : user.getNotes())
			notes.add(new NoteJson(not));
		
		if(notes.isEmpty()) {
			ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found for the user", "Resource not available");
			return new ResponseEntity<Object>(resp, HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<Object>(notes, HttpStatus.OK);
		}
	}
	
	public ResponseEntity<Object> getNoteById(String username, Long id){
		
		Note note;
		try {
			note = this.noteDao.getNote(id);
		}
		catch(NoResultException e){
			ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found", "Resource not available");
			return new ResponseEntity<Object>(resp, HttpStatus.NOT_FOUND);
		}
		if(!note.getCreatedBy().getUsername().equals(username)) {
			ApiResponse resp = new ApiResponse(HttpStatus.UNAUTHORIZED, "The requested resource not authorized for the user", "Resource not available");
			return new ResponseEntity<Object>(resp, HttpStatus.UNAUTHORIZED);
		}
		else {
			return new ResponseEntity<Object>(new NoteJson(note), HttpStatus.OK);
		}	
	}
}
