package com.arenella.recruit.campaign.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.CampaignListingRegistration;

/**
* Unit tests for the CampaignListingRegistrationEntity class
*/
class CampaignListingRegistrationEntityTest {
	
	private final UUID 		LISTING_ID 	= UUID.randomUUID();
	private final String 	OWNER_ID 	= "rec1";
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testConstruction() {
		
		CampaignListingRegistrationEntity entity = 
				CampaignListingRegistrationEntity
				.builder()
					.listingId(LISTING_ID)
					.listingOwnerId(OWNER_ID)
				.build();
		
		assertEquals(LISTING_ID, entity.getListingId());
		assertEquals(OWNER_ID, entity.getListingOwnerId());
		
	}
	
	/**
	* Tests conversion of Entity to Domain representation
	*/
	@Test 
	void testFromEntity() {
		
		CampaignListingRegistrationEntity entity = 
				CampaignListingRegistrationEntity
				.builder()
					.listingId(LISTING_ID)
					.listingOwnerId(OWNER_ID)
				.build();
		
		assertEquals(LISTING_ID, entity.getListingId());
		assertEquals(OWNER_ID, 	 entity.getListingOwnerId());
		
		CampaignListingRegistration registration = CampaignListingRegistrationEntity.fromEntity(entity);
		
		assertEquals(LISTING_ID, registration.listingId());
		assertEquals(OWNER_ID, 	 registration.listingOwnerId());
		
	}
	
	/**
	* Tests conversion of Domain to Entity representation
	*/
	@Test 
	void testToEntity() {
		
		CampaignListingRegistration registration = new CampaignListingRegistration(LISTING_ID, OWNER_ID);
		
		assertEquals(LISTING_ID, registration.listingId());
		assertEquals(OWNER_ID, 	 registration.listingOwnerId());
		
		CampaignListingRegistrationEntity entity = CampaignListingRegistrationEntity.toEntity(registration);
		
		assertEquals(LISTING_ID, entity.getListingId());
		assertEquals(OWNER_ID, 	 entity.getListingOwnerId());
		
	}

}
