package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the PrivateChatAPIInbound class 
*/
class PrivateChatAPIInboundTest {

	private static final String SENDER_ID 		= "1111";
	private static final String RECIPIENT_ID 	= "2222";
	
	/**
	* Tests construction via Builder 
	*/
	@Test
	void testConstruction() {
		
		PrivateChatAPIInbound chat = PrivateChatAPIInbound
				.builder()
					.senderId(SENDER_ID)
					.recipientId(RECIPIENT_ID)
				.build();
				
		assertEquals(SENDER_ID, chat.getSenderId());
		assertEquals(RECIPIENT_ID, chat.getRecipientId());
		
	}
	
}
