package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the SavedCandidate class
* @author K Parkings
*/
public class SavedCandidateTest {

	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final String 	userId 			= "kparkings";
		final long 		candidateId 	= 123;
		final String 	notes 			= "some notes";
		
		SavedCandidate savedCanidate = 
				SavedCandidate
				.builder()
					.userId(userId)
					.candidateId(candidateId)
					.notes(notes)
				.build();
		
		assertEquals(userId, 		savedCanidate.getUserId());
		assertEquals(candidateId, 	savedCanidate.getCandidateId());
		assertEquals(notes, 		savedCanidate.getNotes());
		
	}
	
}