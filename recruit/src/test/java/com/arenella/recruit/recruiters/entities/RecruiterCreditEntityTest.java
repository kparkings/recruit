package com.arenella.recruit.recruiters.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterCredit;
import com.arenella.recruit.recruiters.dao.CreditEntity;

/**
* Unit tests for the RecruiterCreditEntity class
* @author K Parkings
*/
class RecruiterCreditEntityTest {

	private static final String 	RECRUITER_ID 	= "recruiter44";
	private static final int 		CREDITS 		= 4;
	private static final boolean 	PAID_SUBSCRIPTION 	= true;
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		CreditEntity entity = 
				CreditEntity
				.builder()
					.recruiterId(RECRUITER_ID)
					.credits(CREDITS)
					.paidSubscription(PAID_SUBSCRIPTION)
				.build();
		
		assertEquals(RECRUITER_ID, 		entity.getRecruiterId());
		assertEquals(CREDITS, 			entity.getCredits());
		assertEquals(PAID_SUBSCRIPTION, entity.hasPaidSubscription());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test
	void testConvertFromEntity() {
		
		CreditEntity entity = 
				CreditEntity
				.builder()
					.recruiterId(RECRUITER_ID)
					.credits(CREDITS)
					.paidSubscription(PAID_SUBSCRIPTION)
				.build();
		
		assertEquals(RECRUITER_ID, 		entity.getRecruiterId());
		assertEquals(CREDITS, 			entity.getCredits());
		assertEquals(PAID_SUBSCRIPTION, entity.hasPaidSubscription());
		
		RecruiterCredit domain = CreditEntity.convertFromEntity(entity);
	
		assertEquals(RECRUITER_ID, 		domain.getRecruiterId());
		assertEquals(CREDITS, 			domain.getCredits());
		assertEquals(PAID_SUBSCRIPTION, domain.hasPaidSubscription());
		
	}

	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	void testConvertToEntity() {
		
		RecruiterCredit domain = 
				RecruiterCredit
				.builder()
					.recruiterId(RECRUITER_ID)
					.credits(CREDITS)
					.paidSubscription(PAID_SUBSCRIPTION)
				.build();
		
		assertEquals(RECRUITER_ID, 		domain.getRecruiterId());
		assertEquals(CREDITS, 			domain.getCredits());
		assertEquals(PAID_SUBSCRIPTION, domain.hasPaidSubscription());
		
		CreditEntity entity = CreditEntity.convertToEntity(domain);
	
		assertEquals(RECRUITER_ID, 		entity.getRecruiterId());
		assertEquals(CREDITS, 			entity.getCredits());
		assertEquals(PAID_SUBSCRIPTION, entity.hasPaidSubscription());
		
	}

}