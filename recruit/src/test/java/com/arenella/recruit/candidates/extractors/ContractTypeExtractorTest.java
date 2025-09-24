package com.arenella.recruit.candidates.extractors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the ContractTypeExtractor class
* @author K Parkings
*/
class ContractTypeExtractorTest {

	/**
	* If both contract types identified no need to set filters
	* @throws Exception
	*/
	@Test
	void testBothContractTypesIdentified() {
		
		CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		ContractTypeExtractor 				extractor 		= new ContractTypeExtractor();
		
		extractor.extractFilters("freelance permanent", filterBuilder);
		
		assertNull(filterBuilder.build().getPerm());
		assertNull(filterBuilder.build().getFreelance());
		
	}
	
	/**
	* If neither contract type identified no need to set filters
	* @throws Exception
	*/
	@Test
	void testNeitherContractTypesIdentified() {
		
		CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		ContractTypeExtractor 				extractor 		= new ContractTypeExtractor();
		
		extractor.extractFilters("", filterBuilder);
		
		assertNull(filterBuilder.build().getPerm());
		assertNull(filterBuilder.build().getFreelance());
		
	}
	
	/**
	* If neither Freelance type identified no need to set filters
	* @throws Exception
	*/
	@Test
	void testContractTypeIdentified() {
		
		CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		ContractTypeExtractor 				extractor 		= new ContractTypeExtractor();
		
		extractor.extractFilters("freelance", filterBuilder);
		
		assertEquals(PERM.FALSE, 		filterBuilder.build().getPerm());
		assertEquals(FREELANCE.TRUE, 	filterBuilder.build().getFreelance());
		
	}
	
	/**
	* If neither Perm type identified no need to set filters
	* @throws Exception
	*/
	@Test
	void testPermTypeIdentified() {
		
		CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		ContractTypeExtractor 				extractor 		= new ContractTypeExtractor();
		
		extractor.extractFilters("permanent", filterBuilder);
		
		assertEquals(PERM.TRUE, 		filterBuilder.build().getPerm());
		assertEquals(FREELANCE.FALSE, 	filterBuilder.build().getFreelance());
		
	}
	
}
