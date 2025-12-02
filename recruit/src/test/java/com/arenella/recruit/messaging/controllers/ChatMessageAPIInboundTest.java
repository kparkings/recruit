package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the ChatMessageAPIInbound class 
*/
class ChatMessageAPIInboundTest {

	private static final String 	MESSAGE 		= "a Message";
	
	/**
	* Tests construction via Builder 
	*/
	@Test
	void testCreation() {
		
		ChatMessageAPIInbound msg = ChatMessageAPIInbound
				.builder()
					.message(MESSAGE)
				.build();
		
		assertEquals(MESSAGE, 		msg.getMessage());
		
	}
	
}
