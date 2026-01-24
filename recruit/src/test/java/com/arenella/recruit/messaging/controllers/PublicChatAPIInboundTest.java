package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.PublicChat;
import com.arenella.recruit.messaging.beans.PublicChat.AUDIENCE_TYPE;

/**
* Unit tests for the PublicCHatAPIInbound Class 
*/
public class PublicChatAPIInboundTest {

	private static final UUID 				ID					= UUID.randomUUID();
	private static final AUDIENCE_TYPE		AUDIENCE_TYPE_VAL	= AUDIENCE_TYPE.ALL;
	private static final UUID				PARENT_CHAT			= UUID.randomUUID();
	private static final String				OWNER_ID			= "rec001";
	private static final LocalDateTime 		CREATED				= LocalDateTime.of(2026, 1, 20, 19, 10, 1);
	private static final String 			MESSAGE				= "Some message";
	private static final Set<String>		LIKES				= Set.of("rec2","112");
	
	/**
	* Tests construction via Builder
	*/
	@Test
	void testBuilder() {
		
		PublicChatAPIInbound chat = PublicChatAPIInbound
				.builder()
					.message(MESSAGE)
					.parentChat(PARENT_CHAT)
				.build();
		
		assertEquals(MESSAGE, 				chat.getMessage());
		assertEquals(PARENT_CHAT, 			chat.getParentChat().orElseThrow());
		
	}
	
	/**
	* Tests construction via Builder using an existing Chat 
	*/
	@Test
	void testBuilderFromExistingChat() {
		
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
		
		PublicChatAPIInbound newChat = PublicChatAPIInbound
				.builder()
					.publicChat(chat)
				.build();
		
		assertEquals(MESSAGE, 				newChat.getMessage());
		assertEquals(PARENT_CHAT, 			newChat.getParentChat().orElseThrow());
		
	}
	
	/**
	* Test case that no parent Chat exists 
	*/
	@Test
	void testConstructorNoParent() {
		
		PublicChatAPIInbound chat = PublicChatAPIInbound
				.builder()
				.build();
		
		assertTrue(chat.getParentChat().isEmpty());
				
	}
	
}