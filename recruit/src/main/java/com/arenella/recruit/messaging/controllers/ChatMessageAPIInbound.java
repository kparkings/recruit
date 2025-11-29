package com.arenella.recruit.messaging.controllers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* New Message for a Chat 
*/
@JsonDeserialize(builder=ChatMessageAPIInbound.ChatMessageAPIInboundBuilder.class)
public class ChatMessageAPIInbound {

	private String 	message;

	/**
	* Constructor base upon a Builder
	* @param builder - Contains initialization values
	*/
	public ChatMessageAPIInbound(ChatMessageAPIInboundBuilder builder) {
		this.message 	= builder.message;
	}
	
	/**
	* 
	* @return
	*/
	public String getMessage() {
		return this.message;
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder
	*/
	public static ChatMessageAPIInboundBuilder builder() {
		return new ChatMessageAPIInboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class ChatMessageAPIInboundBuilder{
	
		private String 	message;

		/**
		* Sets the message to add to the Chat
		* @param message - message
		* @return Builder
		*/
		public ChatMessageAPIInboundBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Returns a new Initialized instance
		* @return A new Initialized instance
		*/
		public ChatMessageAPIInbound build() {
			return new ChatMessageAPIInbound(this);
		}
		
	}
	
}