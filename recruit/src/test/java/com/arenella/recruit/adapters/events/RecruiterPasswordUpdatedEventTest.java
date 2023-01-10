package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the RecruiterPasswordUpdatedEvent class
* @author K Parkings
*/
public class RecruiterPasswordUpdatedEventTest {

	/**
	* Tests the constructor
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final String recruiterId = "kparkings";
		final String newPassword = "@djef8*@#00";
			
		RecruiterPasswordUpdatedEvent event = new RecruiterPasswordUpdatedEvent(recruiterId, newPassword);
		
		assertEquals(recruiterId, event.getRecruiterId());
		assertEquals(newPassword, event.getNewPassword());
		
	}
	
}
