package com.restapi.daos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
			attachment = new Attachment(file.getOriginalFilename(), file.getContentType(), note);
			this.entityManager.persist(attachment);
			Files.createDirectories(Paths.get(UPLOADED_FOLDER));
			filename = attachment.getId();
			Path path = Paths.get(UPLOADED_FOLDER + filename);
			Files.write(path, file.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachment;
	}
}
