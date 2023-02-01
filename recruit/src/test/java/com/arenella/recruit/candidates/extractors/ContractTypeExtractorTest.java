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
public class ContractTypeExtractorTest {

	/**
	* If both contract types identified no need to set filters
	* @throws Exception
	*/
	@Test
	public void testBothContractTypesIdentified() throws Exception{
		
		CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		ContractTypeExtractor 				extractor 		= new ContractTypeExtractor();
		
		extractor.extractFilters("contract permanent", filterBuilder);
		
		assertNull(filterBuilder.build().getPerm());
		assertNull(filterBuilder.build().getFreelance());
		
	}
	
	/**
	* If neither contract type identified no need to set filters
	* @throws Exception
	*/
	@Test
	public void testNeitherContractTypesIdentified() throws Exception{
		
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
	public void testContractTypeIdentified() throws Exception{
		
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
	public void testPermTypeIdentified() throws Exception{
		
		CandidateExtractedFiltersBuilder 	filterBuilder 	= CandidateExtractedFilters.builder();
		ContractTypeExtractor 				extractor 		= new ContractTypeExtractor();
		
		extractor.extractFilters("permanent", filterBuilder);
		
		assertEquals(PERM.TRUE, 		filterBuilder.build().getPerm());
		assertEquals(FREELANCE.FALSE, 	filterBuilder.build().getFreelance());
		
	}
	
}
