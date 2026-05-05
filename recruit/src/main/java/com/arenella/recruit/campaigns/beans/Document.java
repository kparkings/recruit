package com.arenella.recruit.campaigns.beans;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
* Represents a Document associated with either a Campaign or Role
*/
public class Document {
	
	public enum DocumentType {DOC,PDF}
	
	private UUID 			documentId; 
	private UUID 			campaignId;
	private UUID 			roleId;
	private String 			title; 
	private DocumentType 	type; 
	private byte[] 			bytes;
	private LocalDateTime 	created;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public Document(DocumentBuilder builder) {
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
	public static DocumentBuilder builder(){
		return new DocumentBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class DocumentBuilder {
		
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
		public DocumentBuilder documentId(UUID documentId) {
			this.documentId = documentId;
			return this;
		} 
		
		/**
		* Sets the Unique Id of the Campaign
		* @param campaignId - Campaign Id
		* @return Builder
		*/
		public DocumentBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* If Document is associated with a Role, sets the Id of the 
		* Role
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public DocumentBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the documents title
		* @param title - Title of the Document
		* @return Builder
		*/
		public DocumentBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the type of the Document
		* @param type - Document type
		* @return Builder
		*/
		public DocumentBuilder type(DocumentType type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the bytes of the *physical* document file
		* @param bytes - Bytes of the document
		* @return Builder
		*/
		public DocumentBuilder bytes(byte[] bytes) {
			this.bytes = bytes;
			return this;
		}
		
		/**
		* Sets when the document was created
		* @param created - Creation Date/Time
		* @return Builder
		*/
		public DocumentBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Returns an Initialized instance of a 
		* Document
		* @return initialized instance
		*/
		public Document build() {
			return new Document(this);
		}
		
	}
		
}