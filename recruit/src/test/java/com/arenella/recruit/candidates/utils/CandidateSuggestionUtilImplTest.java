package com.arenella.recruit.candidates.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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
@ExtendWith(MockitoExtension.class)
class CandidateSuggestionUtilImplTest {
	
	@Spy
	private SkillsSynonymsUtil spySkillsSynonymsUtil;
	
	@InjectMocks
	private CandidateSuggestionUtilImpl util = new CandidateSuggestionUtilImpl();
	
	/**
	* If no skills requirements then expect perfect match
	* @throws Exception
	*/
	@Test
	void testIsPerfectMatch_emptySkills() {
	
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("JAVA","SPRING")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
	
		assertTrue(util.isPerfectMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements and Candidate has no skills expect perfect match
	* @throws Exception
	*/
	@Test
	void testIsPerfectMatch_emptyCandidateSkills_emptySkills() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of()).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isPerfectMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
	}
	
	/**
	* Test skills requirements and Candidate Skills are exact match
	* @throws Exception
	*/
	@Test
	void testIsPerfectMatch_perfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("JAVA","SPRING")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("Java","Spring")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills().stream().map(String::toLowerCase).collect(Collectors.toSet()));
		
		assertTrue(util.isPerfectMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Test Candidate has all the required skills and more. Expected perfect match
	* @throws Exception
	*/
	@Test
	void testIsPerfectMatch_tooPerfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("JAVA","SPRING","HIBERNATE")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("Java","Spring")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills().stream().map(String::toLowerCase).collect(Collectors.toSet()));
		
		assertTrue(util.isPerfectMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Tests Candidate does not have all the required skills
	* @throws Exception
	*/
	@Test
	void testIsPerfectMatch_not_perfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("JAVA")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("Java","Spring")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills());
		
		assertFalse(util.isPerfectMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements then expect perfect match
	* @throws Exception
	*/
	@Test
	void testIsExcellentMatch_emptySkills() {
	
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isExcellentMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements and Candidate has no skills expect perfect match
	* @throws Exception
	*/
	@Test
	void testIsExcellentMatch_emptyCandidateSkills_emptySkills() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of()).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isExcellentMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
	}
	
	/**
	* Test skills requirements and Candidate Skills are exact match
	* @throws Exception
	*/
	@Test
	void testIsExcellentMatch_perfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills());
		
		assertTrue(util.isExcellentMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Test Candidate has all the required skills and more. Expected perfect match
	* @throws Exception
	*/
	@Test
	void testIsExcellentMatch_tooPerfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills());
		
		assertTrue(util.isExcellentMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Tests Candidate does not have all the required skills
	* @throws Exception
	*/
	@Test
	void testIsExcellentMatch_not_perfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills());
		
		assertFalse(util.isExcellentMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements then expect perfect match
	* @throws Exception
	*/
	@Test
	void testIsGoodMatch_emptySkills() {
	
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isGoodMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements and Candidate has no skills expect perfect match
	* @throws Exception
	*/
	@Test
	void testIsGoodMatch_emptyCandidateSkills_emptySkills() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of()).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isGoodMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
	}
	
	/**
	* Test skills requirements and Candidate Skills are exact match
	* @throws Exception
	*/
	@Test
	void testIsGoodMatch_perfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills());
		
		assertTrue(util.isGoodMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Test Candidate has all the required skills and more. Expected perfect match
	* @throws Exception
	*/
	@Test
	void testIsGoodMatch_tooPerfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills());
		
		assertTrue(util.isGoodMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Tests Candidate does not have all the required skills
	* @throws Exception
	*/
	@Test
	void testIsGoodMatch_not_perfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills());
		
		assertFalse(util.isGoodMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements then expect perfect match
	* @throws Exception
	*/
	@Test
	void testIsAverageMatch_emptySkills() {
	
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isAverageMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements and Candidate has no skills expect perfect match
	* @throws Exception
	*/
	@Test
	void testIsAverageMatch_emptyCandidateSkills_emptySkills() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of()).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isAverageMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
	}
	
	/**
	* Test skills requirements and Candidate Skills are exact match
	* @throws Exception
	*/
	@Test
	void testIsAverageMatch_perfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills());
		
		assertTrue(util.isAverageMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Test Candidate has all the required skills and more. Expected perfect match
	* @throws Exception
	*/
	@Test
	void testIsAverageMatch_tooPerfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills());
		
		assertTrue(util.isAverageMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Tests Candidate does not have all the required skills
	* @throws Exception
	*/
	@Test
	void testIsAverageMatch_not_perfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills());
		
		assertFalse(util.isAverageMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements then expect perfect match
	* @throws Exception
	*/
	@Test
	void testIsPoorMatch_emptySkills() {
	
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isPoorMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* If no skills requirements and Candidate has no skills expect perfect match
	* @throws Exception
	*/
	@Test
	void testIsPoorMatch_emptyCandidateSkills_emptySkills() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of()).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of()).build();
		
		assertTrue(util.isPoorMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
	}
	
	/**
	* Test skills requirements and Candidate Skills are exact match
	* @throws Exception
	*/
	@Test
	void testIsPoorMatch_perfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills());
		
		assertTrue(util.isPoorMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Test Candidate has all the required skills and more. Expected perfect match
	* @throws Exception
	*/
	@Test
	void testIsPoorMatch_tooPerfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2","3","4","5","6","7")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills());
		
		assertTrue(util.isPoorMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Tests Candidate does not have all the required skills
	* @throws Exception
	*/
	@Test
	void testIsPoorMatch_not_perfectMatch() {
		
		Candidate 				candidate 	= Candidate.builder().skills(Set.of("1","2")).build();
		CandidateFilterOptions 	filters 	= CandidateFilterOptions.builder().skills(Set.of("1","2","3","4","5","6","7","8","9","10")).build();
		
		Mockito.when(this.spySkillsSynonymsUtil.extractSynonymsForSkills(Mockito.anySet())).thenReturn(candidate.getSkills());
		
		assertFalse(util.isPoorMatch(new CandidateSearchAccuracyWrapper(candidate), filters, Set.of()));
		
	}
	
	/**
	* Tests perfect match for languages
	* @throws Exception
	*/
	@Test
	void testIsPerfectMatch_languages_perfectMatch() {
		
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
	void testIsPerfectMatch_languages_not_perfectMatch() {
		
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