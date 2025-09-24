package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;

/**
* Unit tests for the SavedCandidateAPIOutbound class
* @author K Parkings
*/
class SavedCandidateAPIOutboundTest {

	private static final String 	USER_ID 		= "kparkings";
	private static final long 		CANDIDATE_ID 	= 123;
	private static final String 	NOTES 			= "some notes";
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	void testConstructor() {
		
		SavedCandidateAPIOutbound savedCanidate = 
				SavedCandidateAPIOutbound
				.builder()
					.userId(USER_ID)
					.candidateId(CANDIDATE_ID)
					.notes(NOTES)
				.build();
		
		assertEquals(USER_ID, 		savedCanidate.getUserId());
		assertEquals(CANDIDATE_ID, 	savedCanidate.getCandidateId());
		assertEquals(NOTES, 		savedCanidate.getNotes());
		
	}
	
	/**
	* Tests conversion from domain representation to API Out-bound representation 
	* @throws Exception
	*/
	@Test
	void testConvertFromDomain() {
		
		Candidate candidate = Candidate.builder().candidateId(String.valueOf(CANDIDATE_ID)).build();
		
		CandidateSearchAccuracyWrapper wrapper = new CandidateSearchAccuracyWrapper(candidate);
		
		SavedCandidateAPIOutbound savedCandidate = SavedCandidateAPIOutbound.convertFromDomain(SavedCandidate
				.builder()
				.userId(USER_ID)
				.candidateId(CANDIDATE_ID)
				.notes(NOTES)
			.build(),
			wrapper); 
		
		assertEquals(USER_ID, 						savedCandidate.getUserId());
		assertEquals(CANDIDATE_ID, 					savedCandidate.getCandidateId());
		assertEquals(NOTES, 						savedCandidate.getNotes());
		assertEquals(String.valueOf(CANDIDATE_ID), 	savedCandidate.getCandidate().getCandidateId());
		
	}
	
}