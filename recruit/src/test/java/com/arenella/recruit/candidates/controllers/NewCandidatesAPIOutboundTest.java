package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.controllers.NewCandidatesAPIOutbound.SummaryItem;
import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* Unit tests for the NewCandidatesAPIOutbound class
* @author K Parkings
*/
public class NewCandidatesAPIOutboundTest {

	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		Candidate c1 = Candidate.builder().country(COUNTRY.BELGIUM).yearsExperience(3).roleSought("Java Dev").build();
		Candidate c2 = Candidate.builder().country(COUNTRY.NETHERLANDS).yearsExperience(5).roleSought("C# Dev").build();
		Candidate c3 = Candidate.builder().country(COUNTRY.NETHERLANDS).yearsExperience(1).roleSought("Tester").build();
		
		NewCandidatesAPIOutbound summary = NewCandidatesAPIOutbound
				.builder()
					.addCandidate(c1)
					.addCandidate(c2)
					.addCandidate(c3)
				.build();
		
		SummaryItem sum1 = (SummaryItem)summary.getCandidateSummary().toArray()[0];
		SummaryItem sum2 = (SummaryItem)summary.getCandidateSummary().toArray()[1];
		SummaryItem sum3 = (SummaryItem)summary.getCandidateSummary().toArray()[2];
		
		assertEquals(c2.getRoleSought(), 		sum1.getFunctionDesc());
		assertEquals(c2.getYearsExperience(), 	sum1.getYearsExperience());
		assertEquals(c2.getCountry(), 			sum1.getCountry());
		
		assertEquals(c1.getRoleSought(), 		sum2.getFunctionDesc());
		assertEquals(c1.getYearsExperience(), 	sum2.getYearsExperience());
		assertEquals(c1.getCountry(), 			sum2.getCountry());
		
		assertEquals(c3.getRoleSought(), 		sum3.getFunctionDesc());
		assertEquals(c3.getYearsExperience(), 	sum3.getYearsExperience());
		assertEquals(c3.getCountry(), 			sum3.getCountry());
		
	}
	
}