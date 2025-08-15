package com.arenella.recruit.candidates.extractors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;

/**
* Unit tests for the CityExtractor class 
*/
class CityExtractorTest {


	/*
	* Tests if no matcing city empty string is returned
	*/
	@Test
	void testExtractorNomatch() {
		
		CityExtractor extractor = new CityExtractor();
		
		CandidateExtractedFiltersBuilder filtersBuilder = CandidateExtractedFilters.builder();
		
		extractor.extractFilters("boop", filtersBuilder);
		
		assertEquals("", filtersBuilder.build().getCity());
		
	}
	
	/*
	* Tests if multiple cities found, empty string is returned
	*/
	@Test
	void testExtractorMultipleMatches() {
		
		CityExtractor extractor = new CityExtractor();
		
		CandidateExtractedFiltersBuilder filtersBuilder = CandidateExtractedFilters.builder();
		
		extractor.extractFilters("amsterdam berlin", filtersBuilder);
		
		assertEquals("", filtersBuilder.build().getCity());
		
	}
	
	/*
	* Tests if single city found, formatted city value is returned
	*/
	@Test
	void testExtractorSingleMatch() {
		
		CityExtractor extractor = new CityExtractor();
		
		CandidateExtractedFiltersBuilder filtersBuilder = CandidateExtractedFilters.builder();
		
		extractor.extractFilters("amsterdam", filtersBuilder);
		
		assertEquals("Amsterdam", filtersBuilder.build().getCity());
		
	}
	
}
