package com.arenella.recruit.recruiters.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.Recruiter;

/**
* Unit tests for the RecruiterBasicInfoAPIOutbound class 
*/
class RecruiterBasicInfoAPIOutboundTest {

	private static final String RECRUITER_ID 	= "rec1";
	private static final String FIRST_NAME 		= "john";
	private static final String SURNAME 		= "doe";
	
	/**
	* Tests construction via builder 
	*/
	@Test
	void testContruction() {
		
		RecruiterBasicInfoAPIOutbound rec = 
				RecruiterBasicInfoAPIOutbound
				.builder()
					.recruiterId(RECRUITER_ID)
					.firstname(FIRST_NAME)
					.surname(SURNAME)
				.build();
		
		assertEquals(RECRUITER_ID, 	rec.getRecruiterId());
		assertEquals(FIRST_NAME, 	rec.getFirstname());
		assertEquals(SURNAME, 		rec.getSurname());
		
	}
	
	/**
	* Tests conversion from Domain to API Outbound representation 
	*/
	@Test
	void testFromDomain() {
		
		Recruiter rec = 
				Recruiter
				.builder()
					.userId(RECRUITER_ID)
					.firstName(FIRST_NAME)
					.surname(SURNAME)
				.build();
		
		assertEquals(RECRUITER_ID, 	rec.getUserId());
		assertEquals(FIRST_NAME, 	rec.getFirstName());
		assertEquals(SURNAME, 		rec.getSurname());
		
		RecruiterBasicInfoAPIOutbound outbound = RecruiterBasicInfoAPIOutbound.fromDomain(rec);
		
		assertEquals(RECRUITER_ID, 	outbound.getRecruiterId());
		assertEquals(FIRST_NAME, 	outbound.getFirstname());
		assertEquals(SURNAME, 		outbound.getSurname());
		
	}
	
}
