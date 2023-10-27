package com.arenella.recruit.recruiters.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterCredit;
import com.arenella.recruit.recruiters.dao.CreditEntity;

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
		
		CreditEntity entity = 
				CreditEntity
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
		
		CreditEntity entity = 
				CreditEntity
				.builder()
					.recruiterId(RECRUITER_ID)
					.credits(CREDITS)
				.build();
		
		assertEquals(RECRUITER_ID, 	entity.getRecruiterId());
		assertEquals(CREDITS, 		entity.getCredits());
		
		RecruiterCredit domain = CreditEntity.convertFromEntity(entity);
	
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
		
		CreditEntity entity = CreditEntity.convertToEntity(domain);
	
		assertEquals(RECRUITER_ID, 	entity.getRecruiterId());
		assertEquals(CREDITS, 		entity.getCredits());
		
	}

}