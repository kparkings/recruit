package com.arenella.recruit.campaigns.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.CampaignLogo.PHOTO_FORMAT;
import com.arenella.recruit.campaigns.entities.AppointmentEntity;
import com.arenella.recruit.campaigns.entities.CampaignEntity;
import com.arenella.recruit.campaigns.entities.CandidateEntity;
import com.arenella.recruit.campaigns.entities.DocumentEntity;
import com.arenella.recruit.campaigns.entities.NoteEntity;
import com.arenella.recruit.campaigns.entities.ParticipationEntity;
import com.arenella.recruit.campaigns.entities.RoleEntity;

/**
* Unit tests for the CampaignEntity class 
*/
class CampaignEntityTest {

	private static final UUID 						ID					= UUID.randomUUID();
	private static final String 					NAME				= "ABN AMRO";
	private static final String 					DESCRIPTION			= "Campaign for IT roles for the Client ABN Amro";
	private static final CampaignLogo 				LOGO				= new CampaignLogo(new byte[] {}, PHOTO_FORMAT.jpeg);
	private static final LocalDateTime				CREATED				= LocalDateTime.of(2026, 3,27,17,49,11);
	private static final Set<RoleEntity>			ROLES				= Set.of(RoleEntity.builder().build());
	private static final Set<CandidateEntity>		CANDIDATES			= Set.of(CandidateEntity.builder().build());
	private static final Set<ParticipationEntity> 	PARTICIPANTS		= Set.of(ParticipationEntity.builder().build());
	private static final Set<NoteEntity> 			NOTES				= Set.of(NoteEntity.builder().build());
	private static final Set<AppointmentEntity> 	APPOINTMENTS		= Set.of(AppointmentEntity.builder().build());
	private static final Set<DocumentEntity> 		DOCUMENTS			= Set.of(DocumentEntity.builder().build());
	private static final Set<Role>					ROLESDOMAIN			= Set.of(Role.builder().build());
	private static final Set<Candidate>				CANDIDATESDOMAIN	= Set.of(Candidate.builder().build());
	private static final Set<Participation> 		PARTICIPANTSDOMAIN	= Set.of(Participation.builder().build());
	private static final Set<Note> 					NOTESDOMAIN			= Set.of(Note.builder().build());
	private static final Set<Appointment> 			APPOINTMENTSDOMAIN	= Set.of(Appointment.builder().build());
	private static final Set<Document> 				DOCUMENTSDOMAIN		= Set.of(Document.builder().build());
	
	/**
	* Tests construction via Builder 
	*/
	@Test
	void testBuilder() {
		
		CampaignEntity campaign = CampaignEntity
				.builder()
					.roles(ROLES)
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
		
		assertEquals(ID, 					campaign.getId());
		assertEquals(NAME, 					campaign.getName());
		assertEquals(DESCRIPTION, 			campaign.getDescription());
		assertEquals(LOGO.imageBytes(), 	campaign.getLogoBytes().get());
		assertEquals(LOGO.format(), 		campaign.getLogoFormat().get());
		assertEquals(CREATED, 				campaign.getCreated());
		
		assertEquals(1, campaign.getRoles().size());
		assertEquals(1, campaign.getCandidates().size());
		assertEquals(1, campaign.getParticipations().size());
		assertEquals(1, campaign.getNotes().size());
		assertEquals(1, campaign.getAppointments().size());
		assertEquals(1, campaign.getDocuments().size());
		
	}
	
