package com.arenella.recruit.candidates.extractors;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Factory for extracting filter information out of Files
* @author K Parkings
*/
@Component
public class DocumentFilterExtractionUtil {

	@Autowired
	private JobTitleExtractor 		jobTitleExtractor;
	
	@Autowired
	private SeniorityExtractor 		seniorityExtractor;
	
	@Autowired
	CountryExtractor 				countryExtractor;
	
	@Autowired
	private LanguageExtractor 		languageExtractor;
	
	@Autowired
	private SkillExtractor 			skillExtractor;
	
	@Autowired
	private ContractTypeExtractor 	contractTypeExtractor;
	
	public static Set<String> uk = Set.of(" uk ", "uk.","uk\\t", "england", "wales", "scotland", "edinburgh","glasgow", "london", "liverpool", "manchester", "oxford", "glousester", "surrey", "Buckinghamshire", "Berkshire", "hounslow", "Milton Keynes", "edgware", "Leicester", "bracknell", "barking", "Colchester", "cardiff", "Brentford", "Stoke-on-Trent", "maidenhead", "guildford", "reading", "leeds");
	
	public CandidateExtractedFilters extractFilters(FileType fileType, byte[] fileBytes) throws Exception{

		String documentText = getInstance(fileType).extract(fileBytes);
		
		CandidateExtractedFiltersBuilder filterBuilder = CandidateExtractedFilters.builder();
		
		extractFilters(documentText,  filterBuilder);
		
		return filterBuilder.build();
		
	}
	
	/**
	* Returns an implementation of the Extractor suitable for
	* the FileType of the Curriculum
	* @param fileType - FileType of the Curriculum
	* @return Extractor
	*/
	public DocumentFilterExtractor getInstance(FileType fileType) {
		
		if (fileType == null) {
			return null;
		}
		
		switch(fileType) {
			case pdf:{
				return new PDFExtractor();
			}
			case doc:
			case docx:{
				return new WordExtractor(); 
			}
		}
		
		return null;
	}

	/**
	* Sets filters based upon the contents of the document
	* @param documentText 		- Text from document
	* @param filtersBuilder	  	- filters to be set
	*/
	private void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
	
		documentText = documentText.toLowerCase();
		
		jobTitleExtractor.extractFilters(documentText, filterBuilder);
		seniorityExtractor.extractFilters(documentText, filterBuilder);
		countryExtractor.extractFilters(documentText, filterBuilder);
		languageExtractor.extractFilters(documentText, filterBuilder);
		skillExtractor.extractFilters(documentText, filterBuilder);
		contractTypeExtractor.extractFilters(documentText, filterBuilder);
		
		
	}
	

	
	
	
	
	
	
	
	

	
}