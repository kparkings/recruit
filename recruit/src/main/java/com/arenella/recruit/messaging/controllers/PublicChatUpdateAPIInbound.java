package com.arenella.recruit.messaging.controllers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* API Inbound representation of PublicChat 
*/
@JsonDeserialize(builder=PublicChatUpdateAPIInbound.PublicChatUpdateAPIInboundBuilder.class)
public class PublicChatUpdateAPIInbound {

	private String 				message;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public PublicChatUpdateAPIInbound(PublicChatUpdateAPIInboundBuilder builder) {
		this.message 		= builder.message;
	}
	
	/**
	* Returns the text of the message
	* @return message body of the Chat
	*/
	public String getMessage() {
		return this.message;
	}
	
	/**
	* Returns a builder for the class
	* @return
	*/
	public static PublicChatUpdateAPIInboundBuilder builder() {
		return new PublicChatUpdateAPIInboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class PublicChatUpdateAPIInboundBuilder {
		
		private String 				message;
		
		/**
		* Sets the message text
		* @param message - message
		* @return Builder
		*/
		public PublicChatUpdateAPIInboundBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Returns an initialized instance of the class
		* @return instance
		*/
		public PublicChatUpdateAPIInbound build() {
			return new PublicChatUpdateAPIInbound(this);
		}
		
	}
	
}
