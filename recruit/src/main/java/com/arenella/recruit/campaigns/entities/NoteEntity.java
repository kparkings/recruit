package com.arenella.recruit.campaigns.entities;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.Note;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Entity representation of a Campaign note 
*/
@Entity
@Table(schema="campaigns", name="notes")
public class NoteEntity {
	
	@Id
	@Column(name="id")
	private UUID			id;
	
	@Column(name="campaign_id")
	private UUID 			campaignId;
	
	@Column(name="role_id")
	private UUID 			roleId;
	
	@Column(name="created")
	private LocalDateTime 	created;
	
	@Column(name="title")
	private String 			title;
	
	@Column(name="text")
	private String 			text;
	
	/**
	* Default constructor 
	*/
	public NoteEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public NoteEntity(NoteEntityBuilder builder) {
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
	public static NoteEntityBuilder builder() {
		return new NoteEntityBuilder();
	}
	
	/**
	* Builder for the Note class 
	*/
	public static class NoteEntityBuilder {
		
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
		public NoteEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the Id of the Campaign the note is associated with
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public NoteEntityBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* If the Note is associated with a specific Role, the Id of the
		* Role
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public NoteEntityBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets when the Note was created
		* @param created - Creation Date/Time
		* @return Builder
		*/
		public NoteEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the Title of the Note
		* @param title - Title of the Note
		* @return Builder
		*/ 
		public NoteEntityBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the text of the Note
		* @param text - message/body of the Note
		* @return Builder
		*/
		public NoteEntityBuilder text(String text) {
			this.text = text;
			return this;
		}
		
		/**
		* Returns an Initialized Note
		* @return Initialized Note
		*/
		public NoteEntity build() {
			return new NoteEntity(this);
		}
		
	}

	/**
	* Converts from Entity to Domain representation
	* @param entity - To convert
	* @return Converted
	*/
	public static Note fromEntity(NoteEntity entity) {
		return Note
				.builder()
					.campaignId(entity.getCampaignId())
					.created(entity.getCreated())
					.id(entity.getId())
					.roleId(entity.getRoleId().orElse(null))
					.text(entity.getText())
					.title(entity.getTitle().orElse(null))
				.build();
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param note - To convert
	* @return converted
	*/
	public static NoteEntity toEntity(Note note) {
		return NoteEntity
				.builder()
					.campaignId(note.getCampaignId())
					.created(note.getCreated())
					.id(note.getId())
					.roleId(note.getRoleId().orElse(null))
					.text(note.getText())
					.title(note.getTitle().orElse(null))
				.build();
	}
	
}