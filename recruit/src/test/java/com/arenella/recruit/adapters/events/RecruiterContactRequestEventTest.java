package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the RecruiterContactRequestEvent class
* @author K Parkings
*/
public class RecruiterContactRequestEventTest {

	/**
	* Tests the constructor 
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final String senderRecruiterId 		= "kparkings";
		final String recipientRecruiterId 	= "bparkings";
		final String title 					= "contact request";
		final String message 				= "hello";
		
		RecruiterContactRequestEvent event = new RecruiterContactRequestEvent(senderRecruiterId, recipientRecruiterId, title, message);
		
		assertEquals(senderRecruiterId, 		event.getSenderRecruiterId());
		assertEquals(recipientRecruiterId, 		event.getRecipientRecruiterId());
		assertEquals(title, 					event.getTitle());
		assertEquals(message, 					event.getMessage());
		
	}
	
}
