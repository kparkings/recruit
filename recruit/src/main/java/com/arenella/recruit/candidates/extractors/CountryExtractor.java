package com.arenella.recruit.candidates.extractors;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;
import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* Extractor to determine countries to search in
* @author K Parkings
*/
@Component
public class CountryExtractor implements JobSpecifcationFilterExtractor{
	
	public static final Set<String> UK 					= DocumentFilterExtractionUtil.UK;
	public static final Set<String> NETHERLANDS 		= Set.of("'s-gravenhage","groningen","brabant","zuid-holland","arnhem","nieuwegein","schiphol", "zwolle","netherlands","nederland", "amsterdam", "apeldoorn", "utrecht", "rotterdam", "randstad", "amstelveen", "woerden", "amersfoort", "soest", "den haag", "the hague", "overijssel", "gelderland", "almere", "eindhoven", "enschede", "limburg", "flevoland", "alkmaar");
	public static final Set<String> BELGIUM 			= Set.of("belgium", "brussels", "leuven","mechelen", "bruxelles", "gand", "courtrai", "antwerp", "antwerpen", "flemish", "flams", "wallon", "Mechelen", "Liege", " gent", "Charleoi", "Meeuwen","Kortrijk", "Namur"); 
	public static final Set<String> LUXEMBOURG 			= Set.of("luxembourg");
	public static final Set<String> REPUBLIC_OF_IRELAND = Set.of("ireland", "dublin", "galway", "letterkenny", "limerick", "kildare");
	public static final Set<String> NORTHERN_IRELAND 	= Set.of("ireland");
	public static final Set<String> GERMANY 			= Set.of("germany");
	public static final Set<String> POLAND 				= Set.of("poland");
	public static final Set<String> FRANCE 				= Set.of("france");
	public static final Set<String> SPAIN 				= Set.of("spain");
	public static final Set<String> ITALY 				= Set.of("italy");
	public static final Set<String> PORTUGAL 			= Set.of("portugal");
	public static final Set<String> AUSTRIA 			= Set.of("austria");
	public static final Set<String> ROMANIA 			= Set.of("romania");
	public static final Set<String> GREECE 				= Set.of("greece");
	public static final Set<String> UKRAINE 			= Set.of("ukraine","ucraine");
	public static final Set<String> SWITZERLAND 		= Set.of("zwitzerland");
	public static final Set<String> CZECH_REPUBLIC 		= Set.of("czech republic");
	public static final Set<String> SWEDEN 				= Set.of("sweden");
	public static final Set<String> HUNGARY 			= Set.of("hungary");
	public static final Set<String> BULGARIA 			= Set.of("bulgaria");
	public static final Set<String> DENMARK 			= Set.of("denmark");
	public static final Set<String> FINLAND 			= Set.of("finland");
	public static final Set<String> SLOVAKIA	 		= Set.of("slovakia");
	public static final Set<String> CROATIA 			= Set.of("croatia");
	public static final Set<String> LITHUANIA 			= Set.of("lithuania");
	public static final Set<String> SLOVENIA 			= Set.of("slovenia");
	public static final Set<String> LATVIA 				= Set.of("latvia");
	public static final Set<String> ESTONIA 			= Set.of("estonia");
	public static final Set<String> CYPRUS 				= Set.of("cyprus");
	public static final Set<String> MALTA 				= Set.of("malta");
	public static final Set<String> NORWAY 				= Set.of("norway");
	public static final Set<String> LIECHTENSTEIN 		= Set.of("liechtenstein");
	public static final Set<String> TURKEY 				= Set.of("turkey");
	public static final Set<String> INDIA 				= Set.of("india");
	public static final Set<String> PAKISTAN 			= Set.of("pakistan");
	public static final Set<String> US 					= Set.of("united states", "usa");
	public static final Set<String> CANADA 				= Set.of("canada");
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
			
		Set<COUNTRY> extractedCountries = new HashSet<>();
		
		UK.stream().filter(documentText::contains).findAny().ifPresent(l 					-> extractedCountries.add(COUNTRY.UK));
		NETHERLANDS.stream().filter(documentText::contains).findAny().ifPresent(l 			-> extractedCountries.add(COUNTRY.NETHERLANDS));
		BELGIUM.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.BELGIUM)); 
		LUXEMBOURG.stream().filter(documentText::contains).findAny().ifPresent(l 			-> extractedCountries.add(COUNTRY.LUXEMBOURG));
		REPUBLIC_OF_IRELAND.stream().filter(documentText::contains).findAny().ifPresent(l 	-> extractedCountries.add(COUNTRY.REPUBLIC_OF_IRELAND));
		NORTHERN_IRELAND.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedCountries.add(COUNTRY.NORTHERN_IRELAND));
		GERMANY.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.GERMANY));
		POLAND.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.POLAND));
		FRANCE.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.FRANCE));
		SPAIN.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.SPAIN));
		ITALY.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.ITALY));
		PORTUGAL.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.PORTUGAL));
		AUSTRIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.AUSTRIA));
		ROMANIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.ROMANIA));
		GREECE.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.GREECE));
		UKRAINE.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.UKRAINE));
		SWITZERLAND.stream().filter(documentText::contains).findAny().ifPresent(l 			-> extractedCountries.add(COUNTRY.SWITZERLAND));
		CZECH_REPUBLIC.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedCountries.add(COUNTRY.CZECH_REPUBLIC));
		SWEDEN.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.SWEDEN));
		HUNGARY.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.HUNGARY));
		BULGARIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.BULGARIA));
		DENMARK.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.DENMARK));
		FINLAND.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.FINLAND));
		SLOVAKIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.SLOVAKIA));
		CROATIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.CROATIA));
		LITHUANIA.stream().filter(documentText::contains).findAny().ifPresent(l 			-> extractedCountries.add(COUNTRY.LITHUANIA));
		SLOVENIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.SLOVENIA));
		LATVIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.LATVIA));
		ESTONIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.ESTONIA));
		CYPRUS.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.CYPRUS));
		MALTA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.MALTA));
		NORWAY.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.NORWAY));
		LIECHTENSTEIN.stream().filter(documentText::contains).findAny().ifPresent(l 		-> extractedCountries.add(COUNTRY.LIECHTENSTEIN));
		TURKEY.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.TURKEY));
		INDIA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.INDIA));
		PAKISTAN.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.PAKISTAN));
		US.stream().filter(documentText::contains).findAny().ifPresent(l 					-> extractedCountries.add(COUNTRY.US));
		CANADA.stream().filter(documentText::contains).findAny().ifPresent(l 				-> extractedCountries.add(COUNTRY.CANADA));
		
			//boolean includeNL = nl.stream().filter(documentText::contains).count() > 0;
			//boolean includeIE = ie.stream().filter(documentText::contains).count() > 0;
			//boolean includeBE = be.stream().filter(documentText::contains).count() > 0;
			//boolean includeUK = DocumentFilterExtractionUtil.UK.stream().filter(documentText::contains).count() > 0;
		
			//if (!includeNL && !includeIE && !includeBE && !includeUK) {
			//	return;
			//}
			
			//if (includeNL && includeIE && includeBE && includeUK) {
			//	return;
			//}
			
			//filterBuilder.netherlands(includeNL);
			//filterBuilder.belgium(includeBE);
			//filterBuilder.uk(includeUK);
			//filterBuilder.ireland(includeIE);
			filterBuilder.countries(extractedCountries);
			
	}
}