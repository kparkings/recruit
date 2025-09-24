package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.RecruiterCredit;

/**
* Unit tests for the RecruiterCreditEntity class
* @author K Parkings
*/
class RecruiterCreditEntityTest {

	private static final String 	RECRUITER_ID 		= "recruiter44";
	private static final int 		CREDITS 			= 4;
	private static final boolean 	PAID_SUBSCRIPTION 	= true;
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		CandidateRecruiterCreditEntity entity = 
				CandidateRecruiterCreditEntity
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
		
		CandidateRecruiterCreditEntity entity = 
				CandidateRecruiterCreditEntity
				.builder()
					.recruiterId(RECRUITER_ID)
					.credits(CREDITS)
					.paidSubscription(PAID_SUBSCRIPTION)
				.build();
		
		assertEquals(RECRUITER_ID, 		entity.getRecruiterId());
		assertEquals(CREDITS, 			entity.getCredits());
		assertEquals(PAID_SUBSCRIPTION, entity.hasPaidSubscription());
		
		RecruiterCredit domain = CandidateRecruiterCreditEntity.convertFromEntity(entity);
	
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
		
		assertEquals(RECRUITER_ID, 			domain.getRecruiterId());
		assertEquals(CREDITS, 				domain.getCredits());
		assertEquals(PAID_SUBSCRIPTION, 	domain.hasPaidSubscription());
		
		CandidateRecruiterCreditEntity entity = CandidateRecruiterCreditEntity.convertToEntity(domain);
	
		assertEquals(RECRUITER_ID, 		entity.getRecruiterId());
		assertEquals(CREDITS, 			entity.getCredits());
		assertEquals(PAID_SUBSCRIPTION, entity.hasPaidSubscription());
		
	}

}