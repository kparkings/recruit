package com.arenella.recruit.listings.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the ListingFunctionSynonymUtil class
*/
class ListingFunctionSynonymUtilTest {

	ListingFunctionSynonymUtil util = new ListingFunctionSynonymUtil();
	
	/**
	* Tests the original search term is included 
	*/
	@Test
	void testExtractAllFunctionAndSynonymsContainsOriginalSearchTerm() {
		
		final String searchTerm = " Java DEVeloPer ";
		
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("JAVA DEVELOPER"));
		assertEquals(4,ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).size());
	}
	
	/**
	* Tests the original search term is included when no types match
	*/
	@Test
	void testExtractAllFunctionAndSynonymsContainsOriginalSearchTermNoTypeMatch() {
		
		final String searchTerm = " booP ";
		
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("BOOP"));
	
		assertEquals(1,ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).size());
		
	}
	
	/**
	* Tests synonyms added for Java 
	*/
	@Test
	void testExtractAllFunctionAndSynonymsSingleTypeMatch() {
		
		final String searchTerm = " Java DEVeloPer ";
		
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("JAVA DEVELOPER"));
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("JAVA"));
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("J2EE"));
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("SPRING"));
		assertEquals(4,ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).size());
		
	}
	
	/**
	* Tests synonyms added when multiple types match 
	*/
	@Test
	void testExtractAllFunctionAndSynonymsMultipleTypeMatch() {
		
		final String searchTerm = " Java or C# DEVeloPer ";
		
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("JAVA OR C# DEVELOPER"));
		
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("JAVA"));
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("J2EE"));
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("SPRING"));
		
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("C#"));
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("DOTNETT"));
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("DOT NET"));
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains(".NET"));
		assertTrue(ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).contains("WPF"));
		
		assertEquals(9,ListingFunctionSynonymUtil.extractAllFunctionAndSynonyms(searchTerm).size());
		
	}
	
}