	/**
	* Tests collections exist but are empty by default to avoid 
	* possibility of NullPointer Exceptions 
	*/
	@Test
	void testDefaults() {
	
		CampaignEntity campaign = CampaignEntity.builder().build();
	
		assertTrue(campaign.getRoles().isEmpty());
		assertTrue(campaign.getCandidates().isEmpty());
		assertTrue(campaign.getParticipations().isEmpty());
		assertTrue(campaign.getNotes().isEmpty());
		assertTrue(campaign.getAppointments().isEmpty());
		assertTrue(campaign.getDocuments().isEmpty());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	*/
	@Test
	void testFromEntity() {
		
		CampaignEntity entity = CampaignEntity
				.builder()
					.roles(ROLES)
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
		
		assertEquals(ID, 					entity.getId());
		assertEquals(NAME, 					entity.getName());
		assertEquals(DESCRIPTION, 			entity.getDescription());
		assertEquals(LOGO.imageBytes(), 	entity.getLogoBytes().get());
		assertEquals(LOGO.format(), 		entity.getLogoFormat().get());
		assertEquals(CREATED, 				entity.getCreated());
		
		assertEquals(1, entity.getRoles().size());
		assertEquals(1, entity.getCandidates().size());
		assertEquals(1, entity.getParticipations().size());
		assertEquals(1, entity.getNotes().size());
		assertEquals(1, entity.getAppointments().size());
		assertEquals(1, entity.getDocuments().size());
		
		Campaign campaign = CampaignEntity.fromEntity(entity);
		
		assertEquals(ID, 					campaign.getId());
		assertEquals(NAME, 					campaign.getName());
		assertEquals(DESCRIPTION, 			campaign.getDescription());
		assertEquals(LOGO.imageBytes(), 	campaign.getLogo().get().imageBytes());
		assertEquals(LOGO.format(), 		campaign.getLogo().get().format());
		assertEquals(CREATED, 				campaign.getCreated());
		
		assertEquals(1, campaign.getRoles().size());
		assertEquals(1, campaign.getCandidates().size());
		assertEquals(1, campaign.getParticipations().size());
		assertEquals(1, campaign.getNotes().size());
		assertEquals(1, campaign.getAppointments().size());
		assertEquals(1, campaign.getDocuments().size());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	*/
	@Test
	void testToEntity() {
		
		Campaign campaign = Campaign
				.builder()
					.roles(ROLESDOMAIN)
					.candidates(CANDIDATESDOMAIN)
					.appointments(APPOINTMENTSDOMAIN)
					.created(CREATED)
					.description(DESCRIPTION)
					.documents(DOCUMENTSDOMAIN)
					.id(ID)
					.logo(LOGO)
					.name(NAME)
					.notes(NOTESDOMAIN)
					.participants(PARTICIPANTSDOMAIN)
				.build();
		
		assertEquals(ID, 					campaign.getId());
		assertEquals(NAME, 					campaign.getName());
		assertEquals(DESCRIPTION, 			campaign.getDescription());
		assertEquals(LOGO.imageBytes(), 	campaign.getLogo().get().imageBytes());
		assertEquals(LOGO.format(), 		campaign.getLogo().get().format());
		assertEquals(CREATED, 				campaign.getCreated());
		
		assertEquals(1, campaign.getRoles().size());
		assertEquals(1, campaign.getCandidates().size());
		assertEquals(1, campaign.getParticipations().size());
		assertEquals(1, campaign.getNotes().size());
		assertEquals(1, campaign.getAppointments().size());
		assertEquals(1, campaign.getDocuments().size());
		
		CampaignEntity entity = CampaignEntity.toEntity(campaign);
		
		assertEquals(ID, 					entity.getId());
		assertEquals(NAME, 					entity.getName());
		assertEquals(DESCRIPTION, 			entity.getDescription());
		assertEquals(LOGO.imageBytes(), 	entity.getLogoBytes().get());
		assertEquals(LOGO.format(), 		entity.getLogoFormat().get());
		assertEquals(CREATED, 				entity.getCreated());
		
		assertEquals(1, entity.getRoles().size());
		assertEquals(1, entity.getCandidates().size());
		assertEquals(1, entity.getParticipations().size());
		assertEquals(1, entity.getNotes().size());
		assertEquals(1, entity.getAppointments().size());
		assertEquals(1, entity.getDocuments().size());
		
	}
	
}