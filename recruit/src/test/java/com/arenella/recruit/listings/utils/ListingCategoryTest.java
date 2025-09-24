package com.arenella.recruit.listings.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.Listing;

/**
* Unit tests for the ListingCategory class 
* @author K Parkings
*/
class ListingCategoryTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	void testConstructor() {
		
		ListingCategory lc = new ListingCategory(Listing.TECH.JAVA, Set.of("java","j2ee"));
		
		assertEquals(Listing.TECH.JAVA, lc.getCategory());
		
		lc.getKeywords().stream().filter(kw -> kw.equals("java")).findAny().orElseThrow();
		lc.getKeywords().stream().filter(kw -> kw.equals("j2ee")).findAny().orElseThrow();
		
	}
	
}
