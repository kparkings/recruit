package com.arenella.recruit.candidates.extractors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;

/**
* Unit test for JobTitleExtractor class
* @author K Parkings
*/

class JobTitleExtractorTest {

	/**
	* Tests that if no job title determined no 
	* job title filter set
	* @throws Exception
	*/
	@Test
	void testNoJobTitlesFound() {
		
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
	void testSingleJobTitlesFound()  {
		
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
	void testMultipleJobTitlesFoundSingleHasHighestWeight() {
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final JobTitleExtractor 				extractor 		= new JobTitleExtractor();
		
		extractor.extractFilters(" java developer scrum master java engineer", filterBuilder);
		
		assertEquals("Java Developer",filterBuilder.build().getJobTitle());
		
	}
	
	/**
	* Tests that if two or more job types have an equal weighting for score that 
	* preference is given to non it recruiter roles
	* @throws Exception
	*/
	@Test
	void testGiveWeightToNonITRecruiterJobType() {
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final JobTitleExtractor 				extractor 		= new JobTitleExtractor();
		
		extractor.extractFilters(" java developer it recruiter", filterBuilder);
		
		assertEquals("Java Developer",filterBuilder.build().getJobTitle());
		
	}
	
	/**
	* Tests case that if both Web Developer and a subtype of 
	* Web Developer are found the sub type is selected
	*/
	@Test
	void testWebDeveloperSubTypePresent() {
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final JobTitleExtractor 				extractor 		= new JobTitleExtractor();
		
		extractor.extractFilters(" web developer react web developer", filterBuilder);
		
		assertEquals("React Developer",filterBuilder.build().getJobTitle());
		
	}
	
	/**
	* Tests case that  both Web Developer and not subtypes of 
	* Web Developer are found the Web developer is selected
	*/
	@Test
	void testWebDeveloperSubTypeNotPresent() {
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final JobTitleExtractor 				extractor 		= new JobTitleExtractor();
		
		extractor.extractFilters(" web developer ", filterBuilder);
		
		assertEquals("Web Developer",filterBuilder.build().getJobTitle());
		
	}
	
	/**
	* Tests case that if both Software Developer and a subtype of 
	* Software Developer are found the sub type is selected
	*/
	@Test
	void testSoftwareDeveloperSubTypePresent() {
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final JobTitleExtractor 				extractor 		= new JobTitleExtractor();
		
		extractor.extractFilters(" software developer java software developer", filterBuilder);
		
		assertEquals("Java Developer",filterBuilder.build().getJobTitle());
		
	}
	
	/**
	* Tests case that  both Software Developer and not subtypes of 
	* Software Developer are found the Web developer is selected
	*/
	@Test
	void testSoftwareDeveloperSubTypeNotPresent() {
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final JobTitleExtractor 				extractor 		= new JobTitleExtractor();
		
		extractor.extractFilters(" software developer ", filterBuilder);
		
		assertEquals("Software Developer",filterBuilder.build().getJobTitle());
		
	}

	/**
	* Tests case that if both Architect and a subtype of 
	* Architect are found the sub type is selected
	*/
	@Test
	void testArchitectSubTypePresent() {
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final JobTitleExtractor 				extractor 		= new JobTitleExtractor();
		
		extractor.extractFilters(" architect data architect architect", filterBuilder);
		
		assertEquals("Data architect",filterBuilder.build().getJobTitle());
		
	}
	
	/**
	* Tests case that both Software Developer and not subtypes of 
	* Software Developer are found the Web developer is selected
	*/
	@Test
	void testArchitectSubTypeNotPresent() {
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final JobTitleExtractor 				extractor 		= new JobTitleExtractor();
		
		extractor.extractFilters(" architect ", filterBuilder);
		
		assertEquals("Architect",filterBuilder.build().getJobTitle());
		
	}
	
	/**
	* Tests case that if both Manager and a subtype of 
	* Manager are found the sub type is selected
	*/
	@Test
	void testManagerSubTypePresent() {
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final JobTitleExtractor 				extractor 		= new JobTitleExtractor();
		
		extractor.extractFilters(" project manager cto project manager", filterBuilder);
		
		assertEquals("CTO",filterBuilder.build().getJobTitle());
		
	}
	
	/**
	* Tests case that  both Software Developer and not subtypes of 
	* Software Developer are found the Web developer is selected
	*/
	@Test
	void testManagerSubTypeNotPresent() {
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final JobTitleExtractor 				extractor 		= new JobTitleExtractor();
		
		extractor.extractFilters(" project manager ", filterBuilder);
		
		assertEquals("Project manager",filterBuilder.build().getJobTitle());
		
	}
	
}