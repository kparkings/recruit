package com.arenella.recruit.campaigns.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the Role class 
*/
class RoleTest {

	private static final UUID 				ID				= UUID.randomUUID();
	private static final String 			NAME			= "ABN AMRO";
	private static final String 			DESCRIPTION		= "Campaign for IT roles for the Client ABN Amro";
	private static final LocalDateTime		CREATED			= LocalDateTime.of(2026, 3,27,17,49,11);
	private static final Set<Candidate>		CANDIDATES		= Set.of(Candidate.builder().build());
	private static final Set<Participation> PARTICIPANTS	= Set.of(Participation.builder().build());
	private static final Set<Note> 			NOTES			= Set.of(Note.builder().build());
	private static final Set<Appointment> 	APPOINTMENTS	= Set.of(Appointment.builder().build());
	private static final Set<Document> 		DOCUMENTS		= Set.of(Document.builder().build());
	
	/**
	* Tests construction via Builder 
	*/
	@Test
	void testBuilder() {
		
		Role role = Role
				.builder()
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
		assertEquals(NAME, 			role.getName());
		assertEquals(DESCRIPTION, 	role.getDescription());
		assertEquals(CREATED, 		role.getCreated());
		
		assertEquals(1, role.getCandidates().size());
		assertEquals(1, role.getParticipations().size());
		assertEquals(1, role.getNotes().size());
		assertEquals(1, role.getAppointments().size());
		assertEquals(1, role.getDocuments().size());
		
		Role updatedRole = Role
				.builder()
					.from(role)
					.candidate(Candidate.builder().build())
					.participation(Participation.builder().build())
					.note(Note.builder().build())
					.appointment(Appointment.builder().build())
					.document(Document.builder().build())
				.build();
	
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
	
		Role role = Role.builder().build();
	
		assertTrue(role.getCandidates().isEmpty());
		assertTrue(role.getParticipations().isEmpty());
		assertTrue(role.getNotes().isEmpty());
		assertTrue(role.getAppointments().isEmpty());
		assertTrue(role.getDocuments().isEmpty());
		
	}
	
}
