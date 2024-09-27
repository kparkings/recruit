package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the RecruiterDeletedEvent class
* @author K Parkings
*/
public class RecruiterDeletedEventTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final String recruiterId = "aRecId";
		
		RecruiterDeletedEvent event = new RecruiterDeletedEvent(recruiterId);
	
		assertEquals(recruiterId, event.getRecruiterId());		
				
	}
	
}
