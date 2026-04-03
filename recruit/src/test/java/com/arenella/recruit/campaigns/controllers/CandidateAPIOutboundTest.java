package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Candidate;
import com.arenella.recruit.campaigns.beans.Candidate.Type;

/**
* Unit tests for the CandidateAPIOutbound class 
*/
class CandidateAPIOutboundTest {

	private static final String 	ID 						= "123";
	private static final Type 		TYPE 					= Type.INTERNAL;
	private static final String 	FIRST_NAME 				= "kevin";
	private static final String 	SURNAME 				= "parkings";
	private static final String 	COUNTRY_CODE 			= "it";
	private static final String 	JOB_TITLE 				= "Java Developer";
	private static final String 	EMAIL 					= "kparkings@gmail.com";
	private static final boolean 	DELETED_FROM_SYSTEM 	= false;
	
	/**
	* Tests Construction based upon a Builder 
	*/
	@Test
	void testConstructor() {
		
		Candidate candidate = Candidate
				.builder()
					.id(ID)
					.type(TYPE)
					.firstName(FIRST_NAME)
					.surname(SURNAME)
					.countryCode(COUNTRY_CODE)
					.jobTitle(JOB_TITLE)
					.email(EMAIL)
					.deletedFromSystem(DELETED_FROM_SYSTEM)
				.build();
		
		CandidateAPIOutbound outbound = CandidateAPIOutbound
				.builder()
					.from(candidate)
				.build();
		
		assertEquals(ID, 					outbound.getId());
		assertEquals(TYPE, 					outbound.getType());
		assertEquals(FIRST_NAME, 			outbound.getFirstName());
		assertEquals(SURNAME, 				outbound.getSurname());
		assertEquals(COUNTRY_CODE, 			outbound.getCountryCode());
		assertEquals(JOB_TITLE, 			outbound.getJobTitle());
		assertEquals(EMAIL, 				outbound.getEmail());
		assertEquals(DELETED_FROM_SYSTEM, 	outbound.isDeleteFromSystem());
		
	}
	
}