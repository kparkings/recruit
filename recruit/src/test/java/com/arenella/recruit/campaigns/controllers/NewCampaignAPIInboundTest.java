package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.CampaignLogo.PHOTO_FORMAT;

/**
* Unit tests for the NewCampaignAPIInbound class 
*/
class NewCampaignAPIInboundTest {

	private static final String 		NAME			= "ING";
	private static final String 		DESCRIPTION		= "ING Bank IT roles";
	private static final CampaignLogo 	LOGO			= new CampaignLogo(new byte[] {}, PHOTO_FORMAT.png);
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testBuilder() {
		
		NewCampaignAPIInbound campaign = NewCampaignAPIInbound
				.builder()
					.name(NAME)
					.description(DESCRIPTION)
					.logo(LOGO)
				.build();
		
		assertEquals(NAME, 			campaign.getName());
		assertEquals(DESCRIPTION, 	campaign.getDescription());
		assertEquals(LOGO, 			campaign.getLogo().get());
		
	}
	
	/**
	* Tests construction via a Builder when no Logo 
	* is proviced
	*/
	@Test
	void testBuilderNoLogo() {
		
		NewCampaignAPIInbound campaign = NewCampaignAPIInbound
				.builder()
					.name(NAME)
					.description(DESCRIPTION)
				.build();
		
		assertEquals(NAME, 			campaign.getName());
		assertEquals(DESCRIPTION, 	campaign.getDescription());
		
		assertTrue(campaign.getLogo().isEmpty());
		
	}
	
}