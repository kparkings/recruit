package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.PublicChat;
import com.arenella.recruit.messaging.beans.ChatParticipant.CHAT_PARTICIPANT_TYPE;
import com.arenella.recruit.messaging.beans.PublicChat.AUDIENCE_TYPE;

/**
* Unit tests for the PublicChatAPIOutbound class 
*/
class PublicChatAPIOutboundTest {
	
	private static final UUID 							ID					= UUID.randomUUID();
	private static final AUDIENCE_TYPE					AUDIENCE_TYPE_VAL	= AUDIENCE_TYPE.ALL;
	private static final UUID							PARENT_CHAT			= UUID.randomUUID();
	private static final String							OWNER_ID			= "rec001";
	private static final LocalDateTime 					CREATED				= LocalDateTime.of(2026, 1, 20, 19, 10, 1);
	private static final String 						MESSAGE				= "Some message";
	private static final Set<String>					LIKES				= Set.of("rec2","112");
	private static final ChatParticipant 				PARTICIPANT 		= ChatParticipant.builder().firstName("Unknown").surname("Unknown").participantId(OWNER_ID).type(CHAT_PARTICIPANT_TYPE.RECRUITER).build();
	private static final ChatParticipantAPIOutbound 	OWNER 				= ChatParticipantAPIOutbound.builder().chatParticipant(PARTICIPANT).build();
	
	/**
	* Tests construction via Builder
	*/
	@Test
	void testBuilder() {
		
		PublicChatAPIOutbound chat = PublicChatAPIOutbound
				.builder()
					.id(ID)
					.audienceType(AUDIENCE_TYPE_VAL)
					.created(CREATED)
					.likes(LIKES)
					.message(MESSAGE)
					.owner(OWNER)
					.parentChat(PARENT_CHAT)
				.build();
		
		assertEquals(ID, 					chat.getId());
		assertEquals(AUDIENCE_TYPE_VAL, 	chat.getAudienceType());
		assertEquals(CREATED, 				chat.getCreated());
		assertEquals(MESSAGE, 				chat.getMessage());
		assertEquals(PARENT_CHAT, 			chat.getParentChat().orElseThrow());
		assertEquals(OWNER_ID, 				chat.getOwner().getId());
		
		assertTrue(chat.getLikes().contains("rec2"));
		assertTrue(chat.getLikes().contains("112"));
		
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
		
		PublicChatAPIOutbound newChat = PublicChatAPIOutbound
				.builder()
					.publicChat(chat, PARTICIPANT)
				.build();
		
		assertEquals(ID, 					newChat.getId());
		assertEquals(AUDIENCE_TYPE_VAL, 	newChat.getAudienceType());
		assertEquals(CREATED, 				newChat.getCreated());
		assertEquals(MESSAGE, 				newChat.getMessage());
		assertEquals(PARENT_CHAT, 			newChat.getParentChat().orElseThrow());
		assertEquals(OWNER_ID, 				newChat.getOwner().getId());
		
		assertTrue(newChat.getLikes().contains("rec2"));
		assertTrue(newChat.getLikes().contains("112"));
		
	}
	
	/**
	* Test case that no parent Chat exists 
	*/
	@Test
	void testConstructorNoParent() {
		
		PublicChatAPIOutbound chat = PublicChatAPIOutbound
				.builder()
				.build();
		
		assertTrue(chat.getParentChat().isEmpty());
				
	}
	
}