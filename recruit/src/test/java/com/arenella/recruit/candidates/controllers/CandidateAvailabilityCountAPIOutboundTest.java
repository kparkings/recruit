package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CandidateAvailabilityCountAPIOutbound class
* @author K Parkings
*/
class CandidateAvailabilityCountAPIOutboundTest {

	/**
	* Tests construction of object
	* @throws Exception
	*/
	@Test
	void testConstructor() {
		
		
		final long available 	= 10;
		final long unavailable 	= 20;
	
		CandidateAvailabilityCountAPIOutbound counts = new CandidateAvailabilityCountAPIOutbound(available, unavailable);
		
		assertEquals(available, 				counts.getAvailable());
		assertEquals(unavailable, 				counts.getUnavailable());
		assertEquals(available + unavailable, 	counts.getTotalRegisteredCandidates());
		
	}
	
}
