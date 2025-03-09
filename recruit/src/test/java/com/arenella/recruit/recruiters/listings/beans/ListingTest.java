package com.arenella.recruit.recruiters.listings.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.Listing.Country;
import com.arenella.recruit.listings.beans.Listing.currency;
import com.arenella.recruit.listings.beans.Listing.language;
import com.arenella.recruit.listings.beans.Listing.listing_type;
import com.arenella.recruit.listings.beans.ListingViewedEvent;

/**
* Unit tests for the Listing class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ListingTest {

	private static final 	String 			SKILL_JAVA 			= "java";
	private static final 	String 			SKILL_CSHARP 		= "c#";
	
	private final 			UUID 						listingId			= UUID.randomUUID();
	private final 			String						ownerId				= "kparking";
	private final 			String						ownerName			= "Kevin Parkings";
	private final 			String 						ownerCompany		= "Arenella BV";
	private final 			String						ownerEmail			= "admin@arenella-ict.com";
	private final 			LocalDateTime				created				= LocalDateTime.of(2021, 11, 24, 00 , 00 ,00);
	private final 			String 						title				= "aTitle";
	private final 			String 						description			= "aDesc";
	private final 			listing_type 				type		 		= listing_type.CONTRACT_ROLE;
	private final 			Country 					country				= Listing.Country.NETHERLANDS;
	private final 			String 						location			= "Den Haag";
	private final 			int 						yearsExperience		= 10;
	private final 			Set<language> 				languages			= new LinkedHashSet<>();
	private final 			Set<String>					skills			 	= Set.of(SKILL_JAVA, SKILL_CSHARP);
	private final 			String 						rate				= "115.00";
	private final 			currency					currency			= Listing.currency.EUR;
	private final 			Set<ListingViewedEvent>		views				= Set.of(ListingViewedEvent.builder().build());
	private final 			boolean						active				= true;
	
	/**
	* Sets up test environment 
	*/
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
								.skills(skills)
								.listingId(listingId)
								.location(location)
								.ownerId(ownerId)
								.ownerName(ownerName)
								.ownerCompany(ownerCompany)
								.ownerEmail(ownerEmail)
								.rate(rate)
								.title(title)
								.type(type)
								.yearsExperience(yearsExperience)
								.views(views)
								.active(active)
							.build();
		
		assertEquals(country, 			listing.getCountry());
		assertEquals(created, 			listing.getCreated());
		assertEquals(currency, 			listing.getCurrency());
		assertEquals(description, 		listing.getDescription());
		assertEquals(listingId, 		listing.getListingId());
		assertEquals(location, 			listing.getLocation());
		assertEquals(ownerId, 			listing.getOwnerId());
		assertEquals(rate, 				listing.getRate());
		assertEquals(title,	 			listing.getTitle());
		assertEquals(type, 				listing.getType());
		assertEquals(yearsExperience, 	listing.getYearsExperience());
		assertEquals(views, 			listing.getViews());
		assertEquals(ownerName, 		listing.getOwnerName());
		assertEquals(ownerCompany, 		listing.getOwnerCompany());
		assertEquals(ownerEmail, 		listing.getOwnerEmail());
		
		assertTrue(listing.getLanguages().contains(Listing.language.DUTCH));
		assertTrue(listing.getLanguages().contains(Listing.language.FRENCH));
		assertEquals(listing.getLanguages().size(), 2);
		assertTrue(listing.isActive());
		
		assertTrue(listing.getSkills().contains(SKILL_JAVA));
		assertTrue(listing.getSkills().contains(SKILL_CSHARP));
		assertEquals(listing.getSkills().size(), 2);
		
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
		assertTrue(listing.getSkills().isEmpty());
		
	}
	
	/**
	* Tests Setter methods set values correctly
	* @throws Exception
	*/
	@Test
	public void testSetters() throws Exception {
		
		final LocalDateTime updatedCreated = LocalDateTime.of(2025, 3, 9, 19, 11);
		
		Listing listing = Listing.builder().build();
		
		assertNull(listing.getListingId());
		assertNull(listing.getOwnerId());		
		assertNull(listing.getCreated());
		
		listing.initializeAsNewListing();
		listing.setOwnerId(ownerId);
		listing.setActive(false);
		listing.setListingId(listingId);
		
		assertEquals(ownerId, 				listing.getOwnerId());
		assertEquals(listingId, 			listing.getListingId());
		assertTrue(listing.getListingId() 	instanceof UUID);
		assertTrue(listing.getCreated() 	instanceof LocalDateTime);
		assertFalse(listing.isActive());
		
		listing.setCreated(updatedCreated);
		
		assertEquals(updatedCreated, listing.getCreated());
	}
	
	/**
	* Tests adding view events
	*/
	@Test
	void testAddView() {
		
		Listing listing = Listing.builder().build();
		
		assertTrue(listing.getViews().isEmpty());
		
		listing.addView(ListingViewedEvent.builder().build());
		listing.addView(ListingViewedEvent.builder().build());
		
		assertEquals(2, listing.getViews().size());
		
	}
		
}