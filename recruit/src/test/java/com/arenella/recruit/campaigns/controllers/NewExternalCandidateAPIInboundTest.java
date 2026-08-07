package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the NewExternalCandidateAPIInbound class 
*/
class NewExternalCandidateAPIInboundTest {

	private static final String FIRSTNAME		= "bill";
	private static final String SURNAME			= "bao";
	private static final String COUNTRY_CODE	= "es";
	private static final String JOB_TITLE		= "React Developer";
	private static final String EMAIL			= "billb@boop.es";
	
	/**
	* Tests construction 
	*/
	@Test
	void testConstruction() {
		
		 NewExternalCandidateAPIInbound candidate =  NewExternalCandidateAPIInbound
				 .builder()
				 	.firstName(FIRSTNAME)
				 	.surname(SURNAME)
				 	.countryCode(COUNTRY_CODE)
				 	.jobTitle(JOB_TITLE)
				 	.email(EMAIL)
				 .build();
		 
		 assertEquals(FIRSTNAME, 	candidate.getFirstName());
		 assertEquals(SURNAME, 		candidate.getSurname());
		 assertEquals(COUNTRY_CODE, candidate.getCountryCode());
		 assertEquals(JOB_TITLE, 	candidate.getJobTitle());
		 assertEquals(EMAIL, 		candidate.getEmail());
		
	}
	
}
