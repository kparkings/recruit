package com.arenella.recruit.campaign.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.CampaignListing;
import com.arenella.recruit.campaigns.entities.CampaignListingEntity;

/**
* Unit tests for the CampaignListingEntity class 
*/
class CampaignListingEntityTest {

	private static final UUID ID			= UUID.randomUUID();
	private static final UUID LISTING_ID	= UUID.randomUUID();
	private static final UUID CAMPAIGN_ID	= UUID.randomUUID();
	private static final UUID ROLE_ID		= UUID.randomUUID();
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testConstruction() {
		
		CampaignListingEntity entity = CampaignListingEntity
				.builder()
					.id(ID)
					.listingId(LISTING_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
				.build();
		
		assertEquals(ID, 			entity.getId());
		assertEquals(LISTING_ID,	entity.getListingId());
		assertEquals(CAMPAIGN_ID, 	entity.getCampaignId());
		assertEquals(ROLE_ID, 		entity.getRoleId().orElseThrow());
	}
	
	/**
	* Test default case where Optional role not provided 
	*/
	@Test
	void testDefaults() {
		
		CampaignListingEntity entity = CampaignListingEntity
				.builder()
				.build();
		
		assertTrue(entity.getRoleId().isEmpty());
		
	}
	
	/**
	* Test conversion from Entity to Domain representation 
	*/
	@Test
	void testFromEntity() {
		
		CampaignListingEntity entity = CampaignListingEntity
				.builder()
					.id(ID)
					.listingId(LISTING_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
				.build();
		
		assertEquals(ID, 			entity.getId());
		assertEquals(LISTING_ID,	entity.getListingId());
		assertEquals(CAMPAIGN_ID, 	entity.getCampaignId());
		assertEquals(ROLE_ID, 		entity.getRoleId().orElseThrow());
		
		CampaignListing association = CampaignListingEntity.fromEntity(entity);
		
		assertEquals(ID, 			association.getId());
		assertEquals(LISTING_ID,	association.getListingId());
		assertEquals(CAMPAIGN_ID, 	association.getCampaignId());
		assertEquals(ROLE_ID, 		association.getRoleId().orElseThrow());
		
	}
	
	/**
	* Test conversion from Domain to Entity representation 
	*/
	@Test
	void testToEntity() {
		
		CampaignListingEntity association = CampaignListingEntity
				.builder()
					.id(ID)
					.listingId(LISTING_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
				.build();
		
		assertEquals(ID, 			association.getId());
		assertEquals(LISTING_ID,	association.getListingId());
		assertEquals(CAMPAIGN_ID, 	association.getCampaignId());
		assertEquals(ROLE_ID, 		association.getRoleId().orElseThrow());
		
		CampaignListing entity = CampaignListingEntity.fromEntity(association);
		
		assertEquals(ID, 			entity.getId());
		assertEquals(LISTING_ID,	entity.getListingId());
		assertEquals(CAMPAIGN_ID, 	entity.getCampaignId());
		assertEquals(ROLE_ID, 		entity.getRoleId().orElseThrow());
		
	}
	
}