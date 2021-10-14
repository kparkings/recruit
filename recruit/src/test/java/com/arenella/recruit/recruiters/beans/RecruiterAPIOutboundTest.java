package com.arenella.recruit.recruiters.beans;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.Recruiter.language;

/**
* Unit tests for the RecruiterAPIOutbound class
* @author K Parkings
*/
public class RecruiterAPIOutboundTest {

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
		final boolean 		active			= false;
		final language 		language		= Recruiter.language.DUTCH;
		
		RecruiterAPIOutbound recruiter = RecruiterAPIOutbound
														.builder()
															.companyName(companyName)
															.email(email)
															.firstName(firstName)
															.active(active)
															.language(language)
															.surname(surname)
															.userId(userId)
														.build();
								
		assertEquals(recruiter.getCompanyName(), 	companyName);
		assertEquals(recruiter.getEmail(), 			email);
		assertEquals(recruiter.getFirstName(), 		firstName);
		assertEquals(recruiter.isActive(), 			active);
		assertEquals(recruiter.getLanguage(), 		language);
		assertEquals(recruiter.getSurname(), 		surname);
		assertEquals(recruiter.getUserId(), 		userId);
		
	}
	
}