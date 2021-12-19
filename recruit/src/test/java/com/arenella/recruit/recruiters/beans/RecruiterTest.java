package com.arenella.recruit.recruiters.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
		final String		companyName		= "arenella";
		final boolean 		active			= false;
		final language 		language		= Recruiter.language.DUTCH;
		final LocalDate		accountCreated	= LocalDate.of(2021, 5, 1);
		
		Recruiter recruiter = Recruiter
								.builder()
									.accountCreated(accountCreated)
									.companyName(" "+ companyName.toUpperCase() + " ")
									.email(" "+ email.toUpperCase() + " ")
									.firstName(" "+ firstName.toLowerCase() + " ")
									.active(active)
									.language(language)
									.surname(" "+ surname.toUpperCase() + " ")
									.userId(" "+ userId.toUpperCase() + " ")
								.build();
		
		assertEquals(recruiter.getAccountCreated(), accountCreated);
		assertEquals(recruiter.getCompanyName(), 	companyName);
		assertEquals(recruiter.getEmail(), 			email);
		assertEquals(recruiter.getFirstName(), 		firstName);
		assertEquals(recruiter.isActive(), 			active);
		assertEquals(recruiter.getLanguage(), 		language);
		assertEquals(recruiter.getSurname(), 		surname);
		assertEquals(recruiter.getUserId(), 		userId);
		
	}
	
	/**
	* Test if initial activation of Recruiter both the 
	* active flag and the accountCreated Date are set
	*/
	@Test
	public void testActivateAccount_initial() throws Exception{
		
		Recruiter recruiter = Recruiter.builder().build();
		
		assertFalse(recruiter.isActive());
		assertNull(recruiter.getAccountCreated());
		
		recruiter.activateAccount();
		
		assertTrue(recruiter.isActive());
		assertNotNull(recruiter.getAccountCreated());
		
	}
	
	/**
	* Tests disable of Account
	* @throws Exception
	*/
	@Test
	public void testDisableAccount() throws Exception{
		
		Recruiter recruiter = Recruiter.builder().build();
		
		recruiter.activateAccount();
		
		LocalDate accountCreated = recruiter.getAccountCreated();
		
		recruiter.disableAccount();
		
		assertEquals(recruiter.getAccountCreated(), accountCreated);
		assertFalse(recruiter.isActive());
		
	}
	
	/**
	* Tests reactivation of the Account. In this scenario active 
	* will become true but the account creation date will remain the same
	* @throws Exception
	*/
	@Test
	public void testReactivationOfAccount() throws Exception{
		
		Recruiter recruiter = Recruiter.builder().build();
		
		recruiter.activateAccount();
		
		LocalDate accountCreated = recruiter.getAccountCreated();
		
		recruiter.disableAccount();
		recruiter.activateAccount();
		
		assertEquals(recruiter.getAccountCreated(), accountCreated);
		assertTrue(recruiter.isActive());
		
	}
	
	/**
	* Tests adding the initial subscription for the Recruiter 
	*/
	@Test
	public void testAddInitialSubscription() {
		
		Recruiter recruiter = Recruiter.builder().build();
		
		assertTrue(recruiter.getSubscriptions().isEmpty());
		
		recruiter.addInitialSubscription(FirstGenRecruiterSubscription.builder().build());
		
		assertFalse(recruiter.getSubscriptions().isEmpty());
		
	}
	
	/**
	* Tests Exception thrown if an attempt is made to add an initial subscription to a Recruiter
	* that already has at least one subscription
	*/
	@Test
	public void testAddInitialSubscription_existingSubscription() {
		
		Recruiter recruiter = Recruiter.builder().build();
		
		assertTrue(recruiter.getSubscriptions().isEmpty());
		
		recruiter.addInitialSubscription(FirstGenRecruiterSubscription.builder().build());
		
		assertThrows(IllegalStateException.class, () -> {
			recruiter.addInitialSubscription(FirstGenRecruiterSubscription.builder().build());
		});
	}
	
}