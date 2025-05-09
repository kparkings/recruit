package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CandidateProfileViewedEvent class
*/
class CandidateProfileViewedEventTest {

	private static final UUID 			ID 				= UUID.randomUUID();
	private static final String 		RECRUITER_ID 	= "REC1";
	private static final String 		CANDIDATE_ID 	= "CANDIDATE_ID";
	private static final LocalDateTime 	VIEWED 			= LocalDateTime.of(2025, 5, 1, 22, 48);
	
	/**
	* Tests construction via builder
	*/
	@Test
	void testBuilder() {
		
		CandidateProfileViewedEvent entity = 
				CandidateProfileViewedEvent
				.builder()
					.id(ID)
					.recruiterId(RECRUITER_ID)
					.candidateId(CANDIDATE_ID)
					.viewed(VIEWED)
				.build();
		
		assertEquals(ID, 			entity.getId());
		assertEquals(RECRUITER_ID, 	entity.recruiterId());
		assertEquals(CANDIDATE_ID, 	entity.candidateId());
		assertEquals(VIEWED, 		entity.getViewed());
		
	}
	
}
