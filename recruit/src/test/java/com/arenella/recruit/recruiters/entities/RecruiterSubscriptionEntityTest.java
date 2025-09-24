package com.arenella.recruit.recruiters.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.CreditBasedSubscription;
import com.arenella.recruit.recruiters.beans.PaidPeriodRecruiterSubscription;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.INVOICE_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Unit tests for the RecruiterSubscriptionEntity class
* @author K Parkings
*/
class RecruiterSubscriptionEntityTest {

	private static final LocalDateTime 			CREATED 			= LocalDateTime.of(2021, 12, 18, 10, 10);
	private static final LocalDateTime 			ACTIVATED_DATE 		= LocalDateTime.of(2021, 12, 24, 10, 10);
	private static final String					RECRUITER_ID		= "kparkings";
	private static final UUID					SUBSCRIPTION_ID		= UUID.randomUUID();
	private static final subscription_status 	STATUS 				= subscription_status.ACTIVE;
	private static final subscription_type		TYPE				= subscription_type.ONE_MONTH_SUBSCRIPTION;
	private static final INVOICE_TYPE			INVOICE_TYPE_VAL	= INVOICE_TYPE.BUSINESS;
	
	/**
	* Tests creation via the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		RecruiterSubscriptionEntity subscription = RecruiterSubscriptionEntity
																			.builder()
																				.activateDate(ACTIVATED_DATE)
																				.created(CREATED)
																				.recruiterId(RECRUITER_ID)
																				.subscriptionId(SUBSCRIPTION_ID)
																				.status(STATUS)
																				.type(TYPE)
																				.currentSubscription(true)
																				.invoiceType(INVOICE_TYPE_VAL)
																			.build();
		
		assertEquals(ACTIVATED_DATE, 													subscription.getActivatedDate());
		assertEquals(CREATED, 															subscription.getCreated());
		assertEquals(RECRUITER_ID, 														subscription.getRecruiterId());
		assertEquals(STATUS, 															subscription.getStatus());
		assertEquals(SUBSCRIPTION_ID, 													subscription.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.ONE_MONTH_SUBSCRIPTION, 	subscription.getType());
		assertEquals(INVOICE_TYPE_VAL, 													subscription.getInvoiceType().get());
		assertTrue(subscription.isCurrentSubscription());
	
	}

	/**
	* Tests the setters
	* @throws Exception
	*/
	@Test
	void testSetters() {
		
		RecruiterSubscriptionEntity subscription = RecruiterSubscriptionEntity
				.builder()
				.build();
		
		subscription.setActivatedDate(ACTIVATED_DATE);
		subscription.setCreated(CREATED);
		subscription.setRecruiterId(RECRUITER_ID);
		subscription.setStatus(STATUS);
		subscription.setSubscriptionId(SUBSCRIPTION_ID);
		subscription.setType(TYPE);
		subscription.setCurrentSubscription(true);
		subscription.setInvoiceType(INVOICE_TYPE_VAL);

		assertEquals(ACTIVATED_DATE, 													subscription.getActivatedDate());
		assertEquals(CREATED, 															subscription.getCreated());
		assertEquals(RECRUITER_ID, 														subscription.getRecruiterId());
		assertEquals(STATUS, 															subscription.getStatus());
		assertEquals(SUBSCRIPTION_ID, 													subscription.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.ONE_MONTH_SUBSCRIPTION, 	subscription.getType());
		assertEquals(INVOICE_TYPE_VAL, 													subscription.getInvoiceType().get());
		assertTrue(subscription.isCurrentSubscription());
		
	}
	
	/**
	* Tests conversion of the the Domain representation of a RecruiterSubscription
	* to an Entity representation
	* @throws Exception
	*/
	@Test
	void testConvertToEntity_noExistingEntity() {
		
		CreditBasedSubscription subscription = CreditBasedSubscription
				.builder()
					.activateDate(ACTIVATED_DATE)
					.created(CREATED)
					.recruiterId(RECRUITER_ID)
					.subscriptionId(SUBSCRIPTION_ID)
					.status(STATUS)
					.currentSubscription(true)
				.build();
		
		RecruiterSubscriptionEntity entity = RecruiterSubscriptionEntity.convertToEntity(subscription, Optional.empty());
		
		assertEquals(ACTIVATED_DATE, 													entity.getActivatedDate());
		assertEquals(CREATED, 															entity.getCreated());
		assertEquals(RECRUITER_ID, 														entity.getRecruiterId());
		assertEquals(STATUS, 															entity.getStatus());
		assertEquals(SUBSCRIPTION_ID, 													entity.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.CREDIT_BASED_SUBSCRIPTION, entity.getType());
		assertTrue(entity.isCurrentSubscription());
		assertTrue(entity.getInvoiceType().isEmpty());
		
	}
	
