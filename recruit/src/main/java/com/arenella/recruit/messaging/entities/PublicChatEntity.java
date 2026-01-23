package com.arenella.recruit.messaging.entities;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.messaging.beans.PublicChat;
import com.arenella.recruit.messaging.beans.PublicChat.AUDIENCE_TYPE;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

/**
* Entity representation of a PublicChat 
*/
@Entity
@Table(schema="chats", name="public_chats")
public class PublicChatEntity {
	
	@Id
	@Column(name="id")
	private UUID 				id;
	
	@Column(name="audience_type")
	@Enumerated(EnumType.STRING)
	private AUDIENCE_TYPE		audienceType;
	
	@Column(name="parent_chat")
	private UUID				parentChat;
	
	@Column(name="owner_id")
	private String				ownerId;
	
	@Column(name="created")
	private LocalDateTime 		created;
	
	@Column(name="message")
	private String 				message;
	
	@Column(name="user_id")
	@ElementCollection(targetClass=String.class, fetch = FetchType.EAGER)
	@CollectionTable(schema="chats", name="public_chat_likes", joinColumns=@JoinColumn(name="chat_id"))
	private Set<String>			likes					= new LinkedHashSet<>();
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public PublicChatEntity(PublicChatEntityBuilder builder) {
		
		this.id 			= builder.id;
		this.audienceType 	= builder.audienceType;
		this.parentChat 	= builder.parentChat;
		this.ownerId 		= builder.ownerId;
		this.created 		= builder.created;
		this.message 		= builder.message;
		this.likes			= builder.likes;
		
	}
	
	/**
	* Returns the Id of the Chat
	* @return unique id
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns what type of User can view the post
	* @return type of audience
	*/
	public AUDIENCE_TYPE getAudienceType() {
		return this.audienceType;
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
	* Returns the id of the owner/creator of the Chat
	* @return Id
	*/
	public String getOwnerId() {
		return this.ownerId;
	}
	
	/**
	* Returns when the Chat was created
	* @return when the Chat was created
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Returns the text of the message
	* @return message body of the Chat
	*/
	public String getMessage() {
		return this.message;
	}
	
	/**
	* Returns the Id's of Users who liked the Chat
	* @return Id's of liking User's
	*/
	public Set<String> getLikes() {
		return this.likes;
	}
	
	/**
	* Returns a builder for the class
	* @return
	*/
	public static PublicChatEntityBuilder builder() {
		return new PublicChatEntityBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class PublicChatEntityBuilder {
		
		private UUID 				id;
		private AUDIENCE_TYPE		audienceType;
		private UUID				parentChat;
		private String				ownerId;
		private LocalDateTime 		created;
		private String 				message;
		private Set<String>			likes					= new LinkedHashSet<>();
		
		/**
		* Sets the Unique Identifier of the Chat
		* @param id - Chat Id
		* @return Builder
		*/
		public PublicChatEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets what type of user can view the Chat. Allows for example the system 
		* to add a chat only visible to Recruiters and not Candidates
		* @param audienceType - Type of Audience
		* @return Builder
		*/
		public PublicChatEntityBuilder audienceType(AUDIENCE_TYPE audienceType) {
			this.audienceType = audienceType;
			return this;
		}
		
		/**
		* Immediate parent of the Chat. If Chat has a Parent
		* @param parentChat - Id of the parten chat 
		* @return Builder
		*/
		public PublicChatEntityBuilder parentChat(UUID parentChat) {
			this.parentChat = parentChat;
			return this;
		}
		
		/**
		* Sets the Unique Id of the Owner of the Chat.
		* @param ownerId - Who created the Message
		* @return Builder
		*/
		public PublicChatEntityBuilder ownerId(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		/**
		* Sets when the Chat was created
		* @param created - creation date
		* @return Builder
		*/
		public PublicChatEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the message text
		* @param message - message
		* @return Builder
		*/
		public PublicChatEntityBuilder message(String message) {
			this.message = message;
			return this;
		}
	
		/**
		* Sets the likes left by other Users
		* @param likes - Likes for Post
		* @return Builder
		*/
		public PublicChatEntityBuilder likes(Set<String> likes) {
			this.likes.clear();
			this.likes.addAll(likes);
			return this;
		}
		
		/**
		* Returns an initialized instance of the class
		* @return instance
		*/
		public PublicChatEntity build() {
			return new PublicChatEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - Entity representation
	* @return Domain representation
	*/
	public static PublicChat fromEntity(PublicChatEntity entity) {
		return PublicChat
				.builder()
					.audienceType(entity.getAudienceType())
					.created(entity.getCreated())
					.id(entity.getId())
					.message(entity.getMessage())
					.likes(entity.getLikes())
					.ownerId(entity.getOwnerId())
					.parentChat(entity.getParentChat().orElse(null))
				.build();
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param chat - Domain representation
	* @return Entity representation
	*/
	public static PublicChatEntity toEntity(PublicChat chat) {
		return PublicChatEntity
				.builder()
					.audienceType(chat.getAudienceType())
					.created(chat.getCreated())
					.id(chat.getId())
					.message(chat.getMessage())
					.likes(chat.getLikes())
					.ownerId(chat.getOwnerId())
					.parentChat(chat.getParentChat().orElse(null))
				.build();
	}
	
}