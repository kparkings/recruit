package com.arenella.recruit.candidates.messaging.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.ChatMessage;


/**
* Unit tests for the ChatMessage class 
*/
class ChatMessageTest {

	private static final UUID 			ID 				= UUID.randomUUID();
	private static final UUID 			CHAT_ID 		= UUID.randomUUID();
	private static final String			SENDER_ID		= "user1";
	private static final String			RECIPIENT_ID 	= "user2";
	private static final LocalDateTime 	CREATED			= LocalDateTime.of(2025, 11, 21, 19, 55, 44);
	private static final String 		MESSAGE 		= "A message";
	
	/**
	* Tests construction via the Builder 
	*/
	@Test
	void testBuilder() {
		
		ChatMessage msg = ChatMessage
				.builder()
					.id(ID)
					.chatId(CHAT_ID)
					.senderId(SENDER_ID)
					.recipientId(RECIPIENT_ID)
					.created(CREATED)
					.message(MESSAGE)
				.build();
		
		assertEquals(ID, 			msg.getId());
		assertEquals(CHAT_ID, 		msg.getChatId());
		assertEquals(SENDER_ID, 	msg.getSenderId());
		assertEquals(RECIPIENT_ID, 	msg.getRecipientId());
		assertEquals(MESSAGE, 		msg.getMessage());
		
	}
	
}