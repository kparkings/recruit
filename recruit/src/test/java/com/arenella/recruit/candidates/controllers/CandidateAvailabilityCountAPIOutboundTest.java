package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CandidateAvailabilityCountAPIOutbound class
* @author K Parkings
*/
public class CandidateAvailabilityCountAPIOutboundTest {

	/**
	* Tests construction of object
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		
		final long available 	= 10;
		final long unavailable 	= 20;
	
		CandidateAvailabilityCountAPIOutbound counts = new CandidateAvailabilityCountAPIOutbound(available, unavailable);
		
		assertEquals(available, 				counts.getAvailable());
		assertEquals(unavailable, 				counts.getUnavailable());
		assertEquals(available + unavailable, 	counts.getTotalRegisteredCandidates());
		
	}
	
}
