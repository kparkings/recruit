package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CurriculumSkillsExtractionEvent class
* @author K Parkings
*/
public class CurriculumSkillsExtractionEventTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception {
		
		final long	 		id 		= 7889;
		final Set<String> 	skills 	= Set.of("java", "react");
		
		CurriculumSkillsExtractionEvent event = new CurriculumSkillsExtractionEvent(id, skills);
		
		assertEquals(id, event.getCurriculumId());
		assertTrue(event.getSkills().contains("java"));
		assertTrue(event.getSkills().contains("react"));
		
	}
	
	
}
