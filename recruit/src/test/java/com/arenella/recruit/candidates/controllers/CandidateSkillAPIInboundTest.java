package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateSkill;
import com.arenella.recruit.candidates.entities.CandidateSkillEntity.VALIDATION_STATUS;

/**
* Unit tests for the CandidateSkill class
* @author K Parkings
*/
public class CandidateSkillAPIInboundTest {

	private static final String 			SKILL_NAME 	= "java";
	private static final VALIDATION_STATUS 	STATUS 		= VALIDATION_STATUS.ACCEPTED;

	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		CandidateSkillAPIInbound skill = CandidateSkillAPIInbound
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
		
		CandidateSkillAPIInbound skill = CandidateSkillAPIInbound
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
		
		CandidateSkillAPIInbound skill = CandidateSkillAPIInbound
				.builder()
				.skill(SKILL_NAME)
				.build();
		
		assertEquals(VALIDATION_STATUS.PENDING, skill.getValidationStatus());
		
	}
	
	/**
	* Tests conversion from Domain to API Outbound representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromDomain() throws Exception {
	
		CandidateSkillAPIInbound inbound = CandidateSkillAPIInbound
				.builder()
				.skill(SKILL_NAME)
				.validationStatus(STATUS)
				.build();
		
		CandidateSkill skill  = CandidateSkillAPIInbound.convertToDomain(inbound);
		
		assertEquals(SKILL_NAME, skill.getSkill());
		assertEquals(STATUS, skill.getValidationStatus());
	}
	
}