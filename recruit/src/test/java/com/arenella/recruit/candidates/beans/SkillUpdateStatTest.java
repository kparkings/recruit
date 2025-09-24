package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the SkillUpdateStat class
* @author K Parkings
*/
class SkillUpdateStatTest {

	private static final long 		CANDIDATE_ID 		= 111;
	private static final LocalDate 	LAST_UPDATED 		= LocalDate.of(2024, 8, 24);
	private static final long 		ADDED_SKILL_COUNT 	= 2;
	
	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	void testConstructor() {
	
		SkillUpdateStat stat = new SkillUpdateStat(CANDIDATE_ID, LAST_UPDATED, ADDED_SKILL_COUNT);
	
		assertEquals(CANDIDATE_ID,		stat.getCandidateId());
		assertEquals(LAST_UPDATED, 		stat.getLastUpdate());
		assertEquals(ADDED_SKILL_COUNT, stat.getAddedSkillsCount());
	
	}
	
}
