package com.arenella.recruit.candidates.extractors;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Extractor to set filters relating to the type of contract
* @author K Parkings
*/
@Component
public class ContractTypeExtractor implements JobSpecifcationFilterExtractor{

	final Set<String> perm 		= Set.of("vast contract","vakantiedagen","reiskostenvergoeding", "% bonus", "jaarsalaris","+ bonus", "bruto per jaar","bruto per maand", "wat bieden we", "permanent", "perm", "vast", "vaste", "per year", "per anum", "days holiday", "vakantie dagen", "vacation days", "salary");
	final Set<String> contract 	= Set.of("6 months", "3 months", "interim", "freelance", "per hour", "per uur", "uurtarief", "hourly rate", "day rate");
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	@Override
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
		
		boolean isPerm 		= perm.stream().filter(a -> documentText.contains(a)).count() > 0;
		boolean isContract 	= contract.stream().filter(a -> documentText.contains(a)).count() > 0;
		
		if (isPerm && isContract) {
			return;
		}
		
		if (!isPerm && !isContract) {
			return;
		}
		
		filterBuilder.perm(isPerm ? PERM.TRUE : PERM.FALSE);
		filterBuilder.freelance(isContract ? FREELANCE.TRUE :  FREELANCE.FALSE);
		
	}

}
