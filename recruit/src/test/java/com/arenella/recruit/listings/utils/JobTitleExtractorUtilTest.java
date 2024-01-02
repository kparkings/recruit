package com.arenella.recruit.listings.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingAlertFilterOptions;

/**
* Unit test for JobTitleExtractor class
* @author K Parkings
*/
public class JobTitleExtractorUtilTest {

	/**
	* Tests that if no category was determined no category will be given
	* @throws Exception
	*/
	@Test
	public void testNoJobTitlesFound() throws Exception{
		
		final ListingAlertFilterOptions.ListingAlertFilterOptionsBuilder 	filterBuilder 	= ListingAlertFilterOptions.builder();
		final CategoryExtractorUtil 										extractor 		= new CategoryExtractorUtil();
		
		extractor.extractFilters("", filterBuilder);
		
		assertTrue(filterBuilder.build().getCategories().isEmpty());
		
	}
	
	/**
	* Tests that if a single job title is determined job title filter 
	* will be set
	* @throws Exception
	*/
	@Test
	public void testSingleJobTitlesFound() throws Exception{
		
		final ListingAlertFilterOptions.ListingAlertFilterOptionsBuilder 	filterBuilder 	= ListingAlertFilterOptions.builder();
		final CategoryExtractorUtil 										extractor 		= new CategoryExtractorUtil();
		
		extractor.extractFilters(" java developer", filterBuilder);
		
		assertEquals(Listing.TECH.JAVA,filterBuilder.build().getCategories().stream().findFirst().get());
		
	}
	
	/**
	* Tests that if a multiple job titles have been identified but one has more references that is the
	* selected job title
	* @throws Exception
	*/
	@Test
	public void testMultipleJobTitlesFoundSingleHasHighestWeight() throws Exception{
		
		final ListingAlertFilterOptions.ListingAlertFilterOptionsBuilder 	filterBuilder 	= ListingAlertFilterOptions.builder();
		final CategoryExtractorUtil 										extractor 		= new CategoryExtractorUtil();
		
		extractor.extractFilters(" java developer scrum master java engineer", filterBuilder);
		
		assertEquals(Listing.TECH.JAVA,filterBuilder.build().getCategories().stream().findFirst().get());
		
	}
	
}