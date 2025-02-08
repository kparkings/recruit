package com.arenella.recruit.candidates.extractors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;

/**
* Unit tests for the LanguageExtractor class
* @author K Parkings
*/
public class LanguageExtractorTest {

	/**
	* Tests conditions to filter by English language
	* @throws Exception
	*/
	@Test
	public void testForEnglish() throws Exception {
		runTest(LanguageExtractor.ENGLISH, Language.LANGUAGE.ENGLISH);
	}
	
	/**
	* Tests conditions to filter by English language
	* @throws Exception
	*/
	@Test
	public void testForDutch() throws Exception {
		runTest(LanguageExtractor.DUTCH, Language.LANGUAGE.DUTCH);
	}
	
	/**
	* Tests conditions to filter by English language
	* @throws Exception
	*/
	@Test
	public void testForFrench() throws Exception {
		runTest(LanguageExtractor.FRENCH, Language.LANGUAGE.FRENCH);
	}
	
	/**
	* Tests if no language could be identified no filters for languages
	* will be set.
	* @throws Exception
	*/
	@Test
	public void testNoLanguagesIdentified() throws Exception{
		
		CandidateExtractedFiltersBuilder filterBuilder = CandidateExtractedFilters.builder();
		
		LanguageExtractor extractor = new LanguageExtractor();
		
		extractor.extractFilters("adadad adad", filterBuilder);
		
		//assertFalse(filterBuilder.build().getEnglish());
		//assertFalse(filterBuilder.build().getDutch());
		//assertFalse(filterBuilder.build().getFrench());
		
		assertFalse(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.ENGLISH));
		assertFalse(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.DUTCH));
		assertFalse(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.FRENCH));
		
	}
	
	/**
	* Where English place names are being used we assume the role is in the UK
	* and therefore the language will be English
	* @throws Exception
	*/
	@Test
	public void testBasedOnUKPlaceNames() throws Exception{
		
		CandidateExtractedFiltersBuilder filterBuilder = CandidateExtractedFilters.builder();
		
		LanguageExtractor extractor = new LanguageExtractor();
		
		extractor.extractFilters("adadad london adad", filterBuilder);
		
		assertTrue(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.ENGLISH));
		
		
	}
	
	/**
	* Tests two languages could be identified filters for those languages
	* will be set.
	* @throws Exception
	*/
	@Test
	public void testMultipleMatchesIdentified() throws Exception{
		
		CandidateExtractedFiltersBuilder filterBuilder = CandidateExtractedFilters.builder();
		
		LanguageExtractor extractor = new LanguageExtractor();
		
		extractor.extractFilters("adadad english french adad", filterBuilder);
		
		//assertTrue(filterBuilder.build().getEnglish());
		//assertFalse(filterBuilder.build().getDutch());
		//assertTrue(filterBuilder.build().getFrench());
		
		assertTrue(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.ENGLISH));
		assertFalse(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.DUTCH));
		assertTrue(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.FRENCH));
		
		filterBuilder = CandidateExtractedFilters.builder();
		
		extractor = new LanguageExtractor();
		
		extractor.extractFilters("adadad english dutch adad", filterBuilder);
		
		//assertTrue(filterBuilder.build().getEnglish());
		//assertTrue(filterBuilder.build().getDutch());
		//assertFalse(filterBuilder.build().getFrench());
		
		assertTrue(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.ENGLISH));
		assertTrue(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.DUTCH));
		assertFalse(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.FRENCH));
		
		filterBuilder = CandidateExtractedFilters.builder();
		
		extractor = new LanguageExtractor();
		
		extractor.extractFilters("adadad francais dutch adad", filterBuilder);
	
		assertFalse(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.ENGLISH));
		assertTrue(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.DUTCH));
		assertTrue(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.FRENCH));
		
		
	}
	
	/**
	* Tests if all languages could be identified no filters for languages
	* will be set.
	* @throws Exception
	*/
	@Test
	public void testAllLanguagesIdentified() throws Exception{
		
		CandidateExtractedFiltersBuilder filterBuilder = CandidateExtractedFilters.builder();
		
		LanguageExtractor extractor = new LanguageExtractor();
		
		extractor.extractFilters("adadad english dutch french adad", filterBuilder);
		
		assertTrue(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.ENGLISH));
		assertTrue(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.DUTCH));
		assertTrue(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.FRENCH));
		
	}

	/**
	* Utility method to check if correct language is being identified
	* @param words - Words to use to generate test job specification text
	* @param lang  - Expected langy
	*/
	private void runTest(Set<String> words, Language.LANGUAGE lang) {
		
		words.stream().forEach(word -> {
			
			CandidateExtractedFiltersBuilder filterBuilder = CandidateExtractedFilters.builder();
			
			LanguageExtractor extractor = new LanguageExtractor();
			
			extractor.extractFilters("adadad \n " + word + " adad", filterBuilder);
			
			assertTrue(filterBuilder.build().getLanguages().contains(lang));
			
			//switch(lang) {
			//	case "EN":{
			//		assertTrue(filterBuilder.build().getEnglish());
		//			break;
		//		}
		//		case "NL":{
		//			assertTrue(filterBuilder.build().getDutch());
		//			break;
		//		}
		//		case "FR":{
		//			assertTrue(filterBuilder.build().getFrench());
		//			break;
		//		}
		//	}
		//	
		});
		
	}
	
	/**
	* If Country has previously been identified as the UK makes assumption that 
	* English language is a requirement
	* @throws Exception
	*/
	@Test
	public void testIfCountryEnglandIncludEnglishLang() throws Exception{
		
		CandidateExtractedFiltersBuilder filterBuilder = CandidateExtractedFilters.builder();
		
		filterBuilder.uk(false);
		
		LanguageExtractor extractor = new LanguageExtractor();
		
		extractor.extractFilters("", filterBuilder);
		
		assertFalse(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.ENGLISH));
		
		filterBuilder.uk(true);
		
		extractor.extractFilters("", filterBuilder);
		
		assertTrue(filterBuilder.build().getLanguages().contains(Language.LANGUAGE.ENGLISH));
		
	}
	
}