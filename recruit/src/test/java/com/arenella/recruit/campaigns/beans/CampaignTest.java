package com.arenella.recruit.campaigns.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.CampaignLogo.PHOTO_FORMAT;
import com.arenella.recruit.campaigns.beans.Document.DocumentType;

/**
* Unit tests for the Campaign class 
*/
class CampaignTest {

	private static final UUID 				ID				= UUID.randomUUID();
	private static final String 			NAME			= "ABN AMRO";
	private static final String 			DESCRIPTION		= "Campaign for IT roles for the Client ABN Amro";
	private static final CampaignLogo 		LOGO			= new CampaignLogo(new byte[] {}, PHOTO_FORMAT.jpeg);
	private static final LocalDateTime		CREATED			= LocalDateTime.of(2026, 3,27,17,49,11);
	private static final Set<Participation> PARTICIPANTS	= Set.of(Participation.builder().build());
	private static final Set<Note> 			NOTES			= Set.of(Note.builder().build());
	private static final Set<Appointment> 	APPOINTMENTS	= Set.of(Appointment.builder().build());
	private static final Set<Document> 		DOCUMENTS		= Set.of(new Document("spec", DocumentType.pdf, new byte[] {}, LocalDateTime.of(2026, 3, 28, 15, 7, 55)));
	
	/**
	* Tests construction via Builder 
	*/
	@Test
	void testBuilder() {
		
		Campaign campaign = Campaign
				.builder()
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
		
		assertEquals(ID, 			campaign.getId());
		assertEquals(NAME, 			campaign.getName());
		assertEquals(DESCRIPTION, 	campaign.getDescription());
		assertEquals(LOGO, 			campaign.getLogo());
		assertEquals(CREATED, 		campaign.getCreated());
		
		assertEquals(1, campaign.getParticipations().size());
		assertEquals(1, campaign.getNotes().size());
		assertEquals(1, campaign.getAppointments().size());
		assertEquals(1, campaign.getDocuments().size());
		
		Campaign updatedCampaign = Campaign
				.builder()
					.from(campaign)
					.participation(Participation.builder().build())
					.note(Note.builder().build())
					.appointment(Appointment.builder().build())
					.document(new Document("spec", DocumentType.pdf, new byte[] {}, LocalDateTime.of(2026, 3, 28, 15, 7, 55)))
				.build();
	
		assertEquals(ID, 			updatedCampaign.getId());
		assertEquals(NAME, 			updatedCampaign.getName());
		assertEquals(DESCRIPTION, 	updatedCampaign.getDescription());
		assertEquals(LOGO, 			updatedCampaign.getLogo());
		assertEquals(CREATED, 		updatedCampaign.getCreated());
		
		assertEquals(2, updatedCampaign.getParticipations().size());
		assertEquals(2, updatedCampaign.getNotes().size());
		assertEquals(2, updatedCampaign.getAppointments().size());
		assertEquals(2, updatedCampaign.getDocuments().size());
		
	}
	
	/**
	* Tests collections exist but are empty by default to avoid 
	* possibility of NullPointer Exceptions 
	*/
	@Test
	void testDefaults() {
	
		Campaign campaign = Campaign.builder().build();
	
		assertTrue(campaign.getParticipations().isEmpty());
		assertTrue(campaign.getNotes().isEmpty());
		assertTrue(campaign.getAppointments().isEmpty());
		assertTrue(campaign.getDocuments().isEmpty());
		
	}
	
}