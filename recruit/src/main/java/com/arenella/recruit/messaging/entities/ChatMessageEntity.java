package com.arenella.recruit.messaging.entities;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.messaging.beans.ChatMessage;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

/**
* Entity representation of a ChatMessage 
*/
@Entity
@Table(schema="chats", name="chat_messages")
public class ChatMessageEntity {

	@Id
	@Column(name="id")
	private UUID 			id;
	
	@Column(name="chat_id")
	private UUID 			chatId;
	
	@Column(name="sender_id")
	private String			senderId;
	
	@Column(name="recipient_id")
	private String			recipientId;
	
	@Column(name="created")
	private LocalDateTime 	created;
	
	@Column(name="message")
	private String 			message;
	
	@Column(name="user")
	@ElementCollection(targetClass=String.class, fetch = FetchType.LAZY)
	@CollectionTable(schema="chats", name="message_likes", joinColumns=@JoinColumn(name="message_id"))
	private Set<String>		likes							= new LinkedHashSet<>();
	
	public ChatMessageEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public ChatMessageEntity(ChatMessageEntityBuilder builder) {
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
	public static ChatMessageEntityBuilder builder() {
		return new ChatMessageEntityBuilder();
	}
	
	/**
	* Builder for the class
	*/
	public static class ChatMessageEntityBuilder {
	
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
		public ChatMessageEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the unique Id of the chat the message is part of
		* @param chatId - Unique id of the chat
		* @return Builder
		*/
		public ChatMessageEntityBuilder chatId(UUID chatId) {
			this.chatId = chatId;
			return this;
		}
		
		/**
		* Sets the unique id of the Sender of the message
		* @param senderId- Id of the Sender
		* @return Builder
		*/
		public ChatMessageEntityBuilder senderId(String senderId) {
			this.senderId = senderId;
			return this;
		}
		
		/**
		* Sets the unique id of the Recipient of the message
		* @param recipientId - Id of the Recipient
		* @return Builder
		*/
		public ChatMessageEntityBuilder recipientId(String recipientId) {
			this.recipientId = recipientId;
			return this;
		}
		
		/**
		* Set when message was sent
		* @param created - creation 
		* @return Builder
		*/
		public ChatMessageEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the body of the message
		* @param message - message content
		* @return Builder
		*/
		public ChatMessageEntityBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Sets the Id's of the users who liked the message
		* @param likes - Who liked the message
		* @return Builder
		*/
		public ChatMessageEntityBuilder likes(Set<String> likes) {
			this.likes = likes;
			return this;
		}
		
		/**
		* Returns an initialized instance
		* @return Initialized instance
		*/
		public ChatMessageEntity build() {
			return new ChatMessageEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - To convert
	* @return Converted
	*/
	public static ChatMessage fromEntity(ChatMessageEntity entity) {
		return ChatMessage
				.builder()
					.id(entity.getId())
					.chatId(entity.getChatId())
					.senderId(entity.getSenderId())
					.recipientId(entity.getRecipientId())
					.created(entity.getCreated())
					.message(entity.getMessage())
				.build();
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param entity - To convert
	* @return Converted
	*/
	public static ChatMessageEntity toEntity(ChatMessage message) {
		
		return ChatMessageEntity
				.builder()
				.id(message.getId())
				.chatId(message.getChatId())
				.senderId(message.getSenderId())
				.recipientId(message.getRecipientId())
				.created(message.getCreated())
				.message(message.getMessage())
				.build();
	}
	
}