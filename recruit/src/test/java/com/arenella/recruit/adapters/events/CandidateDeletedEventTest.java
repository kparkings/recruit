package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CandidateDeletedEvent class
* @author K Parkings
*/
public class CandidateDeletedEventTest {

	/**
	* Test Event creation
	* @throws Exception
	*/
	@Test
	public void testConsructor() throws Exception{
		
		final String candidateId = "12334";
		
		CandidateDeletedEvent event = new CandidateDeletedEvent(candidateId);
		
		assertEquals(candidateId, event.getCandidateId());
		
	}
}
