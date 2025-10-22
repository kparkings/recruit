package com.arenella.recruit.candidates.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Unit tests for the CandidateFunctionExtractorImpl class
* @author K Parkings
*/
class CandidateFunctionExtractorImplTest {

	/**
	* Tests correct FUNCTION returned 
	* @throws Exception
	*/
	@Test
	void testExtraction() {
		
		CandidateFunctionExtractorImpl extractor = new CandidateFunctionExtractorImpl();
		
		ReflectionTestUtils.invokeMethod(extractor, "init");
		
		extractor.extractFunctions("c# developer").stream().filter(f -> 			f == FUNCTION.CSHARP_DEV).findAny().orElseThrow();
		extractor.extractFunctions("it support analyst").stream().filter(f -> 		f == FUNCTION.SUPPORT).findAny().orElseThrow();
		extractor.extractFunctions("business analyst").stream().filter(f -> 		f == FUNCTION.BA).findAny().orElseThrow();
		extractor.extractFunctions("ui/ux designer").stream().filter(f -> 			f == FUNCTION.UI_UX).findAny().orElseThrow();
		extractor.extractFunctions("project manager").stream().filter(f ->	 		f == FUNCTION.PROJECT_MANAGER).findAny().orElseThrow();
		extractor.extractFunctions("architect").stream().filter(f -> 				f == FUNCTION.ARCHITECT).findAny().orElseThrow();
		extractor.extractFunctions("test analyst").stream().filter(f -> 			f == FUNCTION.TESTER).findAny().orElseThrow();
		extractor.extractFunctions("web developer").stream().filter(f ->	 		f == FUNCTION.WEB_DEV).findAny().orElseThrow();
		extractor.extractFunctions("scrum master").stream().filter(f -> 			f == FUNCTION.SCRUM_MASTER).findAny().orElseThrow();
		extractor.extractFunctions("data scientist").stream().filter(f -> 			f == FUNCTION.DATA_SCIENTIST).findAny().orElseThrow();
		extractor.extractFunctions("network administrator").stream().filter(f -> 	f == FUNCTION.NETWORK_ADMINISTRATOR).findAny().orElseThrow();
		extractor.extractFunctions("software developer").stream().filter(f -> 		f == FUNCTION.SOFTWARE_DEVELOPER).findAny().orElseThrow();
		extractor.extractFunctions("it security").stream().filter(f -> 				f == FUNCTION.IT_SECURITY).findAny().orElseThrow();
		extractor.extractFunctions("it recruiter").stream().filter(f -> 			f == FUNCTION.IT_RECRUITER).findAny().orElseThrow();
		extractor.extractFunctions("sdet").stream().filter(f -> 					f == FUNCTION.SOFTWARE_DEV_IN_TEST).findAny().orElseThrow();
		extractor.extractFunctions("java developer").stream().filter(f -> 			f == FUNCTION.JAVA_DEV).findAny().orElseThrow();
		
	}
	
	/**
	* If a user searches for architects but doesn't specify the type all 
	* sub architect types must be added 
	*/
	@Test
	void testArchitecTopLevelFunctionAddsChildArchitectFunctions() {
		
		CandidateFunctionExtractorImpl extractor = new CandidateFunctionExtractorImpl();
		
		ReflectionTestUtils.invokeMethod(extractor, "init");
		
		Set<FUNCTION> extractedFunctions = extractor.extractFunctions("architect");
		
		assertTrue(extractedFunctions.contains(FUNCTION.ARCHITECT));
		assertTrue(extractedFunctions.contains(FUNCTION.SOFTWARE_ARCHITECT));
		assertTrue(extractedFunctions.contains(FUNCTION.DATA_ARCHITECT));
		assertTrue(extractedFunctions.contains(FUNCTION.INFRASTRUCTURE_ARCHITECT));
		assertTrue(extractedFunctions.contains(FUNCTION.SOLUTIONS_ARCHITECT));
		assertTrue(extractedFunctions.contains(FUNCTION.ENTERPRISE_ARCHITECT));
		
	}
	
	/**
	* If a user searches for web developers but doesn't specify the type all 
	* sub web developer types must be added 
	*/
	@Test
	void testWebDevTopLevelFunctionAddsChildWebFunctions() {
		
		CandidateFunctionExtractorImpl extractor = new CandidateFunctionExtractorImpl();
		
		ReflectionTestUtils.invokeMethod(extractor, "init");
		
		Set<FUNCTION> extractedFunctions = extractor.extractFunctions("web developer");
		
		assertTrue(extractedFunctions.contains(FUNCTION.RUBY));
		assertTrue(extractedFunctions.contains(FUNCTION.RUBY_ON_RAILS));
		assertTrue(extractedFunctions.contains(FUNCTION.GO));
		assertTrue(extractedFunctions.contains(FUNCTION.REACT));
		assertTrue(extractedFunctions.contains(FUNCTION.VUE));
		assertTrue(extractedFunctions.contains(FUNCTION.NEXT));
		assertTrue(extractedFunctions.contains(FUNCTION.EXPRES));
		assertTrue(extractedFunctions.contains(FUNCTION.RUST));
	
	}
	
}