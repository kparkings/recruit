package com.arenella.recruit.messaging.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the PrivateChat class 
*/
class PrivateChatTest {

	private static final UUID 					ID							= UUID.randomUUID();
	private static final String					SENDER_ID					= "User1";
	private static final String					RECIPIENT_ID				= "User2";
	private static final LocalDateTime 			CREATED						= LocalDateTime.of(2025,11,23,18,30,01);
	private static final LocalDateTime			LAST_UPDATE					= LocalDateTime.of(2025,11,24,18,30,01);
	private static final Map<UUID,ChatMessage>	REPLIES 					= Map.of(UUID.randomUUID(), ChatMessage.builder().build());
	private static final LocalDateTime			LAST_KEY_PRESS_SENDER		= LocalDateTime.of(2025,11,23,18,30,03);
	private static final LocalDateTime			LAST_KEY_PRESS_RECIPIENT	= LocalDateTime.of(2025,11,24,18,30,05);
	private static final boolean				BLOCKED_BY_SENDER			=true;
	private static final boolean				BLOCKED_BY_RECIPIENT		=false;
	private static final LocalDateTime   		LAST_VIEWED_BY_SENDER		= LocalDateTime.of(2025,11,24,18,30,05);
	private static final LocalDateTime   		LAST_VIEWED_BY_RECIPIENT	= LocalDateTime.of(2025,11,24,18,30,05);
	private static final LocalDateTime   		LAST_REMINDER_SENDER		= LocalDateTime.of(2025,12,18,18,20,21);
	private static final LocalDateTime   		LAST_REMINDER_RECIPIENT		= LocalDateTime.of(2025,12,18,18,20,22);

	/**
	* Tests construction via Builder 
	*/
	@Test
	void testBuilder() {
		
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
					.lastMissedMessageAlertSender(LAST_REMINDER_SENDER)
					.lastMissedMessageAlertRecipient(LAST_REMINDER_RECIPIENT)
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
		assertEquals(LAST_REMINDER_SENDER,chat.getLastMissedMessageAlertSender().get());
		assertEquals(LAST_REMINDER_RECIPIENT,chat.getLastMissedMessageAlertRecipient().get());
	}
	
	/**
	* Tests adding message to chat
	* LastUpdate should be updated to match the creation time
	* of the latest message
	* 
	*/
	@Test
	void testAddReply() {
		
		final LocalDateTime messageCreated = LocalDateTime.of(2025, 11, 23 , 18, 52 ,01);
		
		PrivateChat chat 			= PrivateChat.builder().build();
		ChatMessage chatMessage 	= ChatMessage.builder().senderId(SENDER_ID).recipientId(RECIPIENT_ID).created(messageCreated).build();

		chat.addReply(chatMessage);
		
		assertEquals(messageCreated, chat.getLastUpdated());
		
	}
	
	/**
	* Tests deletion of message. Id should remain so we can display the 
	* face a message was deleted to the user but the message itself 
	* needs to be removed
	*/
	@Test
	void testDeleteReply() {
		
		PrivateChat chat 			= PrivateChat.builder().id(UUID.randomUUID()).senderId(SENDER_ID).recipientId(RECIPIENT_ID).build();
		ChatMessage chatMessage1 	= ChatMessage.builder().id(UUID.randomUUID()).senderId(SENDER_ID).recipientId(RECIPIENT_ID).build();
		ChatMessage chatMessage2 	= ChatMessage.builder().id(UUID.randomUUID()).senderId(SENDER_ID).recipientId(RECIPIENT_ID).build();
		ChatMessage chatMessage3 	= ChatMessage.builder().id(UUID.randomUUID()).senderId(SENDER_ID).recipientId(RECIPIENT_ID).build();
		
		chat.addReply(chatMessage1);
		chat.addReply(chatMessage2);
		chat.addReply(chatMessage3);
		
		assertNotNull(chat.getReplies().get(chatMessage2.getId()));
		
		chat.deleteReply(chatMessage2.getId());
		
		assertNull(chat.getReplies().get(chatMessage2.getId()));
		
	}
	
