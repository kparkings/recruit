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
	
	public static final Set<String> ENGLISH 	= Set.of("english", "engels", "anglais");
	public static final Set<String> DUTCH 		= Set.of("dutch", "nederlands", "néerlandais", "neerlandais","nederlandse");
	public static final Set<String> FRENCH 		= Set.of("french", "francais", "français","franse");
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
	
			boolean includeEN = ENGLISH.stream().filter(place -> documentText.contains(place)).count() 	> 0;
			boolean includeNL = DUTCH.stream().filter(place -> documentText.contains(place)).count() 	> 0;
			boolean includeFR = FRENCH.stream().filter(place -> documentText.contains(place)).count() 	> 0;
			
			if (!includeEN && filterBuilder.build().getUK()) {
				includeEN = true;
			}
			
			if (!includeEN) {
				includeEN = DocumentFilterExtractionUtil.uk.stream().filter(place -> documentText.contains(" "+place + " ") || documentText.contains(" "+place + ".")).count() > 0;
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
