package com.arenella.recruit.campaigns.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CampaignListing class 
*/
class CampaignListingTest {

	private static final UUID ID			= UUID.randomUUID();
	private static final UUID LISTING_ID	= UUID.randomUUID();
	private static final UUID CAMPAIGN_ID	= UUID.randomUUID();
	private static final UUID ROLE_ID		= UUID.randomUUID();
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testConstruction() {
		
		CampaignListing association = CampaignListing
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
	}
	
	/**
	* Test default case where Optional role not provided 
	*/
	@Test
	void testDefaults() {
		
		CampaignListing association = CampaignListing
				.builder()
				.build();
		
		assertTrue(association.getRoleId().isEmpty());
		
	}
	
}
