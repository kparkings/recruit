package com.arenella.recruit.messaging.controllers;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.PublicChat;
import com.arenella.recruit.messaging.beans.PublicChat.AUDIENCE_TYPE;

/**
* API Oubound representation of PublicChat 
*/
public class PublicChatAPIOutbound {

	private UUID 							id;
	private AUDIENCE_TYPE					audienceType;
	private UUID							parentChat;
	private ChatParticipantAPIOutbound		owner;
	private LocalDateTime 					created;
	private String 							message;
	private Set<String>						likes					= new LinkedHashSet<>();
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public PublicChatAPIOutbound(PublicChatAPIOutboundBuilder builder) {
		
		this.id 			= builder.id;
		this.audienceType 	= builder.audienceType;
		this.parentChat 	= builder.parentChat;
		this.owner 			= builder.owner;
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
	* Returns the id of the owner/cretor of the Chat
	* @return Id
	*/
	public ChatParticipantAPIOutbound getOwner() {
		return this.owner;
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
	public static PublicChatAPIOutboundBuilder builder() {
		return new PublicChatAPIOutboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class PublicChatAPIOutboundBuilder {
		
		private UUID 							id;
		private AUDIENCE_TYPE					audienceType;
		private UUID							parentChat;
		private ChatParticipantAPIOutbound		owner;
		private LocalDateTime 					created;
		private String 							message;
		private Set<String>						likes					= new LinkedHashSet<>();
		
		/**
		* Populates builder with values from existing
		* PublicChatAPIOutbound
		* @param chat - Contains initialization values
		* @return Builder
		*/
		public PublicChatAPIOutboundBuilder publicChat(PublicChat chat, ChatParticipant owner) {
			this.id 				= chat.getId();
			this.audienceType 		= chat.getAudienceType();
			this.parentChat 		= chat.getParentChat().orElse(null);
			this.owner 				= ChatParticipantAPIOutbound.builder().chatParticipant(owner).build();
			this.created 			= chat.getCreated();
			this.message 			= chat.getMessage();
			this.likes.clear();
			this.likes.addAll(chat.getLikes());
			return this;
		}
		
		/**
		* Sets the Unique Identifier of the Chat
		* @param id - Chat Id
		* @return Builder
		*/
		public PublicChatAPIOutboundBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets what type of user can view the Chat. Allows for example the system 
		* to add a chat only visible to Recruiters and not Candidates
		* @param audienceType - Type of Audience
		* @return Builder
		*/
		public PublicChatAPIOutboundBuilder audienceType(AUDIENCE_TYPE audienceType) {
			this.audienceType = audienceType;
			return this;
		}
		
		/**
		* Immediate parent of the Chat. If Chat has a Parent
		* @param parentChat - Id of the parten chat 
		* @return Builder
		*/
		public PublicChatAPIOutboundBuilder parentChat(UUID parentChat) {
			this.parentChat = parentChat;
			return this;
		}
		
		/**
		* Sets the Owner of the Chat.
		* @param ownerId - Who created the Message
		* @return Builder
		*/
		public PublicChatAPIOutboundBuilder owner(ChatParticipantAPIOutbound owner) {
			this.owner = owner;
			return this;
		}
		
		/**
		* Sets when the Chat was created
		* @param created - creation date
		* @return Builder
		*/
		public PublicChatAPIOutboundBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the message text
		* @param message - message
		* @return Builder
		*/
		public PublicChatAPIOutboundBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Sets the likes left by other Users
		* @param likes - Likes for Post
		* @return Builder
		*/
		public PublicChatAPIOutboundBuilder likes(Set<String> likes) {
			this.likes.clear();
			this.likes.addAll(likes);
			return this;
		}
		
		/**
		* Returns an initialized instance of the class
		* @return instance
		*/
		public PublicChatAPIOutbound build() {
			return new PublicChatAPIOutbound(this);
		}
		
	}
}
