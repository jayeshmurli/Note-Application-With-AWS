package com.restapi.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.restapi.model.Note;
import com.restapi.model.User;

@Service
public class NoteDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public Note saveNote(Note note) {
		this.entityManager.persist(note);
		return note;
	}
}
