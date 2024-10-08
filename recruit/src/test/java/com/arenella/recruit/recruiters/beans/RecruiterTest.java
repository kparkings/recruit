package com.arenella.recruit.recruiters.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
		
		final String 		userId						= "kparkings";
		final String 		firstName					= "kevin";
		final String 		surname						= "parkings";
		final String 		email						= "admin@arenella-ict.com";
		final String		companyName					= "arenella";
		final String 		companyAddress				= "Julianastraat 16, Noordwijk, 2202KD";
		final String 		companyCountry				= "Nederland";
		final String 		companyVatNumber			= "123214";
		final String 		companyRegistrationNumber 	= "AAFF23";
		final boolean 		active						= false;
		final language 		language					= Recruiter.language.DUTCH;
		final LocalDate		accountCreated				= LocalDate.of(2021, 5, 1);
		final boolean		visibleToOtherRecruiters 	= true;
		
		Recruiter recruiter = Recruiter
								.builder()
									.accountCreated(accountCreated)
									.companyName(" "+ companyName.toUpperCase() + " ")
									.companyAddress(companyAddress)
									.companyCountry(companyCountry)
									.companyVatNumber(companyVatNumber)
									.companyRegistrationNumber(companyRegistrationNumber)
									.email(" "+ email.toUpperCase() + " ")
									.firstName(" "+ firstName.toLowerCase() + " ")
									.active(active)
									.language(language)
									.surname(" "+ surname.toUpperCase() + " ")
									.userId(" "+ userId.toUpperCase() + " ")
									.visibleToOtherRecruiters(visibleToOtherRecruiters)
								.build();
		
		assertEquals(accountCreated, 				recruiter.getAccountCreated());
		assertEquals(companyName, 					recruiter.getCompanyName());
		assertEquals(companyAddress, 				recruiter.getCompanyAddress());
		assertEquals(companyCountry, 				recruiter.getCompanyCountry());
		assertEquals(companyVatNumber, 				recruiter.getCompanyVatNumber());
		assertEquals(companyRegistrationNumber, 	recruiter.getCompanyRegistrationNumber());
		assertEquals(email, 						recruiter.getEmail());
		assertEquals(firstName, 					recruiter.getFirstName());
		assertEquals(active, 						recruiter.isActive());
		assertEquals(language, 						recruiter.getLanguage());
		assertEquals(surname, 						recruiter.getSurname());
		assertEquals(userId, 						recruiter.getUserId());
		assertTrue(recruiter.isVisibleToOtherRecruiters());
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
		
		assertFalse(recruiter.isVisibleToOtherRecruiters());
		
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
	* Tests adding a subscription for the Recruiter 
	*/
	@Test
	public void testAddInitialSubscription() {
		
		Recruiter recruiter = Recruiter.builder().build();
		
		assertTrue(recruiter.getSubscriptions().isEmpty());
		
		recruiter.addSubscription(CreditBasedSubscription.builder().build());
		
		assertFalse(recruiter.getSubscriptions().isEmpty());
		
	}
	
	/**
	* Tests that it is posible to update whether a recruiters is visible
	* to other recruiters or not
	* @throws Exception
	*/
	@Test
	public void testUpdateIsVisibleToOtherRecruiters() throws Exception {
		
		Recruiter recruiter = Recruiter.builder().build();
		
		assertFalse(recruiter.isVisibleToOtherRecruiters());
		
		recruiter.setVisibleToOtherRecruiters(true);
		
		assertTrue(recruiter.isVisibleToOtherRecruiters());
		
		recruiter.setVisibleToOtherRecruiters(false);
		
		assertFalse(recruiter.isVisibleToOtherRecruiters());
		
	}
	
}