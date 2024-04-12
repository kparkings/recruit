package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Language;

/**
* Unit tests for the LanguageDocument class
* @author K Parkings
*/
public class LanguageDocumentTest {

	private static final Language.LANGUAGE	LANGUAGE	= Language.LANGUAGE.ITALIAN;
	private static final Language.LEVEL		LEVEL		= Language.LEVEL.PROFICIENT;
	
	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConsturction() throws Exception{
		
		LanguageDocument doc = new LanguageDocument(LANGUAGE, LEVEL);
		
		assertEquals(LANGUAGE, 	doc.getLanguage());
		assertEquals(LEVEL, 	doc.getLevel());
		
	}
	
	/**
	* Tests conversion from Domain to Persistence representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromDomain() throws Exception{
		
		Language 			language 	= Language.builder().language(LANGUAGE).level(LEVEL).build();
		LanguageDocument 	doc 		= LanguageDocument.convertFromDomain(language);
		
		assertEquals(LANGUAGE, 	doc.getLanguage());
		assertEquals(LEVEL, 	doc.getLevel());
		
	}
	
	/**
	* Tests conversion from Persistence to Domain representation
	* @throws Exception
	*/
	@Test
	public void testConvertToDomain() throws Exception{
		
		LanguageDocument 	doc 		= new LanguageDocument(LANGUAGE, LEVEL);
		Language 			language 	= LanguageDocument.convertToDomain(doc);
		
		assertEquals(LANGUAGE, 	language.getLanguage());
		assertEquals(LEVEL, 	language.getLevel());
		
	}
	
}
