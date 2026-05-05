package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the AddNoteAPIInbound class 
*/
class AddNoteAPIInboundTest {

	private static final UUID 			CAMPAIGN_ID		= UUID.randomUUID();
	private static final UUID 			ROLE_ID 		= UUID.randomUUID();
	private static final String 		TITLE 			= "Meeting Notes";
	private static final String 		TEXT 			= "We had a meeting.";
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testBuilder() {
		
		AddNoteAPIInbound note = AddNoteAPIInbound
				.builder()
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.title(TITLE)
					.text(TEXT)
				.build();
		
		assertEquals(CAMPAIGN_ID, 	note.getCampaignId());
		assertEquals(ROLE_ID, 		note.getRoleId().get());
		assertEquals(TITLE, 		note.getTitle().get());
		assertEquals(TEXT, 			note.getText());
		
	}
	
	/**
	* Tests construction via a Builder where the non mandatory 
	* values are not provided
	*/
	@Test
	void testBuilderDefaultValues() {
		
		AddNoteAPIInbound note = AddNoteAPIInbound
				.builder()
					.campaignId(CAMPAIGN_ID)
					.text(TEXT)
				.build();
		
		assertEquals(CAMPAIGN_ID, 	note.getCampaignId());
		assertEquals(TEXT, 			note.getText());
		
		assertTrue(note.getRoleId().isEmpty());
		assertTrue(note.getTitle().isEmpty());
		
	}
	
}