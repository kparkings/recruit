package com.arenella.recruit.messaging.entities;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.arenella.recruit.messaging.beans.ChatMessage;
import com.arenella.recruit.messaging.beans.PrivateChat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
* Represents a conversation via text based messages between
* two users of the system
*/
@Entity
@Table(schema="chats", name="private_chats")
public class PrivateChatEntity {
	
	@Id
	@Column(name="id")
	private UUID 						id;
	
	@Column(name="sender_id")
	private String						senderId;
	
	@Column(name="recipient_id")
	private String						recipientId;
	
	@Column(name="created")
	private LocalDateTime 				created;
	
	@Column(name="last_updated")
	private LocalDateTime				lastUpdated;
	
	@Column(name="last_key_press_sender")
	private LocalDateTime				lastKeyPressSender;
	
	@Column(name="last_key_press_recipient")
	private LocalDateTime				lastKeyPressRecipient;
	
	@Column(name="blocked_by_sender")
	private boolean						blockedBySender;
	
	@Column(name="blocked_by_recipient")
	private boolean						blockedByRecipient;
	
	@Column(name="last_viewed_by_sender")
	private LocalDateTime   			lastViewedBySender;
	
	@Column(name="last_viewed_by_recipient")
	private LocalDateTime   			lastViewedByRecipient; 
	
