package com.arenella.recruit.candidates.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the SkillSynonym class 
* @author K Parkings
*/
class SkillSynonymTest {

	/**
	* Tests case in which skill is one of the synonyms for the skill
	* @throws Exception
	*/
	@Test
	void testIsSynonym() {
		
		Set<String> extracted = new HashSet<>();
		
		SkillSynonym skillSynonym = new SkillSynonym("name", Set.of("spring-test", "Spring test ", " springTest"));
		
		skillSynonym.getSynonymsForSkill(extracted, "sPrInGTest");
		
		assertEquals(3, extracted.size());
		
	}
	
	/**
	* Tests case in which skill is not one of the synonyms for the skill
	* @throws Exception
	*/
	@Test
	void testIsNotSynonym() {
		
		Set<String> extracted = new HashSet<>();
		
		SkillSynonym skillSynonym = new SkillSynonym("name", Set.of("spring-test", "Spring test ", " springTest"));
		
		skillSynonym.getSynonymsForSkill(extracted, "java");
		
		assertEquals(0, extracted.size());
		
	}
	
	/**
	* Tests that is Secondary synonyms are provided they are non matched on. Secondary Synonyms
	* are synonyms that also should be returned when a primary synonym matches the skill but not the 
	* other was round
	* 
	* Example. Pen Tester (primary) could have testing as a secondary synonym as it is a type of Tester but 
	* 		   having testing does not mean you are a pen tester
	* 
	* @throws Exception
	*/
	@Test
	void testSecondaySynonyms_butNoMatchOnPrimary() {
		
		Set<String> extracted = new HashSet<>();
		
		SkillSynonym ss1 = new SkillSynonym("ss-2-1", Set.of("Spring","Spring Core"));
		SkillSynonym ss2 = new SkillSynonym("ss-2-2", Set.of("Intergation testing"));
		
		SkillSynonym skillSynonym = new SkillSynonym("name", Set.of("spring-test", "Spring test ", " springTest"), ss1, ss2);
		
		skillSynonym.getSynonymsForSkill(extracted, "Intergation testing");
		
		assertEquals(0, extracted.size());
		
	}
	
	/**
	* Tests that is Secondary synonyms are provided they are non matched on. Secondary Synonyms
	* are synonyms that also should be returned when a primary synonym matches the skill but not the 
	* other was round
	* 
	* Example. Pen Tester (primary) could have testing as a secondary synonym as it is a type of Tester but 
	* 		   having testing does not mean you are a pen tester
	* 
	* @throws Exception
	*/
	@Test
	void testSecondaySynonyms_MatchOnPrimary() {
		
		Set<String> extracted = new HashSet<>();
		
		SkillSynonym ss1 = new SkillSynonym("ss-2-1", Set.of("Spring","Spring Core"));
		SkillSynonym ss2 = new SkillSynonym("ss-2-2", Set.of("Intergation testing"));
		
		SkillSynonym skillSynonym = new SkillSynonym("name", Set.of("spring-test", "Spring test ", " springTest"), ss1, ss2);
		
		skillSynonym.getSynonymsForSkill(extracted, "spring-test");
		
		assertEquals(6, extracted.size());
		
	}
	
	/**
	* Tests extraction of synonyms in a nested context
	* @throws Exception
	*/
	@Test
	void testSecondaySynonyms_nested_extraction() {
		
		Set<String> extracted = new HashSet<>();
		
		SkillSynonym ss1 = new SkillSynonym("ss-2-1", Set.of("Spring","Spring Core"));
		SkillSynonym ss2 = new SkillSynonym("ss-2-2", Set.of("Intergation testing"));
		SkillSynonym ss3 = new SkillSynonym("ss-2-3", Set.of("Boop1"), ss2);
		SkillSynonym ss4 = new SkillSynonym("ss-2-4", Set.of("Boop2"));
		SkillSynonym ss5 = new SkillSynonym("ss-2-4", Set.of("Boop3"));
		SkillSynonym ss6 = new SkillSynonym("ss-2-4", Set.of("Boop4"), ss4, ss5);
		
		SkillSynonym skillSynonym = new SkillSynonym("name", Set.of("spring-test", "Spring test ", " springTest"), ss1, ss2, ss3, ss6);
		
		skillSynonym.getSynonymsForSkill(extracted, "spring-test");
		
		assertEquals(10, extracted.size());
		
	}
	
}