	/**
	* Test update of Senders last keystroke
	*/
	@Test
	void testUpdateLastKeyPressSender() {
		
		final LocalDateTime lastKeyPress = LocalDateTime.of(2025,11,27,23,21,1);
		
		PrivateChat chat = PrivateChat.builder().lastKeyPressSender(lastKeyPress).build();
		
		assertEquals(lastKeyPress, chat.getLastKeyPressSender().get());
		
		chat.updateLastKeyPressSender();
		
		LocalDateTime lastKeyPressAfterUpdate = chat.getLastKeyPressSender().get();
		
		assertTrue(lastKeyPressAfterUpdate.isAfter(lastKeyPress));
		
	}
	
	/**
	* Test update of Receivers last keystroke
	*/
	@Test
	void testUpdateLastKeyPressRecipient() {
		
		final LocalDateTime lastKeyPress = LocalDateTime.of(2025,11,27,23,21,1);
		
		PrivateChat chat = PrivateChat.builder().lastKeyPressRecipient(lastKeyPress).build();
		
		assertEquals(lastKeyPress, chat.getLastKeyPressRecipient().get());
		
		chat.updateLastKeyPressRecipient();
		
		LocalDateTime lastKeyPressAfterUpdate = chat.getLastKeyPressRecipient().get();
		
		assertTrue(lastKeyPressAfterUpdate.isAfter(lastKeyPress));
		
	}
	
	/**
	* Test update of Senders last view
	*/
	@Test
	void testUpdateLastViewedSender() {
		
		final LocalDateTime lastView = LocalDateTime.of(2025,11,27,23,21,1);
		
		PrivateChat chat = PrivateChat.builder().lastViewedBySender(lastView).build();
		
		assertEquals(lastView, chat.getLastViewedBySender().get());
		
		chat.updateLastViewedBySender();
		
		LocalDateTime lastViewedAfterUpdate = chat.getLastViewedBySender().get();
		
		assertTrue(lastViewedAfterUpdate.isAfter(lastView));
		
	}
	
	/**
	* Test update of Recipients last view
	*/
	@Test
	void testUpdateLastViewedRecipient() {
		
		final LocalDateTime lastView = LocalDateTime.of(2025,11,27,23,21,1);
		
		PrivateChat chat = PrivateChat.builder().lastViewedByRecipient(lastView).build();
		
		assertEquals(lastView, chat.getLastViewedByRecipient().get());
		
		chat.updateLastViewedByRecipient();
		
		LocalDateTime lastViewedAfterUpdate = chat.getLastViewedByRecipient().get();
		
		assertTrue(lastViewedAfterUpdate.isAfter(lastView));
		
	}
	
	/**
	* Tests cloning where only core values are provided
	*/
	@Test
	void testCloneToBuilderWithDefaultValues() {
		
		PrivateChat chat = PrivateChat
				.builder()
					.id(ID)
					.senderId(SENDER_ID)
					.recipientId(RECIPIENT_ID)
				.build();
		
		assertEquals(ID,			chat.getId());
		assertEquals(SENDER_ID,		chat.getSenderId());
		assertEquals(RECIPIENT_ID,	chat.getRecipientId());
		assertNull(chat.getCreated());
		assertNull(chat.getLastUpdated());
		assertFalse(chat.isBlockedBySender());
		assertFalse(chat.isBlockedByRecipient());
		assertTrue(chat.getLastKeyPressSender().isEmpty());
		assertTrue(chat.getLastKeyPressRecipient().isEmpty());
		assertTrue(chat.getLastViewedBySender().isEmpty());
		assertTrue(chat.getLastViewedByRecipient().isEmpty());
		assertTrue(chat.getLastMissedMessageAlertSender().isEmpty());
		assertTrue(chat.getLastMissedMessageAlertRecipient().isEmpty());
		
		PrivateChat clone = chat.cloneToBuilder().build();
	
		assertEquals(ID,			clone.getId());
		assertEquals(SENDER_ID,		clone.getSenderId());
		assertEquals(RECIPIENT_ID,	clone.getRecipientId());
		assertNull(clone.getCreated());
		assertNull(clone.getLastUpdated());
		assertFalse(clone.isBlockedBySender());
		assertFalse(clone.isBlockedByRecipient());
		assertTrue(clone.getLastKeyPressSender().isEmpty());
		assertTrue(clone.getLastKeyPressRecipient().isEmpty());
		assertTrue(clone.getLastViewedBySender().isEmpty());
		assertTrue(clone.getLastViewedByRecipient().isEmpty());
		assertTrue(clone.getLastMissedMessageAlertSender().isEmpty());
		assertTrue(clone.getLastMissedMessageAlertRecipient().isEmpty());
		
		
	}
	
