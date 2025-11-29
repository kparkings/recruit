package com.arenella.recruit.messaging.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.ChatMessage;
import com.arenella.recruit.messaging.beans.PrivateChat;

/**
* Unit tests for the PrivateChatEntity Class
*/
class PrivateChatEntityTest {

	private static final UUID 							ID							= UUID.randomUUID();
	private static final String							SENDER_ID					= "User1";
	private static final String							RECIPIENT_ID				= "User2";
	private static final LocalDateTime 					CREATED						= LocalDateTime.of(2025,11,23,18,30,01);
	private static final LocalDateTime					LAST_UPDATE					= LocalDateTime.of(2025,11,24,18,30,01);
	private static final Map<UUID,ChatMessageEntity>	REPLIES 					= Map.of(UUID.randomUUID(), ChatMessageEntity.builder().build());
	private static final LocalDateTime					LAST_KEY_PRESS_SENDER		= LocalDateTime.of(2025,11,23,18,30,03);
	private static final LocalDateTime					LAST_KEY_PRESS_RECIPIENT	= LocalDateTime.of(2025,11,24,18,30,05);
	private static final boolean						BLOCKED_BY_SENDER			= true;
	private static final boolean						BLOCKED_BY_RECIPIENT		= false;
	private static final LocalDateTime   				LAST_VIEWED_BY_SENDER		= LocalDateTime.of(2025,11,24,18,30,05);
	private static final LocalDateTime   				LAST_VIEWED_BY_RECIPIENT	= LocalDateTime.of(2025,11,24,18,30,05);

	/**
	* Tests construction via Builder 
	*/
	@Test
	void testBuilder() {
		
		PrivateChatEntity chat = PrivateChatEntity
				.builder()
					.id(ID)
					.senderId(SENDER_ID)
					.recipientId(RECIPIENT_ID)
					.created(CREATED)
					.lastUpdate(LAST_UPDATE)
					.replies(REPLIES)
					.lastKeyPressSender(LAST_KEY_PRESS_SENDER)
					.lastKeyPressRecipient(LAST_KEY_PRESS_RECIPIENT)
					.blockedBySender(BLOCKED_BY_SENDER)
					.blockedByRecipient(BLOCKED_BY_RECIPIENT)
					.lastViewedBySender(LAST_VIEWED_BY_SENDER)
					.lastViewedByRecipient(LAST_VIEWED_BY_RECIPIENT)
				.build();
		
		assertEquals(ID,chat.getId());
		assertEquals(SENDER_ID,chat.getSenderId());
		assertEquals(RECIPIENT_ID,chat.getRecipientId());
		assertEquals(CREATED,chat.getCreated());
		assertEquals(LAST_UPDATE,chat.getLastUpdated());
		assertFalse(chat.getReplies().isEmpty());
		assertEquals(LAST_KEY_PRESS_SENDER,chat.getLastKeyPressSender());
		assertEquals(LAST_KEY_PRESS_RECIPIENT,chat.getLastKeyPressRecipient());
		assertEquals(BLOCKED_BY_SENDER,chat.isBlockedBySender());
		assertEquals(BLOCKED_BY_RECIPIENT,chat.isBlockedByRecipient());
		assertEquals(LAST_VIEWED_BY_SENDER,chat.getLastViewedBySender());
		assertEquals(LAST_VIEWED_BY_RECIPIENT,chat.getLastViewedByRecipient());
		
	}
	
