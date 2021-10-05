package com.arenella.recruit.recruiters.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.Recruiter.language;

/**
* Unit tests for the Recruiter class
* @author K Parkings
*/
public class RecruiterTest {

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
		final LocalDate		accountCreated	= LocalDate.of(2021, 5, 1);
		
		Recruiter recruiter = Recruiter
								.builder()
									.accountCreated(accountCreated)
									.companyName(companyName)
									.email(email)
									.firstName(firstName)
									.isActive(active)
									.language(language)
									.surname(surname)
									.userId(userId)
								.build();
		
		assertEquals(recruiter.getAccountCreated(), accountCreated);
		assertEquals(recruiter.getCompanyName(), 	companyName);
		assertEquals(recruiter.getEmail(), 			email);
		assertEquals(recruiter.getFirstName(), 		firstName);
		assertEquals(recruiter.isAvtive(), 			active);
		assertEquals(recruiter.getLanguage(), 		language);
		assertEquals(recruiter.getSurname(), 		surname);
		assertEquals(recruiter.getUserId(), 		userId);
		
	}
	
	/**
	* Tests Defaults
	* 	- account is active
	* @throws Exception
	*/
	@Test
	public void testBuilderDefault() throws Exception {
		
		Recruiter recruiter = Recruiter.builder().build();
		
		assertTrue(recruiter.isAvtive());
		
	}
	
}
