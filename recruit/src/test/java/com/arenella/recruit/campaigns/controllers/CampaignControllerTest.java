package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.CampaignLogo.PHOTO_FORMAT;
import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;
import com.arenella.recruit.campaigns.beans.Document.DocumentType;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;
import com.arenella.recruit.campaigns.services.CampaignContactService;
import com.arenella.recruit.campaigns.services.CampaignService;

/**
* Unit tests for the CampaignController class 
*/
@ExtendWith(MockitoExtension.class)
class CampaignControllerTest {

	@Mock
	private CampaignService 			mockCampaignService;
	
	@Mock
	private CampaignContactService 		mockContactService;
	
	@Mock
	private	Principal					mockPrincipal;
	
	@Mock
	private MultipartFile				mockFile;
	
	@InjectMocks
	private CampaignController 			controller;
	
	/**
	* Test endpoint for retrieving current User's own Campaigns 
	*/
	@Test
	void testFetchCampaignsForUser() {
		
		final String userId = "rec1";
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockCampaignService.fetchCampaignsForUser(userId)).thenReturn(Set.of(Campaign.builder().build(), Campaign.builder().build()));
		
		ResponseEntity<Set<CampaignOverviewAPIOutbound>> response = this.controller.fetchCampaignsForUser(mockPrincipal);
		
