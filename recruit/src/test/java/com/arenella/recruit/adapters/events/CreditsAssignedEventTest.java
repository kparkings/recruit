package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CreditsAssignedEvent class
* @author K Parkings
*/
public class CreditsAssignedEventTest {
	
	/**
	* Tests Construction
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final String	userId 		= "kparkings";
		final int 		credits 	= 3;
		
		CreditsAssignedEvent event = new CreditsAssignedEvent(userId, credits);
		
		assertEquals(userId, event.getUserId());
		assertEquals(credits, event.getCurrentCreditCount());
		
	}

}