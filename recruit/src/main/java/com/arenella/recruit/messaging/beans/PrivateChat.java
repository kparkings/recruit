package com.arenella.recruit.messaging.beans;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
* Represents a conversation via text based messages between
* two users of the system
*/
public class PrivateChat {
	
	private UUID 					id;
	private String					senderId;
	private String					recipientId;
	private LocalDateTime 			created;
	private LocalDateTime			lastUpdated;
	private Map<UUID,ChatMessage>	replies 				= new LinkedHashMap<>();
	private LocalDateTime			lastKeyPressSender;
	private LocalDateTime			lastKeyPressRecipient;
	private boolean					blockedBySender;
	private boolean					blockedByRecipient;
	private LocalDateTime   		lastViewedBySender;
	private LocalDateTime   		lastViewedByRecipient; //For unread = not blocked && > 0 messages with created > last viewed

	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public PrivateChat(PrivateChatBuilder builder) {
		this.id 					= builder.id;
		this.senderId 				= builder.senderId;
		this.recipientId 			= builder.recipientId;
		this.created 				= builder.created;
		this.lastUpdated			= builder.lastUpdated;
		this.replies 				= builder.replies;
		this.lastKeyPressSender 	= builder.lastKeyPressSender;
		this.lastKeyPressRecipient 	= builder.lastKeyPressRecipient;
		this.blockedBySender		= builder.blockedBySender;
		this.blockedByRecipient		= builder.blockedByRecipient;
		this.lastViewedBySender 	= builder.lastViewedBySender;
		this.lastViewedByRecipient	= builder.lastViewedByRecipient;
	}
	
	/**
	* Returns the unique Id of the Char
	* @return unique id
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns the id of the Sender
	* @return unique id
	*/
	public String getSenderId() {
		return this.senderId;
	}
	
	/**
	* Returns the id of the recipient
	* @return unique id
	*/
	public String getRecipientId() {
		return this.recipientId;
	}
	
	/**
	* Returns when the chat was created
	* @return creation moment
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Returns when the Chat was last updated
	* @return last update
	*/
	public LocalDateTime getLastUpdated() {
		return this.lastUpdated;
	}
	
	/**
	* Returns all replies to initial chat message
	* @return replies
	*/
	public Map<UUID, ChatMessage> getReplies() {
		return this.replies;
	}
	
	/**
	* Returns that last time the Sender was actively typing
	* in the chat
	* @return last keypress
	*/
	public Optional<LocalDateTime> getLastKeyPressSender() {
		return Optional.ofNullable(this.lastKeyPressSender);
	}
	
	/**
	* Returns that last time the Recipient was actively typing
	* in the chat
	* @return last keypress
	*/
	public Optional<LocalDateTime> getLastKeyPressRecipient() {
		return Optional.ofNullable(this.lastKeyPressRecipient);
	}
	
	/**
	* Returns whether the Sender has blocked
	* further contact 
	* @return whether contact has been blocked
	*/
	public boolean isBlockedBySender() {
		return this.blockedBySender;
	}
	
	/**
	* Returns whether the Recipient has blocked
	* further contact 
	* @return whether contact has been blocked
	*/
	public boolean isBlockedByRecipient() {
		return this.blockedByRecipient;
	}
	
	/**
	* Returns last time Sender viewed the Chat
	* @return last view by Sender
	*/
	public Optional<LocalDateTime> getLastViewedBySender() {
		return Optional.ofNullable(this.lastViewedBySender);
	}
	
	/**
	* Returns last time Receiver viewed the Chat
	* @return last view by Receiver
	*/
	public Optional<LocalDateTime> getLastViewedByRecipient() {
		return Optional.ofNullable(this.lastViewedByRecipient);
	}
	
	/**
	* Adds a new reply
	* @param message - reply message
	*/
	public void addReply(ChatMessage reply) {
		this.lastUpdated = reply.getCreated();
		this.replies.put(reply.getId(), reply);
	}
	
	/**
	* Removed an existing reply
	* @param replyId - Unique Id of reply 
	*/
	public void deleteReply(UUID replyId) {
		if (this.replies.keySet().contains(replyId)) {
			this.replies.put(replyId, null);
		}
	}
	
	/**
	* Updates the time the Sender was last typing in the Chat
	*/
	public void updateLastKeyPressSender() {
		this.lastKeyPressSender = LocalDateTime.now();
	}
	
	/**
	* Updates the time the Recipient was last typing in the Chat
	*/
	public void updateLastKeyPressRecipient() {
		this.lastKeyPressRecipient = LocalDateTime.now();
	}
	
	/**
	* Updates last viewed by Sender to current time
	*/
	public void updateLastViewedBySender() {
		this.lastViewedBySender = LocalDateTime.now();
	}
	
	/**
	* Updates last viewed by Recipient to current time
	*/
	public void updateLastViewedByRecipient() {
		this.lastViewedByRecipient = LocalDateTime.now();
	}
	
	/**
	* Records when the Chat was last updated 
	*/
	public void markAsUpdated() {
		this.lastUpdated = LocalDateTime.now();
	}
	
