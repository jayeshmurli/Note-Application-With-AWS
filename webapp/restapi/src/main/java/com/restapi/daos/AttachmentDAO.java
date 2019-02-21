package com.restapi.daos;

import java.io.File;
import java.io.FileOutputStream;
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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.restapi.model.Attachment;
import com.restapi.model.Note;

@Service
public class AttachmentDAO {

	private static String UPLOADED_FOLDER = "//attachments//";

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${cloud.islocal}")
	private boolean islocal;

	@Value("${cloud.bucketName}")
	private String bucketName;

	@Transactional
	public Attachment saveAttachment(MultipartFile file, Note note) {
		System.out.println(this.islocal);
		if (this.islocal) {
			return this.saveAttachmentToLocal(file, note);
		} else {
			return this.saveAttachmentToS3Bucket(file, note);
		}

	}

	public List<Attachment> getAttachmentFromNote(Note note) {
		TypedQuery<Attachment> query = this.entityManager.createQuery("SELECT a from Attachment a where a.note = ?1",
				Attachment.class);
		query.setParameter(1, note);
		return query.getResultList();
	}

	private Attachment saveAttachmentToLocal(MultipartFile file, Note note) {
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

	private Attachment saveAttachmentToS3Bucket(MultipartFile file, Note note) {
		Attachment attachment = null;
		String filename;
		try {
			filename = file.getOriginalFilename() + "_" + new Date().getTime();
			AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
			AmazonS3 s3Client = new AmazonS3Client(credentials);
			File tempFile = this.convert(file);
			s3Client.putObject(new PutObjectRequest(this.bucketName, filename, tempFile));
			tempFile.delete();
//			Files.createDirectories(Paths.get(UPLOADED_FOLDER));
//			Path path = Paths.get(UPLOADED_FOLDER + filename);
//			Files.write(path, file.getBytes());
//			attachment = new Attachment(path.toString(), file.getContentType(), note);
//			this.entityManager.persist(attachment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachment;
	}

	private File convert(MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
		try {
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convFile;
	}
}
