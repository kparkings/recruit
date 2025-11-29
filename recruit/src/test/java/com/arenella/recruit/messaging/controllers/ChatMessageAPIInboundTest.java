package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the ChatMessageAPIInbound class 
*/
class ChatMessageAPIInboundTest {

	/**
	* Tests construction via Builder 
	*/
	@Test
	void testCreation() {
		
		final String message = "aMessafe";
		
		ChatMessageAPIInbound msg = ChatMessageAPIInbound.builder().message(message).build();
		
		assertEquals(message, msg.getMessage());
		
	}
	
}
