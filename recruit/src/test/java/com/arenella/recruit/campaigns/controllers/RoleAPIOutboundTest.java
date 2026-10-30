package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Appointment;
import com.arenella.recruit.campaigns.beans.Candidate;
import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Document;
import com.arenella.recruit.campaigns.beans.IMNotification;
import com.arenella.recruit.campaigns.beans.Note;
import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.Role;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;

/**
* Unit tests for the RoleAPIOutbound class 
*/
class RoleAPIOutboundTest {

	private static final UUID 				ID				= UUID.randomUUID();
	private static final UUID 				CAMPAIGN_ID		= UUID.randomUUID();
	private static final String 			NAME			= "ABN AMRO";
	private static final String 			DESCRIPTION		= "Campaign for IT roles for the Client ABN Amro";
	private static final LocalDateTime		CREATED			= LocalDateTime.of(2026, 3,27,17,49,11);
	private static final Set<Candidate>		CANDIDATES		= Set.of(Candidate.builder().id("123").build());
	private static final Set<Participation> PARTICIPANTS	= Set.of(Participation.builder().contactId("11").build());
	private static final Set<Note> 			NOTES			= Set.of(Note.builder().build());
	private static final Set<Appointment> 	APPOINTMENTS	= Set.of(Appointment.builder().build());
	private static final Set<Document> 		DOCUMENTS		= Set.of(Document.builder().build());
	private static final IMNotification		NOTIFICATION 	= IMNotification.builder().roleId(ID).candidateId("123").build();
	
	/**
	* Tests construction of wrapper 
	*/
	@Test
	void testConstruction() {
		
		Role role = Role
				.builder()
					.candidates(CANDIDATES)
					.appointments(APPOINTMENTS)
					.created(CREATED)
					.description(DESCRIPTION)
					.documents(DOCUMENTS)
					.id(ID)
					.campaignId(CAMPAIGN_ID)
					.name(NAME)
					.notes(NOTES)
					.participants(PARTICIPANTS)
				.build();
		
		Contact contact = new Contact("11", "daffy", "boo", "1@1.nl", SubscriptionType.PAID);
		
		RoleAPIOutbound outbound = RoleAPIOutbound.builder().from(role, Set.of(contact), Set.of(NOTIFICATION)).build();
		
		assertEquals(ID, 			outbound.getId());
		assertEquals(NAME, 			outbound.getName());
		assertEquals(DESCRIPTION, 	outbound.getDescription());
		
		assertEquals(1, outbound.getCandidates().size());
		assertEquals(1, outbound.getParticipations().size());
		assertEquals(1, outbound.getNotes().size());
		assertEquals(1, outbound.getAppointments().size());
		assertEquals(1, outbound.getDocuments().size());
		
		assertTrue(outbound.getHasUnreadNotifications());
		
	}
	
}