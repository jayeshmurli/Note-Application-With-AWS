package com.restapi.daos;

import java.io.IOException;
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
}
