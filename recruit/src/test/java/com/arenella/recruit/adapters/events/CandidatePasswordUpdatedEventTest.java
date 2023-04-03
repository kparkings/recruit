package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CandidatePasswordUpdatedEvent class
* @author K Parkings
*/
public class CandidatePasswordUpdatedEventTest {

	/**
	* Tests the Constructor
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final String candidateId = "candidateKev";
		final String newPassword = "!^D^Wjs8s722";
		
		CandidatePasswordUpdatedEvent event = new CandidatePasswordUpdatedEvent(candidateId, newPassword);
		
		assertEquals(candidateId, event.getCandidateId());
		assertEquals(newPassword, event.getNewPassword());
		
	}
	
}