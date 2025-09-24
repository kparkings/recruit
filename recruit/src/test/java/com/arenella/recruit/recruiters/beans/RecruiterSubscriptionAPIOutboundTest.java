package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterSubscription.INVOICE_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Unit tests for the RecruiterSubscriptionAPIOutboud class
* @author K Parkings
*/
class RecruiterSubscriptionAPIOutboundTest {

	private static final UUID 					SUBSCRIPTION_ID		= UUID.randomUUID();
	private static final String 				RECRUITER_ID		= "kparkings";
	private static final LocalDateTime 			CREATED				= LocalDateTime.of(2022, 01, 01, 19, 31);
	private static final LocalDateTime 			ACTIVATED_DATE		= LocalDateTime.of(2022, 02, 01, 19, 31);
	private static final subscription_status	STATUS				= subscription_status.ACTIVE;
	private static final subscription_type		TYPE				= subscription_type.CREDIT_BASED_SUBSCRIPTION;
	private static final INVOICE_TYPE			INVOICE_TYPE_VAL	= INVOICE_TYPE.BUSINESS;
	
	/**
	* Tests construction via the builder
	* @throws Exception
	*/
	@Test
	void testBuilder_activeSubscription() {
		
		RecruiterSubscriptionAPIOutbound subscription = RecruiterSubscriptionAPIOutbound
				.builder()
				.activateDate(ACTIVATED_DATE)
				.created(CREATED)
				.recruiterId(RECRUITER_ID)
				.status(STATUS)
				.subscriptionId(SUBSCRIPTION_ID)
				.type(TYPE)
				.currentSubscription(true)
				.invoiceType(INVOICE_TYPE_VAL)
			.build();
		
		assertEquals(ACTIVATED_DATE, 	subscription.getActivatedDate());
		assertEquals(CREATED, 			subscription.getCreated());
		assertEquals(RECRUITER_ID, 		subscription.getRecruiterId());
		assertEquals(STATUS, 			subscription.getStatus());
		assertEquals(SUBSCRIPTION_ID, 	subscription.getSubscriptionId());
		assertEquals(TYPE, 				subscription.getType());
		assertEquals(INVOICE_TYPE_VAL, 	subscription.getInvoiceType().get());
		assertTrue(subscription.isCurrentSubscription());
		
	}
	
	/**
	* Tests construction via the builder - not active subscription variant
	* @throws Exception
	*/
	@Test
	void testBuilder_nonActiveSubscription() {
		
		final subscription_status	status	= subscription_status.SUBSCRIPTION_ENDED;
		
		RecruiterSubscriptionAPIOutbound subscription = RecruiterSubscriptionAPIOutbound
				.builder()
				.activateDate(ACTIVATED_DATE)
				.created(CREATED)
				.recruiterId(RECRUITER_ID)
				.status(status)
				.subscriptionId(SUBSCRIPTION_ID)
				.type(TYPE)
				.currentSubscription(false)
			.build();
		
		assertEquals(ACTIVATED_DATE, 	subscription.getActivatedDate());
		assertEquals(CREATED, 			subscription.getCreated());
		assertEquals(RECRUITER_ID, 		subscription.getRecruiterId());
		assertEquals(status, 			subscription.getStatus());
		assertEquals(SUBSCRIPTION_ID, 	subscription.getSubscriptionId());
		assertEquals(TYPE, 				subscription.getType());
		assertFalse(subscription.isCurrentSubscription());
		
	}
	
	/**
	* Tests conversion of RecruiterSubscription (domain representation) to 
	* API Outbound representation
	* @throws Exception
	*/
	@Test
	void testConvertFromSubscription() {
		
		CreditBasedSubscription subscription = CreditBasedSubscription
																			.builder()
																				.activateDate(ACTIVATED_DATE)
																				.created(CREATED)
																				.recruiterId(RECRUITER_ID)
																				.status(STATUS)
																				.subscriptionId(SUBSCRIPTION_ID)
																				.currentSubscription(true)
																			.build();
		
		RecruiterSubscriptionAPIOutbound outbound = RecruiterSubscriptionAPIOutbound.convertFromSubscription(subscription);		
				
		assertEquals(ACTIVATED_DATE, 	outbound.getActivatedDate());
		assertEquals(CREATED, 			outbound.getCreated());
		assertEquals(RECRUITER_ID, 		outbound.getRecruiterId());
		assertEquals(STATUS, 			outbound.getStatus());
		assertEquals(SUBSCRIPTION_ID, 	outbound.getSubscriptionId());
		assertEquals(TYPE, 				outbound.getType());
		
		assertTrue(outbound.isCurrentSubscription());
		assertTrue(subscription.getInvoiceType().isEmpty());
		
	}

	/**
	* Tests conversion of RecruiterSubscription (domain representation) to 
	* API Outbound representation
	* @throws Exception
	*/
	@Test
	void testConvertFromSubscription_paidSubscription() {
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																			.builder()
																				.activateDate(ACTIVATED_DATE)
																				.created(CREATED)
																				.recruiterId(RECRUITER_ID)
																				.status(STATUS)
																				.subscriptionId(SUBSCRIPTION_ID)
																				.currentSubscription(true)
																				.invoiceType(INVOICE_TYPE_VAL)
																			.build();
		
		RecruiterSubscriptionAPIOutbound outbound = RecruiterSubscriptionAPIOutbound.convertFromSubscription(subscription);		
				
		assertEquals(INVOICE_TYPE_VAL, outbound.getInvoiceType().get());
		
	}
}