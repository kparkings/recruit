package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CandidateAccountCreatedEvent class
* @author K Parkings
*/
class CandidateAccountCreatedEventTest {

	/**
	* Test construction of the Event
	* @throws Exception
	*/
	@Test
	void testConstructor() {
		
		final String candidateId 	= "can1";
		final String encryptedPass 	= "@ddawd333@@";
		
		CandidateAccountCreatedEvent event = new CandidateAccountCreatedEvent(candidateId, encryptedPass);
		
		assertEquals(candidateId,	event.getCandidateId());
		assertEquals(encryptedPass, event.getEncryptedPassword());
		
	}
	
}
