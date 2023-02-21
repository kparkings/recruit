package com.arenella.recruit.candidates.extractors;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;

/**
* Extractor to determine countries to search in
* @author K Parkings
*/
@Component
public class CountryExtractor implements JobSpecifcationFilterExtractor{
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
	
			Set<String> nl = Set.of("groningen","brabant","zuid-holland","arnhem","nieuwegein","schiphol", "zwolle","netherlands","nederland", "amsterdam", "apeldoorn", "utrecht", "rotterdam", "randstad", "amstelveen", "woerden", "amersfoort", "soest", "den haag", "the hague", "overijssel", "gelderland", "almere", "eindhoven", "enschede", "limburg", "flevoland", "alkmaar");
			Set<String> ie = Set.of("ireland", "dublin", "galway", "letterkenny", "limerick", "kildare");
			Set<String> be = Set.of("belgium", "brussels", "leuven","mechelen", "bruxelles", "gand", "courtrai", "antwerp", "antwerpen", "flemish", "flams", "wallon", "Mechelen", "Liege", " gent", "Charleoi", "Meeuwen","Kortrijk", "Namur");
			
			boolean includeNL = nl.stream().filter(place -> documentText.contains(place)).count() > 0;
			boolean includeIE = ie.stream().filter(place -> documentText.contains(place)).count() > 0;
			boolean includeBE = be.stream().filter(place -> documentText.contains(place)).count() > 0;
			boolean includeUK = DocumentFilterExtractionUtil.uk.stream().filter(place -> documentText.contains(place)).count() > 0;
		
			if (!includeNL && !includeIE && !includeBE && !includeUK) {
				return;
			}
			
			if (includeNL && includeIE && includeBE && includeUK) {
				return;
			}
			
			filterBuilder.netherlands(includeNL);
			filterBuilder.belgium(includeBE);
			filterBuilder.uk(includeUK);
			filterBuilder.ireland(includeIE);
			
	}
}