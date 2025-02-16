package com.arenella.recruit.listings.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.Listing.listing_type;

/**
* Unit tests for the ListingAlertFilterOptions class
* @author K Parkings
*/
public class ListingAlertFilterOptionsTest {

	public static String 					SEARCH_TERM 	= "java";
	public static Listing.listing_type 		CONTRACT_TYPE 	= Listing.listing_type.CONTRACT_ROLE;
	public static Set<Listing.Country> 		COUNTRIES		= Set.of(Listing.Country.NETHERLANDS);
	public static  Set<Listing.TECH> 		CATEGORIES	 	= Set.of(Listing.TECH.ARCHITECT);
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		ListingAlertFilterOptions filters = 
				ListingAlertFilterOptions.builder()
					.categories(CATEGORIES)
					.contractType(CONTRACT_TYPE)
					.countries(COUNTRIES)
					.searchTerm(SEARCH_TERM)
				.build();
		
		assertEquals(SEARCH_TERM, filters.getSearchTerm().get());
		assertEquals(CONTRACT_TYPE, filters.getContractType().get());
		
		filters.getCountries().stream().filter(c  -> c == Listing.Country.NETHERLANDS).findAny().orElseThrow();
		filters.getCategories().stream().filter(c -> c == Listing.TECH.ARCHITECT).findAny().orElseThrow();
	}
	
	/**
	* Test defensive programming techniques are present
	* @throws Exception
	*/
	@Test
	public void testDefaults() throws Exception{
		ListingAlertFilterOptions filters = 
				ListingAlertFilterOptions.builder().build();
		
		assertTrue(filters.getSearchTerm().isEmpty());
		assertTrue(filters.getContractType().isEmpty());
		assertTrue(filters.getCountries().isEmpty());
		assertTrue(filters.getCategories().isEmpty());
		
	}
	
	/**
	* Tests if the Both option is selected it is replaced with null as in this case we want 
	* all types of Contract to be included in the results
	* @throws Exception
	*/
	@Test
	public void testContractTypeBoth() throws Exception{
		
		ListingAlertFilterOptions filters = 
				ListingAlertFilterOptions
				.builder()
					.contractType(listing_type.BOTH)
				.build();
		
		assertTrue(filters.getContractType().isEmpty());
		
	}
}
