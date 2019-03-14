package com.restapi.daos;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.model.Attachment;
import com.restapi.model.Note;
import com.restapi.services.NoteService;

@Service
public class NoteDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	AttachmentDAO attachmentDAO;

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
		
		System.out.println("Deleting notes attached to note");
		List<Attachment> attachments = attachmentDAO.getAttachmentFromNote(noteToBeDeleted);
		
		for (Attachment attachment : attachments)
		{
			attachmentDAO.deleteAttachment(attachment.getId());
		}
		System.out.println("DONE deleting all attachments");
		
		System.out.println("Finally Deleting note");
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
	public Note updateNote(Note note,String id)
	{
		  Note noteToBeUpdated = this.entityManager.find(Note.class, id);
		  //Write code to update the note object here and then merge changes
		  noteToBeUpdated.setTitle(note.getTitle());
		  noteToBeUpdated.setContent(note.getContent());
		  Date currentDate = new Date();
		  noteToBeUpdated.setLastUpdatedOn(currentDate);
		  flushAndClear();
		  return noteToBeUpdated;
					
	}
	
	
}
