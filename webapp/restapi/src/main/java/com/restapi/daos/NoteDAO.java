package com.restapi.daos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.restapi.json.NoteJson;
import com.restapi.model.Note;

@Service
public class NoteDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public Note getNote(String id) {
		TypedQuery<Note> query = this.entityManager.createQuery("SELECT n from Note n where n.id = ?1",
				Note.class);
		query.setParameter(1, id);
		return query.getSingleResult();
	}
	
	@Transactional
	public Note saveNote(Note note) {
		this.entityManager.persist(note);
		return note;
	}
	
	@Transactional
	public void deleteNote(String id) 
	{
		Note noteToBeDeleted = this.entityManager.find(Note.class, id);
		this.entityManager.remove(noteToBeDeleted);
		flushAndClear();		
	}
	
	public Note getNoteFromId(String id) 
	{
		Note noteToBeDeleted = this.entityManager.find(Note.class, id);
		return noteToBeDeleted;
	}
	
	private void flushAndClear() {
	    this.entityManager.flush();
	    this.entityManager.clear();
	}
	
	@Transactional
	public Note updateNote(String id)
	{
		  Note noteToBeUpdated = this.entityManager.find(Note.class, id);
		  //Write code to update the note object here and then merge changes
		  return noteToBeUpdated;
					
	}
	
	
}
