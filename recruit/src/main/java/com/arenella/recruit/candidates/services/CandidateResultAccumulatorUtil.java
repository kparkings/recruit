package com.arenella.recruit.candidates.services;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;

import static com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;
import static com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy.perfect;
import static com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy.excellent;
import static com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy.good;
import static com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy.average;
import static com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy.poor;
/**
* Class used to accumulate the best matching candidates over multiple page iterations.
* It contains buckets for the max number of results desired. Initially filling the
* buckets with the first available results and subsequently replacing the buckets 
* contents with more suitable candidates as they become available until the buckets
* are filled with candidates meeting the threshold of suitability
*  
*/
public class CandidateResultAccumulatorUtil {
	
	private final 	CandidateSuggestionUtil 									suggestionsUtil;
	private final 	CandidateFilterOptions 										suggestionFilterOptions;
	private final 	Set<String> 												searchTermKeywords;
	private final 	Map<CandidateSearchAccuracyWrapper, suggestion_accuracy> 	buckets 					= new LinkedHashMap<>();
	private final 	int															maxResults;
	private boolean obtainedPerfectResults = false;
	
	/**
	* Constructor
	* @param suggestionsUtil			- Contains Utils for determining Candidate accuracy
	* @param suggestionFilterOptions	- Filter options
	* @param searchTermKeywords			- Search term keywords
	*/
	public CandidateResultAccumulatorUtil(CandidateSuggestionUtil suggestionsUtil, CandidateFilterOptions suggestionFilterOptions, Set<String> searchTermKeywords, int maxResults) {
		this.suggestionsUtil 			= suggestionsUtil;
		this.suggestionFilterOptions 	= suggestionFilterOptions;
		this.searchTermKeywords 		= searchTermKeywords;
		this.maxResults 				= maxResults;
	}
	
	/**
	* Returns true if all buckets have been filled and 
	* contain Candidates that are a perfect match
	*/
	public boolean obtainedPerfectResults() {
		return this.obtainedPerfectResults;
	}
	
	public Set<CandidateSearchAccuracyWrapper> getAccumulatedCandidates(){
		return this.buckets.keySet();
	}
	
	/**
	* Process a candidate and if it is more accurate that existing candidates replaces 
	* an existing candidate in the results
	* @param maxResults - Max results required
	* @param candidate  - Candidate to be processed
	*/
	public void processCandidate(Candidate candidate) {
		
		CandidateSearchAccuracyWrapper wrappedCandidate = new CandidateSearchAccuracyWrapper(candidate);
		
		this.getAccuracy(wrappedCandidate).ifPresent(accuracy -> {
			if (buckets.size() < maxResults) {
				buckets.put(wrappedCandidate, accuracy);
			} else {
				this.buckets.entrySet().stream().filter(kv -> isLessAccurate(kv.getValue(), accuracy)).findFirst().ifPresent(replace -> {
					this.buckets.remove(replace.getKey());
					this.buckets.put(wrappedCandidate, accuracy);
				});
			}
		});
		
		long perfectBuckets = this.buckets.values().stream().filter(accuracy -> accuracy == suggestion_accuracy.perfect).count();
	
		if (perfectBuckets == maxResults) {
			this.obtainedPerfectResults = true;
		}
		
	}
	
	private boolean isLessAccurate(suggestion_accuracy baseAccuracy, suggestion_accuracy compareAccuracy) {
		
		if (baseAccuracy == compareAccuracy) {
			return false;
		}
		
		if (baseAccuracy == perfect) {
			return false;
		}
		
		if (baseAccuracy == excellent && Set.of(good, average, poor).contains(compareAccuracy)) {
			return false;
		}
		
		if (baseAccuracy == good && Set.of(average, poor).contains(compareAccuracy)) {
			return false;
		}
		
		if (baseAccuracy == average && Set.of(poor).contains(compareAccuracy)) {
			return false;
		}
		
		return true;
	
	}
	
	/**
	* Calculates the accuracy of a Candidate
	* @param wrappedCandidate - Candidate to be processed
	* @return Accuracy of Candidate against search Filters
	*/
	private Optional<suggestion_accuracy> getAccuracy(CandidateSearchAccuracyWrapper wrappedCandidate) {
		
		if (this.suggestionsUtil.isPerfectMatch(wrappedCandidate, this.suggestionFilterOptions, this.searchTermKeywords)) {
			return Optional.of(suggestion_accuracy.perfect);
		}
		
		if (this.suggestionsUtil.isExcellentMatch(wrappedCandidate, this.suggestionFilterOptions, this.searchTermKeywords)) {
			return Optional.of(suggestion_accuracy.excellent);
		}
		
		if (this.suggestionsUtil.isGoodMatch(wrappedCandidate, this.suggestionFilterOptions, this.searchTermKeywords)) {
			return Optional.of(suggestion_accuracy.good);
		}
		
		if (this.suggestionsUtil.isAverageMatch(wrappedCandidate, this.suggestionFilterOptions, this.searchTermKeywords)) {
			return Optional.of(suggestion_accuracy.average);
		}
		
		if (this.suggestionsUtil.isPoorMatch(wrappedCandidate, this.suggestionFilterOptions, this.searchTermKeywords)) {
			return Optional.of(suggestion_accuracy.poor);
		}
		
		return Optional.empty();
		
	}
	
}