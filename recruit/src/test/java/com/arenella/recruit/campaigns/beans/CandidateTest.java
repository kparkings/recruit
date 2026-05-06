package com.arenella.recruit.campaigns.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Candidate.Type;
import com.arenella.recruit.campaigns.entities.CandidateEntity;

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
	
	/**
	* Tests conversion from Entity to Domain representation
	*/
	@Test
	void testFromEntity() {
		
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
		
		CandidateEntity entity = CandidateEntity.toEntity(candidate);
		
		assertEquals(ID, 					entity.getId());
		assertEquals(TYPE, 					entity.getType());
		assertEquals(FIRST_NAME, 			entity.getFirstName());
		assertEquals(SURNAME, 				entity.getSurname());
		assertEquals(COUNTRY_CODE, 			entity.getCountryCode());
		assertEquals(JOB_TITLE, 			entity.getJobTitle());
		assertEquals(EMAIL, 				entity.getEmail());
		assertEquals(DELETED_FROM_SYSTEM, 	entity.isDeleteFromSystem());
		
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation 
	*/
	@Test
	void testToEntity() {
	
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
				.build();
		
		assertEquals(ID, 					entity.getId());
		assertEquals(TYPE, 					entity.getType());
		assertEquals(FIRST_NAME, 			entity.getFirstName());
		assertEquals(SURNAME, 				entity.getSurname());
		assertEquals(COUNTRY_CODE, 			entity.getCountryCode());
		assertEquals(JOB_TITLE, 			entity.getJobTitle());
		assertEquals(EMAIL, 				entity.getEmail());
		assertEquals(DELETED_FROM_SYSTEM, 	entity.isDeleteFromSystem());
		
		Candidate candidate = CandidateEntity.fromEntity(entity);
		
		assertEquals(ID, 					candidate.getId());
		assertEquals(TYPE, 					candidate.getType());
		assertEquals(FIRST_NAME, 			candidate.getFirstName());
		assertEquals(SURNAME, 				candidate.getSurname());
		assertEquals(COUNTRY_CODE, 			candidate.getCountryCode());
		assertEquals(JOB_TITLE, 			candidate.getJobTitle());
		assertEquals(EMAIL, 				candidate.getEmail());
		assertEquals(DELETED_FROM_SYSTEM, 	candidate.isDeleteFromSystem());
	}
	
	/**
	* Test populating Builder form existing Candidate 
	*/
	@Test
	void testBulderFromExisting() {
		
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
		
		Candidate clone = Candidate.builder().from(candidate).build();
	
		assertEquals(ID, 					clone.getId());
		assertEquals(TYPE, 					clone.getType());
		assertEquals(FIRST_NAME, 			clone.getFirstName());
		assertEquals(SURNAME, 				clone.getSurname());
		assertEquals(COUNTRY_CODE, 			clone.getCountryCode());
		assertEquals(JOB_TITLE, 			clone.getJobTitle());
		assertEquals(EMAIL, 				clone.getEmail());
		assertEquals(DELETED_FROM_SYSTEM, 	clone.isDeleteFromSystem());
		
	}
	
}