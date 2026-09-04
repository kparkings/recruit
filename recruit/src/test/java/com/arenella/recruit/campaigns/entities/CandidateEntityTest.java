package com.arenella.recruit.campaigns.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Candidate.Type;

/**
* Unit tests for the CandidateEntity class 
*/
class CandidateEntityTest {

	private static final String 		ID 								= "123";
	private static final Type 			TYPE 							= Type.INTERNAL;
	private static final String 		FIRST_NAME 						= "kevin";
	private static final String 		SURNAME 						= "parkings";
	private static final String 		COUNTRY_CODE 					= "it";
	private static final String 		JOB_TITLE 						= "Java Developer";
	private static final String 		EMAIL 							= "kparkings@gmail.com";
	private static final boolean 		DELETED_FROM_SYSTEM 			= false;
	public static final LocalDateTime 	CREATED 						= LocalDateTime.of(2026, 9, 4, 18, 13, 01);
	public static final LocalDateTime 	LAST_DATA_RETENTION_AGREEMENT 	= LocalDateTime.of(2026, 9, 4, 19, 13, 01);

	/**
	* Tests Construction based upon a Builder 6
	*/
	@Test
	void testConstructor() {
		
		CandidateEntity entity = CandidateEntity
				.builder()
					.id(ID)
					.type(TYPE)
					.firstName(FIRST_NAME)
					.surname(SURNAME)
					.countryCode(COUNTRY_CODE)
					.jobTitle(JOB_TITLE)
					.email(EMAIL)
					.deletedFromSystem(DELETED_FROM_SYSTEM)
					.created(CREATED)
					.lastDataRetentionConfirmation(LAST_DATA_RETENTION_AGREEMENT)
				.build();
		
		assertEquals(ID, 								entity.getId());
		assertEquals(TYPE, 								entity.getType());
		assertEquals(FIRST_NAME, 						entity.getFirstName());
		assertEquals(SURNAME, 							entity.getSurname());
		assertEquals(COUNTRY_CODE, 						entity.getCountryCode());
		assertEquals(JOB_TITLE, 						entity.getJobTitle());
		assertEquals(EMAIL, 							entity.getEmail());
		assertEquals(DELETED_FROM_SYSTEM, 				entity.isDeleteFromSystem());
		assertEquals(CREATED, 							entity.getCreated());
		assertEquals(LAST_DATA_RETENTION_AGREEMENT, 	entity.getLastDataRetentionConfirmation().orElseThrow());
		
	}
	
}