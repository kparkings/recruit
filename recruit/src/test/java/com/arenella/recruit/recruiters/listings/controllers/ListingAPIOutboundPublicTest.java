package com.arenella.recruit.recruiters.listings.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.beans.Listing.country;
import com.arenella.recruit.listings.beans.Listing.currency;
import com.arenella.recruit.listings.beans.Listing.language;
import com.arenella.recruit.listings.beans.Listing.listing_type;
import com.arenella.recruit.listings.controllers.ListingAPIOutboundPublic;

/**
* Unit tests for the ListingAPIOutbound class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ListingAPIOutboundPublicTest {

	private static final 	String 			SKILL_JAVA 			= "java";
	private static final 	String 			SKILL_CSHARP 		= "c#";
	
	private final 			UUID 						listingId			= UUID.randomUUID();
	private final 			String						ownerId				= "kparking";
	private final 			LocalDateTime				created				= LocalDateTime.of(2021, 11, 24, 10, 01, 01);
	private final 			String 						title				= "aTitle";
	private final 			String 						description			= "aDesc";
	private final 			listing_type 				type		 		= listing_type.CONTRACT_ROLE;
	private final 			country 					country				= Listing.country.NETHERLANDS;
	private final 			String 						location			= "Den Haag";
	private final 			int 						yearsExperience		= 10;
	private final 			Set<language> 				languages			= new LinkedHashSet<>();
	private final 			Set<String>					skills			 	= Set.of(SKILL_JAVA, SKILL_CSHARP);
	private final 			String 						rate				= "115.00";
	private final 			currency					currency			= Listing.currency.EUR;
	private final 			Set<ListingViewedEvent>		views				= Set.of(ListingViewedEvent.builder().build());
	private final 			String						ownerName			= "Kevin Parkings";
	private final 			String 						ownerCompany		= "Arenella BV";
	private final 			String						ownerEmail			= "kparkings@gmail.com";
	
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
		
		ListingAPIOutboundPublic listing = ListingAPIOutboundPublic
													.builder()
														.country(country)
														.created(created)
														.currency(currency)
														.description(description)
														.languages(languages)
														.skills(skills)
														.listingId(listingId)
														.location(location)
														.ownerName(ownerName)
														.ownerCompany(ownerCompany)
														.ownerEmail(ownerEmail)
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
		assertEquals(rate, 				listing.getRate());
		assertEquals(title,	 			listing.getTitle());
		assertEquals(type, 				listing.getType());
		assertEquals(yearsExperience, 	listing.getYearsExperience());
		assertEquals(ownerName, 		listing.getOwnerName());
		assertEquals(ownerCompany, 		listing.getOwnerCompany());
		assertEquals(ownerEmail, 		listing.getOwnerEmail());
		
		assertTrue(listing.getLanguages().contains(Listing.language.DUTCH));
		assertTrue(listing.getLanguages().contains(Listing.language.FRENCH));
		assertEquals(listing.getLanguages().size(), 2);
		
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
		
		ListingAPIOutboundPublic listing = ListingAPIOutboundPublic.builder().build();
		
		assertTrue(listing.getLanguages().isEmpty());
		assertTrue(listing.getSkills().isEmpty());
		
	}
	
	/**
	* Tests conversion from Domain version of Listing to API 
	* Outbound representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromListing() throws Exception {
	
		Listing listing = Listing
				.builder()
					.country(country)
					.created(created)
					.currency(currency)
					.description(description)
					.languages(languages)
					.listingId(listingId)
					.skills(skills)
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
				.build();
		
		ListingAPIOutboundPublic outbound = ListingAPIOutboundPublic.convertFromListing(listing);
		
		assertEquals(country, 			outbound.getCountry());
		assertEquals(created, 			outbound.getCreated());
		assertEquals(currency, 			outbound.getCurrency());
		assertEquals(description, 		outbound.getDescription());
		assertEquals(listingId, 		outbound.getListingId());
		assertEquals(location, 			outbound.getLocation());
		assertEquals(rate, 				outbound.getRate());
		assertEquals(title,	 			outbound.getTitle());
		assertEquals(type, 				outbound.getType());
		assertEquals(yearsExperience, 	outbound.getYearsExperience());
		assertEquals(ownerName, 		outbound.getOwnerName());
		assertEquals(ownerCompany, 		outbound.getOwnerCompany());
		assertEquals(ownerEmail, 		outbound.getOwnerEmail());
		
		assertTrue(outbound.getLanguages().contains(Listing.language.DUTCH));
		assertTrue(outbound.getLanguages().contains(Listing.language.FRENCH));
		assertEquals(outbound.getLanguages().size(), 2);
		
		assertTrue(outbound.getSkills().contains(SKILL_JAVA));
		assertTrue(outbound.getSkills().contains(SKILL_CSHARP));
		assertEquals(outbound.getSkills().size(), 2);
		
	}
		
}