package com.arenella.recruit.messaging.beans;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
* A single message in a chat between two 
* users of the system
*/
public class ChatMessage {

	private UUID 			id;
	private UUID 			chatId;
	private String			senderId;
	private String			recipientId;
	private LocalDateTime 	created;
	private String 			message;
	private Set<String>		likes							= new LinkedHashSet<>();
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public ChatMessage(ChatMessageBuilder builder) {
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
	* Add user like to message
	* @param userId - Unique id of the message
	*/
	public void addLike(String userId) {
		this.likes.add(userId);
	}
	
	/**
	* If present removed like for specified
	* User
	* @param userId - Unique id of user who likes the message
	*/
	public void removeLike(String userId) {
		this.likes.remove(userId);
	}
	
	/**
	* Returns a builder for the class
	* @return builder
	*/
	public static ChatMessageBuilder builder() {
		return new ChatMessageBuilder();
	}
	
	/**
	* Builder for the class
	*/
	public static class ChatMessageBuilder {
	
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
		public ChatMessageBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the unique Id of the chat the message is part of
		* @param chatId - Unique id of the chat
		* @return Builder
		*/
		public ChatMessageBuilder chatId(UUID chatId) {
			this.chatId = chatId;
			return this;
		}
		
		/**
		* Sets the unique id of the Sender of the message
		* @param senderId- Id of the Sender
		* @return Builder
		*/
		public ChatMessageBuilder senderId(String senderId) {
			this.senderId = senderId;
			return this;
		}
		
		/**
		* Sets the unique id of the Recipient of the message
		* @param recipientId - Id of the Recipient
		* @return Builder
		*/
		public ChatMessageBuilder recipientId(String recipientId) {
			this.recipientId = recipientId;
			return this;
		}
		
		/**
		* Set when message was sent
		* @param created - creation 
		* @return Builder
		*/
		public ChatMessageBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the body of the message
		* @param message - message content
		* @return Builder
		*/
		public ChatMessageBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Sets the Id's of the users who liked the message
		* @param likes - Who liked the message
		* @return Builder
		*/
		public ChatMessageBuilder likes(Set<String> likes) {
			this.likes = likes;
			return this;
		}
		
		/**
		* Returns an initialized instance
		* @return Initialized instance
		*/
		public ChatMessage build() {
			return new ChatMessage(this);
		}
		
	}
	
}