package com.arenella.recruit.curriculum.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.curriculum.entity.CurriculumRecruiterCreditEntity;
import com.arenella.recruit.curriculum.beans.RecruiterCredit;

/**
* Unit tests for the CurriculumCreditEntityTest class
* @author K Parkings
*/
class CurriculumRecruiterCreditEntityTest {

	private static final String 	RECRUITER_ID 	= "recruiter44";
	private static final int 		CREDITS 		= 4;
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		CurriculumRecruiterCreditEntity entity = 
				CurriculumRecruiterCreditEntity
				.builder()
					.recruiterId(RECRUITER_ID)
					.credits(CREDITS)
				.build();
		
		assertEquals(RECRUITER_ID, 	entity.getRecruiterId());
		assertEquals(CREDITS, 		entity.getCredits());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test
	void testConvertFromEntity() {
		
		CurriculumRecruiterCreditEntity entity = 
				CurriculumRecruiterCreditEntity
				.builder()
					.recruiterId(RECRUITER_ID)
					.credits(CREDITS)
				.build();
		
		assertEquals(RECRUITER_ID, 	entity.getRecruiterId());
		assertEquals(CREDITS, 		entity.getCredits());
		
		RecruiterCredit domain = CurriculumRecruiterCreditEntity.convertFromEntity(entity);
	
		assertEquals(RECRUITER_ID, 	domain.getRecruiterId());
		assertEquals(CREDITS, 		domain.getCredits());
		
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
				.build();
		
		assertEquals(RECRUITER_ID, 	domain.getRecruiterId());
		assertEquals(CREDITS, 		domain.getCredits());
		
		CurriculumRecruiterCreditEntity entity = CurriculumRecruiterCreditEntity.convertToEntity(domain);
	
		assertEquals(RECRUITER_ID, 	entity.getRecruiterId());
		assertEquals(CREDITS, 		entity.getCredits());
		
	}

}