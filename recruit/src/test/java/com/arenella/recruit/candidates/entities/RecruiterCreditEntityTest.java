package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.RecruiterCredit;

/**
* Unit tests for the RecruiterCreditEntity class
* @author K Parkings
*/
public class RecruiterCreditEntityTest {

	final String 	RECRUITER_ID 	= "recruiter44";
	final int 		CREDITS 		= 4;
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		CandidateRecruiterCreditEntity entity = 
				CandidateRecruiterCreditEntity
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
	public void testConvertFromEntity() throws Exception{
		
		CandidateRecruiterCreditEntity entity = 
				CandidateRecruiterCreditEntity
				.builder()
					.recruiterId(RECRUITER_ID)
					.credits(CREDITS)
				.build();
		
		assertEquals(RECRUITER_ID, 	entity.getRecruiterId());
		assertEquals(CREDITS, 		entity.getCredits());
		
		RecruiterCredit domain = CandidateRecruiterCreditEntity.convertFromEntity(entity);
	
		assertEquals(RECRUITER_ID, 	domain.getRecruiterId());
		assertEquals(CREDITS, 		domain.getCredits());
		
	}

	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception{
		
		RecruiterCredit domain = 
				RecruiterCredit
				.builder()
					.recruiterId(RECRUITER_ID)
					.credits(CREDITS)
				.build();
		
		assertEquals(RECRUITER_ID, 	domain.getRecruiterId());
		assertEquals(CREDITS, 		domain.getCredits());
		
		CandidateRecruiterCreditEntity entity = CandidateRecruiterCreditEntity.convertToEntity(domain);
	
		assertEquals(RECRUITER_ID, 	entity.getRecruiterId());
		assertEquals(CREDITS, 		entity.getCredits());
		
	}

}