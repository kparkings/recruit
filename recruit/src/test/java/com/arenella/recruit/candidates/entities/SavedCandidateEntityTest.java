package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.controllers.SavedCandidate;

/**
* Unit tests for the SavedCandidateEntity class 
* @author K Parkings
*/
public class SavedCandidateEntityTest {

	private static final String 	USER_ID 		= "kparkings";
	private static final long 		CANDIDATE_ID 	= 123;
	private static final String 	NOTES 			= "some notes";
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		SavedCandidateEntity savedCandidate = 
				SavedCandidateEntity
				.builder()
					.candidateId(CANDIDATE_ID)
					.notes(NOTES)
				.build();
		
		assertEquals(CANDIDATE_ID, 	savedCandidate.getCandidateId());
		assertEquals(NOTES, 		savedCandidate.getNotes());
		
	}
	
	/**
	* Tests conversion from Entity representation to Domain 
	* representation
	* @throws Exception
	*/
	@Test
	public void testConvertToDomain() throws Exception {
		
		SavedCandidate savedCandidate = SavedCandidateEntity.convertFromEntity(SavedCandidateEntity
				.builder()
				.userId(USER_ID)
				.candidateId(CANDIDATE_ID)
				.notes(NOTES)
			.build()); 
		
		assertEquals(USER_ID, 		savedCandidate.getUserId());
		assertEquals(CANDIDATE_ID, 	savedCandidate.getCandidateId());
		assertEquals(NOTES, 		savedCandidate.getNotes());
		
	}

	/**
	* Tests conversion from Domain representation to Entity 
	* representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromDomain() throws Exception {
		
		SavedCandidateEntity savedCandidate = SavedCandidateEntity.convertToEntity(SavedCandidate
				.builder()
				.userId(USER_ID)
				.candidateId(CANDIDATE_ID)
				.notes(NOTES)
				.build()); 
		
		assertEquals(USER_ID, 		savedCandidate.getUserId());
		assertEquals(CANDIDATE_ID, 	savedCandidate.getCandidateId());
		assertEquals(NOTES, 		savedCandidate.getNotes());
		
	}
}
