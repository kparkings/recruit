package com.arenella.recruit.campaigns.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Appointment;
import com.arenella.recruit.campaigns.beans.Candidate;
import com.arenella.recruit.campaigns.beans.Document;
import com.arenella.recruit.campaigns.beans.Note;
import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.Role;

/**
* Unit tests for the RoleEntity class 
*/
class RoleEntityTest {

	private static final UUID 						ID				= UUID.randomUUID();
	private static final UUID 						CAMPAIGN_ID		= UUID.randomUUID();
	private static final String 					NAME			= "ABN AMRO";
	private static final String 					DESCRIPTION		= "Campaign for IT roles for the Client ABN Amro";
	private static final LocalDateTime				CREATED			= LocalDateTime.of(2026, 3,27,17,49,11);
	private static final Set<CandidateEntity>		CANDIDATES		= Set.of(CandidateEntity.builder().build());
	private static final Set<ParticipationEntity> 	PARTICIPANTS	= Set.of(ParticipationEntity.builder().build());
	private static final Set<NoteEntity> 			NOTES			= Set.of(NoteEntity.builder().build());
	private static final Set<AppointmentEntity> 	APPOINTMENTS	= Set.of(AppointmentEntity.builder().build());
	private static final Set<DocumentEntity> 		DOCUMENTS		= Set.of(DocumentEntity.builder().build());
	
	/**
	* Tests construction via Builder 
	*/
	@Test
	void testBuilder() {
		
		RoleEntity role = RoleEntity
				.builder()
					.campaignId(CAMPAIGN_ID)
					.candidates(CANDIDATES)
					.appointments(APPOINTMENTS)
					.created(CREATED)
					.description(DESCRIPTION)
					.documents(DOCUMENTS)
					.id(ID)
					.name(NAME)
					.notes(NOTES)
					.participants(PARTICIPANTS)
				.build();
		
		assertEquals(ID, 			role.getId());
		assertEquals(CAMPAIGN_ID, 	role.getCampaignId());
		assertEquals(NAME, 			role.getName());
		assertEquals(DESCRIPTION, 	role.getDescription());
		assertEquals(CREATED, 		role.getCreated());
		
		assertEquals(1, role.getCandidates().size());
		assertEquals(1, role.getParticipations().size());
		assertEquals(1, role.getNotes().size());
		assertEquals(1, role.getAppointments().size());
		assertEquals(1, role.getDocuments().size());
		
		RoleEntity updatedRole = RoleEntity
				.builder()
					.from(role)
					.candidate(CandidateEntity.builder().build())
					.participation(ParticipationEntity.builder().build())
					.note(NoteEntity.builder().build())
					.appointment(AppointmentEntity.builder().build())
					.document(DocumentEntity.builder().build())
				.build();
	
		assertEquals(CAMPAIGN_ID, 	updatedRole.getCampaignId());
		assertEquals(ID, 			updatedRole.getId());
		assertEquals(NAME, 			updatedRole.getName());
		assertEquals(DESCRIPTION, 	updatedRole.getDescription());
		assertEquals(CREATED, 		updatedRole.getCreated());
		
		assertEquals(2, updatedRole.getCandidates().size());
		assertEquals(2, updatedRole.getParticipations().size());
		assertEquals(2, updatedRole.getNotes().size());
		assertEquals(2, updatedRole.getAppointments().size());
		assertEquals(2, updatedRole.getDocuments().size());
		
	}
	
	/**
	* Tests collections exist but are empty by default to avoid 
	* possibility of NullPointer Exceptions 
	*/
	@Test
	void testDefaults() {
	
		RoleEntity role = RoleEntity.builder().build();
	
		assertTrue(role.getCandidates().isEmpty());
		assertTrue(role.getParticipations().isEmpty());
		assertTrue(role.getNotes().isEmpty());
		assertTrue(role.getAppointments().isEmpty());
		assertTrue(role.getDocuments().isEmpty());
		
	}

	/**
	* Tests conversion from Entity to Domain representaiton 
	*/
	@Test
	void fromEntity() {
		
		RoleEntity entity = RoleEntity
				.builder()
					.campaignId(CAMPAIGN_ID)
					.candidates(CANDIDATES)
					.appointments(APPOINTMENTS)
					.created(CREATED)
					.description(DESCRIPTION)
					.documents(DOCUMENTS)
					.id(ID)
					.name(NAME)
					.notes(NOTES)
					.participants(PARTICIPANTS)
				.build();
		
		assertEquals(ID, 			entity.getId());
		assertEquals(CAMPAIGN_ID, 	entity.getCampaignId());
		assertEquals(NAME, 			entity.getName());
		assertEquals(DESCRIPTION, 	entity.getDescription());
		assertEquals(CREATED, 		entity.getCreated());
		
		assertEquals(1, entity.getCandidates().size());
		assertEquals(1, entity.getParticipations().size());
		assertEquals(1, entity.getNotes().size());
		assertEquals(1, entity.getAppointments().size());
		assertEquals(1, entity.getDocuments().size());
		
		Role role = RoleEntity.fromEntity(entity);
		
		assertEquals(ID, 			role.getId());
		assertEquals(NAME, 			role.getName());
		assertEquals(DESCRIPTION, 	role.getDescription());
		assertEquals(CREATED, 		role.getCreated());
		
		assertEquals(1, role.getCandidates().size());
		assertEquals(1, role.getParticipations().size());
		assertEquals(1, role.getNotes().size());
		assertEquals(1, role.getAppointments().size());
		assertEquals(1, role.getDocuments().size());
		
		
	}
	
	/**
	* Tests conversion from Domain to Entity representaiton 
	*/
	@Test
	void toEntity() {
		
		Role role = Role
				.builder()
					.candidates(Set.of(Candidate.builder().build()))
					.appointments(Set.of(Appointment.builder().build()))
					.created(CREATED)
					.description(DESCRIPTION)
					.documents(Set.of(Document.builder().build()))
					.id(ID)
					.name(NAME)
					.notes(Set.of(Note.builder().build()))
					.participants(Set.of(Participation.builder().build()))
				.build();
		
		assertEquals(ID, 			role.getId());
		assertEquals(NAME, 			role.getName());
		assertEquals(DESCRIPTION, 	role.getDescription());
		assertEquals(CREATED, 		role.getCreated());
		
		assertEquals(1, role.getCandidates().size());
		assertEquals(1, role.getParticipations().size());
		assertEquals(1, role.getNotes().size());
		assertEquals(1, role.getAppointments().size());
		assertEquals(1, role.getDocuments().size());
		
		RoleEntity entity = RoleEntity.toEntity(role, CAMPAIGN_ID);
		
		assertEquals(ID, 			entity.getId());
		assertEquals(CAMPAIGN_ID, 	entity.getCampaignId());
		assertEquals(NAME, 			entity.getName());
		assertEquals(DESCRIPTION, 	entity.getDescription());
		assertEquals(CREATED, 		entity.getCreated());
		
		assertEquals(1, entity.getCandidates().size());
		assertEquals(1, entity.getParticipations().size());
		assertEquals(1, entity.getNotes().size());
		assertEquals(1, entity.getAppointments().size());
		assertEquals(1, entity.getDocuments().size());
		
	}
	
}