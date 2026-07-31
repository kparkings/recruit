package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the MultiUserChatMessageAPIInbound class
*/
class MultiUserChatMessageAPIInboundTest {

	private static final Set<String> RECIPIENT_IDS = Set.of("1","2","3");
	private static final String MESSAGE = "A Message.";
	
	/**
	* Tests construction via the Builder 
	*/
	@Test
	void testConstruction() {
		
		MultiUserChatMessageAPIInbound inbound = MultiUserChatMessageAPIInbound
				.builder()
				.recipientIds(RECIPIENT_IDS)
				.message(MESSAGE)
				.build();
		
		assertEquals(MESSAGE,				 inbound.getMessage());
		assertEquals(RECIPIENT_IDS.size(), 	inbound.getRecipientIds().size());
	}
	
	/**
	* Tests construction via the Builder
	* Set should be empty not null to avoid unexpected nullPointer
	*/
	@Test
	void testConstructionDefaults() {
		
		MultiUserChatMessageAPIInbound inbound = MultiUserChatMessageAPIInbound
				.builder()
				.build();
		
		assertTrue(inbound.getRecipientIds().isEmpty());
		
	}
	
}
