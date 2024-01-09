package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateSkill;
import com.arenella.recruit.candidates.entities.CandidateSkillEntity.VALIDATION_STATUS;

/**
* Unit tests for the CandidateSkillEntity class
* @author K Parkings
*/
public class CandidateSkillEntityTest {

	final static String SKILL = "Java";
	
	/**
	* Tests construction 
	*/
	@Test
	public void testConstructor() {
		
		
		CandidateSkillEntity entity = new CandidateSkillEntity(SKILL);
		
		assertEquals(VALIDATION_STATUS.PENDING, entity.getValidationStatus());
		assertEquals(SKILL, entity.getSkill());
		
		entity.setValidationStatus(VALIDATION_STATUS.ACCEPTED);
		
		assertEquals(VALIDATION_STATUS.ACCEPTED, entity.getValidationStatus());
		
	}

	/**
	* Tests Entity to Domain conversion
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception {
		
		CandidateSkillEntity entity = new CandidateSkillEntity(SKILL);
		entity.setValidationStatus(VALIDATION_STATUS.ACCEPTED);
		
		CandidateSkill skill = CandidateSkillEntity.convertFromEntity(entity);
		
		assertEquals(SKILL, skill.getSkill());
		assertEquals(VALIDATION_STATUS.ACCEPTED, skill.getValidationStatus());
		
	}
	
	/**
	* Tests Domain to Entity conversion
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception {
		
		CandidateSkill skill = CandidateSkill
				.builder()
					.skill(SKILL)
					.validationStatus(VALIDATION_STATUS.ACCEPTED)
				.build();
		
		CandidateSkillEntity entity = CandidateSkillEntity.convertToEntity(skill);
		
		
		assertEquals(SKILL, entity.getSkill());
		assertEquals(VALIDATION_STATUS.ACCEPTED, entity.getValidationStatus());
		
	}
	
}