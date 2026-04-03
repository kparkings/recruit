package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Appointment;
import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.Candidate;
import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Document;
import com.arenella.recruit.campaigns.beans.Note;
import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.CampaignLogo.PHOTO_FORMAT;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;
import com.arenella.recruit.campaigns.beans.Document.DocumentType;

/**
* Unit tests for the CampaignAPIOutbound class 
*/
class CampaignAPIOutboundTest {

	private static final UUID 				ID				= UUID.randomUUID();
	private static final String 			NAME			= "ABN AMRO";
	private static final String 			DESCRIPTION		= "Campaign for IT roles for the Client ABN Amro";
	private static final CampaignLogo 		LOGO			= new CampaignLogo(new byte[] {}, PHOTO_FORMAT.jpeg);
	private static final LocalDateTime		CREATED			= LocalDateTime.of(2026, 3,27,17,49,11);
	private static final Set<Candidate>		CANDIDATES		= Set.of(Candidate.builder().build());
	private static final Set<Participation> PARTICIPANTS	= Set.of(Participation.builder().contactId("rec1").build());
	private static final Set<Note> 			NOTES			= Set.of(Note.builder().build());
	private static final Set<Appointment> 	APPOINTMENTS	= Set.of(Appointment.builder().build());
	private static final Set<Document> 		DOCUMENTS		= Set.of(new Document("spec", DocumentType.PDF, new byte[] {}, LocalDateTime.of(2026, 3, 28, 15, 7, 55)));
	
	/**
	* Test construction via Builder
	*/
	@Test
	void testConstruction() {
	
		Campaign campaign = Campaign
				.builder()
					.candidates(CANDIDATES)
					.appointments(APPOINTMENTS)
					.created(CREATED)
					.description(DESCRIPTION)
					.documents(DOCUMENTS)
					.id(ID)
					.logo(LOGO)
					.name(NAME)
					.notes(NOTES)
					.participants(PARTICIPANTS)
				.build();
		
		CampaignAPIOutbound outbound = CampaignAPIOutbound
				.builder()
					.from(campaign, Set.of(new Contact("rec1", "", "", "", SubscriptionType.CREDIT)))
				.build();
		
		assertEquals(ID, 			outbound.getId());
		assertEquals(NAME, 			outbound.getName());
		assertEquals(DESCRIPTION, 	outbound.getDescription());
		assertEquals(LOGO, 			outbound.getLogo().get());
		
		assertEquals(1, outbound.getCandidates().size());
		assertEquals(1, outbound.getParticipations().size());
		assertEquals(1, outbound.getNotes().size());
		assertEquals(1, outbound.getAppointments().size());
		assertEquals(1, outbound.getDocuments().size());
	}
	
}