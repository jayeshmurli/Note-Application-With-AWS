package com.restapi.json;

import java.util.Date;

import com.restapi.model.Note;

public class NoteJson {
	private Long id;
	private String title;
	private String content;
	private Date createdOn;
	private Date lastUpdatedOn;
	private Long createdById;

	public NoteJson() {

	}

	public NoteJson(Note note) {
		this.id = note.getId();
		this.title = note.getTitle();
		this.setContent(note.getContent());
		this.createdOn = note.getCreatedOn();
		this.lastUpdatedOn = note.getLastUpdatedOn();
		this.createdById = note.getCreatedBy().getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdBy) {
		this.createdById = createdBy;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
