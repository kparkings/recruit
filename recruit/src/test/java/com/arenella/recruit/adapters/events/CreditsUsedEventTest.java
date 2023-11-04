package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CreditsUsedEvent class
* @author K Parkings
*/
public class CreditsUsedEventTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final String 	userId 	= "kparkings";
		final int		credits	= 2;
		
		CreditsUsedEvent event = new CreditsUsedEvent(userId, credits);
	
		assertEquals(userId, 	event.getUserId());
		assertEquals(credits, 	event.getCredits());
	
	}
	
	
	
}
