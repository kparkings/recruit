package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the OfferedCandidateContactRequestEvent class
* @author K Parkings
*/
public class OfferedCandidateContactRequestEventTest {

	private static final String 	MESSAGE 				= "A Message";
	private static final String 	RECIPIENT_ID 			= "rparkings";
	private static final String 	SENDER_ID 				= "lparkings";
	private static final String 	SENDER_NAME 			= "Lorraine";
	private static final UUID		OFFERED_CANDIDATE_ID 	= UUID.randomUUID();
	private static final String 	OFFERED_CANDIDATE_TITLE = "Java Developer Groningen";
	
	/**
	* Tests the builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		OfferedCandidateContactRequestEvent event = 
				OfferedCandidateContactRequestEvent
				.builder()
					.message(MESSAGE)
					.recipientId(RECIPIENT_ID)
					.senderId(SENDER_ID)
					.senderName(SENDER_NAME)
					.offeredCandidateId(OFFERED_CANDIDATE_ID)
					.offeredCandidateTitle(OFFERED_CANDIDATE_TITLE)
				.build();
		
		assertEquals(MESSAGE, 					event.getMessage());
		assertEquals(RECIPIENT_ID, 				event.getRecipientId());
		assertEquals(SENDER_ID, 				event.getSenderId());
		assertEquals(SENDER_NAME, 				event.getSenderName());
		assertEquals(OFFERED_CANDIDATE_ID, 		event.getOfferedCandidateId());
		assertEquals(OFFERED_CANDIDATE_TITLE, 	event.getOfferedCandidateTitle());
		
	}
	
}