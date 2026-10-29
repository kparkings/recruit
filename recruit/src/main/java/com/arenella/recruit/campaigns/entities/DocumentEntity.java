package com.arenella.recruit.campaigns.entities;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.Document;
import com.arenella.recruit.campaigns.beans.Document.DocumentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Entity representation of a Document  
*/
@Entity
@Table(schema="campaigns", name="documents")
public class DocumentEntity {

	@Id
	@Column(name="id")
	private UUID 			documentId; 
	
	@Column(name="campaign_id")
	private UUID 			campaignId;
	
	@Column(name="role_id")
	private UUID 			roleId;
	
	@Column(name="title")
	private String 			title;
	
	@Column(name="type")
	private DocumentType 	type;
	
	@Column(name="bytes")
	private byte[] 			bytes;
	
	@Column(name="created")
	private LocalDateTime 	created;
	
	/**
	* Default constructor 
	*/
	public DocumentEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public DocumentEntity(DocumentEntityBuilder builder) {
		this.documentId 	= builder.documentId; 
		this.campaignId 	= builder.campaignId;
		this.roleId 		= builder.roleId;
		this.title 			= builder.title; 
		this.type 			= builder.type; 
		this.bytes 			= builder.bytes;
		this.created 		= builder.created;
	}
	
	/**
	* Returns the unique Id of the Document
	* @return Id of Document
	*/
	public UUID getDocumentId() {
		return this.documentId;
	}
	
	/**
	* Returns the Id of the Campaign the Document 
	* is associated with 
	* @return Id of the Campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	}
	
	/**
	* If the Document is at the Role level returns the Id 
	* of the Role
	* @return Role Id
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
	}
	
	/**
	* Returns the title of the Document
	* @return name/title of the Document
	*/
	public String getTitle() {
		return this.title;
	}
	
	/**
	* Returns the type of the Document
	* @return document type
	*/
	public DocumentType getType() {
		return this.type;
	} 
	
	/**
	* Returns the bytes of the *physical* document
	* @return bytes to generate the Document
	*/
	public byte[] getBytes() {
		return this.bytes;
	}
	
	/**
	* Returns when the Document was created
	* @return Creation Date/TIme
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder
	*/
	public static DocumentEntityBuilder builder(){
		return new DocumentEntityBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class DocumentEntityBuilder {
		
		private UUID 			documentId; 
		private UUID 			campaignId;
		private UUID 			roleId;
		private String 			title; 
		private DocumentType 	type; 
		private byte[] 			bytes;
		private LocalDateTime 	created;
		
		/**
		* Sets the Unique Id of the Document
		* @param documentId - Id of the Document
		* @return Builder
		*/
		public DocumentEntityBuilder documentId(UUID documentId) {
			this.documentId = documentId;
			return this;
		} 
		
		/**
		* Sets the Unique Id of the Campaign
		* @param campaignId - Campaign Id
		* @return Builder
		*/
		public DocumentEntityBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* If Document is associated with a Role, sets the Id of the 
		* Role
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public DocumentEntityBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the documents title
		* @param title - Title of the Document
		* @return Builder
		*/
		public DocumentEntityBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the type of the Document
		* @param type - Document type
		* @return Builder
		*/
		public DocumentEntityBuilder type(DocumentType type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the bytes of the *physical* document file
		* @param bytes - Bytes of the document
		* @return Builder
		*/
		public DocumentEntityBuilder bytes(byte[] bytes) {
			this.bytes = bytes;
			return this;
		}
		
		/**
		* Sets when the document was created
		* @param created - Creation Date/Time
		* @return Builder
		*/
		public DocumentEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Returns an Initialized instance of a 
		* Document
		* @return initialized instance
		*/
		public DocumentEntity build() {
			return new DocumentEntity(this);
		}
		
	}
	
	/**
	* Converts from Domain to Entity representation of a Document 
	* @param document - Document associated with a Campaign/Role
	* @return Entity representation
	*/
	public static DocumentEntity toEntity(Document document) {
		return DocumentEntity
				.builder()
					.bytes(document.getBytes())
					.campaignId(document.getCampaignId())
					.created(document.getCreated())
					.documentId(document.getDocumentId())
					.roleId(document.getRoleId().orElse(null))
					.title(document.getTitle())
					.type(document.getType())
				.build();
	}
	
	/**
	* Converts from Entity to Domain representation of a Document
	* @param entity - To convert
	* @return converted
	*/
	public static Document fromEntity(DocumentEntity entity) {
		return Document
				.builder()
					.bytes(entity.getBytes())
					.campaignId(entity.getCampaignId())
					.created(entity.getCreated())
					.documentId(entity.getDocumentId())
					.roleId(entity.getRoleId().orElse(null))
					.title(entity.getTitle())
					.type(entity.getType())
				.build();
	}
	
}