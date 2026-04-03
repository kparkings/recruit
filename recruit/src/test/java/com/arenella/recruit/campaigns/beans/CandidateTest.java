package com.arenella.recruit.campaigns.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Candidate.Type;

/**
* Unit tests for the Candidate class 
*/
class CandidateTest {

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
		
		assertEquals(ID, 					candidate.getId());
		assertEquals(TYPE, 					candidate.getType());
		assertEquals(FIRST_NAME, 			candidate.getFirstName());
		assertEquals(SURNAME, 				candidate.getSurname());
		assertEquals(COUNTRY_CODE, 			candidate.getCountryCode());
		assertEquals(JOB_TITLE, 			candidate.getJobTitle());
		assertEquals(EMAIL, 				candidate.getEmail());
		assertEquals(DELETED_FROM_SYSTEM, 	candidate.isDeleteFromSystem());
		
	}
	
}