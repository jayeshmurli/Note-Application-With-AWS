package com.restapi.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.restapi.daos.AttachmentDAO;
import com.restapi.daos.NoteDAO;
import com.restapi.json.AttachmentJSON;
import com.restapi.model.Attachment;
import com.restapi.model.Note;
import com.restapi.response.ApiResponse;

@Service
public class AttachmentService {

	@Autowired
	NoteDAO noteDAO;

	@Autowired
	AttachmentDAO attachmentDAO;

	public ResponseEntity<Object> addAttachmenttoNote(String username, String noteId, MultipartFile file) {
		AttachmentJSON attachmentJSON = null;
		ApiResponse apiResponse = null;
		try {
			Note note = this.noteDAO.getNoteFromId(noteId);
			if (note == null) {
				apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Note not found", "Note not found");
				return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
			} else if (!note.getCreatedBy().getUsername().equals(username)) {
				apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, "Resource not owned by user",
						"Resource not owned by user");
				return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
			} else {
				attachmentJSON = new AttachmentJSON(this.attachmentDAO.saveAttachment(file, note));
			}

		} catch (Exception e) {
			System.err.println(e);
		}

		return new ResponseEntity<Object>(attachmentJSON, HttpStatus.CREATED);
	}
	
	public ResponseEntity<Object> getAttachmenttoNote(String username, String noteId) {
		List<AttachmentJSON> attachmentJSON = new ArrayList<AttachmentJSON>();
		ApiResponse apiResponse = null;
		try {
			Note note = this.noteDAO.getNoteFromId(noteId);
			if (note == null) {
				apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Note not found", "Note not found");
				return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
			} else if (!note.getCreatedBy().getUsername().equals(username)) {
				apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, "Resource not owned by user",
						"Resource not owned by user");
				return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
			} else {
				for(Attachment at : this.attachmentDAO.getAttachmentFromNote(note))
					attachmentJSON.add( new AttachmentJSON(at));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Object>(attachmentJSON, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> deleteAttachmentToNote(String username, String noteId , String attachmentId) 
	{
		ApiResponse apiResponse = null;
		try {
			Note note = this.noteDAO.getNoteFromId(noteId);
			if (note == null) {
				apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Note not found", "Note not found");
				return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
			} else if (!note.getCreatedBy().getUsername().equals(username)) {
				apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, "Resource not owned by user",
						"Resource not owned by user");
				return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
			} 
			else 
			{
				Attachment attachmentToBeDeleted = this.attachmentDAO.getAttachmentFromId(attachmentId);
				if (attachmentToBeDeleted == null) {
					apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Attachment not found", "Attachment not found");
					return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
				}else
				{
					//delete actual file from local/S3 bucket
					boolean successfullyDeleted = this.attachmentDAO.deleteFromMemory(attachmentToBeDeleted);
					if (successfullyDeleted)
						this.attachmentDAO.deleteAttachment(attachmentId);
					else
					{
						apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
								"Resource could not be deleted");
						return new ResponseEntity<Object>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Object>(null, HttpStatus.NO_CONTENT);
		
	}
	
	public ResponseEntity<Object> updateAttachmentToNote(String username, String noteId , String attachmentId,MultipartFile file) 
	{
		ApiResponse apiResponse = null;
		AttachmentJSON attachmentJSON;
		try {
			Note note = this.noteDAO.getNoteFromId(noteId);
			if (note == null) {
				apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Note not found", "Note not found");
				return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
			} else if (!note.getCreatedBy().getUsername().equals(username)) {
				apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, "Resource not owned by user",
						"Resource not owned by user");
				return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
			} 
			else 
			{
				Attachment attachmentToBeDeleted = this.attachmentDAO.getAttachmentFromId(attachmentId);
				if (attachmentToBeDeleted == null) {
					apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Attachment not found", "Attachment not found");
					return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
				}else
				{
					//delete actual file from local/S3 bucket
					boolean successfullyDeleted = this.attachmentDAO.deleteFromMemory(attachmentToBeDeleted);
					if (successfullyDeleted)
					{	this.attachmentDAO.deleteAttachment(attachmentId);
					attachmentJSON = new AttachmentJSON(this.attachmentDAO.saveAttachment(file, note));
					}
					else
					{
						apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
								"Resource could not be deleted");
						return new ResponseEntity<Object>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Object>(null, HttpStatus.NO_CONTENT);
		
	}
}
