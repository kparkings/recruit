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

	/**
	* Tests creation via Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		final String 									userId			= "kparkings";
		final String 									firstName		= "kevin";
		final String 									surname			= "parkings";
		final String 									email			= "kparkings@gmail.com";
		final String									companyName		= "Arenella";
		final boolean 									active			= false;
		final language 									language		= Recruiter.language.DUTCH;
		final RecruiterSubscriptionAPIOutbound 			subscription 	= RecruiterSubscriptionAPIOutbound.builder().build();
		final Set<RecruiterSubscriptionAPIOutbound> 	subscriptions 	=	Set.of(subscription);
		
		RecruiterAPIOutbound recruiter = RecruiterAPIOutbound
														.builder()
															.companyName(companyName)
															.email(email)
															.firstName(firstName)
															.active(active)
															.language(language)
															.surname(surname)
															.userId(userId)
															.subscriptions(subscriptions)
														.build();
								
		assertEquals(recruiter.getCompanyName(), 	companyName);
		assertEquals(recruiter.getEmail(), 			email);
		assertEquals(recruiter.getFirstName(), 		firstName);
		assertEquals(recruiter.isActive(), 			active);
		assertEquals(recruiter.getLanguage(), 		language);
		assertEquals(recruiter.getSurname(), 		surname);
		assertEquals(recruiter.getUserId(), 		userId);
		assertTrue(recruiter.getSubscriptions().contains(subscription));
		
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