	/**
	* Tests conversion from Entity to Domain 
	* without replies
	*/
	@Test
	void testFromEntityWithoutReplies() {
		
		PrivateChatEntity entity = PrivateChatEntity
				.builder()
					.id(ID)
					.senderId(SENDER_ID)
					.recipientId(RECIPIENT_ID)
					.created(CREATED)
					.lastUpdate(LAST_UPDATE)
					.replies(REPLIES)
					.lastKeyPressSender(LAST_KEY_PRESS_SENDER)
					.lastKeyPressRecipient(LAST_KEY_PRESS_RECIPIENT)
					.blockedBySender(BLOCKED_BY_SENDER)
					.blockedByRecipient(BLOCKED_BY_RECIPIENT)
					.lastViewedBySender(LAST_VIEWED_BY_SENDER)
					.lastViewedByRecipient(LAST_VIEWED_BY_RECIPIENT)
				.build();
		
		assertEquals(ID,						entity.getId());
		assertEquals(SENDER_ID,					entity.getSenderId());
		assertEquals(RECIPIENT_ID,				entity.getRecipientId());
		assertEquals(CREATED,					entity.getCreated());
		assertEquals(LAST_UPDATE,				entity.getLastUpdated());
		assertFalse(entity.getReplies().isEmpty());
		assertEquals(LAST_KEY_PRESS_SENDER,		entity.getLastKeyPressSender());
		assertEquals(LAST_KEY_PRESS_RECIPIENT,	entity.getLastKeyPressRecipient());
		assertEquals(BLOCKED_BY_SENDER,			entity.isBlockedBySender());
		assertEquals(BLOCKED_BY_RECIPIENT,		entity.isBlockedByRecipient());
		assertEquals(LAST_VIEWED_BY_SENDER,		entity.getLastViewedBySender());
		assertEquals(LAST_VIEWED_BY_RECIPIENT,	entity.getLastViewedByRecipient());
		
		PrivateChat chat = PrivateChatEntity.fromEntityWithoutReplies(entity);
		
		assertEquals(ID,						chat.getId());
		assertEquals(SENDER_ID,					chat.getSenderId());
		assertEquals(RECIPIENT_ID,				chat.getRecipientId());
		assertEquals(CREATED,					chat.getCreated());
		assertEquals(LAST_UPDATE,				chat.getLastUpdated());
		assertTrue(chat.getReplies().isEmpty());
		assertEquals(LAST_KEY_PRESS_SENDER,		chat.getLastKeyPressSender().get());
		assertEquals(LAST_KEY_PRESS_RECIPIENT,	chat.getLastKeyPressRecipient().get());
		assertEquals(BLOCKED_BY_SENDER,			chat.isBlockedBySender());
		assertEquals(BLOCKED_BY_RECIPIENT,		chat.isBlockedByRecipient());
		assertEquals(LAST_VIEWED_BY_SENDER,		chat.getLastViewedBySender().get());
		assertEquals(LAST_VIEWED_BY_RECIPIENT,	chat.getLastViewedByRecipient().get());
		
	}
	
	/**
	* Tests conversion from Entity to Domain 
	*/
	@Test
	void fromEntity() {
		
		PrivateChatEntity entity = PrivateChatEntity
				.builder()
					.id(ID)
					.senderId(SENDER_ID)
					.recipientId(RECIPIENT_ID)
					.created(CREATED)
					.lastUpdate(LAST_UPDATE)
					.replies(REPLIES)
					.lastKeyPressSender(LAST_KEY_PRESS_SENDER)
					.lastKeyPressRecipient(LAST_KEY_PRESS_RECIPIENT)
					.blockedBySender(BLOCKED_BY_SENDER)
					.blockedByRecipient(BLOCKED_BY_RECIPIENT)
					.lastViewedBySender(LAST_VIEWED_BY_SENDER)
					.lastViewedByRecipient(LAST_VIEWED_BY_RECIPIENT)
				.build();
		
		assertEquals(ID,						entity.getId());
		assertEquals(SENDER_ID,					entity.getSenderId());
		assertEquals(RECIPIENT_ID,				entity.getRecipientId());
		assertEquals(CREATED,					entity.getCreated());
		assertEquals(LAST_UPDATE,				entity.getLastUpdated());
		assertFalse(entity.getReplies().isEmpty());
		assertEquals(LAST_KEY_PRESS_SENDER,		entity.getLastKeyPressSender());
		assertEquals(LAST_KEY_PRESS_RECIPIENT,	entity.getLastKeyPressRecipient());
		assertEquals(BLOCKED_BY_SENDER,			entity.isBlockedBySender());
		assertEquals(BLOCKED_BY_RECIPIENT,		entity.isBlockedByRecipient());
		assertEquals(LAST_VIEWED_BY_SENDER,		entity.getLastViewedBySender());
		assertEquals(LAST_VIEWED_BY_RECIPIENT,	entity.getLastViewedByRecipient());
		
		PrivateChat chat = PrivateChatEntity.fromEntity(entity);
		
		assertEquals(ID,						chat.getId());
		assertEquals(SENDER_ID,					chat.getSenderId());
		assertEquals(RECIPIENT_ID,				chat.getRecipientId());
		assertEquals(CREATED,					chat.getCreated());
		assertEquals(LAST_UPDATE,				chat.getLastUpdated());
		assertFalse(chat.getReplies().isEmpty());
		assertEquals(LAST_KEY_PRESS_SENDER,		chat.getLastKeyPressSender().get());
		assertEquals(LAST_KEY_PRESS_RECIPIENT,	chat.getLastKeyPressRecipient().get());
		assertEquals(BLOCKED_BY_SENDER,			chat.isBlockedBySender());
		assertEquals(BLOCKED_BY_RECIPIENT,		chat.isBlockedByRecipient());
		assertEquals(LAST_VIEWED_BY_SENDER,		chat.getLastViewedBySender().get());
		assertEquals(LAST_VIEWED_BY_RECIPIENT,	chat.getLastViewedByRecipient().get());
		
	}
	
