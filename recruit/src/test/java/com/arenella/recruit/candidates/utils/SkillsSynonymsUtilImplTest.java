package com.arenella.recruit.candidates.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
* Unit tests for SkillsSynonymsUtilImpl class
* @author K Parkings
*/
public class SkillsSynonymsUtilImplTest {

	/**
	* Runs an individual test case
	* @param testcase - case to test
	*/
	private void run(String expected, Set<String> testcase) {
		
		SkillsSynonymsUtilImpl util = new SkillsSynonymsUtilImpl();
		
		Set<String> extracted = new HashSet<>();
		
		util.addSynonymsForSkills(extracted, testcase);
		
		assertTrue(extracted.contains(expected));
	}
	
	/**
	* Test synonyms are added
	* @throws Exception
	*/
	@Test
	public void testAddtSynonymsForSkills() throws Exception {
		
		//JS
		run("javascript", 				Set.of("js"));
		run("js", 						Set.of("javascript"));
		//CSS
		run("css", 						Set.of("cascading style sheets"));
		run("cascading style sheets", 	Set.of("css"));
		
		//Node
		run("node", 					Set.of("nodejs"));
		run("node", 					Set.of("node.js"));
		run("nodejs", 					Set.of("node"));
		run("nodejs", 					Set.of("node.js"));
		run("node.js", 					Set.of("node"));
		run("node.js", 					Set.of("nodejs"));
		
		//Vue
		run("vue", 						Set.of("vuejs"));
		run("vue", 						Set.of("vue.js"));
		run("vuejs", 					Set.of("vue"));
		run("vuejs", 					Set.of("vue.js"));
		run("vue.js", 					Set.of("vue"));
		run("vue.js", 					Set.of("vuejs"));
		
		//React
		run("react", 					Set.of("reactjs"));
		run("react", 					Set.of("react.js"));
		run("reactjs", 					Set.of("react"));
		run("reactjs", 					Set.of("react.js"));
		run("react.js", 				Set.of("react"));
		run("react.js", 				Set.of("reactjs"));
	
		//C#
		run("c#", 						Set.of("csharp"));
		run("csharp", 					Set.of("c#"));
	
		//Front end
		run("fe", 						Set.of("frontend"));
		run("fe", 						Set.of("frontend"));
		run("frontend", 				Set.of("fe"));
		run("frontend", 				Set.of("front-end"));
		run("front-end", 				Set.of("fe"));
		run("front-end", 				Set.of("frontend"));
		
		
		//Back end
		run("be", 						Set.of("backend"));
		run("be", 						Set.of("backend"));
		run("backend", 					Set.of("be"));
		run("backend", 					Set.of("back-end"));
		run("back-end", 				Set.of("be"));
		run("back-end", 				Set.of("backend"));
		
		//Spring
		run("spring core", 				Set.of("spring"));
		run("springcore", 				Set.of("spring"));
		run("spring", 					Set.of("spring core"));
		run("spring", 					Set.of("springcore"));
		
		run("spring", 					Set.of("springdata"));
		run("spring core", 				Set.of("springdata"));
		run("springcore", 				Set.of("springdata"));
		
		run("spring", 					Set.of("springtest"));
		run("spring core", 				Set.of("springtest"));
		run("springcore", 				Set.of("springtest"));
		
		run("spring", 					Set.of("springmvc"));
		run("spring core", 				Set.of("springmvc"));
		run("springcore", 				Set.of("springmvc"));
		
		run("spring", 					Set.of("springboot"));
		run("spring core", 				Set.of("springboot"));
		run("springcore", 				Set.of("springboot"));
		
		run("spring data", 				Set.of("springdata"));
		run("spring data", 				Set.of("spring-data"));
		
		run("spring test", 				Set.of("springtest"));
		run("spring test", 				Set.of("spring-test"));
		
		run("spring mvc", 				Set.of("springmvc"));
		run("spring mvc", 				Set.of("spring-mvc"));
		
		run("spring boot", 				Set.of("springboot"));
		run("spring boot", 				Set.of("spring-boot"));
		
	}
	
}