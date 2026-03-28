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
	private static final ContactAPIOutbound 	CONTACT 			= new ContactAPIOutbound("kevin","parkings");
	private static final ParticipantType 		TYPE 				= ParticipantType.EDIT;
	
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
				.build();
		
		assertEquals(PARTICIPATION_ID, 	participation.getParticipanttionId());
		assertEquals("kevin", 			participation.getContact().firstName());
		assertEquals("parkings", 		participation.getContact().surname());
		assertEquals(TYPE, 				participation.getType());
	
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
				.build();
		
		Contact contact = new Contact("kparings", "kevin", "parkings", "kparkings@gmail.com", SubscriptionType.PAID);
		
		ParticipationAPIOutbound participation = ParticipationAPIOutbound
				.builder()
					.from(participationDomain, contact)
				.build();
		
		assertEquals(PARTICIPATION_ID, 	participation.getParticipanttionId());
		assertEquals("kevin", 			participation.getContact().firstName());
		assertEquals("parkings", 		participation.getContact().surname());
		assertEquals(TYPE, 				participation.getType());
	
	}
	
}