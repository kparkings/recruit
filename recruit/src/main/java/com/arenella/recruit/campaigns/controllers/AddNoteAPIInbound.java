package com.arenella.recruit.campaigns.controllers;

import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Command to add a Note to an existing 
* Campaign or Role 
*/
@JsonDeserialize(builder=AddNoteAPIInbound.AddNoteAPIInboundBuilder.class)
public class AddNoteAPIInbound {

	private UUID 			campaignId;
	private UUID 			roleId;
	private String 			title;
	private String 			text;
	
	/**
	* Constructor
	* @param builder - Contains initialization values
	*/
	public AddNoteAPIInbound(AddNoteAPIInboundBuilder builder) {
		this.campaignId 	= builder.campaignId;
		this.roleId 		= builder.roleId;
		this.title 			= builder.title;
		this.text 			= builder.text;
	}
	
	/**
	* Returns the unique Id of the Campaign the Note
	* related to
	* @return Id of the Campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	}
	
	/**
	* If a Role level note, returns the unique Id of the Role the Note
	* is related to
	* @return Id of the Role
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
	}
	
	/**
	* Returns, if provided the Notes title 
	* @return Title for the Note
	*/
	public Optional<String> getTitle() {
		return Optional.ofNullable(this.title);
	}
	
	/**
	* Returns the body/text of the Note
	* @return Note text
	*/
	public String getText() {
		return this.text;
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder for the Class
	*/
	public static AddNoteAPIInboundBuilder builder() {
		return new AddNoteAPIInboundBuilder();
	}
	
	/**
	* Builder for the Class
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class AddNoteAPIInboundBuilder{
	
		private UUID 			campaignId;
		private UUID 			roleId;
		private String 			title;
		private String 			text;
		
		/**
		* Sets the Id of the Campaign the Note is associated with
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public AddNoteAPIInboundBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* If Note is on the Role level sets the Id of the Role the Note
		* is associated with 
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public AddNoteAPIInboundBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the title of the Note
		* @param title - Title of the Note
		* @return Builder
		*/
		public AddNoteAPIInboundBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the Body/Text of the Note
		* @param text - Not contents
		* @return Builder
		*/
		public AddNoteAPIInboundBuilder text(String text) {
			this.text = text;
			return this;
		}
		
		/**
		* Returns an initialized instance of the Class
		* @return Initialize instance
		*/
		public AddNoteAPIInbound build() {
			return new AddNoteAPIInbound(this);
		}
		
	}
	
}