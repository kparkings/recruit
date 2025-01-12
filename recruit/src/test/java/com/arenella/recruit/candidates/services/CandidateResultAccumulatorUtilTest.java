package com.arenella.recruit.candidates.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

import static com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy.excellent;

/**
* Unit tests for the CandidateResultAccumulatorUtil class
*/
@ExtendWith(MockitoExtension.class)
class CandidateResultAccumulatorUtilTest {

	@Mock
	private CandidateSuggestionUtil mockUtil;
	
	/**
	* Tests case that is is possible to fill all the buckets with perfect results
	*/
	@Test
	void testAllPerfectBucketsFilled() {
		
		CandidateFilterOptions		 	filters 			= CandidateFilterOptions.builder().build();
		Set<String> 					searchTermKeywords 	= Set.of();
		int 							maxResults 			= 5;
		CandidateResultAccumulatorUtil 	accumulator 	= new CandidateResultAccumulatorUtil(mockUtil, filters, searchTermKeywords, maxResults);
		
		assertFalse(accumulator.obtainedPerfectResults());
		assertTrue(accumulator.getAccumulatedCandidates().isEmpty());
		
		Mockito.when(this.mockUtil.isPerfectMatch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
		
		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());
		
		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());

		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());

		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());

		accumulator.processCandidate(Candidate.builder().build());
		
		assertTrue(accumulator.obtainedPerfectResults());
		assertEquals(maxResults, accumulator.getAccumulatedCandidates().size());
		
	}
	
	/**
	* Tests case that Candidates are processed but there are not enough Candidates meeting the 
	* threshold to be included in the results. In this case we expect only the candidates that 
	* met the threshold to be included. 
	*/
	@Test
	void testNoEngoughMatchesToFillBuckets() {
		
		CandidateFilterOptions		 	filters 			= CandidateFilterOptions.builder().build();
		Set<String> 					searchTermKeywords 	= Set.of();
		int 							maxResults 			= 5;
		CandidateResultAccumulatorUtil 	accumulator 	= new CandidateResultAccumulatorUtil(mockUtil, filters, searchTermKeywords, maxResults);
		
		assertFalse(accumulator.obtainedPerfectResults());
		assertTrue(accumulator.getAccumulatedCandidates().isEmpty());
		
		Mockito.when(this.mockUtil.isPerfectMatch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(false);
		Mockito.when(this.mockUtil.isExcellentMatch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
		Mockito.when(this.mockUtil.isGoodMatch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
		Mockito.when(this.mockUtil.isAverageMatch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
		Mockito.when(this.mockUtil.isPoorMatch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
		
		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());
		
		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());

		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());

		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());

		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());
		assertEquals(1, accumulator.getAccumulatedCandidates().size());
		
	}
	
	@Test
	void testBetterResultsReplaceExistingBuckets() {

		CandidateFilterOptions		 	filters 			= CandidateFilterOptions.builder().build();
		Set<String> 					searchTermKeywords 	= Set.of();
		int 							maxResults 			= 5;
		CandidateResultAccumulatorUtil 	accumulator 	= new CandidateResultAccumulatorUtil(mockUtil, filters, searchTermKeywords, maxResults);
		
		assertFalse(accumulator.obtainedPerfectResults());
		assertTrue(accumulator.getAccumulatedCandidates().isEmpty());
		
		Mockito.when(this.mockUtil.isPerfectMatch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
		Mockito.when(this.mockUtil.isExcellentMatch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
		Mockito.when(this.mockUtil.isGoodMatch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
		Mockito.when(this.mockUtil.isAverageMatch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
		Mockito.when(this.mockUtil.isPoorMatch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
		
		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());
		
		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());

		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());

		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());

		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());
		assertEquals(maxResults, accumulator.getAccumulatedCandidates().size());
		
		Mockito.when(this.mockUtil.isPerfectMatch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
		Mockito.when(this.mockUtil.isExcellentMatch(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);

		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());
		
		accumulator.processCandidate(Candidate.builder().build());
		
		assertFalse(accumulator.obtainedPerfectResults());
		
		assertEquals(maxResults, accumulator.getAccumulatedCandidates().size());
		
		
		@SuppressWarnings("unchecked")
		Map<CandidateSearchAccuracyWrapper, suggestion_accuracy> buckets = (Map<CandidateSearchAccuracyWrapper, suggestion_accuracy>) ReflectionTestUtils.getField(accumulator, "buckets");
		
		assertEquals(2, buckets.values().stream().filter(a -> a == excellent).count());
		
	}

}