	/**
	* Tests conversion from Domain to Entity 
	*/
	@Test
	void toEntity() {
		
		final Map<UUID,ChatMessage>REPLY_MSS = Map.of(UUID.randomUUID(), ChatMessage.builder().build());
		
		PrivateChat chat = PrivateChat
				.builder()
					.id(ID)
					.senderId(SENDER_ID)
					.recipientId(RECIPIENT_ID)
					.created(CREATED)
					.lastUpdate(LAST_UPDATE)
					.replies(REPLY_MSS)
					.lastKeyPressSender(LAST_KEY_PRESS_SENDER)
					.lastKeyPressRecipient(LAST_KEY_PRESS_RECIPIENT)
					.blockedBySender(BLOCKED_BY_SENDER)
					.blockedByRecipient(BLOCKED_BY_RECIPIENT)
					.lastViewedBySender(LAST_VIEWED_BY_SENDER)
					.lastViewedByRecipient(LAST_VIEWED_BY_RECIPIENT)
				.build();
		
		assertEquals(ID,						chat.getId());
		assertEquals(SENDER_ID,					chat.getSenderId());
		assertEquals(RECIPIENT_ID,				chat.getRecipientId());
		assertEquals(CREATED,					chat.getCreated());
		assertEquals(LAST_UPDATE,				chat.getLastUpdated());
		assertFalse(chat.getReplies().isEmpty());
		assertEquals(LAST_KEY_PRESS_SENDER,		chat.getLastKeyPressSender().get());
		assertEquals(LAST_KEY_PRESS_RECIPIENT,	chat.getLastKeyPressRecipient().get());
		assertEquals(BLOCKED_BY_SENDER,			chat.isBlockedBySender());
		assertEquals(BLOCKED_BY_RECIPIENT,		chat.isBlockedByRecipient());
		assertEquals(LAST_VIEWED_BY_SENDER,		chat.getLastViewedBySender().get());
		assertEquals(LAST_VIEWED_BY_RECIPIENT,	chat.getLastViewedByRecipient().get());
		
		PrivateChatEntity entity = PrivateChatEntity.toEntity(chat);
		
		assertEquals(ID,						entity.getId());
		assertEquals(SENDER_ID,					entity.getSenderId());
		assertEquals(RECIPIENT_ID,				entity.getRecipientId());
		assertEquals(CREATED,					entity.getCreated());
		assertEquals(LAST_UPDATE,				entity.getLastUpdated());
		assertFalse(entity.getReplies().isEmpty());
		assertEquals(LAST_KEY_PRESS_SENDER,		entity.getLastKeyPressSender());
		assertEquals(LAST_KEY_PRESS_RECIPIENT,	entity.getLastKeyPressRecipient());
		assertEquals(BLOCKED_BY_SENDER,			entity.isBlockedBySender());
		assertEquals(BLOCKED_BY_RECIPIENT,		entity.isBlockedByRecipient());
		assertEquals(LAST_VIEWED_BY_SENDER,		entity.getLastViewedBySender());
		assertEquals(LAST_VIEWED_BY_RECIPIENT,	entity.getLastViewedByRecipient());
		
	}
	
}