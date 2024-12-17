package com.arenella.recruit.candidates.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;

/**
* Unit tests for the CandidateSuggestionUtilImpl class
* @author K Parkings
*/
public class CandidateSuggestionUtilImplTest {

	CandidateSuggestionUtilImpl util = new CandidateSuggestionUtilImpl();
	
	/**
	* If no skills requirements then expect perfect match
	* @throws Exception
	*/
	@Test
	public void testIsPerfectMatch_emptySkills() throws Exception{
	
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("JAVA","SPRING")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isPerfectMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements and Candidate has no skills expect perfect match
	* @throws Exception
	*/
	@Test
	public void testIsPerfectMatch_emptyCandidateSkills_emptySkills() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of()).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isPerfectMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
	}
	
	/**
	* Test skills requirements and Candidate Skills are exact match
	* @throws Exception
	*/
	@Test
	public void testIsPerfectMatch_perfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("JAVA","SPRING")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("Java","Spring")).build();
		
		assertTrue(util.isPerfectMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Test Candidate has all the required skills and more. Expected perfect match
	* @throws Exception
	*/
	@Test
	public void testIsPerfectMatch_tooPerfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("JAVA","SPRING","HIBERNATE")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("Java","Spring")).build();
		
		assertTrue(util.isPerfectMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Tests Candidate does not have all the required skills
	* @throws Exception
	*/
	@Test
	public void testIsPerfectMatch_not_perfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("JAVA")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("Java","Spring")).build();
		
		assertFalse(util.isPerfectMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements then expect perfect match
	* @throws Exception
	*/
	@Test
	public void testIsExcellentMatch_emptySkills() throws Exception{
	
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isExcellentMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements and Candidate has no skills expect perfect match
	* @throws Exception
	*/
	@Test
	public void testIsExcellentMatch_emptyCandidateSkills_emptySkills() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of()).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isExcellentMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
	}
	
	/**
	* Test skills requirements and Candidate Skills are exact match
	* @throws Exception
	*/
	@Test
	public void testIsExcellentMatch_perfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		
		assertTrue(util.isExcellentMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Test Candidate has all the required skills and more. Expected perfect match
	* @throws Exception
	*/
	@Test
	public void testIsExcellentMatch_tooPerfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9")).build();
		
		assertTrue(util.isExcellentMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Tests Candidate does not have all the required skills
	* @throws Exception
	*/
	@Test
	public void testIsExcellentMatch_not_perfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		
		assertFalse(util.isExcellentMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements then expect perfect match
	* @throws Exception
	*/
	@Test
	public void testIsGoodMatch_emptySkills() throws Exception{
	
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isGoodMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements and Candidate has no skills expect perfect match
	* @throws Exception
	*/
	@Test
	public void testIsGoodMatch_emptyCandidateSkills_emptySkills() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of()).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isGoodMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
	}
	
	/**
	* Test skills requirements and Candidate Skills are exact match
	* @throws Exception
	*/
	@Test
	public void testIsGoodMatch_perfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8")).build();
		
		assertTrue(util.isGoodMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Test Candidate has all the required skills and more. Expected perfect match
	* @throws Exception
	*/
	@Test
	public void testIsGoodMatch_tooPerfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9")).build();
		
		assertTrue(util.isGoodMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Tests Candidate does not have all the required skills
	* @throws Exception
	*/
	@Test
	public void testIsGoodMatch_not_perfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		
		assertFalse(util.isGoodMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements then expect perfect match
	* @throws Exception
	*/
	@Test
	public void testIsAverageMatch_emptySkills() throws Exception{
	
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isAverageMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements and Candidate has no skills expect perfect match
	* @throws Exception
	*/
	@Test
	public void testIsAverageMatch_emptyCandidateSkills_emptySkills() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of()).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isAverageMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
	}
	
	/**
	* Test skills requirements and Candidate Skills are exact match
	* @throws Exception
	*/
	@Test
	public void testIsAverageMatch_perfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8")).build();
		
		assertTrue(util.isAverageMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Test Candidate has all the required skills and more. Expected perfect match
	* @throws Exception
	*/
	@Test
	public void testIsAverageMatch_tooPerfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6")).build();
		
		assertTrue(util.isAverageMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Tests Candidate does not have all the required skills
	* @throws Exception
	*/
	@Test
	public void testIsAverageMatch_not_perfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		
		assertFalse(util.isAverageMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements then expect perfect match
	* @throws Exception
	*/
	@Test
	public void testIsPoorMatch_emptySkills() throws Exception{
	
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isPoorMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements and Candidate has no skills expect perfect match
	* @throws Exception
	*/
	@Test
	public void testIsPoorMatch_emptyCandidateSkills_emptySkills() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of()).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isPoorMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
	}
	
	/**
	* Test skills requirements and Candidate Skills are exact match
	* @throws Exception
	*/
	@Test
	public void testIsPoorMatch_perfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8")).build();
		
		assertTrue(util.isPoorMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Test Candidate has all the required skills and more. Expected perfect match
	* @throws Exception
	*/
	@Test
	public void testIsPoorMatch_tooPerfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6")).build();
		
		assertTrue(util.isPoorMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Tests Candidate does not have all the required skills
	* @throws Exception
	*/
	@Test
	public void testIsPoorMatch_not_perfectMatch() throws Exception{
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		
		assertFalse(util.isPoorMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Tests perfect match for languages
	* @throws Exception
	*/
	@Test
	public void testIsPerfectMatch_languages_perfectMatch() throws Exception {
		
		Candidate 				candidate 	= Candidate.builder().languages(Set.of(
																				 Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build()
																				,Language.builder().language(LANGUAGE.ENGLISH).level(LEVEL.PROFICIENT).build()
																				,Language.builder().language(LANGUAGE.FRENCH).level(LEVEL.PROFICIENT).build())
																			).build();
		
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder()
				
				.languages(Set.of(
						LANGUAGE.ENGLISH,
						LANGUAGE.DUTCH,
						LANGUAGE.FRENCH)
				).build();
		
		assertTrue(util.isPerfectMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Tests not perfect match for languages
	* @throws Exception
	*/
	@Test
	public void testIsPerfectMatch_languages_not_perfectMatch() throws Exception {
		
		Candidate 				candidate 	= Candidate.builder().languages(Set.of(
																				 Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.BASIC).build()
																				,Language.builder().language(LANGUAGE.ENGLISH).level(LEVEL.PROFICIENT).build()
																				,Language.builder().language(LANGUAGE.FRENCH).level(LEVEL.PROFICIENT).build())
																			).build();
		
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder()
				.languages(Set.of(
						LANGUAGE.ENGLISH,
						LANGUAGE.DUTCH,
						LANGUAGE.FRENCH)
				).build();
		
		assertFalse(util.isPerfectMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
}