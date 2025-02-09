package com.arenella.recruit.candidates.extractors;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* Extractor to determine languages to filter on
* @author K Parkings
*/
@Component
public class LanguageExtractor implements JobSpecifcationFilterExtractor{
	
	public static final Set<String> ENGLISH 	= Set.of("english", "engels", "anglais");
	public static final Set<String> DUTCH 		= Set.of("dutch", "nederlands", "néerlandais", "neerlandais","nederlandse");
	public static final Set<String> FRENCH 		= Set.of("french", "francais", "français","franse");
	public static final Set<String> BULGARIAN 	= Set.of("bulgarian"); 
	public static final Set<String> CROATIAN 	= Set.of("croatian");
	public static final Set<String> CZECH 		= Set.of("czech"); 
	public static final Set<String> DANISH 		= Set.of("danish");
	public static final Set<String> ESTONIAN 	= Set.of("estonian");
	public static final Set<String> FINNISH 	= Set.of("finnish"); 
	public static final Set<String> GERMAN 		= Set.of("german"); 
	public static final Set<String> GREEK 		= Set.of("greek"); 
	public static final Set<String> HUNGARIAN 	= Set.of("hungarian");
	public static final Set<String> HINDI 		= Set.of("hindi");
	public static final Set<String> ITALIAN 	= Set.of("italian");
	public static final Set<String> LATVIAN 	= Set.of("latvian");
	public static final Set<String> LITHUANIAN 	= Set.of("lithuanian");
	public static final Set<String> MALTESE 	= Set.of("maltese");
	public static final Set<String> NORWEGEN 	= Set.of("norwegen");
	public static final Set<String> POLISH 		= Set.of("polish"); 
	public static final Set<String> PORTUGUESE 	= Set.of("portuguese");
	public static final Set<String> ROMANIAN 	= Set.of("romanian"); 
	public static final Set<String> RUSSIAN 	= Set.of("russian");
	public static final Set<String> SWEDISH 	= Set.of("swedish");
	public static final Set<String> SPANISH 	= Set.of("spanish"); 
	public static final Set<String> SLOVAKIAN 	= Set.of("slovakian");
	public static final Set<String> SLOVENIAN 	= Set.of("slovenian");
	public static final Set<String> TURKISH 	= Set.of("turkish");
	public static final Set<String> UKRAINIAN 	= Set.of("ukrainian");
	public static final Set<String> URDU 		= Set.of("urdi");
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
	
			Set<Language.LANGUAGE> extractedLanguages = new HashSet<>();
		
			ENGLISH.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.ENGLISH));
			DUTCH.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.DUTCH));
			FRENCH.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.FRENCH));
			BULGARIAN.stream().filter(documentText::contains).findAny().ifPresent(l 	-> extractedLanguages.add(Language.LANGUAGE.BULGARIAN));
			CROATIAN.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.CROATIAN));
			CZECH.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.CZECH));
			DANISH.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.DANISH));
			ESTONIAN.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.ESTONIAN));
			FINNISH.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.FINNISH));
			GERMAN.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.GERMAN));
			GREEK.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.GREEK));
			HUNGARIAN.stream().filter(documentText::contains).findAny().ifPresent(l 	-> extractedLanguages.add(Language.LANGUAGE.HUNGARIAN));
			HINDI.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.HINDI));
			ITALIAN.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.ITALIAN));
			LITHUANIAN.stream().filter(documentText::contains).findAny().ifPresent(l 	-> extractedLanguages.add(Language.LANGUAGE.LITHUANIAN));
			MALTESE.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.MALTESE));
			NORWEGEN.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.NORWEGEN));
			POLISH.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.POLISH));
			PORTUGUESE.stream().filter(documentText::contains).findAny().ifPresent(l 	-> extractedLanguages.add(Language.LANGUAGE.PORTUGUESE));
			ROMANIAN.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.ROMANIAN));
			RUSSIAN.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.RUSSIAN));
			SWEDISH.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.SWEDISH));
			SPANISH.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.SPANISH));
			SLOVAKIAN.stream().filter(documentText::contains).findAny().ifPresent(l 	-> extractedLanguages.add(Language.LANGUAGE.SLOVAKIAN));
			SLOVENIAN.stream().filter(documentText::contains).findAny().ifPresent(l 	-> extractedLanguages.add(Language.LANGUAGE.SLOVENIAN));
			TURKISH.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.TURKISH));
			UKRAINIAN.stream().filter(documentText::contains).findAny().ifPresent(l 	-> extractedLanguages.add(Language.LANGUAGE.UKRAINIAN));
			URDU.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedLanguages.add(Language.LANGUAGE.URDU));
			
			//boolean includeEN = ENGLISH.stream().filter(documentText::contains).count() 	> 0;
			//boolean includeNL = DUTCH.stream().filter(documentText::contains).count() 	> 0;
			//boolean includeFR = FRENCH.stream().filter(documentText::contains).count() 	> 0;
		
			
			
			if (!extractedLanguages.contains(Language.LANGUAGE.ENGLISH) && filterBuilder.build().getCountries().contains(COUNTRY.UK)) {
				extractedLanguages.add(Language.LANGUAGE.ENGLISH);
			}
			
			if (!extractedLanguages.contains(Language.LANGUAGE.ENGLISH)) {
				DocumentFilterExtractionUtil.UK.stream().filter(place -> documentText.contains(" "+place + " ") || documentText.contains(" "+place + ".")).findAny().ifPresent(l -> extractedLanguages.add(Language.LANGUAGE.ENGLISH));
			}
			
			filterBuilder.languages(extractedLanguages);
			
			
			//if (!includeEN && !includeNL && !includeFR) {
			//	return;
			//}
			
			//f (includeEN && includeNL && includeFR) {
			//	return;
			//}
			
			//filterBuilder.english(includeEN);
			//filterBuilder.dutch(includeNL);
			//filterBuilder.french(includeFR);
			
	}
}
