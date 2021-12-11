package com.arenella.recruit.recruiters.listings.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
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
import com.arenella.recruit.listings.dao.ListingEntity;

/**
* Unit tests for the ListingEntity class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ListingEntityTest {

	private static final 	String 			SKILL_JAVA 			= "java";
	private static final 	String 			SKILL_CSHARP 		= "c#";
	
	private final 			UUID 			listingId			= UUID.randomUUID();
	private final 			String			ownerId				= "kparking";
	private final 			String			ownerName			= "Kevin Parkings";
	private final 			String 			ownerCompany		= "Arenella BV";
	private final 			String			ownerEmail			= "kparkings@gmail.com";
	private final 			LocalDateTime	created				= LocalDateTime.of(2021, 11, 24, 00, 10, 01);
	private final 			String 			title				= "aTitle";
	private final 			String 			description			= "aDesc";
	private final 			listing_type 	type		 		= listing_type.CONTRACT_ROLE;
	private final 			country 		country				= Listing.country.NETHERLANDS;
	private final 			String 			location			= "Den Haag";
	private final 			int 			yearsExperience		= 10;
	private final 			Set<language> 	languages			= new LinkedHashSet<>();
	private final 			Set<String>		skills			 	= Set.of(SKILL_JAVA, SKILL_CSHARP);
	private final 			float 			rate				= 115.0f;
	private final 			currency		currency			= Listing.currency.EUR;
	private final 			int				views				= 10;
	
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
		
		ListingEntity listing = ListingEntity
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
		assertEquals(views, 			listing.getViews());
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
		
		ListingEntity listing = ListingEntity.builder().build();
		
		assertTrue(listing.getLanguages().isEmpty());
		assertTrue(listing.getSkills().isEmpty());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation of Listing 
	*/
	@Test
	public void testConvertFromEntity() {
		
		ListingEntity listingEntity = ListingEntity
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
				.build();
		
		Listing listing = ListingEntity.convertFromEntity(listingEntity);
		
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
		assertEquals(views, 			listing.getViews());
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
	* Tests conversion from Domain to Entity representation of the 
	* Listing where an Entity for the Listing already exists
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity_existingEntity() throws Exception {
		
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
				.build();
		
			ListingEntity existingEntity	= ListingEntity.builder().listingId(listingId).build();
			ListingEntity entity 			= ListingEntity.convertToEntity(listing, Optional.of(existingEntity));

			assertTrue(entity == existingEntity);
			
			assertEquals(country, 						entity.getCountry());
			assertEquals(existingEntity.getCreated(), 	entity.getCreated());
			assertEquals(currency, 						entity.getCurrency());
			assertEquals(description, 					entity.getDescription());
			assertEquals(listingId, 					entity.getListingId());
			assertEquals(location, 						entity.getLocation());
			assertEquals(existingEntity.getOwnerId(), 	entity.getOwnerId());
			assertEquals(rate, 							entity.getRate(), 0);
			assertEquals(title,	 						entity.getTitle());
			assertEquals(type, 							entity.getType());
			assertEquals(yearsExperience, 				entity.getYearsExperience());
			assertEquals(views, 						entity.getViews());
			assertEquals(ownerName, 					entity.getOwnerName());
			assertEquals(ownerCompany, 					entity.getOwnerCompany());
			assertEquals(ownerEmail, 					entity.getOwnerEmail());
			
			assertTrue(entity.getLanguages().contains(Listing.language.DUTCH));
			assertTrue(entity.getLanguages().contains(Listing.language.FRENCH));
			assertEquals(entity.getLanguages().size(), 2);
			
			assertTrue(entity.getSkills().contains(SKILL_JAVA));
			assertTrue(entity.getSkills().contains(SKILL_CSHARP));
			assertEquals(entity.getSkills().size(), 2);

	}
	
	/**
	* Tests conversion from Domain to Entity representation of the 
	* Listing where an Entity for the Listing does not already exists
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity_noExistingEntity() throws Exception {
		
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
				.build();
		
			ListingEntity entity 			= ListingEntity.convertToEntity(listing, Optional.empty());
	
			assertEquals(country, 			entity.getCountry());
			assertEquals(created, 			entity.getCreated());
			assertEquals(currency, 			entity.getCurrency());
			assertEquals(description, 		entity.getDescription());
			assertEquals(listingId, 		entity.getListingId());
			assertEquals(location, 			entity.getLocation());
			assertEquals(ownerId, 			entity.getOwnerId());
			assertEquals(rate, 				entity.getRate(), 0);
			assertEquals(title,	 			entity.getTitle());
			assertEquals(type, 				entity.getType());
			assertEquals(yearsExperience, 	entity.getYearsExperience());
			assertEquals(views, 			entity.getViews());
			assertEquals(ownerName, 		entity.getOwnerName());
			assertEquals(ownerCompany, 		entity.getOwnerCompany());
			assertEquals(ownerEmail, 		entity.getOwnerEmail());
			
			assertTrue(entity.getLanguages().contains(Listing.language.DUTCH));
			assertTrue(entity.getLanguages().contains(Listing.language.FRENCH));
			assertEquals(entity.getLanguages().size(), 2);
			
			assertTrue(entity.getSkills().contains(SKILL_JAVA));
			assertTrue(entity.getSkills().contains(SKILL_CSHARP));
			assertEquals(entity.getSkills().size(), 2);
			
	}
	
}