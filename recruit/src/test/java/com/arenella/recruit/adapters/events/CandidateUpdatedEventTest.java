package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;

/**
* Unit tests for the CandidateUpdatedEvent class
* @author K Parkings
*/
class CandidateUpdatedEventTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	void testConstructor() {
		
		final String candidateId 	= "123";
		final String firstname 		= "kevin";
		final String surname 		= "parkings";
		final String email 			= "admin@arenella-ict.com";
		
		CandidateUpdatedEvent event = new CandidateUpdatedEvent(candidateId, firstname, surname, email);
		
		assertEquals(candidateId, 	event.getCandidateId());
		assertEquals(firstname, 	event.getFirstName());
		assertEquals(surname, 		event.getSurname());
		assertEquals(email, 		event.getEmail());
		assertTrue(event.getPhoto().isEmpty());
		
	}
	
	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	void testConstructorWithPhoto() {
		
		final String candidateId 	= "123";
		final String firstname 		= "kevin";
		final String surname 		= "parkings";
		final String email 			= "admin@arenella-ict.com";
		
		CandidateUpdatedEvent event = new CandidateUpdatedEvent(candidateId, firstname, surname, email, new Photo(new byte[] {}, PHOTO_FORMAT.jpeg));
		
		assertEquals(candidateId, 	event.getCandidateId());
		assertEquals(firstname, 	event.getFirstName());
		assertEquals(surname, 		event.getSurname());
		assertEquals(email, 		event.getEmail());
		assertFalse(event.getPhoto().isEmpty());
		
	}
	
}