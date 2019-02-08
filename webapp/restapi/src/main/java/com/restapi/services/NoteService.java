package com.restapi.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.restapi.daos.NoteDAO;
import com.restapi.daos.UserDAO;
import com.restapi.json.NoteJson;
import com.restapi.model.Note;
import com.restapi.model.User;

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
}
