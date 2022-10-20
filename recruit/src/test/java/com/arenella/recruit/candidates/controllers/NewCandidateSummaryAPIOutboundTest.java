package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.controllers.NewCandidateSummaryAPIOutbound.SummaryItem;
import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Unit tests for the NewCandidateSummaryAPIOutbound class
* @author K Parkings
*/
public class NewCandidateSummaryAPIOutboundTest {

	/**
	* Tests construction of summary
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		Candidate c1 = Candidate.builder().function(FUNCTION.TESTER).build();
		Candidate c2 = Candidate.builder().function(FUNCTION.CSHARP_DEV).build();
		Candidate c3 = Candidate.builder().function(FUNCTION.JAVA_DEV).build();
		Candidate c4 = Candidate.builder().function(FUNCTION.TESTER).build();
		Candidate c5 = Candidate.builder().function(FUNCTION.JAVA_DEV).build();
		Candidate c6 = Candidate.builder().function(FUNCTION.JAVA_DEV).build();
		
		NewCandidateSummaryAPIOutbound summary = NewCandidateSummaryAPIOutbound
				.builder()
					.addCandidate(c1)
					.addCandidate(c2)
					.addCandidate(c3)
					.addCandidate(c4)
					.addCandidate(c5)
					.addCandidate(c6)
				.build();
		
		SummaryItem item1 = (SummaryItem)summary.getCandidatesSummary().toArray()[0];	
		SummaryItem item2 = (SummaryItem)summary.getCandidatesSummary().toArray()[1];	
		SummaryItem item3 = (SummaryItem)summary.getCandidatesSummary().toArray()[2];	
	
		assertEquals(FUNCTION.JAVA_DEV, 	item1.getFunctionType());
		assertEquals(FUNCTION.TESTER, 		item2.getFunctionType());
		assertEquals(FUNCTION.CSHARP_DEV, 	item3.getFunctionType());
	
		assertEquals(3,	item1.getCount());
		assertEquals(2, item2.getCount());
		assertEquals(1, item3.getCount());
	
		
	}
	
}
