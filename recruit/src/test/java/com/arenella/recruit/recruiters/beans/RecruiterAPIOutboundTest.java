package com.arenella.recruit.recruiters.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.Recruiter.language;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;

/**
* Unit tests for the RecruiterAPIOutbound class
* @author K Parkings
*/
public class RecruiterAPIOutboundTest {

	private static final String 		USER_ID						= "kparkings";
	private static final String 		FIRST_NAME					= "kevin";
	private static final String 		SURNAME						= "parkings";
	private static final String 		EMAIL						= "kparkings@gmail.com";
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
	public void testBuilder() throws Exception {
		
		final RecruiterSubscriptionAPIOutbound 			subscription 				= RecruiterSubscriptionAPIOutbound.builder().build();
		final Set<RecruiterSubscriptionAPIOutbound> 	subscriptions 				=	Set.of(subscription);
		final boolean 									active						= false;
		
		RecruiterAPIOutbound recruiter = RecruiterAPIOutbound
														.builder()
															.companyName(COMPANY_NAME)
															.companyAddress(COMPANY_ADDRESS)
															.companyCountry(COMPANY_COUNTRY)
															.companyVatNumber(COMPANY_VAT_NUMBER)
															.companyRegistrationNumber(COMPANY_REGISTRATION_NUMBER)
															.email(EMAIL)
															.firstName(FIRST_NAME)
															.active(active)
															.language(LANGUAGE)
															.surname(SURNAME)
															.userId(USER_ID)
															.subscriptions(subscriptions)
														.build();
								
		assertEquals(COMPANY_NAME, 					recruiter.getCompanyName());
		assertEquals(COMPANY_ADDRESS, 				recruiter.getCompanyAddress());
		assertEquals(COMPANY_COUNTRY, 				recruiter.getCompanyCountry());
		assertEquals(COMPANY_VAT_NUMBER, 			recruiter.getCompanyVatNumber());
		assertEquals(COMPANY_REGISTRATION_NUMBER, 	recruiter.getCompanyRegistrationNumber());
		assertEquals(EMAIL, 						recruiter.getEmail());
		assertEquals(FIRST_NAME, 					recruiter.getFirstName());
		assertEquals(active, 						recruiter.isActive());
		assertEquals(LANGUAGE, 						recruiter.getLanguage());
		assertEquals(SURNAME, 						recruiter.getSurname());
		assertEquals(USER_ID, 						recruiter.getUserId());
		assertTrue(recruiter.getSubscriptions().contains(subscription));
		
	}
	
	/**
	* Tests conversion from APIInbound representation to 
	* Domain representation
	* @throws Exception
	*/
	@Test
	public void testConvertToDomain() throws Exception{
		
		Recruiter recruiter = Recruiter
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

		RecruiterAPIOutbound recruiterAPIOutbound = RecruiterAPIOutbound.convertFromDomain(recruiter);
		
		assertEquals(COMPANY_NAME.toLowerCase(), 	recruiterAPIOutbound.getCompanyName());
		assertEquals(COMPANY_ADDRESS, 				recruiterAPIOutbound.getCompanyAddress());
		assertEquals(COMPANY_COUNTRY, 				recruiterAPIOutbound.getCompanyCountry());
		assertEquals(COMPANY_VAT_NUMBER, 			recruiterAPIOutbound.getCompanyVatNumber());
		assertEquals(COMPANY_REGISTRATION_NUMBER, 	recruiterAPIOutbound.getCompanyRegistrationNumber());
		assertEquals(EMAIL, 						recruiterAPIOutbound.getEmail());
		assertEquals(FIRST_NAME, 					recruiterAPIOutbound.getFirstName());
		assertEquals(LANGUAGE, 						recruiterAPIOutbound.getLanguage());
		assertEquals(SURNAME, 						recruiterAPIOutbound.getSurname());
		assertEquals(USER_ID, 						recruiterAPIOutbound.getUserId());

	}
	
	/**
	* Defensive programming test. Checks subscription collections exists but is empty by default
	* @throws Exception
	*/
	@Test
	public void testBuilderDefaults() throws Exception {
		
		RecruiterAPIOutbound recruiter = RecruiterAPIOutbound.builder().build();
		
		assertTrue(recruiter.getSubscriptions().isEmpty());
		assertFalse(recruiter.getHasActiveSubscription());
		
	}
	
	/**
	* Tests hasActiveSubscription set to false if awaiting_activation
	* @throws Exception
	*/
	@Test
	public void testBuilder_subsriptionStatus_awaiting_activation() throws Exception{
		
		RecruiterAPIOutbound recruiter = 
				RecruiterAPIOutbound
					.builder()
						.subscriptions(Set.of(RecruiterSubscriptionAPIOutbound.builder().status(subscription_status.AWAITING_ACTIVATION).currentSubscription(true).build()))
					.build();
		
		assertFalse(recruiter.getHasActiveSubscription());
		
	}
	
	/**
	* Tests hasActiveSubscription set to true if pending_payment
	* @throws Exception
	*/
	@Test
	public void testBuilder_subsriptionStatus_active_pending_payment() throws Exception{
		
		RecruiterAPIOutbound recruiter = 
				RecruiterAPIOutbound
					.builder()
						.subscriptions(Set.of(RecruiterSubscriptionAPIOutbound.builder().status(subscription_status.ACTIVE_PENDING_PAYMENT).currentSubscription(true).build()))
					.build();
		
		assertTrue(recruiter.getHasActiveSubscription());
		
	}
	
	/**
	* Tests hasActiveSubscription set to true if active
	* @throws Exception
	*/
	@Test
	public void testBuilder_subsriptionStatus_active() throws Exception{
		
		RecruiterAPIOutbound recruiter = 
				RecruiterAPIOutbound
					.builder()
						.subscriptions(Set.of(RecruiterSubscriptionAPIOutbound.builder().status(subscription_status.ACTIVE).currentSubscription(true).build()))
					.build();
		
		assertTrue(recruiter.getHasActiveSubscription());
		
	}
	
	/**
	* Tests hasActiveSubscription set to false if pending_payment
	* @throws Exception
	*/
	@Test
	public void testBuilder_subsriptionStatus_disabled_pending_payment() throws Exception{
		
		RecruiterAPIOutbound recruiter = 
				RecruiterAPIOutbound
					.builder()
						.subscriptions(Set.of(RecruiterSubscriptionAPIOutbound.builder().status(subscription_status.DISABLED_PENDING_PAYMENT).currentSubscription(true).build()))
					.build();
		
		assertFalse(recruiter.getHasActiveSubscription());
		
	}
	
	/**
	* Tests hasActiveSubscription set to false if subscription_ended
	* @throws Exception
	*/
	@Test
	public void testBuilder_subsriptionStatus_subscription_ended() throws Exception{
		
		RecruiterAPIOutbound recruiter = 
				RecruiterAPIOutbound
					.builder()
						.subscriptions(Set.of(RecruiterSubscriptionAPIOutbound.builder().status(subscription_status.SUBSCRIPTION_ENDED).currentSubscription(true).build()))
					.build();
		
		assertFalse(recruiter.getHasActiveSubscription());
		
	}
	
}