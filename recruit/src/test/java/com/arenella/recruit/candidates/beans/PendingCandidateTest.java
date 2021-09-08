package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit test for the PendingCandidate Class
* @author K Parkings
*/
public class PendingCandidateTest {

	private static final UUID 			pendingCandidateId 		= UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
	private static final String 		email					= "kparkings@gmail.com";
	private static final boolean 		freelance 				= true;
	private static final boolean		perm 					= true;
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	public void testInitializationFromBuilder() {
		
		PendingCandidate candidate = PendingCandidate
												.builder()
													.pendingCandidateId(pendingCandidateId)
													.email(email)
													.freelance(freelance)
													.perm(perm)
													.build();
		
		assertEquals(candidate.getPendingCandidateId(), 		pendingCandidateId);
		assertEquals(candidate.getEmail(), 						email);
		assertEquals(candidate.isFreelance(), 					freelance);
		assertEquals(candidate.isPerm(), 						perm);
		
	}
	
}