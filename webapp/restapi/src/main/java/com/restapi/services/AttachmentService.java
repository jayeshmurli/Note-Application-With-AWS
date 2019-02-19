package com.restapi.services;

import java.io.IOException;

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
			} else if (note.getCreatedBy().getUsername().equals(username)) {
				Attachment attachment = new Attachment(file.getOriginalFilename(), file.getContentType(),
						file.getBytes(), note);
				attachmentJSON = new AttachmentJSON(this.attachmentDAO.saveNote(attachment));
			} else {
				apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, "Resource not owned by user",
						"Resource not owned by user");
				return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			System.err.println(e);
		}

		return new ResponseEntity<Object>(attachmentJSON, HttpStatus.CREATED);
	}
}
