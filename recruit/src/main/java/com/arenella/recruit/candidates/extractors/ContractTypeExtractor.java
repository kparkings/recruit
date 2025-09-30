package com.arenella.recruit.candidates.extractors;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

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

	final Set<String> perm 		= Set.of("salarispakket","0k","1k","2k","3k","4k","5k","6k","7k","8k","9k","pensioen","paid leave","13e mois", "pas de freelance","cdi","salaris ", "altersvorsorge ","5,000","0,000","5.000","0.000","annual bonus", "vast contract","vakantiedagen","reiskostenvergoeding", "% bonus", "jaarsalaris","+ bonus", "bruto per jaar","bruto per maand", "wat bieden we", "maandsalaris", "permanent", "perm.","perm ", "vast", "vaste", "per year", "per anum", "days holiday", "vakantie dagen", "vacation days", "full-time", "full time");
	final Set<String> contract 	= Set.of("type: Contract","+ months","year contract","contract role","contract opportunity","months contract"," maanden","monate", "cdd"," p.u"," p/u"," p/d","detachering", "verlenging", "euros/day","daily rate","b2b","optie tot verlenging","6 month", "3 month", "interim", "ir35","freelance", "per hour", "per uur", "uurtarief", "hourly rate", "day rate","month contract","per day", "payroll constructie");
	
	final Set<String> permExcludeTerms 		= Set.of("payroll constructie","vaste klant");
	final Set<String> contractExcludeTerms 	= Set.of();
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	@Override
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
	
		AtomicReference<String> 	permDocumentText 		= new AtomicReference<>();
		AtomicReference<String> 	contractDocumentText 	=new AtomicReference<>();
	
		permDocumentText.set(documentText);
		contractDocumentText.set(documentText);
		
		permExcludeTerms.stream().forEach(w -> permDocumentText.set(permDocumentText.get().replaceAll(w, " ")));
		contractExcludeTerms.stream().forEach(w -> contractDocumentText.set(permDocumentText.get().replaceAll(w, " ")));
		
		//Set<String> debugPerm 		= perm.stream().filter(permDocumentText.get()::contains).collect(Collectors.toSet());
		//Set<String> debugContract 	= contract.stream().filter(contractDocumentText.get()::contains).collect(Collectors.toSet());
		
		boolean isPerm 		= perm.stream().filter(permDocumentText.get()::contains).count() > 0;
		boolean isContract 	= contract.stream().filter(contractDocumentText.get()::contains).count() > 0;
			
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
