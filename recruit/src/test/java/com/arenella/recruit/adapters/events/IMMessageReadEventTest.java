package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the IMMessageReadEvent class 
*/
class IMMessageReadEventTest {

	private static final String SENDER_ID 		= "candidate1";
	private static final String RECIPIENT_ID 	= "recruiter88";
	
	/**
	* Tests construction
	*/
	@Test
	void testConstructor() {
		
		IMMessageReadEvent event = IMMessageReadEvent.builder()
					.senderId(SENDER_ID)
					.recipientId(RECIPIENT_ID)
				.build();
		
		assertEquals(SENDER_ID, 	event.getSenderId());
		assertEquals(RECIPIENT_ID, 	event.getRecipientId());
		
	}
	
}
