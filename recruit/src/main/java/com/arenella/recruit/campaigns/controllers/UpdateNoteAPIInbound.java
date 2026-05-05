package com.arenella.recruit.campaigns.controllers;

import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Command to add a Note to an existing 
* Campaign or Role 
*/
@JsonDeserialize(builder=UpdateNoteAPIInbound.UpdateNoteAPIInboundBuilder.class)
public class UpdateNoteAPIInbound {

	private String 			title;
	private String 			text;
	
	/**
	* Constructor
	* @param builder - Contains initialization values
	*/
	public UpdateNoteAPIInbound(UpdateNoteAPIInboundBuilder builder) {
		this.title 			= builder.title;
		this.text 			= builder.text;
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
	public static UpdateNoteAPIInboundBuilder builder() {
		return new UpdateNoteAPIInboundBuilder();
	}
	
	/**
	* Builder for the Class
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class UpdateNoteAPIInboundBuilder{
	
		private String 			title;
		private String 			text;
		
		/**
		* Sets the title of the Note
		* @param title - Title of the Note
		* @return Builder
		*/
		public UpdateNoteAPIInboundBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the Body/Text of the Note
		* @param text - Not contents
		* @return Builder
		*/
		public UpdateNoteAPIInboundBuilder text(String text) {
			this.text = text;
			return this;
		}
		
		/**
		* Returns an initialized instance of the Class
		* @return Initialize instance
		*/
		public UpdateNoteAPIInbound build() {
			return new UpdateNoteAPIInbound(this);
		}
		
	}
	
}