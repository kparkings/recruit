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
public class RecruiterSubscriptionAPIOutboundTest {

	private static final UUID 					subscriptionId		= UUID.randomUUID();
	private static final String 				recruiterId			= "kparkings";
	private static final LocalDateTime 			created				= LocalDateTime.of(2022, 01, 01, 19, 31);
	private static final LocalDateTime 			activatedDate		= LocalDateTime.of(2022, 02, 01, 19, 31);
	private static final subscription_status	status				= subscription_status.ACTIVE;
	private static final subscription_type		type				= subscription_type.CREDIT_BASED_SUBSCRIPTION;
	private static final INVOICE_TYPE			invoiceType			= INVOICE_TYPE.BUSINESS;
	
	/**
	* Tests construction via the builder
	* @throws Exception
	*/
	@Test
	public void testBuilder_activeSubscription() throws Exception{
		
		RecruiterSubscriptionAPIOutbound subscription = RecruiterSubscriptionAPIOutbound
				.builder()
				.activateDate(activatedDate)
				.created(created)
				.recruiterId(recruiterId)
				.status(status)
				.subscriptionId(subscriptionId)
				.type(type)
				.currentSubscription(true)
				.invoiceType(invoiceType)
			.build();
		
		assertEquals(activatedDate, 	subscription.getActivatedDate());
		assertEquals(created, 			subscription.getCreated());
		assertEquals(recruiterId, 		subscription.getRecruiterId());
		assertEquals(status, 			subscription.getStatus());
		assertEquals(subscriptionId, 	subscription.getSubscriptionId());
		assertEquals(type, 				subscription.getType());
		assertEquals(invoiceType, 		subscription.getInvoiceType());
		assertTrue(subscription.isCurrentSubscription());
		
	}
	
	/**
	* Tests construction via the builder - not active subscription variant
	* @throws Exception
	*/
	@Test
	public void testBuilder_nonActiveSubscription() throws Exception{
		
		final subscription_status	status	= subscription_status.SUBSCRIPTION_ENDED;
		
		RecruiterSubscriptionAPIOutbound subscription = RecruiterSubscriptionAPIOutbound
				.builder()
				.activateDate(activatedDate)
				.created(created)
				.recruiterId(recruiterId)
				.status(status)
				.subscriptionId(subscriptionId)
				.type(type)
				.currentSubscription(false)
			.build();
		
		assertEquals(activatedDate, 	subscription.getActivatedDate());
		assertEquals(created, 			subscription.getCreated());
		assertEquals(recruiterId, 		subscription.getRecruiterId());
		assertEquals(status, 			subscription.getStatus());
		assertEquals(subscriptionId, 	subscription.getSubscriptionId());
		assertEquals(type, 				subscription.getType());
		assertFalse(subscription.isCurrentSubscription());
		
	}
	
	/**
	* Tests conversion of RecruiterSubscription (domain representation) to 
	* API Outbound representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromSubscription() throws Exception {
		
		CreditBasedSubscription subscription = CreditBasedSubscription
																			.builder()
																				.activateDate(activatedDate)
																				.created(created)
																				.recruiterId(recruiterId)
																				.status(status)
																				.subscriptionId(subscriptionId)
																				.currentSubscription(true)
																			.build();
		
		RecruiterSubscriptionAPIOutbound outbound = RecruiterSubscriptionAPIOutbound.convertFromSubscription(subscription);		
				
		assertEquals(activatedDate, 	outbound.getActivatedDate());
		assertEquals(created, 			outbound.getCreated());
		assertEquals(recruiterId, 		outbound.getRecruiterId());
		assertEquals(status, 			outbound.getStatus());
		assertEquals(subscriptionId, 	outbound.getSubscriptionId());
		assertEquals(type, 				outbound.getType());
		
		assertTrue(outbound.isCurrentSubscription());
		assertTrue(subscription.getInvoiceType().isEmpty());
		
	}

	/**
	* Tests conversion of RecruiterSubscription (domain representation) to 
	* API Outbound representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromSubscription_paidSubscription() throws Exception {
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																			.builder()
																				.activateDate(activatedDate)
																				.created(created)
																				.recruiterId(recruiterId)
																				.status(status)
																				.subscriptionId(subscriptionId)
																				.currentSubscription(true)
																				.invoiceType(invoiceType)
																			.build();
		
		RecruiterSubscriptionAPIOutbound outbound = RecruiterSubscriptionAPIOutbound.convertFromSubscription(subscription);		
				
		assertEquals(invoiceType, 				outbound.getInvoiceType().get());
		
	}
}