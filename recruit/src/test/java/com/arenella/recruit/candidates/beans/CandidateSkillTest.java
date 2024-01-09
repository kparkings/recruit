package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.entities.CandidateSkillEntity.VALIDATION_STATUS;

/**
* Unit tests for the CandidateSkill class
* @author K Parkings
*/
public class CandidateSkillTest {

	private static final String 			SKILL_NAME 	= "java";
	private static final VALIDATION_STATUS 	STATUS 		= VALIDATION_STATUS.ACCEPTED;

	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		
		CandidateSkill skill = CandidateSkill
				.builder()
				.skill(SKILL_NAME)
				.validationStatus(STATUS)
				.build();
		
		assertEquals(SKILL_NAME, skill.getSkill());
		assertEquals(STATUS, skill.getValidationStatus());
		
	}
	
	/**
	* Tests setters
	* @throws Exception
	*/
	@Test
	public void testSetters() throws Exception{
		
		CandidateSkill skill = CandidateSkill
				.builder()
				.skill(SKILL_NAME)
				.validationStatus(STATUS)
				.build();
		
		assertEquals(SKILL_NAME, skill.getSkill());
		assertEquals(STATUS, skill.getValidationStatus());
		
		skill.setValidationStatus(VALIDATION_STATUS.REJECTED);
		
		assertEquals(VALIDATION_STATUS.REJECTED, skill.getValidationStatus());		
		
	}
	
	/**
	* Tests setters
	* @throws Exception
	*/
	@Test
	public void testDefaults() throws Exception{
		
		CandidateSkill skill = CandidateSkill
				.builder()
				.skill(SKILL_NAME)
				.build();
		
		assertEquals(VALIDATION_STATUS.PENDING, skill.getValidationStatus());
		
	}
	
}