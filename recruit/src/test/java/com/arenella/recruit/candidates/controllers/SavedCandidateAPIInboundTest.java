package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the SavedCandidateAPIInbound class
* @author K Parkings
*/
public class SavedCandidateAPIInboundTest {

	private static final String 	USER_ID 		= "kparkings";
	private static final long 		CANDIDATE_ID 	= 123;
	private static final String 	NOTES 			= "some notes";
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		SavedCandidateAPIInbound savedCandidate = 
				SavedCandidateAPIInbound
				.builder()
					.candidateId(CANDIDATE_ID)
					.notes(NOTES)
				.build();
		
		assertEquals(CANDIDATE_ID, 	savedCandidate.getCandidateId());
		assertEquals(NOTES, 		savedCandidate.getNotes());
		
	}
	
	/**
	* Tests conversion from API In-bound representation to Domain 
	* representation
	* @throws Exception
	*/
	@Test
	public void testConvertToDomain() throws Exception {
		
		SavedCandidate savedCandidate = SavedCandidateAPIInbound.convertToDomain(SavedCandidateAPIInbound
				.builder()
				.candidateId(CANDIDATE_ID)
				.notes(NOTES)
			.build(),
			USER_ID); 
		
		assertEquals(USER_ID, 		savedCandidate.getUserId());
		assertEquals(CANDIDATE_ID, 	savedCandidate.getCandidateId());
		assertEquals(NOTES, 		savedCandidate.getNotes());
		
	}
	
}