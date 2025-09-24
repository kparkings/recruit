package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the RecruiterHasOpenSubscriptionEvent class
* @author K Parkings
*/
class RecruiterHasOpenSubscriptionEventTest {

	/**
	* Test construction of event
	* @throws Exception
	*/
	@Test
	void testConstruction() {
		
		final String recruiterId = "kparkings";
		
		RecruiterHasOpenSubscriptionEvent event = new RecruiterHasOpenSubscriptionEvent(recruiterId);
		
		assertEquals(recruiterId, event.geRecruiterId());
		
	}
	
}
