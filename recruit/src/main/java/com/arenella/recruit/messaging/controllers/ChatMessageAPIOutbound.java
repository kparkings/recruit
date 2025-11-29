package com.arenella.recruit.messaging.controllers;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.messaging.beans.ChatMessage;

/**
* API Outbound representation of a Chat Message 
*/
public class ChatMessageAPIOutbound {

	private UUID 			id;
	private UUID 			chatId;
	private String			senderId;
	private String			recipientId;
	private LocalDateTime 	created;
	private String 			message;
	private Set<String>		likes		= new LinkedHashSet<>();
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public ChatMessageAPIOutbound(ChatMessageAPIOutboundBuilder builder) {
		this.id 				= builder.id;
		this.chatId 			= builder.chatId;
		this.senderId 			= builder.senderId;
		this.recipientId 		= builder.recipientId;
		this.created 			= builder.created;
		this.message 			= builder.message;
		this.likes				= builder.likes;
	}
	
	/**
	* Returns the uniqueId of the message
	* @return messages id
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns the unique id of the chat the 
	* message belongs to
	* @return parent chat message is part of
	*/
	public UUID getChatId() {
		return this.chatId;
	}
	
	/**
	* Returns the unique id of the sender of the message
	* @return unique id of sender
	*/
	public String getSenderId() {
		return this.senderId;
	}
	
	/**
	* Returns the unique id of the recipient of the message
	* @return unique id of the recipient
	*/
	public String getRecipientId() {
		return this.recipientId;
	}
	
	/**
	* Returns when the message was sent
	* @return creation 
	*/
	public LocalDateTime getCreated(){
		return this.created;
	}
	
	/**
	* Returns the message body
	* @return message
	*/
	public String getMessage () {
		return this.message;
	}
	
	/**
	* Return all likes for the message
	* @return likes
	*/
	public Set<String> getLikes(){
		return this.likes;
	}

	/**
	* Returns a Builder for the Class
	* @return
	*/
	public static ChatMessageAPIOutboundBuilder builder() {
		return new ChatMessageAPIOutboundBuilder();
	}
	
	/**
	* Builder for the class
	*/
	public static class ChatMessageAPIOutboundBuilder{
		
		private UUID 			id;
		private UUID 			chatId;
		private String			senderId;
		private String			recipientId;
		private LocalDateTime 	created;
		private String 			message;
		private Set<String>		likes		= new LinkedHashSet<>();
		
		/**
		* 
		* @param id
		* @return
		*/
		public ChatMessageAPIOutboundBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the unique Id of the chat the message is part of
		* @param chatId - Unique id of the chat
		* @return Builder
		*/
		public ChatMessageAPIOutboundBuilder chatId(UUID chatId) {
			this.chatId = chatId;
			return this;
		}
		
		/**
		* Sets the unique id of the Sender of the message
		* @param senderId- Id of the Sender
		* @return Builder
		*/
		public ChatMessageAPIOutboundBuilder senderId(String senderId) {
			this.senderId = senderId;
			return this;
		}
		
		/**
		* Sets the unique id of the Recipient of the message
		* @param recipientId - Id of the Recipient
		* @return Builder
		*/
		public ChatMessageAPIOutboundBuilder recipientId(String recipientId) {
			this.recipientId = recipientId;
			return this;
		}
		
		/**
		* Set when message was sent
		* @param created - creation 
		* @return Builder
		*/
		public ChatMessageAPIOutboundBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the body of the message
		* @param message - message content
		* @return Builder
		*/
		public ChatMessageAPIOutboundBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Sets the Id's of the users who liked the message
		* @param likes - Who liked the message
		* @return Builder
		*/
		public ChatMessageAPIOutboundBuilder likes(Set<String> likes) {
			this.likes = likes;
			return this;
		}
		
		/**
		* Returns initialized instance
		* @return initialized with values form Builder
		*/
		public ChatMessageAPIOutbound build() {
			return new ChatMessageAPIOutbound(this);
		}
		
	}
	
	/**
	* Converts from Domain to API Outbound representation
	* @param message - To be converted
	* @return converted
	*/
	public static ChatMessageAPIOutbound fromDomain(ChatMessage message) {
		return ChatMessageAPIOutbound
			.builder()
				.chatId(message.getChatId())
				.created(message.getCreated())
				.id(message.getId())
				.likes(message.getLikes())
				.message(message.getMessage())
				.recipientId(message.getRecipientId())
				.senderId(message.getSenderId())
			.build();
	}
	
}