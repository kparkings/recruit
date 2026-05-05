package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;

/**
* Unit tests for the AddParticipationAPIInbound class 
*/
class AddParticipationAPIInboundTest {

	private static final String 			CONTACT_ID		= "rec33";
	private static final UUID 				CAMPAIGN_ID		= UUID.randomUUID(); 
	private static final UUID 				ROLE_ID			= UUID.randomUUID();
	private static final ParticipantType 	TYPE			= ParticipantType.ADMIN;
	
	/**
	* Tests construction via a Builder 
	*/
	@Test 
	void testConstruction() {
		
		AddParticipationAPIInbound participation = AddParticipationAPIInbound
				.builder()
					.contactId(CONTACT_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.type(TYPE)
				.build();
		
		assertEquals(CONTACT_ID, 	participation.getContactId());
		assertEquals(CAMPAIGN_ID, 	participation.getCampaignId());
		assertEquals(ROLE_ID, 		participation.getRoleId().get());
		assertEquals(TYPE, 			participation.getType());
		
	}
	
	/**
	* Tests construction via a Builder at Campaign and not Role level
	*/
	@Test 
	void testConstructionNoRole() {
		
		AddParticipationAPIInbound participation = AddParticipationAPIInbound
				.builder()
					.contactId(CONTACT_ID)
					.campaignId(CAMPAIGN_ID)
					.type(TYPE)
				.build();
		
		assertEquals(CONTACT_ID, 	participation.getContactId());
		assertEquals(CAMPAIGN_ID, 	participation.getCampaignId());
		assertEquals(TYPE, 			participation.getType());
		
		assertTrue(participation.getRoleId().isEmpty());
		
	}
	
}