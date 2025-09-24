package com.arenella.recruit.recruiters.beans;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.Recruiter.language;

class RecruiterAPIInboundTest {

	private static final String 		USER_ID						= "kparkings";
	private static final String 		FIRST_NAME					= "kevin";
	private static final String 		SURNAME						= "parkings";
	private static final String 		EMAIL						= "admin@arenella-ict.com";
	private static final String			COMPANY_NAME				= "Arenella";
	private static final String 		COMPANY_ADDRESS				= "Julianastraat 16, Noordwijk, 2202KD";
	private static final String 		COMPANY_COUNTRY				= "Nederland";
	private static final String 		COMPANY_VAT_NUMBER			= "123214";
	private static final String 		COMPANY_REGISTRATION_NUMBER = "AAFF23";
	private static final language 		LANGUAGE					= Recruiter.language.DUTCH;
	
	/**
	* Tests creation via Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		RecruiterAPIInbound recruiter = RecruiterAPIInbound
													.builder()
														.companyName(COMPANY_NAME)
														.companyAddress(COMPANY_ADDRESS)
														.companyCountry(COMPANY_COUNTRY)
														.companyVatNumber(COMPANY_VAT_NUMBER)
														.companyRegistrationNumber(COMPANY_REGISTRATION_NUMBER)
														.email(EMAIL)
														.firstName(FIRST_NAME)
														.language(LANGUAGE)
														.surname(SURNAME)
														.userId(USER_ID)
													.build();
		
		assertEquals(COMPANY_NAME, 					recruiter.getCompanyName());
		assertEquals(COMPANY_ADDRESS, 				recruiter.getCompanyAddress());
		assertEquals(COMPANY_COUNTRY, 				recruiter.getCompanyCountry());
		assertEquals(COMPANY_VAT_NUMBER, 			recruiter.getCompanyVatNumber());
		assertEquals(COMPANY_REGISTRATION_NUMBER, 	recruiter.getCompanyRegistrationNumber());
		assertEquals(EMAIL, 						recruiter.getEmail());
		assertEquals(FIRST_NAME, 					recruiter.getFirstName());
		assertEquals(LANGUAGE, 						recruiter.getLanguage());
		assertEquals(SURNAME, 						recruiter.getSurname());
		assertEquals(USER_ID, 						recruiter.getUserId());
		
	}
	
	/**
	* Tests conversion from APIInbound representation to 
	* Domain representation
	* @throws Exception
	*/
	@Test
	void testConvertToDomain() {
		
		RecruiterAPIInbound recruiterAPIOutbound = RecruiterAPIInbound
				.builder()
					.companyName(COMPANY_NAME)
					.companyAddress(COMPANY_ADDRESS)
					.companyCountry(COMPANY_COUNTRY)
					.companyVatNumber(COMPANY_VAT_NUMBER)
					.companyRegistrationNumber(COMPANY_REGISTRATION_NUMBER)
					.email(EMAIL)
					.firstName(FIRST_NAME)
					.language(LANGUAGE)
					.surname(SURNAME)
					.userId(USER_ID)
				.build();

		Recruiter recruiter = RecruiterAPIInbound.convertToDomain(recruiterAPIOutbound);
		
		assertEquals(COMPANY_NAME.toLowerCase(), 	recruiter.getCompanyName());
		assertEquals(COMPANY_ADDRESS, 				recruiter.getCompanyAddress());
		assertEquals(COMPANY_COUNTRY, 				recruiter.getCompanyCountry());
		assertEquals(COMPANY_VAT_NUMBER, 			recruiter.getCompanyVatNumber());
		assertEquals(COMPANY_REGISTRATION_NUMBER, 	recruiter.getCompanyRegistrationNumber());
		assertEquals(EMAIL, 						recruiter.getEmail());
		assertEquals(FIRST_NAME, 					recruiter.getFirstName());
		assertEquals(LANGUAGE, 						recruiter.getLanguage());
		assertEquals(SURNAME, 						recruiter.getSurname());
		assertEquals(USER_ID, 						recruiter.getUserId());

	}
	
}