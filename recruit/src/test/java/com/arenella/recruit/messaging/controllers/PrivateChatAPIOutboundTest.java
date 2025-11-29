package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.ChatMessage;
import com.arenella.recruit.messaging.beans.PrivateChat;

/**
* Unit tests for the PrivateChatAPIOutbound class 
*/
class PrivateChatAPIOutboundTest {

	/**
	* Tests construction via a Builder 
	*/
	private static final UUID 								ID							= UUID.randomUUID();
	private static final String								SENDER_ID					= "User1";
	private static final String								RECIPIENT_ID				= "User2";
	private static final LocalDateTime 						CREATED						= LocalDateTime.of(2025,11,23,18,30,01);
	private static final LocalDateTime						LAST_UPDATE					= LocalDateTime.of(2025,11,24,18,30,01);
	private static final Map<UUID,ChatMessage>				REPLIES 					= Map.of(UUID.randomUUID(), ChatMessage.builder().build());
	private static final Map<UUID,ChatMessageAPIOutbound>	REPLIES_OUTBOUND 					= Map.of(UUID.randomUUID(), ChatMessageAPIOutbound.builder().build());
	private static final LocalDateTime						LAST_KEY_PRESS_SENDER		= LocalDateTime.of(2025,11,23,18,30,03);
	private static final LocalDateTime						LAST_KEY_PRESS_RECIPIENT	= LocalDateTime.of(2025,11,24,18,30,05);
	private static final boolean							BLOCKED_BY_SENDER			= true;
	private static final boolean							BLOCKED_BY_RECIPIENT		= false;
	private static final LocalDateTime   					LAST_VIEWED_BY_SENDER		= LocalDateTime.of(2025,11,24,18,30,05);
	private static final LocalDateTime   					LAST_VIEWED_BY_RECIPIENT	= LocalDateTime.of(2025,11,24,18,30,05);

	/**
	* Tests construction via Builder 
	*/
	@Test
	void testBuilder() {
		
		PrivateChatAPIOutbound chat = PrivateChatAPIOutbound
				.builder()
					.id(ID)
					.senderId(SENDER_ID)
					.recipientId(RECIPIENT_ID)
					.created(CREATED)
					.lastUpdate(LAST_UPDATE)
					.replies(REPLIES_OUTBOUND)
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
		assertEquals(LAST_KEY_PRESS_SENDER,chat.getLastKeyPressSender().get());
		assertEquals(LAST_KEY_PRESS_RECIPIENT,chat.getLastKeyPressRecipient().get());
		assertEquals(BLOCKED_BY_SENDER,chat.isBlockedBySender());
		assertEquals(BLOCKED_BY_RECIPIENT,chat.isBlockedByRecipient());
		assertEquals(LAST_VIEWED_BY_SENDER,chat.getLastViewedBySender().get());
		assertEquals(LAST_VIEWED_BY_RECIPIENT,chat.getLastViewedByRecipient().get());
		
	}
	
	/**
	* Tests conversion from Domain to APIOutbound representation
	*/
	@Test
	void testFromDomain() {
		
		PrivateChat chat = PrivateChat
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
		
		PrivateChatAPIOutbound outbound = PrivateChatAPIOutbound.fromDomain(chat);
		
		assertEquals(ID,						outbound.getId());
		assertEquals(SENDER_ID,					outbound.getSenderId());
		assertEquals(RECIPIENT_ID,				outbound.getRecipientId());
		assertEquals(CREATED,					outbound.getCreated());
		assertEquals(LAST_UPDATE,				outbound.getLastUpdated());
		assertFalse(outbound.getReplies().isEmpty());
		assertEquals(LAST_KEY_PRESS_SENDER,		outbound.getLastKeyPressSender().get());
		assertEquals(LAST_KEY_PRESS_RECIPIENT,	outbound.getLastKeyPressRecipient().get());
		assertEquals(BLOCKED_BY_SENDER,			outbound.isBlockedBySender());
		assertEquals(BLOCKED_BY_RECIPIENT,		outbound.isBlockedByRecipient());
		assertEquals(LAST_VIEWED_BY_SENDER,		outbound.getLastViewedBySender().get());
		assertEquals(LAST_VIEWED_BY_RECIPIENT,	outbound.getLastViewedByRecipient().get());
		
	}
	
}