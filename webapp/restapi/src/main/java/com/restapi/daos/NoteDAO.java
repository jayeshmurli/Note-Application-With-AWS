package com.restapi.daos;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.restapi.model.Note;
import com.restapi.model.User;

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
}
