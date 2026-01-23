package com.arenella.recruit.messaging.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.PublicChat;
import com.arenella.recruit.messaging.beans.PublicChat.AUDIENCE_TYPE;

/**
* Unit tests for the PublicChatEntity class 
*/
class PublicChatEntityTest {

	private static final UUID 				ID					= UUID.randomUUID();
	private static final AUDIENCE_TYPE		AUDIENCE_TYPE_VAL	= AUDIENCE_TYPE.ALL;
	private static final UUID				PARENT_CHAT			= UUID.randomUUID();
	private static final String				OWNER_ID			= "rec001";
	private static final LocalDateTime 		CREATED				= LocalDateTime.of(2026, 1, 20, 19, 10, 1);
	private static final String 			MESSAGE				= "Some message";
	private static final Set<String>		LIKES				= Set.of("rec2","112");
	
	/**
	* 
	*/
	@Test
	void testConstruction() {
		
		PublicChatEntity chat = PublicChatEntity
				.builder()
					.id(ID)
					.audienceType(AUDIENCE_TYPE_VAL)
					.created(CREATED)
					.likes(LIKES)
					.message(MESSAGE)
					.ownerId(OWNER_ID)
					.parentChat(PARENT_CHAT)
				.build();
		
		assertEquals(ID, 					chat.getId());
		assertEquals(AUDIENCE_TYPE_VAL, 	chat.getAudienceType());
		assertEquals(CREATED, 				chat.getCreated());
		assertEquals(MESSAGE, 				chat.getMessage());
		assertEquals(PARENT_CHAT, 			chat.getParentChat().orElseThrow());
		assertEquals(OWNER_ID, 				chat.getOwnerId());
		
		assertTrue(chat.getLikes().contains("rec2"));
		assertTrue(chat.getLikes().contains("112"));
		
	}
	
	/**
	* Test conversion from Entity to Domain representation 
	*/
	@Test
	void testFromEntity() {
		
		PublicChatEntity entity = PublicChatEntity
				.builder()
					.id(ID)
					.audienceType(AUDIENCE_TYPE_VAL)
					.created(CREATED)
					.likes(LIKES)
					.message(MESSAGE)
					.ownerId(OWNER_ID)
					.parentChat(PARENT_CHAT)
				.build();
		
		assertEquals(ID, 					entity.getId());
		assertEquals(AUDIENCE_TYPE_VAL, 	entity.getAudienceType());
		assertEquals(CREATED, 				entity.getCreated());
		assertEquals(MESSAGE, 				entity.getMessage());
		assertEquals(PARENT_CHAT, 			entity.getParentChat().orElseThrow());
		assertEquals(OWNER_ID, 				entity.getOwnerId());
		
		assertTrue(entity.getLikes().contains("rec2"));
		assertTrue(entity.getLikes().contains("112"));
		
		PublicChat chat = PublicChatEntity.fromEntity(entity);
		
		assertEquals(ID, 					chat.getId());
		assertEquals(AUDIENCE_TYPE_VAL, 	chat.getAudienceType());
		assertEquals(CREATED, 				chat.getCreated());
		assertEquals(MESSAGE, 				chat.getMessage());
		assertEquals(PARENT_CHAT, 			chat.getParentChat().orElseThrow());
		assertEquals(OWNER_ID, 				chat.getOwnerId());
		
		assertTrue(chat.getLikes().contains("rec2"));
		assertTrue(chat.getLikes().contains("112"));
		
	}
	
	/**
	* Test conversion from Entity to Domain representation 
	*/
	@Test
	void testToEntity() {
		
		PublicChat chat = PublicChat
				.builder()
					.id(ID)
					.audienceType(AUDIENCE_TYPE_VAL)
					.created(CREATED)
					.likes(LIKES)
					.message(MESSAGE)
					.ownerId(OWNER_ID)
					.parentChat(PARENT_CHAT)
				.build();
		
		assertEquals(ID, 					chat.getId());
		assertEquals(AUDIENCE_TYPE_VAL, 	chat.getAudienceType());
		assertEquals(CREATED, 				chat.getCreated());
		assertEquals(MESSAGE, 				chat.getMessage());
		assertEquals(PARENT_CHAT, 			chat.getParentChat().orElseThrow());
		assertEquals(OWNER_ID, 				chat.getOwnerId());
		
		assertTrue(chat.getLikes().contains("rec2"));
		assertTrue(chat.getLikes().contains("112"));
		
		PublicChatEntity entity = PublicChatEntity.toEntity(chat);
		
		assertEquals(ID, 					entity.getId());
		assertEquals(AUDIENCE_TYPE_VAL, 	entity.getAudienceType());
		assertEquals(CREATED, 				entity.getCreated());
		assertEquals(MESSAGE, 				entity.getMessage());
		assertEquals(PARENT_CHAT, 			entity.getParentChat().orElseThrow());
		assertEquals(OWNER_ID, 				entity.getOwnerId());
		
		assertTrue(entity.getLikes().contains("rec2"));
		assertTrue(entity.getLikes().contains("112"));
		
	}
	
}