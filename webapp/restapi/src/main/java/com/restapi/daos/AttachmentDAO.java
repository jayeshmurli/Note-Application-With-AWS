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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.restapi.json.AttachmentJSON;
import com.restapi.model.Attachment;
import com.restapi.model.Note;
import com.restapi.response.ApiResponse;

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

	public void deleteAttachment( String id) {
		if (this.islocal) {
			 this.deleteAttachmentFromLocal(id);
		} else {
			 this.deleteAttachmentFromS3Bucket(id);
		}

	}

	@Transactional
	public void deleteAttachmentFromLocal(String id) 
	{
		Attachment attachmentToBeDeleted = this.entityManager.find(Attachment.class, id);
		boolean successfullyDeleted = deleteFromMemory(attachmentToBeDeleted);
		if (successfullyDeleted)
		{
			this.entityManager.remove(attachmentToBeDeleted);
			flushAndClear();
		}
	}
	
	private void flushAndClear() {
		this.entityManager.flush();
		this.entityManager.clear();
	}
	
	@Transactional
	public void deleteAttachmentFromS3Bucket(String id) 
	{
		Attachment attachmentToBeDeleted = this.entityManager.find(Attachment.class, id);
		String entirePath = attachmentToBeDeleted.getFileName();
		String filename = entirePath.substring(entirePath.lastIndexOf("/")+1);
		try {
			AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
			AmazonS3Client s3Client = new AmazonS3Client(credentials);
			s3Client.deleteObject(this.bucketName,filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try{
			this.entityManager.remove(attachmentToBeDeleted);
			flushAndClear();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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
	
	
	@Transactional
	public Attachment updateAttachment(String id, Attachment attachment,MultipartFile file, Note note) 
	{
		if (this.islocal) {
			 return this.updateAttachmentFromLocal(id, attachment, file, note);
		} else {
			 return this.updateAttachmentFromS3Bucket(id, attachment, file, note);
		}
	}
	
	@Transactional
	public Attachment updateAttachmentFromLocal(String id, Attachment attachment,MultipartFile file, Note note) 
	{
		//delete actual file from local
		boolean successfullyDeleted = deleteFromMemory(attachment);
		if (successfullyDeleted)
		{	
			deleteAttachment(id);
			saveAttachment(file, note);
		}

		//deleting entry from DB
		Attachment attachmentToBeUpdated = this.entityManager.find(Attachment.class, id);
	      attachmentToBeUpdated.setFileName(attachment.getFileName());
		  attachmentToBeUpdated.setFileType(attachment.getFileType());
		  flushAndClear();
		  return attachmentToBeUpdated;
	}
	
	public Attachment updateAttachmentFromS3Bucket(String id, Attachment attachment,MultipartFile file, Note note) 
	{
		//delete actual file from S3bucket
		Attachment attachmentToBeUpdated = this.entityManager.find(Attachment.class, id);
		String entirePath = attachmentToBeUpdated.getFileName();
		String filename = entirePath.substring(entirePath.lastIndexOf("/")+1);
		try {
			AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
			AmazonS3Client s3Client = new AmazonS3Client(credentials);
			s3Client.deleteObject(this.bucketName,filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//save new file in S3bucket
		saveAttachmentToS3Bucket(file, note);
		
		

		//deleting entry from DB
		Attachment attachmentToBeUpdated1 = this.entityManager.find(Attachment.class, id);
	      attachmentToBeUpdated1.setFileName(attachment.getFileName());
		  attachmentToBeUpdated1.setFileType(attachment.getFileType());
		  flushAndClear();
		  return attachmentToBeUpdated1;
	}
	
	
	private File convert(MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
		try {
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return convFile;
	}
}
