package com.arenella.recruit.candidates.extractors;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;

/**
* Extractor to determine languages to filter on
* @author K Parkings
*/
@Component
public class LanguageExtractor implements JobSpecifcationFilterExtractor{
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
	
			Set<String> english = Set.of("english", "engels", "anglais");
			Set<String> dutch = Set.of("dutch", "nederlands");
			Set<String> french = Set.of("french", "francais");
			
			boolean includeEN = english.stream().filter(place -> documentText.contains(place)).count() > 0;
			boolean includeNL = dutch.stream().filter(place -> documentText.contains(place)).count() > 0;
			boolean includeFR = french.stream().filter(place -> documentText.contains(place)).count() > 0;
			
			if(!includeEN) {
				includeEN = DocumentFilterExtractionUtil.uk.stream().filter(place -> documentText.contains(place + " ") || documentText.contains(place + ".")).count() > 0;
			}
			
			
			if (!includeEN && !includeNL && !includeFR) {
				return;
			}
			
			if (includeEN && includeNL && includeFR) {
				return;
			}
			
			filterBuilder.english(includeEN);
			filterBuilder.dutch(includeNL);
			filterBuilder.french(includeFR);
			
	}
}
