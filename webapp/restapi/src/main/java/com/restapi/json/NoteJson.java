package com.restapi.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.restapi.model.Note;

public class NoteJson {
	private String id;
	private String title;
	private String content;
	private Date createdOn;
	private Date lastUpdatedOn;
	private String createdById;

	public NoteJson() {

	}

	public NoteJson(Note note) {
		this.id = note.getId();
		this.title = note.getTitle();
		this.setContent(note.getContent());
		this.createdOn = note.getCreatedOn();
		this.lastUpdatedOn = note.getLastUpdatedOn();
		this.createdById = note.getCreatedBy().getUsername();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreatedOn() {
		return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(this.createdOn);
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getLastUpdatedOn() {
		return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(this.lastUpdatedOn);
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdBy) {
		this.createdById = createdBy;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
