package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the ContactRequestEvent class
* @author K Parkings
*/
public class ContactRequestEventTest {

	/**
	* Tests constructor
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final String senderRecruiterId 	= "rec30";
		final String recipientId 		= "can10";
		final String title 				= "aTitle";
		final String message 			= "aMessage";
		
		ContactRequestEvent event = new ContactRequestEvent(senderRecruiterId, recipientId, title, message);
	
		assertEquals(senderRecruiterId, event.getSenderRecruiterId());
		assertEquals(recipientId, 		event.getRecipientId());
		assertEquals(title, 			event.getTitle());
		assertEquals(message, 			event.getMessage());
		
	}
	
}
