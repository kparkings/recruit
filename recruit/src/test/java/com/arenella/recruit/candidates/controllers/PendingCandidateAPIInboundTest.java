package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.PendingCandidate;

/**
* Unit tests for the PendingCandidateAPIInbound class 
* @author K Parkings
*/
public class PendingCandidateAPIInboundTest {

	private static final UUID	 		candidateId 			= UUID.randomUUID();
	private static final String 		firstname				= "Kevin";
	private static final String 		surname					= "Parkings";
	private static final String 		email					= "kparkings@gmail.com";
	private static final boolean 		freelance 				= true;
	private static final boolean 		perm 					= true;
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	public void testInitializationFromBuilder() {
		
		PendingCandidateAPIInbound candidate = PendingCandidateAPIInbound
						.builder()
							.pendingCandidateId(candidateId)
							.firstname(firstname)
							.surname(surname)
							.email(email)
							.freelance(freelance)
							.perm(perm)
							.build();
		
		assertEquals(candidate.getPendingCandidateId(), 		candidateId);
		assertEquals(candidate.getFirstname(), 					firstname);
		assertEquals(candidate.getSurname(), 					surname);
		assertEquals(candidate.getEmail(), 						email);
		assertEquals(candidate.isFreelance(), 					freelance);
		assertEquals(candidate.isPerm(), 						perm);
		
	}
	
	/**
	* Tests that conversion returns a Domain representation of the 
	* Entity representation of a Candidate and the state is copied 
	* successfully 
	*/
	@Test
	public void testConversionToCandidate() {
		
		PendingCandidateAPIInbound pendingCandidateEntity = PendingCandidateAPIInbound
				.builder()
					.pendingCandidateId(candidateId)
					.firstname(firstname)
					.surname(surname)
					.email(email)
					.freelance(freelance)
					.perm(perm)
					.build();
		
		PendingCandidate pendingCandidate = PendingCandidateAPIInbound.convertToPendingCandidate(pendingCandidateEntity);

		assertEquals(pendingCandidate.getPendingCandidateId(), 	candidateId);
		assertEquals(pendingCandidate.getFirstname(), 			firstname);
		assertEquals(pendingCandidate.getSurname(), 			surname);
		assertEquals(pendingCandidate.getEmail(), 				email);
		assertEquals(pendingCandidate.isFreelance(), 					freelance);
		assertEquals(pendingCandidate.isPerm(), 						perm);
		
	}
	
	
}
