package com.restapi.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.restapi.model.Note;

@Service
public class NoteDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public Note getNote(Long id) {
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
	public void deleteNote(long id) 
	{
		Note noteToBeDeleted = this.entityManager.find(Note.class, id);
		this.entityManager.remove(noteToBeDeleted);
		flushAndClear();		
	}
	
	public Note getNoteFromId(long id) 
	{
		Note noteToBeDeleted = this.entityManager.find(Note.class, id);
		return noteToBeDeleted;
	}
	
	private void flushAndClear() {
	    this.entityManager.flush();
	    this.entityManager.clear();
	}
}
