package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CurriculumUpdatedEvent class
* @author K Parkings
*/
public class CurriculumUpdatedEventTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConstruction() throws Exception{
		
		final String curriculumId = "1232";
		
		CurriculumUpdatedEvent event = new CurriculumUpdatedEvent(curriculumId);
		
		assertEquals(curriculumId, event.getCurriculumId());
		
	}
	
}
