package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CandidateUpdatedEvent class
* @author K Parkings
*/
public class CandidateUpdatedEventTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final String candidateId 	= "123";
		final String firstname 		= "kevin";
		final String surname 		= "parkings";
		final String email 			= "kparkings@gmail.com";
		
		CandidateUpdatedEvent event = new CandidateUpdatedEvent(candidateId, firstname, surname, email);
		
		assertEquals(candidateId, 	event.getCandidateId());
		assertEquals(firstname, 	event.getFirstName());
		assertEquals(surname, 		event.getSurname());
		assertEquals(email, 		event.getEmail());
		
	}
	
}