	/**
	* Tests cloning where all data is provided
	*/
	@Test
	void testCloneToBuilderWithProvidedValues() {
		
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
					.lastMissedMessageAlertSender(LAST_REMINDER_SENDER)
					.lastMissedMessageAlertRecipient(LAST_REMINDER_RECIPIENT)
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
		assertEquals(LAST_REMINDER_SENDER,		chat.getLastMissedMessageAlertSender().get());
		assertEquals(LAST_REMINDER_RECIPIENT,	chat.getLastMissedMessageAlertRecipient().get());
		
		PrivateChat clone = chat.cloneToBuilder().build();
		
		assertEquals(ID,						clone.getId());
		assertEquals(SENDER_ID,					clone.getSenderId());
		assertEquals(RECIPIENT_ID,				clone.getRecipientId());
		assertEquals(CREATED,					clone.getCreated());
		assertEquals(LAST_UPDATE,				clone.getLastUpdated());
		assertFalse(chat.getReplies().isEmpty());
		assertEquals(LAST_KEY_PRESS_SENDER,		clone.getLastKeyPressSender().get());
		assertEquals(LAST_KEY_PRESS_RECIPIENT,	clone.getLastKeyPressRecipient().get());
		assertEquals(BLOCKED_BY_SENDER,			clone.isBlockedBySender());
		assertEquals(BLOCKED_BY_RECIPIENT,		clone.isBlockedByRecipient());
		assertEquals(LAST_VIEWED_BY_SENDER,		clone.getLastViewedBySender().get());
		assertEquals(LAST_VIEWED_BY_RECIPIENT,	clone.getLastViewedByRecipient().get());
		assertEquals(LAST_REMINDER_SENDER,		clone.getLastMissedMessageAlertSender().get());
		assertEquals(LAST_REMINDER_RECIPIENT,	clone.getLastMissedMessageAlertRecipient().get());
		
	}
	
	/**
	* Test updating the lastMissedMessageAlertSender value
	* to now
	*/
	@Test
	void testUpdateLastUnreadMessageReminderSentSender() {
		
		PrivateChat chat = PrivateChat
				.builder()
				.build();
		
		assertTrue(chat.getLastMissedMessageAlertSender().isEmpty());
		
		chat.updateLastUnreadMessageReminderSentSender();
		
		assertFalse(chat.getLastMissedMessageAlertSender().isEmpty());
		
	}
	
	/**
	* Test updating the lastMissedMessageAlertRecipient value
	* to now
	*/
	@Test
	void testUpdateLastUnreadMessageReminderSentRecipient() {
		
		PrivateChat chat = PrivateChat
				.builder()
				.build();
		
		assertTrue(chat.getLastMissedMessageAlertRecipient().isEmpty());
		
		chat.updateLastUnreadMessageReminderSentRecipient();
		
		assertFalse(chat.getLastMissedMessageAlertRecipient().isEmpty());
		
	}
	
}