package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterProfile.CONTRACT_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.COUNTRY;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.TECH;

/**
* Unit tests for the RecruiterProfileFilter class
* @author K Parkings
*/
public class RecruiterProfileFilterTest {

	/**
	* Test construction via builder
	* @throws Exception
	*/
	@Test
	public void testBuilder_defaults() throws Exception{
		
		RecruiterProfileFilter filters = RecruiterProfileFilter.builder().build();
		
		assertTrue(filters.getCoreTech().isEmpty());
		assertTrue(filters.getRecruitsContractTypes().isEmpty());
		assertTrue(filters.getRecruitsIn().isEmpty());
		assertTrue(filters.isVisibleToCandidates().isEmpty());
		assertTrue(filters.isVisibleToRecruiters().isEmpty());
		assertTrue(filters.isVisibleToPublic().isEmpty());
		
	}
	
	/**
	* Test construction via builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		final Set<TECH> 			tech 					= Set.of(TECH.ARCHITECT, TECH.CLOUD);
		final Set<CONTRACT_TYPE> 	contractTypes 			= Set.of(CONTRACT_TYPE.FREELANCE);
		final Set<COUNTRY> 			recruitsIn 				= Set.of(COUNTRY.BELGIUM);
		final boolean 				visibleToCandidates 	= true;
		final boolean 				visibleToRecruiters 	= false;
		final boolean 				visibleToPublic 		= true;
		
		RecruiterProfileFilter filters = 
				RecruiterProfileFilter
				.builder()
					.coreTech(tech)
					.recruitsContractTypes(contractTypes)
					.recruitsIn(recruitsIn)
					.visibleToCandidates(visibleToCandidates)
					.visibleToPublic(visibleToPublic)
					.visibleToRecruiters(visibleToRecruiters)
				.build();
		
		assertTrue(filters.getCoreTech().contains(TECH.ARCHITECT));
		assertTrue(filters.getCoreTech().contains(TECH.CLOUD));
		assertTrue(filters.getRecruitsContractTypes().contains(CONTRACT_TYPE.FREELANCE));
		assertTrue(filters.getRecruitsIn().contains(COUNTRY.BELGIUM));
		assertEquals(visibleToCandidates, 	filters.isVisibleToCandidates().get());
		assertEquals(visibleToRecruiters, 	filters.isVisibleToRecruiters().get());
		assertEquals(visibleToPublic, 		filters.isVisibleToPublic().get());
		
	}
	
}