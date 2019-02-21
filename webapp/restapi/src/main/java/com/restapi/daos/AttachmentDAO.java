package com.restapi.daos;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.restapi.model.Attachment;
import com.restapi.model.Note;

@Service
public class AttachmentDAO {

	private static String UPLOADED_FOLDER = "src//main//resources//attachments//";

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public Attachment saveAttachment(MultipartFile file, Note note) {
		Attachment attachment = null;
		String filename;
		try {
			Files.createDirectories(Paths.get(UPLOADED_FOLDER));
			filename = file.getOriginalFilename() + "_" + new Date().getTime();
			Path path = Paths.get(UPLOADED_FOLDER + filename);
			Files.write(path, file.getBytes());
			attachment = new Attachment(path.toString(), file.getContentType(), note);
			this.entityManager.persist(attachment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachment;
	}
	
	public List<Attachment> getAttachmentFromNote(Note note) {
		TypedQuery<Attachment> query = this.entityManager.createQuery("SELECT a from Attachment a where a.note = ?1",
				Attachment.class);
		query.setParameter(1, note);
		return query.getResultList();
	}
	
	public Attachment getAttachmentFromId(String id) 
	{
		Attachment attachmentToBeDeleted = this.entityManager.find(Attachment.class, id);
		return attachmentToBeDeleted;
	}
	
	@Transactional
	public void deleteAttachment(String id) 
	{
		Attachment attachmentToBeDeleted = this.entityManager.find(Attachment.class, id);
		this.entityManager.remove(attachmentToBeDeleted);
		flushAndClear();		
	}
	
	private void flushAndClear() {
	    this.entityManager.flush();
	    this.entityManager.clear();
	}
	
	public boolean deleteFromMemory (Attachment attachmentToBeDeleted)
	{
		String path = attachmentToBeDeleted.getFileName();
		//System.out.println("Path from DB::"+path);
		//System.out.println("Present Project Directory : "+ System.getProperty("user.dir"));

		try {
			java.io.File fileToBeDeleted = new java.io.File((System.getProperty("user.dir")+"/"+path));
			//System.out.println(fileToBeDeleted.getCanonicalPath());
			if(fileToBeDeleted.delete()) { 
	            return true;
	        } 
	        else{ 
	            return false;
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
 
	}
}
