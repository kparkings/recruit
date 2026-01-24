package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the PublicChatUpdateAPIInbound class 
*/
class PublicChatUpdateAPIInboundTest {

	private static final String MESSAGE = "A message";
	
	/**
	* Tests construction via BUilder 
	*/
	@Test
	void testConstruction() {
		
		PublicChatUpdateAPIInbound in = PublicChatUpdateAPIInbound.builder().message(MESSAGE).build();
		
		assertEquals(MESSAGE, in.getMessage());
		
	}
}
