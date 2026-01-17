package com.arenella.recruit.messaging.controllers;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.PrivateChat;

/**
* API Outbound representation of PrivateChat
* Only minimal details should be returned to prevent
* creating opportunities for attacks. 
*/
public class PrivateChatAPIOutbound {

	private UUID 								id;
	private ChatParticipantAPIOutbound			sender;
	private ChatParticipantAPIOutbound			recipient;
	private LocalDateTime 						created;
	private LocalDateTime						lastUpdated;
	private Map<UUID,ChatMessageAPIOutbound>	replies 				= new LinkedHashMap<>();
	private LocalDateTime						lastKeyPressSender;
	private LocalDateTime						lastKeyPressRecipient;
	private boolean								blockedBySender;
	private boolean								blockedByRecipient;
	private LocalDateTime   					lastViewedBySender;
	private LocalDateTime   					lastViewedByRecipient; 

	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public PrivateChatAPIOutbound(PrivateChatAPIOutboundBuilder builder) {
		this.id 					= builder.id;
		this.sender 				= builder.sender;
		this.recipient 				= builder.recipient;
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
	* Returns the Sender
	* @return unique id
	*/
	public ChatParticipantAPIOutbound getSender() {
		return this.sender;
	}
	
	/**
	* Returns the recipient
	* @return unique id
	*/
	public ChatParticipantAPIOutbound getRecipient() {
		return this.recipient;
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
	public Map<UUID, ChatMessageAPIOutbound> getReplies() {
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
	* Returns if the recipient is currently typing.
	* Based upon their last keypress
	* @return if the recipient is currently typing.
	*/
	public boolean getRecipientIsTyping() {
		
		if (this.getLastKeyPressRecipient().isEmpty()) {
			return false;
		}
		
		return this.lastKeyPressRecipient.isAfter(LocalDateTime.now().minusSeconds(5));
		
	}
	
	/**
	* Returns if the sender is currently typing.
	* Based upon their last keypress
	* @return if the sender is currently typing.
	*/
	public boolean getSenderIsTyping() {
		
		if (this.getLastKeyPressSender().isEmpty()) {
			return false;
		}
		
		return this.lastKeyPressSender.isAfter(LocalDateTime.now().minusSeconds(5));
		
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
	* Returns a builder for the Class
	* @return Builder
	*/
	public static PrivateChatAPIOutboundBuilder builder() {
		return new PrivateChatAPIOutboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class PrivateChatAPIOutboundBuilder {
		
		private UUID 								id						= UUID.randomUUID();
		private ChatParticipantAPIOutbound			sender;
		private ChatParticipantAPIOutbound			recipient;
		private LocalDateTime 						created;
		private LocalDateTime						lastUpdated;
		private Map<UUID,ChatMessageAPIOutbound>	replies 				= new LinkedHashMap<>();
		private LocalDateTime						lastKeyPressSender;
		private LocalDateTime						lastKeyPressRecipient;
		private boolean								blockedBySender;
		private boolean								blockedByRecipient;
		private LocalDateTime   					lastViewedBySender;
		private LocalDateTime   					lastViewedByRecipient;
		
		/**
		* Sets the unique id of the Chat
		* @param id - Unique id of the Chat
		* @return Builder
		*/
		public PrivateChatAPIOutboundBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the Sender that initiated the chat
		* @param senderId - Unique id
		* @return Builder
		*/
		public PrivateChatAPIOutboundBuilder sender(ChatParticipantAPIOutbound sender) {
			this.sender = sender;
			return this;
		}
		
		/**
		* Sets the Recipient of the Chat
		* @param recipientId - UniqueId
		* @return Builder
		*/
		public PrivateChatAPIOutboundBuilder recipient(ChatParticipantAPIOutbound recipient){
			this.recipient = recipient;
			return this;
		}
		
		/**
		* Sets when the Chat was created
		* @param created - When Chat was created
		* @return Builder
		*/
		public PrivateChatAPIOutboundBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Returns when the Chat was last updated
		* @param lastUpdated - moment of last update	
		* @return Builder
		*/
		public PrivateChatAPIOutboundBuilder lastUpdate(LocalDateTime lastUpdates) {
			this.lastUpdated = lastUpdates;
			return this;
		}
		
		/**
		* Returns replies to Chat
		* @param replies - Replies to the Chat
		* @return Builder
		*/
		public PrivateChatAPIOutboundBuilder replies(Map<UUID,ChatMessageAPIOutbound>	replies) {
			this.replies.clear();
			this.replies.putAll(replies);
			return this;
		}
		
		/**
		* Returns when the Sender was last typing in the Chat
		* @param lastKeyPressSender - last occurrence of typing
		* @return
		*/
		public PrivateChatAPIOutboundBuilder lastKeyPressSender(LocalDateTime lastKeyPressSender) {
			this.lastKeyPressSender = lastKeyPressSender;
			return this;
		}
		
		/**
		* Returns when the Receiver was last typing in the Chat 
		* @param lastKeyPressRecipient - last occurrence of typing
		* @return
		*/
		public PrivateChatAPIOutboundBuilder lastKeyPressRecipient(LocalDateTime lastKeyPressRecipient) {
			this.lastKeyPressRecipient = lastKeyPressRecipient;
			return this;
		}
		
		/**
		* Sets whether the Sender has blocked further messages being added to 
		* the Chat
		* @param blockedBySender - If Sender has blocked the Chat
		* @return Builder
		*/
		public PrivateChatAPIOutboundBuilder blockedBySender(boolean blockedBySender) {
			this.blockedBySender = blockedBySender;
			return this;
		}
		
		/**
		* Sets whether the Recipient has blocked further messages being added to 
		* the Chat
		* @param blockedByReceiver - If Receiver has blocked the Chat
		* @return Builder
		*/
		public PrivateChatAPIOutboundBuilder blockedByRecipient(boolean blockedByRecipient) {
			this.blockedByRecipient = blockedByRecipient;
			return this;
		}
		
		/**
		* Sets when the Chat was last viewed by the Sender
		* @param lastViewedBySender - last time viewed
		* @return Builder
		*/
		public PrivateChatAPIOutboundBuilder lastViewedBySender(LocalDateTime lastViewedBySender) {
			this.lastViewedBySender = lastViewedBySender;
			return this;
		}
		
		/**
		* Sets when the Chat was last viewed by the Received
		* @param lastViewedBySender - last time viewed
		* @return Builder
		*/
		public PrivateChatAPIOutboundBuilder lastViewedByRecipient(LocalDateTime lastViewedByRecipient) {
			this.lastViewedByRecipient = lastViewedByRecipient;
			return this;
		}
		
		/**
		* Returns an initialized instance
		* @return instance 
		*/
		public PrivateChatAPIOutbound build() {
			return new PrivateChatAPIOutbound(this);
		}
		
	}
	
	/**
	* Converts from Domain to APIOutbound representation of a 
	* PrivateChat
	* @param chat - To be converted
	* @return converted
	*/
	public static PrivateChatAPIOutbound fromDomain(PrivateChat chat, ChatParticipant sender, ChatParticipant recipient) {
		
		Map<UUID, ChatMessageAPIOutbound> replies = new LinkedHashMap<>();
		
		chat.getReplies().entrySet().forEach(es -> replies.put(es.getKey(), ChatMessageAPIOutbound.fromDomain(es.getValue())));
		
		return PrivateChatAPIOutbound
			.builder()
			.id(chat.getId())
			.sender(ChatParticipantAPIOutbound.builder().chatParticipant(sender).build())
			.recipient(ChatParticipantAPIOutbound.builder().chatParticipant(recipient).build())
			.created(chat.getCreated())
			.lastUpdate(chat.getLastUpdated())
			.lastKeyPressSender(chat.getLastKeyPressSender().orElse(null))
			.lastKeyPressRecipient(chat.getLastKeyPressRecipient().orElse(null))
			.blockedBySender(chat.isBlockedBySender())
			.blockedByRecipient(chat.isBlockedByRecipient())
			.lastViewedBySender(chat.getLastViewedBySender().orElse(null))
			.lastViewedByRecipient(chat.getLastViewedByRecipient().orElse(null))
			.replies(replies)
			.build();
	}

}