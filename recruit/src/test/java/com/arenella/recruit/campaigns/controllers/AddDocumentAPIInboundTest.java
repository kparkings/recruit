package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Document.DocumentType;

/**
* Unit tests for the AddDocumentAPIInbound class
*/
class AddDocumentAPIInboundTest {

	private static final UUID 			CAMPAIGN_ID 	= UUID.randomUUID();
	private static final UUID 			ROLE_ID 		= UUID.randomUUID();
	private static final String 		TITLE 			= "Job Spec #22";
	private static final DocumentType 	TYPE 			= DocumentType.PDF; 
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testConstruction() {
		
		AddDocumentAPIInbound document = AddDocumentAPIInbound
				.builder()
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.title(TITLE)
					.type(TYPE)
				.build();
		
		assertEquals(CAMPAIGN_ID, 	document.getCampaignId());
		assertEquals(ROLE_ID, 		document.getRoleId().get());
		assertEquals(TITLE, 		document.getTitle());
		assertEquals(TYPE, 			document.getType());
		
	}
	
	/**
	* Tests construction via a Builder when no Role specified
	*/
	@Test
	void testConstructionNoRole() {
		
		AddDocumentAPIInbound document = AddDocumentAPIInbound
				.builder()
					.campaignId(CAMPAIGN_ID)
					.title(TITLE)
					.type(TYPE)
				.build();
		
		assertEquals(CAMPAIGN_ID, 	document.getCampaignId());
		assertEquals(TITLE, 		document.getTitle());
		assertEquals(TYPE, 			document.getType());
		
		assertTrue(document.getRoleId().isEmpty());
		
	}
	
}
