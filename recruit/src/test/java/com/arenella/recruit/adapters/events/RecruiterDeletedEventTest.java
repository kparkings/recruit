package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the RecruiterDeletedEvent class
* @author K Parkings
*/
class RecruiterDeletedEventTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	void testConstructor() {
		
		final String recruiterId = "aRecId";
		
		RecruiterDeletedEvent event = new RecruiterDeletedEvent(recruiterId);
	
		assertEquals(recruiterId, event.getRecruiterId());		
				
	}
	
}