	/**
	* Returns a builder for the Class
	* @return Builder
	*/
	public static PrivateChatBuilder builder() {
		return new PrivateChatBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class PrivateChatBuilder {
		
		private UUID 					id						= UUID.randomUUID();
		private String					senderId;
		private String					recipientId;
		private LocalDateTime 			created;
		private LocalDateTime			lastUpdated;
		private Map<UUID,ChatMessage>	replies 				= new LinkedHashMap<>();
		private LocalDateTime			lastKeyPressSender;
		private LocalDateTime			lastKeyPressRecipient;
		private boolean					blockedBySender;
		private boolean					blockedByRecipient;
		private LocalDateTime   		lastViewedBySender;
		private LocalDateTime   		lastViewedByRecipient;
		
		/**
		* Sets the unique id of the Chat
		* @param id - Unique id of the Chat
		* @return Builder
		*/
		public PrivateChatBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the uniqueId of the sender that initiated the chat
		* @param senderId - Unique id
		* @return Builder
		*/
		public PrivateChatBuilder senderId(String senderId) {
			this.senderId = senderId;
			return this;
		}
		
		/**
		* Sends the Recipient of the Chat
		* @param recipientId - UniqueId
		* @return Builder
		*/
		public PrivateChatBuilder recipientId(String recipientId){
			this.recipientId = recipientId;
			return this;
		}
		
		/**
		* Sets when the Chat was created
		* @param created - When Chat was created
		* @return Builder
		*/
		public PrivateChatBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Returns when the Chat was last updated
		* @param lastUpdated - moment of last update	
		* @return Builder
		*/
		public PrivateChatBuilder lastUpdate(LocalDateTime lastUpdates) {
			this.lastUpdated = lastUpdates;
			return this;
		}
		
		/**
		* Returns replies to Chat
		* @param replies - Replies to the Chat
		* @return Builder
		*/
		public PrivateChatBuilder replies(Map<UUID,ChatMessage>	replies) {
			this.replies.clear();
			this.replies.putAll(replies);
			return this;
		}
		
		/**
		* Returns when the Sender was last typing in the Chat
		* @param lastKeyPressSender - last occurrence of typing
		* @return
		*/
		public PrivateChatBuilder lastKeyPressSender(LocalDateTime lastKeyPressSender) {
			this.lastKeyPressSender = lastKeyPressSender;
			return this;
		}
		
		/**
		* Returns when the Receiver was last typing in the Chat 
		* @param lastKeyPressRecipient - last occurrence of typing
		* @return
		*/
		public PrivateChatBuilder lastKeyPressRecipient(LocalDateTime lastKeyPressRecipient) {
			this.lastKeyPressRecipient = lastKeyPressRecipient;
			return this;
		}
		
		/**
		* Sets whether the Sender has blocked further messages being added to 
		* the Chat
		* @param blockedBySender - If Sender has blocked the Chat
		* @return Builder
		*/
		public PrivateChatBuilder blockedBySender(boolean blockedBySender) {
			this.blockedBySender = blockedBySender;
			return this;
		}
		
		/**
		* Sets whether the Recipient has blocked further messages being added to 
		* the Chat
		* @param blockedByReceiver - If Receiver has blocked the Chat
		* @return Builder
		*/
		public PrivateChatBuilder blockedByRecipient(boolean blockedByRecipient) {
			this.blockedByRecipient = blockedByRecipient;
			return this;
		}
		
		/**
		* Sets when the Chat was last viewed by the Sender
		* @param lastViewedBySender - last time viewed
		* @return Builder
		*/
		public PrivateChatBuilder lastViewedBySender(LocalDateTime lastViewedBySender) {
			this.lastViewedBySender = lastViewedBySender;
			return this;
		}
		
		/**
		* Sets when the Chat was last viewed by the Received
		* @param lastViewedBySender - last time viewed
		* @return Builder
		*/
		public PrivateChatBuilder lastViewedByRecipient(LocalDateTime lastViewedByRecipient) {
			this.lastViewedByRecipient = lastViewedByRecipient;
			return this;
		}
		
		/**
		* Returns an initialized instance
		* @return instance 
		*/
		public PrivateChat build() {
			return new PrivateChat(this);
		}
		
	}
	
	/**
	* Returns a builder with the current instances values loaded
	* @return Initialized builder
	*/
	public PrivateChatBuilder cloneToBuilder() {
		return PrivateChat
				.builder()
					.id(this.getId())
					.senderId(this.getSenderId())
					.recipientId(this.getRecipientId())
					.created(this.getCreated())
					.lastUpdate(this.getLastUpdated())
					.lastKeyPressSender(this.getLastKeyPressSender().orElse(null))
					.lastKeyPressRecipient(this.getLastKeyPressRecipient().orElse(null))
					.blockedBySender(this.isBlockedBySender())
					.blockedByRecipient(this.isBlockedByRecipient())
					.lastViewedBySender(this.getLastViewedBySender().orElse(null))
					.lastViewedByRecipient(this.getLastViewedByRecipient().orElse(null));
	}

}