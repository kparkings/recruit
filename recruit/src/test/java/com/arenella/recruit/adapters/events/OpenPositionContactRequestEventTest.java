package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the OpenPositionContactRequestEvent class
* @author K Parkings
*/
public class OpenPositionContactRequestEventTest {

	private static final String MESSAGE 				= "A Message";
	private static final String RECIPIENT_ID 			= "rparkings";
	private static final String SENDER_ID 				= "lparkings";
	private static final String SENDER_NAME 			= "Lorraine";
	private static final UUID	OPEN_POSITION_ID 		= UUID.randomUUID();
	private static final String OPEN_POSITION_TITLE 	= "C# Developer UK";
	
	/**
	* Tests the builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		OpenPositionContactRequestEvent event = 
				OpenPositionContactRequestEvent
				.builder()
					.message(MESSAGE)
					.recipientId(RECIPIENT_ID)
					.senderId(SENDER_ID)
					.senderName(SENDER_NAME)
					.openPositionId(OPEN_POSITION_ID)
					.openPositionTitle(OPEN_POSITION_TITLE)
				.build();
		
		assertEquals(MESSAGE, 				event.getMessage());
		assertEquals(RECIPIENT_ID, 			event.getRecipientId());
		assertEquals(SENDER_ID, 			event.getSenderId());
		assertEquals(SENDER_NAME, 			event.getSenderName());
		assertEquals(OPEN_POSITION_ID, 		event.getOpenPositionId());
		assertEquals(OPEN_POSITION_TITLE, 	event.getOpenPositionTitle());
		
	}
	
}