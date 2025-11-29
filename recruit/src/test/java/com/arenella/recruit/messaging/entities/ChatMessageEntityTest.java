package com.arenella.recruit.messaging.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.ChatMessage;

class ChatMessageEntityTest {

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
		
		ChatMessageEntity msg = ChatMessageEntity
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
	
	/**
	* Test conversion from Entity to Domain representation
	*/
	@Test
	void testFromEntity() {
		
		ChatMessageEntity entity = ChatMessageEntity
				.builder()
					.id(ID)
					.chatId(CHAT_ID)
					.senderId(SENDER_ID)
					.recipientId(RECIPIENT_ID)
					.created(CREATED)
					.message(MESSAGE)
				.build();
		
		assertEquals(ID, 			entity.getId());
		assertEquals(CHAT_ID, 		entity.getChatId());
		assertEquals(SENDER_ID, 	entity.getSenderId());
		assertEquals(RECIPIENT_ID, 	entity.getRecipientId());
		assertEquals(MESSAGE, 		entity.getMessage());
		
		ChatMessage message = ChatMessageEntity.fromEntity(entity);
		
		assertEquals(ID, 			message.getId());
		assertEquals(CHAT_ID, 		message.getChatId());
		assertEquals(SENDER_ID, 	message.getSenderId());
		assertEquals(RECIPIENT_ID, 	message.getRecipientId());
		assertEquals(MESSAGE, 		message.getMessage());
		
	}
	
	/**
	* Test conversion from Domain to Entity representation
	*/
	@Test
	void testToEntity() {
		
		ChatMessage message = ChatMessage
				.builder()
					.id(ID)
					.chatId(CHAT_ID)
					.senderId(SENDER_ID)
					.recipientId(RECIPIENT_ID)
					.created(CREATED)
					.message(MESSAGE)
				.build();
		
		assertEquals(ID, 			message.getId());
		assertEquals(CHAT_ID, 		message.getChatId());
		assertEquals(SENDER_ID, 	message.getSenderId());
		assertEquals(RECIPIENT_ID, 	message.getRecipientId());
		assertEquals(MESSAGE, 		message.getMessage());
		
		ChatMessageEntity entity = ChatMessageEntity.toEntity(message);
		
		assertEquals(ID, 			entity.getId());
		assertEquals(CHAT_ID, 		entity.getChatId());
		assertEquals(SENDER_ID, 	entity.getSenderId());
		assertEquals(RECIPIENT_ID, 	entity.getRecipientId());
		assertEquals(MESSAGE, 		entity.getMessage());
		
	}
	
}