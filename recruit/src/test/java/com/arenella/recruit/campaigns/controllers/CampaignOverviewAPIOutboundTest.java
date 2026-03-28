package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.CampaignLogo.PHOTO_FORMAT;

/**
* Unit tests for the CampaignOverviewAPIOutbound class 
*/
class CampaignOverviewAPIOutboundTest {

	private static final UUID 				ID				= UUID.randomUUID();
	private static final String 			NAME			= "ABN AMRO";
	private static final String 			DESCRIPTION		= "Campaign for IT roles for the Client ABN Amro";
	private static final CampaignLogo 		LOGO			= new CampaignLogo(new byte[] {}, PHOTO_FORMAT.jpeg);
	
	/**
	* Tests construction via Builder
	*/
	@Test
	void testBuilder() {
		
		Campaign campaign = Campaign
				.builder()
					.id(ID)
					.name(NAME)
					.description(DESCRIPTION)
					.logo(LOGO)
				.build();
		
		CampaignOverviewAPIOutbound overview = CampaignOverviewAPIOutbound
				.builder()
					.from(campaign)
				.build();
		
		assertEquals(ID, 			overview.getId());
		assertEquals(NAME, 			overview.getName());
		assertEquals(DESCRIPTION, 	overview.getDescription());
		assertEquals(LOGO, 			overview.getLogo().get());
		
	}
	
	/**
	* Test case that Logo is not provided
	*/
	@Test
	void testBuilderNoLogo() {
		 
		Campaign campaign = Campaign
				.builder()
				.build();
			
		CampaignOverviewAPIOutbound overview = CampaignOverviewAPIOutbound
				.builder()
					.from(campaign)
				.build();
			
		assertTrue(overview.getLogo().isEmpty());
			
	}
	
}