package com.arenella.recruit.campaigns.controllers;

import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.Document.DocumentType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Command to create new Document. This contains the metadata. The bytes are 
* uploaded separately 
*/
@JsonDeserialize(builder=AddDocumentAPIInbound.AddDocumentAPIInboundBuilder.class)
public class AddDocumentAPIInbound{

	private UUID 			campaignId;
	private UUID 			roleId;
	private String 			title;
	private DocumentType 	type; 
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public AddDocumentAPIInbound(AddDocumentAPIInboundBuilder builder) {
		this.campaignId 	= builder.campaignId;
		this.roleId 		= builder.roleId;
		this.title 			= builder.title;
		this.type 			= builder.type;
	}
	
	/**
	* Returns the Id of the Campaign to add the Document to
	* @return Id of the Campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	}
	
	/**
	* If Role level Document returns the Id of the Role to 
	* add the Document to
	* @return Id of the Role
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
	}
	
	/**
	* Returns the title of the Document
	* @return title of the Document
	*/
	public String getTitle() {
		return this.title;
	}
	
	/**
	* Returns the Type of the Document
	* @return Type of the Document
	*/
	public DocumentType getType() {
		return this.type;
	} 
	
	/**
	* Return a Builder for the Class
	* @return Builder for the Class
	*/
	public static AddDocumentAPIInboundBuilder builder() {
		return new AddDocumentAPIInboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class AddDocumentAPIInboundBuilder {
		
		private UUID 			campaignId;
		private UUID 			roleId;
		private String 			title;
		private DocumentType 	type;
		
		/**
		* Sets the Id of the Campaign the Document is related to
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public AddDocumentAPIInboundBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* If a Role level Documents sets the Id of the Role the Document
		* is associated with 
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public AddDocumentAPIInboundBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the title of the Document
		* @param title - Document Title
		* @return Builder
		*/
		public AddDocumentAPIInboundBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the Type of the Document
		* @param type - File Type of the Document
		* @return Builder
		*/
		public AddDocumentAPIInboundBuilder type(DocumentType type) {
			this.type = type;
			return this;	
		}
		
		/**
		* Returns an initialized instance of the class
		* @return initialized instance
		*/
		public AddDocumentAPIInbound build() {
			return new AddDocumentAPIInbound(this);
		}
		
	}
	
}