package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.SkillUpdateStat;

/**
* Unit tests for the SkillUpdateStatEntity class
* @author K Parkings
*/
public class SkillUpdateStatEntityTest {

	private static final long 		CANDIDATE_ID 		= 111;
	private static final LocalDate 	LAST_UPDATED 		= LocalDate.of(2024, 8, 24);
	private static final long 		ADDED_SKILL_COUNT 	= 2;
	
	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
	
		SkillUpdateStatEntity entity = new SkillUpdateStatEntity(CANDIDATE_ID, LAST_UPDATED, ADDED_SKILL_COUNT);
	
		assertEquals(CANDIDATE_ID,		entity.getCandidateId());
		assertEquals(LAST_UPDATED, 		entity.getLastUpdate());
		assertEquals(ADDED_SKILL_COUNT, entity.getAddedSkillsCount());
	
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test
	public void testFromEntity() throws Exception{
	
		SkillUpdateStatEntity 	entity = new SkillUpdateStatEntity(CANDIDATE_ID, LAST_UPDATED, ADDED_SKILL_COUNT);
		SkillUpdateStat 		stat = SkillUpdateStatEntity.fromEntity(entity);
	
		assertEquals(CANDIDATE_ID,		stat.getCandidateId());
		assertEquals(LAST_UPDATED, 		stat.getLastUpdate());
		assertEquals(ADDED_SKILL_COUNT, stat.getAddedSkillsCount());
	
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void testToEntity() throws Exception{
		
		SkillUpdateStat 		stat 	= new SkillUpdateStat(CANDIDATE_ID, LAST_UPDATED, ADDED_SKILL_COUNT);
		SkillUpdateStatEntity 	entity 	= SkillUpdateStatEntity.toEntity(stat);
		
		assertEquals(CANDIDATE_ID,		entity.getCandidateId());
		assertEquals(LAST_UPDATED, 		entity.getLastUpdate());
		assertEquals(ADDED_SKILL_COUNT, entity.getAddedSkillsCount());

	}
	
}