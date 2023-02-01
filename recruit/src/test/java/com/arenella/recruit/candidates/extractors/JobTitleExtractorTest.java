package com.arenella.recruit.candidates.extractors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;

/**
* Unit test for JobTitleExtractor class
* @author K Parkings
*/
public class JobTitleExtractorTest {

	/**
	* Tests that if no job title determined no 
	* job title filter set
	* @throws Exception
	*/
	@Test
	public void testNoJobTitlesFound() throws Exception{
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final JobTitleExtractor 				extractor 		= new JobTitleExtractor();
		
		extractor.extractFilters("", filterBuilder);
		
		assertEquals("",filterBuilder.build().getJobTitle());
		
	}
	
	/**
	* Tests that if a single job title is determined job title filter 
	* will be set
	* @throws Exception
	*/
	@Test
	public void testSingleJobTitlesFound() throws Exception{
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final JobTitleExtractor 				extractor 		= new JobTitleExtractor();
		
		extractor.extractFilters(" java developer", filterBuilder);
		
		assertEquals("Java Developer",filterBuilder.build().getJobTitle());
		
	}
	
	/**
	* Tests that if a multiple job titles have been identified but one has more references that is the
	* selected job title
	* @throws Exception
	*/
	@Test
	public void testMultipleJobTitlesFoundSingleHasHighestWeight() throws Exception{
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final JobTitleExtractor 				extractor 		= new JobTitleExtractor();
		
		extractor.extractFilters(" java developer scrum master java engineer", filterBuilder);
		
		assertEquals("Java Developer",filterBuilder.build().getJobTitle());
		
	}
	
	/**
	* Tests the case that multiple job types have been identified and no single 
	* job type has the the maxium number of occurrences of a related search term
	* @throws Exception
	*/
	@Test
	public void testMultipleJobTitlesFoundMultioleHaveHighestWeight() throws Exception{
		
		//TODO: [KP] Decide how to handle this. Currently just selects one.
		
	}
	
}
