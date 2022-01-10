package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the RecruiterHasOpenSubscriptionEvent class
* @author K Parkings
*/
public class RecruiterHasOpenSubscriptionEventTest {

	/**
	* Test construction of event
	* @throws Exception
	*/
	@Test
	public void testConstruction() throws Exception{
		
		final String recruiterId = "kparkings";
		
		RecruiterHasOpenSubscriptionEvent event = new RecruiterHasOpenSubscriptionEvent(recruiterId);
		
		assertEquals(recruiterId, event.geRecruiterId());
		
	}
	
}
