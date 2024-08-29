package com.arenella.recruit.adapters.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the RequestSkillsForCurriculumCommand class
* @author K Parkings
*/
public class RequestSkillsForCurriculumCommandTest {

	/**
	* Tests constructor
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final long curriculumId = 678;
		
		RequestSkillsForCurriculumCommand command = new RequestSkillsForCurriculumCommand(curriculumId);
	
		assertEquals(curriculumId, command.getCurriculumId());
	
	}
	
}
