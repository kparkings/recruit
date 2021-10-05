package com.arenella.recruit.recruiters.beans;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.Recruiter.language;

public class RecruiterAPIInboundTest {

	/**
	* Tests creation via Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		final String 		userId			= "kparkings";
		final String 		firstName		= "kevin";
		final String 		surname			= "parkings";
		final String 		email			= "kparkings@gmail.com";
		final String		companyName		= "Arenella";
		final language 		language		= Recruiter.language.DUTCH;
		
		RecruiterAPIInbound recruiter = RecruiterAPIInbound
													.builder()
														.companyName(companyName)
														.email(email)
														.firstName(firstName)
														.language(language)
														.surname(surname)
														.userId(userId)
													.build();
		
		assertEquals(recruiter.getCompanyName(), 	companyName);
		assertEquals(recruiter.getEmail(), 			email);
		assertEquals(recruiter.getFirstName(), 		firstName);
		assertEquals(recruiter.getLanguage(), 		language);
		assertEquals(recruiter.getSurname(), 		surname);
		assertEquals(recruiter.getUserId(), 		userId);
		
	}
	
}