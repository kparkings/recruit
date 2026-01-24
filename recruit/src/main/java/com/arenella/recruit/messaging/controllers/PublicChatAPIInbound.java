package com.arenella.recruit.messaging.controllers;

import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.messaging.beans.PublicChat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* API Inbound representation of PublicChat 
*/
@JsonDeserialize(builder=PublicChatAPIInbound.PublicChatAPIInboundBuilder.class)
public class PublicChatAPIInbound {

	private UUID				parentChat;
	private String 				message;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public PublicChatAPIInbound(PublicChatAPIInboundBuilder builder) {
		this.parentChat 	= builder.parentChat;
		this.message 		= builder.message;
	}
	
	/**
	* If parent chat exists the unique id of the 
	* direct parent
	* @return id of direct parent Chat
	*/
	public Optional<UUID> getParentChat() {
		return Optional.ofNullable(this.parentChat);
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
	public static PublicChatAPIInboundBuilder builder() {
		return new PublicChatAPIInboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class PublicChatAPIInboundBuilder {
		
		private UUID				parentChat;
		private String 				message;
		
		/**
		* Populates builder with values from existing
		* PublicChat
		* @param chat - Contains initialization values
		* @return Builder
		*/
		public PublicChatAPIInboundBuilder publicChat(PublicChat chat) {
			this.parentChat 		= chat.getParentChat().orElse(null);
			this.message 			= chat.getMessage();;
			return this;
		}
		
		/**
		* Immediate parent of the Chat. If Chat has a Parent
		* @param parentChat - Id of the parten chat 
		* @return Builder
		*/
		public PublicChatAPIInboundBuilder parentChat(UUID parentChat) {
			this.parentChat = parentChat;
			return this;
		}
		
		/**
		* Sets the message text
		* @param message - message
		* @return Builder
		*/
		public PublicChatAPIInboundBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Returns an initialized instance of the class
		* @return instance
		*/
		public PublicChatAPIInbound build() {
			return new PublicChatAPIInbound(this);
		}
		
	}
	
}
