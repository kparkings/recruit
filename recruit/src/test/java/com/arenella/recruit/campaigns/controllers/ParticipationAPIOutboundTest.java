package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;
import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;

/**
* Unit tests for the ParticipationAPIOutbound class 
*/
class ParticipationAPIOutboundTest {

	private static final UUID					PARTICIPATION_ID 	= UUID.randomUUID();
	private static final ContactAPIOutbound 	CONTACT 			= new ContactAPIOutbound("kp001", "kevin","parkings");
	private static final ParticipantType 		TYPE 				= ParticipantType.EDIT;
	private static final UUID 					CAMPAIGN_ID 		= UUID.randomUUID();
	private static final UUID 					ROLE_ID 			= UUID.randomUUID();
	/**
	* Tests construction via the Builder 
	*/
	@Test 
	void testBuilder() {
		
		ParticipationAPIOutbound participation = ParticipationAPIOutbound
				.builder()
					.participationId(PARTICIPATION_ID)
					.contact(CONTACT)
					.type(TYPE)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
				.build();
		
		assertEquals(PARTICIPATION_ID, 	participation.getParticipationId());
		assertEquals("kp001", 			participation.getContact().contactId());
		assertEquals("kevin", 			participation.getContact().firstName());
		assertEquals("parkings", 		participation.getContact().surname());
		assertEquals(TYPE, 				participation.getType());
		assertEquals(CAMPAIGN_ID, 		participation.getCampaignId());
		assertEquals(ROLE_ID, 			participation.getRoleId().get());
	}
	
	/**
	* Tests construction via the Builder 
	*/
	@Test 
	void testBuilderFromDomainObjects() {
		
		Participation participationDomain = Participation
				.builder()
					.participationId(PARTICIPATION_ID)
					.type(TYPE)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
				.build();
		
		Contact contact = new Contact("kp001", "kevin", "parkings", "kparkings@gmail.com", SubscriptionType.PAID);
		
		ParticipationAPIOutbound participation = ParticipationAPIOutbound
				.builder()
					.from(participationDomain, contact)
				.build();
		
		assertEquals(PARTICIPATION_ID, 	participation.getParticipationId());
		assertEquals("kp001", 			participation.getContact().contactId());
		assertEquals("kevin", 			participation.getContact().firstName());
		assertEquals("parkings", 		participation.getContact().surname());
		assertEquals(TYPE, 				participation.getType());
		assertEquals(CAMPAIGN_ID, 		participation.getCampaignId());
		assertEquals(ROLE_ID, 			participation.getRoleId().get());
	
	}
	
}