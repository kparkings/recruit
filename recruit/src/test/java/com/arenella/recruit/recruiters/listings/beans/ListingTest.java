package com.arenella.recruit.recruiters.listings.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.Listing.country;
import com.arenella.recruit.listings.beans.Listing.currency;
import com.arenella.recruit.listings.beans.Listing.language;
import com.arenella.recruit.listings.beans.Listing.listing_type;

/**
* Unit tests for the Listing class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ListingTest {

	private final UUID 				listingId			= UUID.randomUUID();
	private final String			ownerId				= "kparking";
	private final LocalDate 		created				= LocalDate.of(2021, 11, 24);
	private final String 			title				= "aTitle";
	private final String 			description			= "aDesc";
	private final listing_type 		type		 		= listing_type.CONTRACT_ROLE;
	private final country 			country				= Listing.country.NETHERLANDS;
	private final String 			location			= "Den Haag";
	private final int 				yearsExperience		= 10;
	private final Set<language> 	languages			= new LinkedHashSet<>();
	private final float 			rate				= 115.0f;
	private final currency			currency			= Listing.currency.EUR;
	
	@BeforeEach
	public void init() {
		this.languages.add(language.DUTCH);
		this.languages.add(language.FRENCH);
	}
	
	/**
	* Tests instantiation of Instance of class via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		Listing listing = Listing
							.builder()
								.country(country)
								.created(created)
								.currency(currency)
								.description(description)
								.languages(languages)
								.listingId(listingId)
								.location(location)
								.ownerId(ownerId)
								.rate(rate)
								.title(title)
								.type(type)
								.yearsExperience(yearsExperience)
							.build();
		
		assertEquals(country, 			listing.getCountry());
		assertEquals(created, 			listing.getCreated());
		assertEquals(currency, 			listing.getCurrency());
		assertEquals(description, 		listing.getDescription());
		assertEquals(listingId, 		listing.getListingId());
		assertEquals(location, 			listing.getLocation());
		assertEquals(ownerId, 			listing.getOwnerId());
		assertEquals(rate, 				listing.getRate(), 0);
		assertEquals(title,	 			listing.getTitle());
		assertEquals(type, 				listing.getType());
		assertEquals(yearsExperience, 	listing.getYearsExperience());
		
		assertTrue(listing.getLanguages().contains(Listing.language.DUTCH));
		assertTrue(listing.getLanguages().contains(Listing.language.FRENCH));
		assertEquals(listing.getLanguages().size(), 2);
		
		
	}
	
	/**
	* Tests instantiation of Instance of class via the Builder with default values.
	* Expect any Collections to be instantiated but empty
	* @throws Exception
	*/
	@Test
	public void testBuilder_defaults() throws Exception {
		
		Listing listing = Listing.builder().build();
		
		assertTrue(listing.getLanguages().isEmpty());
		
	}
	
}
