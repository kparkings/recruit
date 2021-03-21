package com.arenella.recruit.candidate.beans;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.arenella.recruit.beans.Language;
import com.arenella.recruit.beans.Language.LANGUAGE;
import com.arenella.recruit.beans.Language.LEVEL;

/**
* Unit tests for the Language class 
* @author K Parkings
*/
public class LanguageTest {

	/**
	* Test that the Instance if populated with the values set in the Builder
	* @throws Exception
	*/
	public void testInitializationWithBuilder() throws Exception{
		
		final LANGUAGE 	lang 	= LANGUAGE.FRENCH;
		final LEVEL 	level 	= LEVEL.PROFICIENT;
		
		Language language = Language.builder().language(lang).level(level).build();
		
		assertEquals(language.getLanguage(), 	lang);
		assertEquals(language.getLevel(), 		level);
		
	}
	
	/**
	* Tests Exception is thrown if mandatory Language value has not 
	* been provided to the Builder when the instance is built.
	* @throws Exception
	*/
	@Test(expected=IllegalStateException.class)
	public void testExceptionIfLanguageMissingInBuilder() throws Exception{
		Language.builder().level(LEVEL.BASIC).build();
	}
	
	/**
	* Tests Exception is thrown if mandatory Level value has not 
	* been provided to the Builder when the instance is built.
	* @throws Exception
	*/
	@Test(expected=IllegalStateException.class)
	public void testExceptionIfLevelMissingInBuilder() throws Exception{
		Language.builder().language(LANGUAGE.DUTCH).build();
	}
}
