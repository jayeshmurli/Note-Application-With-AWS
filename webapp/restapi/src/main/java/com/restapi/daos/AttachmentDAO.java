package com.restapi.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.restapi.model.Attachment;
import com.restapi.model.Note;

@Service
public class AttachmentDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public Attachment saveNote(Attachment attachment) {
		this.entityManager.persist(attachment);
		return attachment;
	}
}
