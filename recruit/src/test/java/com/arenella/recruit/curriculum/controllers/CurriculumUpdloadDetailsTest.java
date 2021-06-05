package com.arenella.recruit.curriculum.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CurriculumUpdloadDetailsOutbound class
* @author K Parkings
*/
public class CurriculumUpdloadDetailsTest {

	private static final String java 			= "Java";
	private static final String cSharp 			= "C#";
	private static final String id 				= "100";
	private static final String emailAddress 	= "kparkings@gmail.com";
	
	/**
	* Tests builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		final String java = "Java";
		final String cSharp = "C#";
		
		final Set<String> skills = Set.of(java,cSharp);
		CurriculumUpdloadDetails cvDetails = CurriculumUpdloadDetails
																.builder()
																	.id(id)
																	.emailAddress(emailAddress)
																	.skills(skills)
																.build();
		assertEquals(id, 			cvDetails.getId());
		assertEquals(emailAddress, 	cvDetails.getEmailAddress());
		
		assertTrue(cvDetails.getSkills().contains(java));
		assertTrue(cvDetails.getSkills().contains(cSharp));
	}
	
	/**
	* Tests that existing values are removed from skills 
	* if a subsequent set of skills is added
	* @throws Exception
	*/
	@Test
	public void testSkillsOverridesExistingValues() throws Exception{
		CurriculumUpdloadDetails cvDetails = CurriculumUpdloadDetails
																.builder()
																	.skills(Set.of(java))
																	.skills(Set.of(cSharp))
																	.build();
		
		assertFalse(cvDetails.getSkills().contains(java));
		assertTrue(cvDetails.getSkills().contains(cSharp));
		
	}

	/**
	* Defensive programming test. Tests Collections exist but are 
	* empty
	* @throws Exception
	*/
	@Test
	public void testSkillsInitializedByDefault() throws Exception{
		
		CurriculumUpdloadDetails cvDetails = CurriculumUpdloadDetails
				.builder().build();
		
		assertTrue(cvDetails.getSkills().isEmpty());
		
	}
	
}