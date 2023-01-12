package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.Recruiter.language;

/**
* Unit tests for the RecruiterUpdatedEvent class
* @author K Parkings
*/
public class RecruiterUpdatedEventTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConsructor() throws Exception{
		
		final String 	recruiterId		= "kparkings";
		final String 	firstName		= "kevin";
		final String 	surname			= "parkings";	
		final String 	email			= "kparkings@gmail.com";
		final String	companyName		= "Arenella BV";
		final language 	language		= Recruiter.language.DUTCH;
		
		RecruiterUpdatedEvent event = RecruiterUpdatedEvent
				.builder()
					.companyName(companyName)
					.email(email)
					.firstName(firstName)
					.language(language)
					.recruiterId(recruiterId)
					.surname(surname)
				.build();
		
		assertEquals(recruiterId, 	event.getRecruiterId());
		assertEquals(firstName, 	event.getFirstName());
		assertEquals(surname, 		event.getSurname());
		assertEquals(email, 		event.getEmail());
		assertEquals(companyName, 	event.getCompanyName());
		assertEquals(language, 		event.getLanguage());
		
	}
	
}
