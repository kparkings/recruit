package com.arenella.recruit.candidates.extractors;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
	
	public static final Set<String> UK = Set.of("cambridge","bristol","gbp","stockport","£"," uk ", "uk.","uk\\t", "inside ir35", "milton keynes", "england", "midlands", "derby", "wales", "scotland", "edinburgh","glasgow", "london", "liverpool", "manchester", "oxford", "glousester", "surrey", "Buckinghamshire", "Berkshire", "hounslow", "Milton Keynes", "edgware", "Leicester", "bracknell", "barking", "Colchester", "cardiff", "Brentford", "Stoke-on-Trent", "maidenhead", "guildford", "reading", "leeds");
	
	/**
	* Extracts Filters from a document
	* @param fileType	- Type of document
	* @param fileBytes	- Document as bytes
	* @return			- Filters extracted from the document
	* @throws Exception
	*/
	public CandidateExtractedFilters extractFilters(FileType fileType, byte[] fileBytes) throws Exception{

		String documentText = getInstance(fileType).extract(fileBytes);
		
		return extractFilters(documentText);
	}
	
	/**
	* Extracts filters from a piece of text
	* @param text - text to extract filters from
	* @return extracted Filters
	*/
	public CandidateExtractedFilters extractFilters(String text) {
		
		CandidateExtractedFiltersBuilder filterBuilder = CandidateExtractedFilters.builder();
		
		extractFilters(text,  filterBuilder);
		
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
		
		return switch(fileType) {
			case pdf -> new PDFExtractor();
			case doc, docx -> new WordExtractor(); 
			default -> null;
		};
		
	}
	
	/**
	* Sets a formatted version of the extracted text to the filter
	* @param documentText - Original Text from the Document
	* @return formatted Text
	*/
	private void getExtractedText(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
		
		final String[] 					lines 				= documentText.split("\\n");
		final StringBuilder 			extractedText 		= new StringBuilder();
		final AtomicBoolean 			hitFirstParagraph 	= new AtomicBoolean(false);
		final AtomicReference<String> 	previousLine 		= new AtomicReference<>("");
		
		Arrays.asList(lines).forEach(line -> {
			
			if (hitFirstParagraph.get()) {
				
				if (!line.startsWith("•") && previousLine.get().startsWith("•")) {
					extractedText.append('\n').append('\n');
				}
				
				extractedText.append(line).append('\n');
				
				if (line.startsWith("•")) {
					//Do nothing special 
				} else if (line.startsWith("-") && line.endsWith(".")) {
					extractedText.append('\n');
				}
				else if (line.endsWith(".")) {
					extractedText.append('\n').append('\n');
				} else {
					extractedText.append('\n');
				}
				
			} else {
				if (line.split(" ").length > 10) {
					hitFirstParagraph.set(true);
					extractedText.append(line);
				}
			}
			
			previousLine.set(line);
			
		});
		
		String extractedTextStr = extractedText.toString();
		
		while(extractedTextStr.contains("\n\n\n")) {
			extractedTextStr = extractedTextStr.replace("\n\n\n", "\n\n");
		}
		
		filterBuilder.extractedText(extractedTextStr);
		
	}

	/**
	* Sets filters based upon the contents of the document
	* @param documentText 		- Text from document
	* @param filtersBuilder	  	- filters to be set
	*/
	private void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
		
		this.getExtractedText(documentText, filterBuilder);
		
		documentText = documentText.toLowerCase();
		
		jobTitleExtractor.extractFilters(documentText, filterBuilder);
		seniorityExtractor.extractFilters(documentText, filterBuilder);
		countryExtractor.extractFilters(documentText, filterBuilder);
		languageExtractor.extractFilters(documentText, filterBuilder);
		skillExtractor.extractFilters(documentText, filterBuilder);
		contractTypeExtractor.extractFilters(documentText, filterBuilder);	
		
	}
	
}