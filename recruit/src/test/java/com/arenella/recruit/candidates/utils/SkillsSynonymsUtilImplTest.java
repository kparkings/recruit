package com.arenella.recruit.candidates.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
* Unit tests for SkillsSynonymsUtilImpl class
* @author K Parkings
*/
public class SkillsSynonymsUtilImplTest {

	/**
	* Test synonyms are added
	* @throws Exception
	*/
	@Test
	public void testAddtSynonymsForSkills() throws Exception {
		
		SkillsSynonymsUtilImpl util = new SkillsSynonymsUtilImpl();

		//JS
		assertTrue(util.addtSynonymsForSkills(Set.of("js")).contains("javascript"));
		assertTrue(util.addtSynonymsForSkills(Set.of("javascript")).contains("js"));
		
		//CSS
		assertTrue(util.addtSynonymsForSkills(Set.of("cascading style sheets")).contains("css"));
		assertTrue(util.addtSynonymsForSkills(Set.of("css")).contains("cascading style sheets"));
		
		//Node
		assertTrue(util.addtSynonymsForSkills(Set.of("node")).contains("nodejs"));
		assertTrue(util.addtSynonymsForSkills(Set.of("node")).contains("node.js"));
		assertTrue(util.addtSynonymsForSkills(Set.of("nodejs")).contains("node"));
		assertTrue(util.addtSynonymsForSkills(Set.of("nodejs")).contains("node.js"));
		assertTrue(util.addtSynonymsForSkills(Set.of("node.js")).contains("node"));
		assertTrue(util.addtSynonymsForSkills(Set.of("node.js")).contains("nodejs"));
	
		//Vue
		assertTrue(util.addtSynonymsForSkills(Set.of("vue")).contains("vuejs"));
		assertTrue(util.addtSynonymsForSkills(Set.of("vue")).contains("vue.js"));
		assertTrue(util.addtSynonymsForSkills(Set.of("vuejs")).contains("vue"));
		assertTrue(util.addtSynonymsForSkills(Set.of("vuejs")).contains("vue.js"));
		assertTrue(util.addtSynonymsForSkills(Set.of("vue.js")).contains("vue"));
		assertTrue(util.addtSynonymsForSkills(Set.of("vue.js")).contains("vuejs"));
	
		//React
		assertTrue(util.addtSynonymsForSkills(Set.of("react")).contains("reactjs"));
		assertTrue(util.addtSynonymsForSkills(Set.of("react")).contains("react.js"));
		assertTrue(util.addtSynonymsForSkills(Set.of("reactjs")).contains("react"));
		assertTrue(util.addtSynonymsForSkills(Set.of("reactjs")).contains("react.js"));
		assertTrue(util.addtSynonymsForSkills(Set.of("react.js")).contains("react"));
		assertTrue(util.addtSynonymsForSkills(Set.of("react.js")).contains("reactjs"));
	
		//C#
		assertTrue(util.addtSynonymsForSkills(Set.of("c#")).contains("csharp"));
		assertTrue(util.addtSynonymsForSkills(Set.of("csharp")).contains("c#"));
		
		//Front end
		assertTrue(util.addtSynonymsForSkills(Set.of("fe")).contains("frontend"));
		assertTrue(util.addtSynonymsForSkills(Set.of("fe")).contains("front-end"));
		assertTrue(util.addtSynonymsForSkills(Set.of("frontend")).contains("fe"));
		assertTrue(util.addtSynonymsForSkills(Set.of("frontend")).contains("front-end"));
		assertTrue(util.addtSynonymsForSkills(Set.of("front-end")).contains("fe"));
		assertTrue(util.addtSynonymsForSkills(Set.of("front-end")).contains("frontend"));
		
		//Back end
		assertTrue(util.addtSynonymsForSkills(Set.of("be")).contains("backend"));
		assertTrue(util.addtSynonymsForSkills(Set.of("be")).contains("back-end"));
		assertTrue(util.addtSynonymsForSkills(Set.of("backend")).contains("be"));
		assertTrue(util.addtSynonymsForSkills(Set.of("backend")).contains("back-end"));
		assertTrue(util.addtSynonymsForSkills(Set.of("back-end")).contains("be"));
		assertTrue(util.addtSynonymsForSkills(Set.of("back-end")).contains("backend"));
		
		//Spring
		assertTrue(util.addtSynonymsForSkills(Set.of("spring core")).contains("spring"));
		assertTrue(util.addtSynonymsForSkills(Set.of("springcore")).contains("spring"));
		assertTrue(util.addtSynonymsForSkills(Set.of("spring")).contains("spring core"));
		assertTrue(util.addtSynonymsForSkills(Set.of("spring")).contains("springcore"));
		
		assertTrue(util.addtSynonymsForSkills(Set.of("springdata")).contains("spring"));
		assertTrue(util.addtSynonymsForSkills(Set.of("springdata")).contains("spring core"));
		assertTrue(util.addtSynonymsForSkills(Set.of("springdata")).contains("springcore"));
		
		assertTrue(util.addtSynonymsForSkills(Set.of("springtest")).contains("spring"));
		assertTrue(util.addtSynonymsForSkills(Set.of("springtest")).contains("spring core"));
		assertTrue(util.addtSynonymsForSkills(Set.of("springtest")).contains("springcore"));
		
		assertTrue(util.addtSynonymsForSkills(Set.of("springmvc")).contains("spring"));
		assertTrue(util.addtSynonymsForSkills(Set.of("springmvc")).contains("spring core"));
		assertTrue(util.addtSynonymsForSkills(Set.of("springmvc")).contains("springcore"));
		
		assertTrue(util.addtSynonymsForSkills(Set.of("springboot")).contains("spring"));
		assertTrue(util.addtSynonymsForSkills(Set.of("springboot")).contains("spring core"));
		assertTrue(util.addtSynonymsForSkills(Set.of("springboot")).contains("springcore"));
		
		assertTrue(util.addtSynonymsForSkills(Set.of("spring data")).contains("spring"));
		assertTrue(util.addtSynonymsForSkills(Set.of("spring data")).contains("spring core"));
		assertTrue(util.addtSynonymsForSkills(Set.of("spring data")).contains("springcore"));
		
		assertTrue(util.addtSynonymsForSkills(Set.of("spring test")).contains("spring"));
		assertTrue(util.addtSynonymsForSkills(Set.of("spring test")).contains("spring core"));
		assertTrue(util.addtSynonymsForSkills(Set.of("spring test")).contains("springcore"));
		
		assertTrue(util.addtSynonymsForSkills(Set.of("spring mvc")).contains("spring"));
		assertTrue(util.addtSynonymsForSkills(Set.of("spring mvc")).contains("spring core"));
		assertTrue(util.addtSynonymsForSkills(Set.of("spring mvc")).contains("springcore"));
		
		assertTrue(util.addtSynonymsForSkills(Set.of("spring boot")).contains("spring"));
		assertTrue(util.addtSynonymsForSkills(Set.of("spring boot")).contains("spring core"));
		assertTrue(util.addtSynonymsForSkills(Set.of("spring boot")).contains("springcore"));
		
	}
	
}
