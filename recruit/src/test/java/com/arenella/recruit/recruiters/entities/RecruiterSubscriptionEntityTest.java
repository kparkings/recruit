package com.arenella.recruit.recruiters.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.FirstGenRecruiterSubscription;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Unit tests for the RecruiterSubscriptionEntity class
* @author K Parkings
*/
public class RecruiterSubscriptionEntityTest {

	private static final LocalDateTime 			created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
	private static final LocalDateTime 			activatedDate 		= LocalDateTime.of(2021, 12, 24, 10, 10);
	private static final String					recruiterId			= "kparkings";
	private static final UUID					subscriptionId		= UUID.randomUUID();
	private static final subscription_status 	status 				= subscription_status.ACTIVE;
	private static final subscription_type		type				= subscription_type.FIRST_GEN;
	
	/**
	* Tests creation via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		RecruiterSubscriptionEntity subscription = RecruiterSubscriptionEntity
																			.builder()
																				.activateDate(activatedDate)
																				.created(created)
																				.recruiterId(recruiterId)
																				.subscriptionId(subscriptionId)
																				.status(status)
																				.type(type)
																			.build();
		
		assertEquals(activatedDate, 									subscription.getActivatedDate());
		assertEquals(created, 											subscription.getCreated());
		assertEquals(recruiterId, 										subscription.getRecruiterId());
		assertEquals(status, 											subscription.getStatus());
		assertEquals(subscriptionId, 									subscription.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.FIRST_GEN, subscription.getType());
	
	}

	/**
	* Tests the setters
	* @throws Exception
	*/
	@Test
	public void testSetters() throws Exception {
		
		RecruiterSubscriptionEntity subscription = RecruiterSubscriptionEntity
				.builder()
				.build();
		
		subscription.setActivatedDate(activatedDate);
		subscription.setCreated(created);
		subscription.setRecruiterId(recruiterId);
		subscription.setStatus(status);
		subscription.setSubscriptionId(subscriptionId);
		subscription.seType(type);

		assertEquals(activatedDate, 									subscription.getActivatedDate());
		assertEquals(created, 											subscription.getCreated());
		assertEquals(recruiterId, 										subscription.getRecruiterId());
		assertEquals(status, 											subscription.getStatus());
		assertEquals(subscriptionId, 									subscription.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.FIRST_GEN, subscription.getType());
		
	}
	
	/**
	* Tests conversion of the the Domain representation of a RecruiterSubscription
	* to an Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity_noExistingEntity() throws Exception {
		
		FirstGenRecruiterSubscription subscription = FirstGenRecruiterSubscription
				.builder()
					.activateDate(activatedDate)
					.created(created)
					.recruiterId(recruiterId)
					.subscriptionId(subscriptionId)
					.status(status)
				.build();
		
		RecruiterSubscriptionEntity entity = RecruiterSubscriptionEntity.convertToEntity(subscription, Optional.empty());
		
		assertEquals(activatedDate, 									entity.getActivatedDate());
		assertEquals(created, 											entity.getCreated());
		assertEquals(recruiterId, 										entity.getRecruiterId());
		assertEquals(status, 											entity.getStatus());
		assertEquals(subscriptionId, 									entity.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.FIRST_GEN, entity.getType());
		
	}
	
	/**
	* Tests conversion where an existing version of the Entity exists. This is used
	* for hibernate managed instances to avoid conflicts of duplicate objects
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity_existingEntity() throws Exception {
		
		FirstGenRecruiterSubscription subscription = FirstGenRecruiterSubscription
				.builder()
					.activateDate(activatedDate)
					.created(created)
					.recruiterId(recruiterId)
					.subscriptionId(subscriptionId)
					.status(status)
				.build();
		
		RecruiterSubscriptionEntity existingEntity = RecruiterSubscriptionEntity
				.builder()
				.build();
		
		RecruiterSubscriptionEntity entity = RecruiterSubscriptionEntity.convertToEntity(subscription, Optional.of(existingEntity));
		
		assertEquals(activatedDate, 									entity.getActivatedDate());
		assertEquals(created, 											entity.getCreated());
		assertEquals(recruiterId, 										entity.getRecruiterId());
		assertEquals(status, 											entity.getStatus());
		assertEquals(subscriptionId, 									entity.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.FIRST_GEN, entity.getType());
		
		assertTrue(entity == existingEntity); 
		
	}
	
	/**
	* Tests conversion of Entity representation of RecruiterSubscription to 
	* Domain representation [type == FIRST_GEN
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity_FIRST_GEN() throws Exception {
		
		RecruiterSubscriptionEntity entity = RecruiterSubscriptionEntity
				.builder()
					.activateDate(activatedDate)
					.created(created)
					.recruiterId(recruiterId)
					.subscriptionId(subscriptionId)
					.status(status)
					.type(type)
				.build();
		
		FirstGenRecruiterSubscription subscription = (FirstGenRecruiterSubscription) RecruiterSubscriptionEntity.convertFromEntity(entity);
		
		assertEquals(activatedDate, 									subscription.getActivatedDate());
		assertEquals(created, 											subscription.getCreated());
		assertEquals(recruiterId, 										subscription.getRecruiterId());
		assertEquals(status, 											subscription.getStatus());
		assertEquals(subscriptionId, 									subscription.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.FIRST_GEN, subscription.getType());
		
	}
	
	/**
	* Tests conversion of Entity representation of RecruiterSubscription to 
	* Domain representation [type == YEARLY_SUBSCRIPTION
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity_YEAR_SUBSCRPTION() throws Exception{
		
		assertThrows(IllegalArgumentException.class, () -> {
			
			RecruiterSubscriptionEntity entity = RecruiterSubscriptionEntity
																	.builder()
																		.type(RecruiterSubscription.subscription_type.YEAR_SUBSCRIPTION)
																	.build();
			
			RecruiterSubscriptionEntity.convertFromEntity(entity);
			
		});
	}
	
}