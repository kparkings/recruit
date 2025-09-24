package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.PendingCandidate;

/**
* Unit tests for the PendingCandidateAPIOutboud class
* @author K Parkings
*/
class PendingCandidateAPIOutboudTest {

	/**
	* Tests creation of class via Builder 
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		final UUID 		pendingCandidateId 	= UUID.randomUUID();
		final String 	firstname 			= "Kevin";
		final String 	surname 			= "Parkings";
		final String 	email 				= "admin@arenella-ict.com";
		final boolean 	perm 				= true;
		final boolean 	freelance 			= true;
		
		PendingCandidateAPIOutbound pendingCandidate = PendingCandidateAPIOutbound
																		.builder()
																			.pendingCandidateId(pendingCandidateId)
																			.firstname(firstname)
																			.surname(surname)
																			.email(email)
																			.freelance(freelance)
																			.perm(perm)
																		.build();
		
		assertEquals(pendingCandidate.getPendingCandidateId(), 	pendingCandidateId);
		assertEquals(pendingCandidate.getFirstname(), 			firstname);
		assertEquals(pendingCandidate.getSurname(), 			surname);
		assertEquals(pendingCandidate.getEmail(), 				email);
		assertTrue(pendingCandidate.isFreelance());
		assertTrue(pendingCandidate.isPerm());					
		
	}

	/**
	* Tests the conversion from a PendingCandidate to a PendingCandidateAPIOutboud object
	* @throws Exception
	*/
	@Test
	void testConvertFromPendingCandidate() {
		
		final UUID 		pendingCandidateId 	= UUID.randomUUID();
		final String 	firstname 			= "Kevin";
		final String 	surname 			= "Parkings";
		final String 	email 				= "admin@arenella-ict.com";
		final boolean 	perm 				= true;
		final boolean 	freelance 			= true;
		
		PendingCandidate pendingCandidate = PendingCandidate
													.builder()
													.pendingCandidateId(pendingCandidateId)
													.firstname(firstname)
													.surname(surname)
													.email(email)
													.freelance(freelance)
													.perm(perm)
												.build();
		
		PendingCandidateAPIOutbound pendingCandiateAPIOutbound = PendingCandidateAPIOutbound.convertFromPendingCandidate(pendingCandidate);
				
		assertEquals(pendingCandiateAPIOutbound.getPendingCandidateId(), 	pendingCandidateId);
		assertEquals(pendingCandiateAPIOutbound.getFirstname(), 			firstname);
		assertEquals(pendingCandiateAPIOutbound.getSurname(), 			surname);
		assertEquals(pendingCandiateAPIOutbound.getEmail(), 				email);
		assertTrue(pendingCandiateAPIOutbound.isFreelance());
		assertTrue(pendingCandiateAPIOutbound.isPerm());					
		
	}
	
}