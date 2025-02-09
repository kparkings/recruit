package com.arenella.recruit.candidates.extractors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;
import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* Unit tests for the CountryExtractor class
* @author K Parkings
*/
public class CountryExtractorTest {

	/**
	* If no countries identified no need to set filter
	* @throws Exception
	*/
	@Test
	public void testNoCountriesIdentified() throws Exception{
		
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final CountryExtractor 					extractor 		= new CountryExtractor();
		
		extractor.extractFilters("", filterBuilder);
		
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.UK));
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.REPUBLIC_OF_IRELAND));
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.NETHERLANDS));
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.BELGIUM));
		
	}
	
	/**
	* If all countries identified no need to set filter
	* @throws Exception
	*/
	@Test
	public void testAllIdentified() throws Exception{
	
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final CountryExtractor 					extractor 		= new CountryExtractor();
		
		extractor.extractFilters(" england ireland netherlands belgium.", filterBuilder);
		
		assertTrue(filterBuilder.build().getCountries().contains(COUNTRY.UK));
		assertTrue(filterBuilder.build().getCountries().contains(COUNTRY.REPUBLIC_OF_IRELAND));
		assertTrue(filterBuilder.build().getCountries().contains(COUNTRY.NETHERLANDS));
		assertTrue(filterBuilder.build().getCountries().contains(COUNTRY.BELGIUM));
		
	}
	
	/**
	* If Country is identified no need to set filter
	* @throws Exception
	*/
	@Test
	public void testEngland() throws Exception{
	
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final CountryExtractor 					extractor 		= new CountryExtractor();
		
		extractor.extractFilters(" england ", filterBuilder);
		
		assertTrue(filterBuilder.build().getCountries().contains(COUNTRY.UK));
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.REPUBLIC_OF_IRELAND));
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.NETHERLANDS));
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.BELGIUM));
		
	}
	
	/**
	* If Country is identified no need to set filter
	* @throws Exception
	*/
	@Test
	public void testBelgium() throws Exception{
	
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final CountryExtractor 					extractor 		= new CountryExtractor();
		
		extractor.extractFilters(" belgium ", filterBuilder);
		
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.UK));
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.REPUBLIC_OF_IRELAND));
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.NETHERLANDS));
		assertTrue(filterBuilder.build().getCountries().contains(COUNTRY.BELGIUM));
		
	}
	
	/**
	* If Country is identified no need to set filter
	* @throws Exception
	*/
	@Test
	public void testIreland() throws Exception{
	
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final CountryExtractor 					extractor 		= new CountryExtractor();
		
		extractor.extractFilters(" ireland ", filterBuilder);
		
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.UK));
		assertTrue(filterBuilder.build().getCountries().contains(COUNTRY.REPUBLIC_OF_IRELAND));
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.NETHERLANDS));
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.BELGIUM));
		
	}
	
	/**
	* If Country is identified no need to set filter
	* @throws Exception
	*/
	@Test
	public void testNetherlands() throws Exception{
	
		final CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		final CountryExtractor 					extractor 		= new CountryExtractor();
		
		extractor.extractFilters(" nederland ", filterBuilder);
		
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.UK));
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.REPUBLIC_OF_IRELAND));
		assertTrue(filterBuilder.build().getCountries().contains(COUNTRY.NETHERLANDS));
		assertFalse(filterBuilder.build().getCountries().contains(COUNTRY.BELGIUM));
		
	}
	
}