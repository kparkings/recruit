package com.arenella.recruit.authentication.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;


/**
* Unit tests for the AuthenticatedEvent class
* @author K Parkings
*/
public class AuthenticatedEventTest {

	/**
	* Tests Construction
	* @throws Exception
	*/
	@Test
	public void testContructor() throws Exception{
		
		final String 			userId 		= "kparkings";
		final boolean 			recruiter 	= false;
		final boolean 			candidate 	= true;
		final LocalDateTime 	loggedInAt 	= LocalDateTime.now();
		
		AuthenticatedEvent event = new AuthenticatedEvent(userId, recruiter, candidate, loggedInAt);
		
		assertEquals(userId, event.getUserId());
		assertEquals(loggedInAt, event.getLoggedInAt());
		assertTrue(event.isCandidate());
		assertFalse(event.isRecruiter());
		
	}
	
}