	@OneToMany(mappedBy = "chatId", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
	private Set<ChatMessageEntity>	replies 				= new LinkedHashSet<>();
	
	public PrivateChatEntity() {
		//Hibernate
	}
	
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public PrivateChatEntity(PrivateChatEntityBuilder builder) {
		this.id 					= builder.id;
		this.senderId 				= builder.senderId;
		this.recipientId 			= builder.recipientId;
		this.created 				= builder.created;
		this.lastUpdated			= builder.lastUpdated;
		this.replies 				= builder.replies.values().stream().sorted(Comparator.comparing(ChatMessageEntity::getCreated)).collect(Collectors.toCollection(LinkedHashSet::new));
		this.lastKeyPressSender 	= builder.lastKeyPressSender;
		this.lastKeyPressRecipient 	= builder.lastKeyPressRecipient;
		this.blockedBySender		= builder.blockedBySender;
		this.blockedByRecipient		= builder.blockedByRecipient;
		this.lastViewedBySender 	= builder.lastViewedBySender;
		this.lastViewedByRecipient	= builder.lastViewedByRecipient;
	}
	
	/*
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
	public Map<UUID, ChatMessageEntity> getReplies() {
		
		Map<UUID, ChatMessageEntity> replies = new LinkedHashMap<>();
		
		this.replies.stream().forEach(r -> replies.put(r.getId(), r));
		
		return replies;
	}
	
	/**
	* Returns that last time the Sender was actively typing
	* in the chat
	* @return last keypress
	*/
	public LocalDateTime getLastKeyPressSender() {
		return this.lastKeyPressSender;
	}
	
	/**
	* Returns that last time the Recipient was actively typing
	* in the chat
	* @return last keypress
	*/
	public LocalDateTime getLastKeyPressRecipient() {
		return this.lastKeyPressRecipient;
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
	public LocalDateTime getLastViewedBySender() {
		return this.lastViewedBySender;
	}
	
	/**
	* Returns last time Receiver viewed the Chat
	* @return last view by Receiver
	*/
	public LocalDateTime getLastViewedByRecipient() {
		return this.lastViewedByRecipient;
	}
	
	/**
	* Returns a builder for the Class
	* @return Builder
	*/
	public static PrivateChatEntityBuilder builder() {
		return new PrivateChatEntityBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class PrivateChatEntityBuilder {
		
		private UUID 						id						= UUID.randomUUID();
		private String						senderId;
		private String						recipientId;
		private LocalDateTime 				created					= LocalDateTime.now();
		private LocalDateTime				lastUpdated				= LocalDateTime.now();
		private Map<UUID,ChatMessageEntity>	replies 				= new LinkedHashMap<>();
		private LocalDateTime				lastKeyPressSender		= LocalDateTime.now();
		private LocalDateTime				lastKeyPressRecipient	= LocalDateTime.now();
		private boolean						blockedBySender;
		private boolean						blockedByRecipient;
		private LocalDateTime   			lastViewedBySender;
		private LocalDateTime   			lastViewedByRecipient;
		
		/**
		* Sets the unique id of the Chat
		* @param id - Unique id of the Chat
		* @return Builder
		*/
		public PrivateChatEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the uniqueId of the sender that initiated the chat
		* @param senderId - Unique id
		* @return Builder
		*/
		public PrivateChatEntityBuilder senderId(String senderId) {
			this.senderId = senderId;
			return this;
		}
		
		/**
		* Sends the Recipient of the Chat
		* @param recipientId - UniqueId
		* @return Builder
		*/
		public PrivateChatEntityBuilder recipientId(String recipientId){
			this.recipientId = recipientId;
			return this;
		}
		
		/**
		* Sets when the Chat was created
		* @param created - When Chat was created
		* @return Builder
		*/
		public PrivateChatEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Returns when the Chat was last updated
		* @param lastUpdated - moment of last update	
		* @return Builder
		*/
		public PrivateChatEntityBuilder lastUpdate(LocalDateTime lastUpdates) {
			this.lastUpdated = lastUpdates;
			return this;
		}
		
		/**
		* Returns replies to Chat
		* @param replies - Replies to the Chat
		* @return Builder
		*/
		public PrivateChatEntityBuilder replies(Map<UUID,ChatMessageEntity>	replies) {
			this.replies.clear();
			this.replies.putAll(replies);
			return this;
		}
		
		/**
		* Returns when the Sender was last typing in the Chat
		* @param lastKeyPressSender - last occurrence of typing
		* @return
		*/
		public PrivateChatEntityBuilder lastKeyPressSender(LocalDateTime lastKeyPressSender) {
			this.lastKeyPressSender = lastKeyPressSender;
			return this;
		}
		
		/**
		* Returns when the Receiver was last typing in the Chat 
		* @param lastKeyPressRecipient - last occurrence of typing
		* @return
		*/
		public PrivateChatEntityBuilder lastKeyPressRecipient(LocalDateTime lastKeyPressRecipient) {
			this.lastKeyPressRecipient = lastKeyPressRecipient;
			return this;
		}
		
		/**
		* Sets whether the Sender has blocked further messages being added to 
		* the Chat
		* @param blockedBySender - If Sender has blocked the Chat
		* @return Builder
		*/
		public PrivateChatEntityBuilder blockedBySender(boolean blockedBySender) {
			this.blockedBySender = blockedBySender;
			return this;
		}
		
		/**
		* Sets whether the Recipient has blocked further messages being added to 
		* the Chat
		* @param blockedByReceiver - If Receiver has blocked the Chat
		* @return Builder
		*/
		public PrivateChatEntityBuilder blockedByRecipient(boolean blockedByRecipient) {
			this.blockedByRecipient = blockedByRecipient;
			return this;
		}
		
		/**
		* Sets when the Chat was last viewed by the Sender
		* @param lastViewedBySender - last time viewed
		* @return Builder
		*/
		public PrivateChatEntityBuilder lastViewedBySender(LocalDateTime lastViewedBySender) {
			this.lastViewedBySender = lastViewedBySender;
			return this;
		}
		
		/**
		* Sets when the Chat was last viewed by the Received
		* @param lastViewedBySender - last time viewed
		* @return Builder
		*/
		public PrivateChatEntityBuilder lastViewedByRecipient(LocalDateTime lastViewedByRecipient) {
			this.lastViewedByRecipient = lastViewedByRecipient;
			return this;
		}
		
		/**
		* Returns an initialized instance
		* @return instance 
		*/
		public PrivateChatEntity build() {
			return new PrivateChatEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - To convert
	* @return Converted
	*/
	public static PrivateChat fromEntity(PrivateChatEntity entity) {
		
		
		Map<UUID, ChatMessage> replies = new LinkedHashMap<>();
		
		entity.getReplies().values().stream().forEach(r -> replies.put(r.getId(), ChatMessageEntity.fromEntity(r)));
		
		return PrivateChat
			.builder()
				.id(entity.getId())
				.senderId(entity.getSenderId())
				.recipientId(entity.getRecipientId())
				.created(entity.getCreated())
				.lastUpdate(entity.getLastUpdated())
				.replies(replies)
				.lastKeyPressSender(entity.getLastKeyPressSender())
				.lastKeyPressRecipient(entity.getLastKeyPressRecipient())
				.blockedBySender(entity.isBlockedBySender())
				.blockedByRecipient(entity.isBlockedByRecipient())
				.lastViewedBySender(entity.getLastViewedBySender())
				.lastViewedByRecipient(entity.getLastViewedByRecipient())
			.build();
	}
	
	/**
	* Performs conversion from Entity to domain without the replies. Allowing top level chat 
	* information to be retrieved without all the messages
	* @param entity - To be converted
	* @return converted
	*/
	public static PrivateChat fromEntityWithoutReplies(PrivateChatEntity entity) {
		
		Map<UUID, ChatMessage> replies = new LinkedHashMap<>();
		
		entity.getReplies().values().stream().forEach(r -> replies.put(r.getId(), ChatMessageEntity.fromEntity(r)));
		
		return PrivateChat
			.builder()
				.id(entity.getId())
				.senderId(entity.getSenderId())
				.recipientId(entity.getRecipientId())
				.created(entity.getCreated())
				.lastUpdate(entity.getLastUpdated())
				.lastKeyPressSender(entity.getLastKeyPressSender())
				.lastKeyPressRecipient(entity.getLastKeyPressRecipient())
				.blockedBySender(entity.isBlockedBySender())
				.blockedByRecipient(entity.isBlockedByRecipient())
				.lastViewedBySender(entity.getLastViewedBySender())
				.lastViewedByRecipient(entity.getLastViewedByRecipient())
			.build();
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param entity - To convert
	* @return Converted
	*/
	public static PrivateChatEntity toEntity(PrivateChat chat) {
		
		Map<UUID, ChatMessageEntity> replies = new LinkedHashMap<>();
		
		chat.getReplies().entrySet().stream().forEach(r -> replies.put(r.getKey(), ChatMessageEntity.toEntity(r.getValue())));
		
		return PrivateChatEntity
			.builder()
				.id(chat.getId())
				.senderId(chat.getSenderId())
				.recipientId(chat.getRecipientId())
				.created(chat.getCreated())
				.lastUpdate(chat.getLastUpdated())
				.replies(replies)
				.lastKeyPressSender(chat.getLastKeyPressSender().orElse(null))
				.lastKeyPressRecipient(chat.getLastKeyPressRecipient().orElse(null))
				.blockedBySender(chat.isBlockedBySender())
				.blockedByRecipient(chat.isBlockedByRecipient())
				.lastViewedBySender(chat.getLastViewedBySender().orElse(null))
				.lastViewedByRecipient(chat.getLastViewedByRecipient().orElse(null))
			.build();
		
	}

}