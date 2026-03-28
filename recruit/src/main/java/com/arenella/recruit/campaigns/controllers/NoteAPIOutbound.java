package com.arenella.recruit.campaigns.controllers;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.Note;

/**
* APIOutbound representation of a Note 
*/
public class NoteAPIOutbound {

	private UUID			id;
	private LocalDateTime 	created;
	private String 			title;
	private String 			text;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public NoteAPIOutbound(NoteAPIOutboundBuilder builder) {
		this.id 			= builder.id;
		this.created 		= builder.created;
		this.title 			= builder.title;
		this.text 			= builder.text;
			
	}
	
	/**
	* Returns the Unique id of the Note
	* @return id of the Note
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns when the Note was created
	* @return Date/Time of creation
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Returns if one has been provided the Title of 
	* the Note
	* @return
	*/
	public Optional<String> getTitle() {
		return Optional.ofNullable(this.title);
	}
	
	/**
	* Returns the text of the Note
	* @return Note body
	*/
	public String getText() {
		return this.text;
	}

	/**
	* Returns a Builder for the Class
	* @return Builder
	*/
	public static NoteAPIOutboundBuilder builder() {
		return new NoteAPIOutboundBuilder();
	}
	
	/**
	* Builder for the Note class 
	*/
	public static class NoteAPIOutboundBuilder {
		
		private UUID			id;
		private LocalDateTime 	created;
		private String 			title;
		private String 			text;
		
		/**
		* Populates the Builder with the values from the Domain
		* representation of a Not
		* @param note
		* @return
		*/
		public NoteAPIOutboundBuilder from(Note note) {
			
			this.id 		= note.getId();
			this.created 	= note.getCreated();
			this.text 		= note.getText();
			
			note.getTitle().ifPresent(noteTitle -> this.title = noteTitle);
			
			return this;
			
		}
		
		/**
		* Sets the Unique Id of the Note
		* @param id - Id of the Note
		* @return Builder
		*/
		public NoteAPIOutboundBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets when the Note was created
		* @param created - Creation Date/Time
		* @return Builder
		*/
		public NoteAPIOutboundBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the Title of the Note
		* @param title - Title of the Note
		* @return Builder
		*/ 
		public NoteAPIOutboundBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the text of the Note
		* @param text - message/body of the Note
		* @return Builder
		*/
		public NoteAPIOutboundBuilder text(String text) {
			this.text = text;
			return this;
		}
		
		/**
		* Returns an Initialized Note
		* @return Initialized Note
		*/
		public NoteAPIOutbound build() {
			return new NoteAPIOutbound(this);
		}
		
	}
}