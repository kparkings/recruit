package com.arenella.recruit.candidates.extractors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;

/**
* Unit tests for the SeniorityExtractor class
* @author K Parkings
*/
public class SeniorityExtractorTest {

	/**
	* Tests that if none of the three seniority types can be found 
	* that no experience filters will be set
	* @throws Exception
	*/
	@Test
	public void testUnableToDetermineSeniority_none() throws Exception{
		
		final SeniorityExtractor 				extractor 	= new SeniorityExtractor();
		final CandidateExtractedFiltersBuilder	filters 	= CandidateExtractedFilters.builder();
		
		extractor.extractFilters("", filters);
		
		assertEquals("", filters.build().getExperienceGTE());
		assertEquals("", filters.build().getExperienceLTE());
		
	}
	
	/**
	* Tests that if all of the three seniority types can be found 
	* that no experience filters will be set
	* @throws Exception
	*/
	@Test
	public void testUnableToDetermineSeniority_all() throws Exception{
		
		final SeniorityExtractor 				extractor 	= new SeniorityExtractor();
		final CandidateExtractedFiltersBuilder	filters 	= CandidateExtractedFilters.builder();
		
		extractor.extractFilters("senior medior junior", filters);
		
		assertEquals("", filters.build().getExperienceGTE());
		assertEquals("", filters.build().getExperienceLTE());
		
	}
	
	/**
	* Tests that if both medior and  seniority types can be found 
	* the experience filters will be set
	* @throws Exception
	*/
	@Test
	public void testMediorAndJunior() throws Exception{
		
		final SeniorityExtractor 				extractor 	= new SeniorityExtractor();
		final CandidateExtractedFiltersBuilder	filters 	= CandidateExtractedFilters.builder();
		
		extractor.extractFilters("junior medior ", filters);
		
		assertEquals(SeniorityExtractor.JUNIOR_EDGE_MIN, filters.build().getExperienceGTE());
		assertEquals(SeniorityExtractor.MEDIOR_EDGE_MAX, filters.build().getExperienceLTE());
		
	}
	
	/**
	* Tests that if both medior and  seniority types can be found 
	* the experience filters will be set
	* @throws Exception
	*/
	@Test
	public void testMediorAndSenior() throws Exception{
		
		final SeniorityExtractor 				extractor 	= new SeniorityExtractor();
		final CandidateExtractedFiltersBuilder	filters 	= CandidateExtractedFilters.builder();
		
		extractor.extractFilters("senior medior ", filters);
		
		assertEquals(SeniorityExtractor.MEDIOR_EDGE_MIN, filters.build().getExperienceGTE());
		assertEquals(SeniorityExtractor.SENIOR_EDGE_MAX, filters.build().getExperienceLTE());
		
	}
	
	/**
	* Tests that if both junior and senior types can be found 
	* the experience filters will be set
	* @throws Exception
	*/
	@Test
	public void testJuniorAndSenior() throws Exception{
		
		final SeniorityExtractor 				extractor 	= new SeniorityExtractor();
		final CandidateExtractedFiltersBuilder	filters 	= CandidateExtractedFilters.builder();
		
		extractor.extractFilters("junior senior ", filters);
		
		assertEquals(SeniorityExtractor.JUNIOR_EDGE_MIN, filters.build().getExperienceGTE());
		assertEquals(SeniorityExtractor.SENIOR_EDGE_MAX, filters.build().getExperienceLTE());
		
	}
	
	/**
	* Tests that if only medior type can be found 
	* the experience filters will be set
	* @throws Exception
	*/
	@Test
	public void testOnlyMedior() throws Exception{
		
		final SeniorityExtractor 				extractor 	= new SeniorityExtractor();
		final CandidateExtractedFiltersBuilder	filters 	= CandidateExtractedFilters.builder();
		
		extractor.extractFilters("medior ", filters);
		
		assertEquals(SeniorityExtractor.MEDIOR_EDGE_MIN, filters.build().getExperienceGTE());
		assertEquals(SeniorityExtractor.MEDIOR_EDGE_MAX, filters.build().getExperienceLTE());
		
	}
	
	/**
	* Tests that if only junior type can be found 
	* the experience filters will be set
	* @throws Exception
	*/
	@Test
	public void testOnlyJunior() throws Exception{
		
		final SeniorityExtractor 				extractor 	= new SeniorityExtractor();
		final CandidateExtractedFiltersBuilder	filters 	= CandidateExtractedFilters.builder();
		
		extractor.extractFilters("junior ", filters);
		
		assertEquals(SeniorityExtractor.JUNIOR_EDGE_MIN, filters.build().getExperienceGTE());
		assertEquals(SeniorityExtractor.JUNIOR_EDGE_MAX, filters.build().getExperienceLTE());
		
	}
	
	/**
	* Tests that if only senior type can be found 
	* the experience filters will be set
	* @throws Exception
	*/
	@Test
	public void testOnlySenior() throws Exception{
		
		final SeniorityExtractor 				extractor 	= new SeniorityExtractor();
		final CandidateExtractedFiltersBuilder	filters 	= CandidateExtractedFilters.builder();
		
		extractor.extractFilters("senior ", filters);
		
		assertEquals(SeniorityExtractor.SENIOR_EDGE_MIN, filters.build().getExperienceGTE());
		assertEquals(SeniorityExtractor.SENIOR_EDGE_MAX, filters.build().getExperienceLTE());
		
	}
	
}