	/**
	* Tests conversion where an existing version of the Entity exists. This is used
	* for hibernate managed instances to avoid conflicts of duplicate objects
	* @throws Exception
	*/
	@Test
	void testConvertToEntity_existingEntity() {
		
		CreditBasedSubscription subscription = CreditBasedSubscription
				.builder()
					.activateDate(ACTIVATED_DATE)
					.created(CREATED)
					.recruiterId(RECRUITER_ID)
					.subscriptionId(SUBSCRIPTION_ID)
					.status(STATUS)
					.currentSubscription(true)
				.build();
		
		RecruiterSubscriptionEntity existingEntity = RecruiterSubscriptionEntity
				.builder()
				.build();
		
		RecruiterSubscriptionEntity entity = RecruiterSubscriptionEntity.convertToEntity(subscription, Optional.of(existingEntity));
		
		assertEquals(ACTIVATED_DATE, 													entity.getActivatedDate());
		assertEquals(CREATED, 															entity.getCreated());
		assertEquals(RECRUITER_ID, 														entity.getRecruiterId());
		assertEquals(STATUS, 															entity.getStatus());
		assertEquals(SUBSCRIPTION_ID, 													entity.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.CREDIT_BASED_SUBSCRIPTION, entity.getType());
		assertTrue(entity.isCurrentSubscription());
		assertSame(entity, existingEntity); 
		assertTrue(entity.getInvoiceType().isEmpty());
		
	}
	
	/**
	* Tests conversion of Entity representation of RecruiterSubscription to 
	* Domain representation [type == FIRST_GEN
	* @throws Exception
	*/
	@Test
	void testConvertFromEntity_FIRST_GEN() {
		
		RecruiterSubscriptionEntity entity = RecruiterSubscriptionEntity
				.builder()
					.activateDate(ACTIVATED_DATE)
					.created(CREATED)
					.recruiterId(RECRUITER_ID)
					.subscriptionId(SUBSCRIPTION_ID)
					.status(STATUS)
					.type(subscription_type.CREDIT_BASED_SUBSCRIPTION)
					.currentSubscription(true)
				.build();
		
		CreditBasedSubscription subscription = (CreditBasedSubscription) RecruiterSubscriptionEntity.convertFromEntity(entity);
		
		assertEquals(ACTIVATED_DATE, 									subscription.getActivatedDate());
		assertEquals(CREATED, 											subscription.getCreated());
		assertEquals(RECRUITER_ID, 										subscription.getRecruiterId());
		assertEquals(STATUS, 											subscription.getStatus());
		assertEquals(SUBSCRIPTION_ID, 									subscription.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.CREDIT_BASED_SUBSCRIPTION, subscription.getType());
		assertTrue(subscription.isCurrentSubscription());
		
	}
	
	/**
	* Tests conversion of Entity representation of RecruiterSubscription to 
	* Domain representation [type == YEARLY_SUBSCRIPTION ]
	* @throws Exception
	*/
	@Test
	void testConvertFromEntity_YEARLY_SUBSCRIPTION() {
		
		RecruiterSubscriptionEntity entity = RecruiterSubscriptionEntity
				.builder()
					.activateDate(ACTIVATED_DATE)
					.created(CREATED)
					.recruiterId(RECRUITER_ID)
					.subscriptionId(SUBSCRIPTION_ID)
					.status(STATUS)
					.type(subscription_type.YEAR_SUBSCRIPTION)
					.currentSubscription(true)
					.invoiceType(INVOICE_TYPE_VAL)
				.build();
		
		PaidPeriodRecruiterSubscription subscription = (PaidPeriodRecruiterSubscription) RecruiterSubscriptionEntity.convertFromEntity(entity);
		
		assertEquals(ACTIVATED_DATE, 											subscription.getActivatedDate());
		assertEquals(CREATED, 													subscription.getCreated());
		assertEquals(RECRUITER_ID, 												subscription.getRecruiterId());
		assertEquals(STATUS, 													subscription.getStatus());
		assertEquals(SUBSCRIPTION_ID, 											subscription.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.YEAR_SUBSCRIPTION, subscription.getType());
		assertTrue(subscription.isCurrentSubscription());
		assertEquals(INVOICE_TYPE_VAL, 											subscription.getInvoiceType().get());
		
	}
	
	/**
	* Tests conversion of Entity representation of RecruiterSubscription to 
	* Domain representation [type == YEARLY_SUBSCRIPTION ]
	* @throws Exception
	*/
	@Test
	void testConvertFromEntityCreditBasedSubscription() {
		
		RecruiterSubscriptionEntity entity = RecruiterSubscriptionEntity
				.builder()
					.activateDate(ACTIVATED_DATE)
					.created(CREATED)
					.recruiterId(RECRUITER_ID)
					.subscriptionId(SUBSCRIPTION_ID)
					.status(STATUS)
					.type(subscription_type.CREDIT_BASED_SUBSCRIPTION)
					.currentSubscription(true)
				.build();
		
		CreditBasedSubscription subscription = (CreditBasedSubscription) RecruiterSubscriptionEntity.convertFromEntity(entity);
		
		assertEquals(ACTIVATED_DATE, 													subscription.getActivatedDate());
		assertEquals(CREATED, 															subscription.getCreated());
		assertEquals(RECRUITER_ID, 														subscription.getRecruiterId());
		assertEquals(STATUS, 															subscription.getStatus());
		assertEquals(SUBSCRIPTION_ID, 													subscription.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.CREDIT_BASED_SUBSCRIPTION, subscription.getType());
		assertTrue(subscription.isCurrentSubscription());
		
	}
	
}