		assertEquals(HttpStatus.OK, 	response.getStatusCode());
		assertEquals(2, 				response.getBody().size());
		
	}
	
	/**
	* Tests the retrieval of a single Campaign based upon its id 
	*/
	@Test
	void testFetchCampaign() {
		
		final String 	userId 			= "rec1";
		final String 	contactId 		= "rec5";
		final UUID 		campaignId 		= UUID.randomUUID();
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockCampaignService.fetchCampaignById(campaignId, userId)).thenReturn(Campaign.builder().participation(Participation.builder().contactId(contactId).build()).build());
		when(this.mockContactService.fetchContactsById(Set.of(contactId))).thenReturn(Set.of(new Contact(contactId, "peter", "smith", "ps@fake.ml", SubscriptionType.CREDIT)));
		
		ResponseEntity<CampaignAPIOutbound> response = this.controller.fetchCampaign(campaignId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, 	response.getStatusCode());
		assertNotNull(response.getBody());
		
		CampaignAPIOutbound campaigin = response.getBody();
		
		assertTrue(campaigin.getParticipations().stream().filter(p -> p.getContact().surname().equals("smith")).findAny().isPresent());
		
	}
	
	/**
	* Tests adding a new Campaign 
	* @throws Exception 
	*/
	@Test
	void testAddCampaign() throws Exception {
		
		final String 		userId 			= "rec1";
		final String 		name 			= "";
		final String 		description 	= "";
		final CampaignLogo 	logo 			= new CampaignLogo(new byte[] {}, PHOTO_FORMAT.jpeg);
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.addNewCampaign(NewCampaignAPIInbound
				.builder()
					.name(name)
					.description(description)
					.logo(logo)
				.build(), Optional.of(mockFile),  mockPrincipal);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		verify(this.mockCampaignService).addCampaign(eq(name), eq(description), any(), eq(userId));
		
	}
	
	/**
	* Tests adding a Participation to an existing Campaign
	*/
	@Test
	void testAddParticipation() {
		
		final String 			userId 		= "rec1";
		final String 			contactId 	= "rec34";
		final UUID 				campaignId 	= UUID.randomUUID(); 
		final UUID 				roleId 		= UUID.randomUUID();
		final ParticipantType 	type 		= ParticipantType.ADMIN;
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		AddParticipationAPIInbound participation = AddParticipationAPIInbound
				.builder()
					.campaignId(campaignId)
					.contactId(contactId)
					.roleId(roleId)
					.type(type)
				.build();
		
		ResponseEntity<Void> response = this.controller.addParticipation(participation, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).addParticipationToCampaign(participation.getContactId(), participation.getCampaignId(), participation.getRoleId().get(), participation.getType(), userId);
		
	}
	
	/**
	* Tests the deletion of a Participation in a Campaign 
	*/
	@Test
	void testDeleteParticipation() {
		
		final String 	userId 				= "rec1";
		final UUID 		participationId 	= UUID.randomUUID();
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.deleteParticipation(participationId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).deleteParticipation(participationId, userId);
		
	}
	
	/**
	* Tests adding a Note to a Campaign 
	*/
	@Test 
	void testAddNote() {
		
		final String 	userId 			= "rec1";
		final UUID 		campaignId		= UUID.randomUUID();
		final UUID 		roleId			= UUID.randomUUID();
		final String 	title			= "Meeting notes";
		final String 	text			= "we went to a meeting. It was a good meeting";	
		
		AddNoteAPIInbound note = AddNoteAPIInbound
				.builder()
					.campaignId(campaignId)
					.roleId(roleId)
					.title(title)
					.text(text)
				.build();
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.addNote(note, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).addNoteToCampaign(campaignId, roleId, title, text, userId);
		
	}
	
	/**
	* Tests updating an existing Note 
	*/
	@Test
	void testUpdateNote() {
		
		final String 	userId 			= "rec1";
		final UUID		noteId			= UUID.randomUUID();
		final String 	title			= "Meeting notes";
		final String 	text			= "we went to a meeting. It was a good meeting";	
		
		UpdateNoteAPIInbound note = UpdateNoteAPIInbound
				.builder()
					.title(title)
					.text(text)
				.build();
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.updateNote(noteId, note, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).updateNote(noteId, title, text, userId);
		
	}
	
	/**
	* Tests deletion of a Note 
	*/
	@Test
	void testDeleteNote() {
		
		final String 	userId 	= "rec1";
		final UUID 		noteId 	= UUID.randomUUID();
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.deleteNote(noteId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).deleteNote(noteId, userId);
		
	}
	
	/**
	* Tests adding an Appointment to a Campaign 
	*/
	@Test 
	void testAddAppointment() {
		
		final String 			userId 			= "rec1";
		final UUID 				campaignId		= UUID.randomUUID();
		final UUID				roleId			= UUID.randomUUID();
		final String 			name			= "client meeting";
		final String 			description		= "kennismaking met de bank";
		final String 			videoLink		= "https://www.vid.com/ssefs33";
		final String 			phoneNumber		= "0031 111 222 000";
		final ZonedDateTime 	when			= ZonedDateTime.of(LocalDateTime.of(2026,  4, 1, 20, 8, 12), ZoneId.systemDefault());
		
		AddAppointmentAPIInbound appointment = AddAppointmentAPIInbound
				.builder()
					.campaignId(campaignId)
					.roleId(roleId)
					.name(name)
					.description(description)
					.phoneNumber(phoneNumber)
					.videoLink(videoLink)
					.when(when)
				.build();
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.addAppointment(appointment, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).addAppointment(campaignId, roleId, name, description, phoneNumber, videoLink, when, userId);
		
	}
	
	/**
	* Tests updating an Appointment to a Campaign 
	*/
	@Test 
	void testUpdateAddAppointment() {
		
		final String 			userId 			= "rec1";
		final UUID 				appointmentId	= UUID.randomUUID();
		final String 			name			= "client meeting";
		final String 			description		= "kennismaking met de bank";
		final String 			videoLink		= "https://www.vid.com/ssefs33";
		final String 			phoneNumber		= "0031 111 222 000";
		final ZonedDateTime 	when			= ZonedDateTime.of(LocalDateTime.of(2026,  4, 1, 20, 8, 12), ZoneId.systemDefault());
		
		UpdateAppointmentAPIInbound appointment = UpdateAppointmentAPIInbound
				.builder()
					.name(name)
					.description(description)
					.phoneNumber(phoneNumber)
					.videoLink(videoLink)
					.when(when)
				.build();
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.updateAppointment(appointmentId, appointment, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).updateAppointment(appointmentId, name, description, phoneNumber, videoLink, when, userId);
		
	}
	
	/**
	* Tests the Deletion of an Appointment
	*/
	@Test
	void testDeleteAppointment() {
		
		final String 	userId 			= "rec1";
		final UUID 		appointmentId	= UUID.randomUUID();
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.deleteAppointment(appointmentId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).deleteAppointment(appointmentId, userId);
		
	}
	
	/**
	* Tests adding of a Document to a Campaign
	 * @throws IOException 
	*/
	@Test
	void testAddDocument() throws IOException {
	
		final String 		userId 			= "rec1";
		final UUID 			campaignId		= UUID.randomUUID();
		final UUID 			roleId			= UUID.randomUUID();
		final String 		title			= "job spec #453";
		final DocumentType 	type			= DocumentType.PDF; 
		final MultipartFile mpf				= mock(MultipartFile.class);
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(mpf.getBytes()).thenReturn(new byte[] {});
		
		AddDocumentAPIInbound document = AddDocumentAPIInbound
				.builder()
					.campaignId(campaignId)
					.roleId(roleId)
					.title(title)
					.type(type)
				.build();
		
		ResponseEntity<Void> response = this.controller.addDocument(document, mpf, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).addDocument(campaignId, roleId, title, type, mpf.getBytes(), userId);
		
	}
	
	/**
	* Tests Deleting an existing Document from a Campaign
	*/
	@Test
	void testDeleteDocument() {
		
		final UUID 		documentId = UUID.randomUUID();
		final String 	userId 			= "rec1";
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		
		ResponseEntity<Void> response = this.controller.deleteDocument(documentId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).deleteDocument(documentId, userId);
		
	}
	
	/**
	* Tests adding a Role to an existing Campaign 
	*/
	@Test
	void testAddRole() {
		
		final UUID 		campaiginId 	= UUID.randomUUID();
		final String 	userId 			= "rec1";
		final String 	name 			= "Java developer";
		final String 	desc 			= "Java developer role for 6 months";
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.addRole(campaiginId, NewRoleAPIInbound
				.builder()
					.name(name)
					.description(desc)
				.build(), this.mockPrincipal);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		verify(this.mockCampaignService).addRole(campaiginId, name, desc, userId);
		
	}
	
	/**
	* Tests endpoint for deleting a Campaign 
	*/
	@Test
	void testDeleteCampaign() {
		
		final UUID 		campaignId 		= UUID.randomUUID();
		final String 	userId 			= "rec1";
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.deleteCampaign(campaignId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).deleteCampaign(campaignId, userId);
		
	}
	
	/**
	* Tests endpoint for deleting a Campaign 
	*/
	@Test
	void testDeleteRole() {
		
		final UUID 		roleId	 		= UUID.randomUUID();
		final String 	userId 			= "rec1";
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.deleteRole(roleId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).deleteRole(roleId, userId);
		
	}
	
	/**
	* Tests adding a Candidate to a Campaign
	*/
	@Test
	void  testAddCandidateToCampaign() {
		
		final UUID 		campaignId	 	= UUID.randomUUID();
		final String 	candidateId		= "11";
		final String 	userId 			= "rec1";
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.addCandidateToCampaign(campaignId, candidateId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).addCandidateToCampaign(campaignId, null, candidateId, userId);
		
	}
	
	/**
	* Tests adding a Candidate to a Campaign
	*/
	@Test
	void  testAddCandidateToRole() {
		
		final UUID 		campaignId	 	= UUID.randomUUID();
		final UUID 		roleId	 		= UUID.randomUUID();
		final String 	candidateId		= "11";
		final String 	userId 			= "rec1";
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.addCandidateToRole(campaignId, roleId, candidateId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).addCandidateToCampaign(campaignId, roleId, candidateId, userId);
		
	}
	
	
	
	
	
	
	/**
	* Tests deleting a Candidate to a Campaign
	*/
	@Test
	void  testDeleteCandidateFromCampaign() {
		
		final UUID 		campaignId	 	= UUID.randomUUID();
		final String 	candidateId		= "11";
		final String 	userId 			= "rec1";
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.deleteCandidateFromCampaign(campaignId, candidateId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).deleteCandidateFromCampaign(campaignId, null, candidateId, userId);
		
	}
	
	/**
	* Tests deleting a Candidate from a Campaign
	*/
	@Test
	void  testDeleteCandidateFromRole() {
		
		final UUID 		campaignId	 	= UUID.randomUUID();
		final UUID 		roleId	 		= UUID.randomUUID();
		final String 	candidateId		= "11";
		final String 	userId 			= "rec1";
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		
		ResponseEntity<Void> response = this.controller.deleteCandidateFromRole(campaignId, roleId, candidateId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockCampaignService).deleteCandidateFromCampaign(campaignId, roleId, candidateId, userId);
		
	}
	
	/**
	* Tests retrieval of the Document requested by the documentId
	*/
	//@Test
	//void testFetchCampaignDocument() {
		
	//	final UUID 				documentId 	= UUID.randomUUID();
	//	final String 			userId 		= "rec1";
	//	final String 			title		= "A title"; 
		
	//	when(this.mockPrincipal.getName()).thenReturn(userId);
	//	when(this.mockCampaignService.fetchCampaignDocument(documentId, userId)).thenReturn(Document.builder().title(title).build());
		
	//	ResponseEntity<CampaignDocumentAPIOutbound> response = this.controller.fetchCampaignDocument(documentId, mockPrincipal);
		
	//	assertEquals(HttpStatus.OK, response.getStatusCode());
		
	//	verify(this.mockCampaignService).fetchCampaignDocument(documentId, userId);
		
	//	assertNotNull(response.getBody());
	//	assertEquals(title, response.getBody().title());
		
	//}
	
}