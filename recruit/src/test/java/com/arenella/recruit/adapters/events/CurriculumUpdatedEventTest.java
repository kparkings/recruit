package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CurriculumUpdatedEvent class
* @author K Parkings
*/
class CurriculumUpdatedEventTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	void testConstruction() {
		
		final String curriculumId = "1232";
		
		CurriculumUpdatedEvent event = new CurriculumUpdatedEvent(curriculumId);
		
		assertEquals(curriculumId, event.getCurriculumId());
		
	}
	
}
