package com.arenella.recruit.campaigns.beans;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
* Class represents a Short Note that can be added to a 
* Campaign or a Role to add extra information, reminders or 
* share information between participants. 
*/
public class Note {

	private UUID			id;
	private UUID 			campaignId;
	private UUID 			roleId;
	private LocalDateTime 	created;
	private String 			title;
	private String 			text;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public Note(NoteBuilder builder) {
		this.id 			= builder.id;
		this.campaignId 	= builder.campaignId;
		this.roleId 		= builder.roleId;
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
	* Returns the Id of the Campaign the Note 
	* is associated with
	* @return Id of the campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	}
	
	/**
	* If the Note is associated with a Role returns 
	* the Id of the Role
	* @return Id of the Role
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
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
	public static NoteBuilder builder() {
		return new NoteBuilder();
	}
	
	/**
	* Builder for the Note class 
	*/
	public static class NoteBuilder {
		
		private UUID			id;
		private UUID 			campaignId;
		private UUID 			roleId;
		private LocalDateTime 	created;
		private String 			title;
		private String 			text;
		
		/**
		* Sets the Unique Id of the Note
		* @param id - Id of the Note
		* @return Builder
		*/
		public NoteBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the Id of the Campaign the note is associated with
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public NoteBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* If the Note is associated with a specific Role, the Id of the
		* Role
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public NoteBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets when the Note was created
		* @param created - Creation Date/Time
		* @return Builder
		*/
		public NoteBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the Title of the Note
		* @param title - Title of the Note
		* @return Builder
		*/ 
		public NoteBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the text of the Note
		* @param text - message/body of the Note
		* @return Builder
		*/
		public NoteBuilder text(String text) {
			this.text = text;
			return this;
		}
		
		/**
		* Returns an Initialized Note
		* @return Initialized Note
		*/
		public Note build() {
			return new Note(this);
		}
		
	}

}