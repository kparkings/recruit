package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CandidateNoLongerAvailableEvent class
* @author K Parkings
*/
public class CandidateNoLongerAvailableEventTest {

	/**
	* Tests construction of the Event
	* @throws Exception
	*/
	@Test
	public void testConstruction() throws Exception {
		
		final long candidateId = 123L;
		
		CandidateNoLongerAvailableEvent event = new CandidateNoLongerAvailableEvent(candidateId);
		
		assertEquals(candidateId, event.getCandidateId());
		
	}
	
}
