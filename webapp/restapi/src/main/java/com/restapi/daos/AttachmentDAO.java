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
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.restapi.model.Attachment;
import com.restapi.model.Note;

@Service
public class AttachmentDAO {

	private static String UPLOADED_FOLDER = System.getProperty("user.dir") + "//attachments//";

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
			String fileNameWithOutExt = FilenameUtils.removeExtension(file.getOriginalFilename());
			filename = fileNameWithOutExt + "_" + new Date().getTime() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			// System.out.println("filename::"+filename);

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
			String fileNameWithOutExt = FilenameUtils.removeExtension(file.getOriginalFilename());
			filename = fileNameWithOutExt + "_" + new Date().getTime() + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
			AmazonS3Client s3Client = new AmazonS3Client(credentials);
			File tempFile = this.convert(file);
			s3Client.putObject(new PutObjectRequest(this.bucketName, filename, tempFile));
			String path = s3Client.getResourceUrl(this.bucketName, filename);
			tempFile.delete();
			attachment = new Attachment(path, file.getContentType(), note);
			this.entityManager.persist(attachment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachment;
	}

	public Attachment getAttachmentFromId(String id) {
		Attachment attachmentToBeDeleted = this.entityManager.find(Attachment.class, id);
		return attachmentToBeDeleted;
	}

	@Transactional
	public void deleteAttachment(String id) {
		Attachment attachmentToBeDeleted = this.entityManager.find(Attachment.class, id);
		this.entityManager.remove(attachmentToBeDeleted);
		flushAndClear();
	}

	private void flushAndClear() {
		this.entityManager.flush();
		this.entityManager.clear();
	}

	public boolean deleteFromMemory(Attachment attachmentToBeDeleted) {
		String path = attachmentToBeDeleted.getFileName();

		try {
			java.io.File fileToBeDeleted = new java.io.File((path));
			if (fileToBeDeleted.delete()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	private File convert(MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
		try {
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convFile;
	}
}
