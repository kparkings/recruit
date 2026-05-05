package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the AddAppointmentAPIInbound class 
*/
class AddAppointmentAPIInboundTest {

	private static final UUID 			CAMPAIGN_ID		= UUID.randomUUID();
	private static final UUID			ROLE_ID			= UUID.randomUUID();
	private static final String 		NAME			= "Client Meeting";
	private static final String 		DESCRIPTION		= "Meeting with Steve to discuss new Java role";
	private static final String 		VIDEO_LINK		= "https://www.teams.co.uk?aad11ad";
	private static final String 		PHONE_NUMBER	= "0031 643 220 866";
	private static final ZonedDateTime 	WHEN			= ZonedDateTime.of(LocalDateTime.of(2027, 3,31, 19,23,1), ZoneId.systemDefault());	
	
	/**
	* Tests construction via the Builder
	*/
	@Test
	void testBuilder() {
	
		AddAppointmentAPIInbound appointment = AddAppointmentAPIInbound
				.builder()
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.name(NAME)
					.description(DESCRIPTION)
					.videoLink(VIDEO_LINK)
					.phoneNumber(PHONE_NUMBER)
					.when(WHEN)
				.build();
		
		assertEquals(CAMPAIGN_ID, 	appointment.getCampaignId());
		assertEquals(ROLE_ID, 		appointment.getRoleId().get());
		assertEquals(NAME, 			appointment.getName());
		assertEquals(DESCRIPTION, 	appointment.getDescription());
		assertEquals(VIDEO_LINK, 	appointment.getVideoLink().get());
		assertEquals(PHONE_NUMBER, 	appointment.getPhoneNumber().get());
		assertEquals(WHEN, 			appointment.getWhen());
		
	}

	/**
	* Tests construction via the Builder when none of the Optional 
	* value have been set
	*/
	@Test
	void testBuilderNoOptionalValues() {
	
		AddAppointmentAPIInbound appointment = AddAppointmentAPIInbound
				.builder()
					.campaignId(CAMPAIGN_ID)
					.name(NAME)
					.description(DESCRIPTION)
					.when(WHEN)
				.build();
		
		assertEquals(CAMPAIGN_ID, 	appointment.getCampaignId());
		assertEquals(NAME, 			appointment.getName());
		assertEquals(DESCRIPTION, 	appointment.getDescription());
		assertEquals(WHEN, 			appointment.getWhen());
		
		assertTrue(appointment.getRoleId().isEmpty());
		assertTrue(appointment.getVideoLink().isEmpty());
		assertTrue(appointment.getPhoneNumber().isEmpty());
		
	}
	
}