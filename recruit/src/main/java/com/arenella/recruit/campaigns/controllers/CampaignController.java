package com.arenella.recruit.campaigns.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.CampaignLogo.PHOTO_FORMAT;
import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Document;
import com.arenella.recruit.campaigns.services.CampaignContactService;
import com.arenella.recruit.campaigns.services.CampaignService;
import com.arenella.recruit.curriculum.beans.Curriculum;

/**
* Rest API for working with Campaign's 
*/
@RestController
public class CampaignController {

	private final CampaignService 			campaignService;
	private final CampaignContactService 	contactService;
	
	/**
	* Constructor
	* @param campaignService - Services for interacting with Campaigin's
	* @param contactService  - Services for interacting with Contact's
	*/
	public CampaignController(CampaignService campaignService, CampaignContactService contactService) {
		this.campaignService 	= campaignService;
		this.contactService 	= contactService;
	}
	
	/**
	* Returns a high level collection of Campaigns the authenticated user is a participant in
	* @param currentUser - Current authenticated user 
	* @return Campaigns the User is a participant in
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@GetMapping(path="campaign")
	public ResponseEntity<Set<CampaignOverviewAPIOutbound>> fetchCampaignsForUser(Principal currentUser) {
		
		LinkedHashSet<CampaignOverviewAPIOutbound> campaigns = this.campaignService.fetchCampaignsForUser(currentUser.getName())
				.stream()
				.map(c -> CampaignOverviewAPIOutbound.builder().from(c).build()).collect(Collectors.toCollection(LinkedHashSet::new));
		
		return new ResponseEntity<>(campaigns,  HttpStatus.OK);

	}
	
	/**
	* Returns the Campaign requested Campaign
	* @param campaiginId - Unique Id of the Campaign to return
	* @param currentUser - Currently authenticated User
	* @return Campaign
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@GetMapping(path="campaign/{campaignId}")
	public ResponseEntity<CampaignAPIOutbound> fetchCampaign(@PathVariable("campaignId") UUID campaignId, Principal currentUser) {
		
		Campaign 		campaign 	= this.campaignService.fetchCampaignById(campaignId, currentUser.getName());
		Set<Contact> 	contacts 	= this.contactService.fetchContactsById(campaign.getParticipations().stream().map(p -> p.getContactId()).collect(Collectors.toSet()));
		
		return new ResponseEntity<>(CampaignAPIOutbound.builder().from(campaign, contacts).build(), HttpStatus.OK);
	}
	
	/**
	* Creates a new Campaign with the currentUser as the default admin
	* @param currentUser
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PostMapping(path="campaign",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> addNewCampaign(@RequestPart("command") NewCampaignAPIInbound campaign, @RequestPart("logo") Optional<MultipartFile> logo, Principal currentUser) throws Exception{
		
		byte[] logoBytes = logo.isPresent()  ? logo.get().getBytes() : null;
		
		CampaignLogo logoX = Optional.of(logo).map(_ -> new CampaignLogo(logoBytes,PHOTO_FORMAT.jpeg)).orElse(null);
		
		this.campaignService.addCampaign(campaign.getName(), campaign.getDescription(), Optional.ofNullable(logoX).orElse(null), currentUser.getName());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	/**
	* Deletes a Campaign and all its associated data
	* @param campaiginId - Id of Campaign to delete
	* @param currentUser - currently logged in User
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@DeleteMapping(path="campaign/{campaignId}")
	public ResponseEntity<Void> deleteCampaign(@PathVariable("campaignId")UUID campaignId, Principal currentUser) {
		
		this.campaignService.deleteCampaign(campaignId, currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	* Adds a Role to an existing Campaign
	* @param campaignId - Id of the Campaign to add the Role to
	* @param command	- Details of the new Campaign
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PostMapping(path="campaign/{campaignId}/role")
	public ResponseEntity<Void> addRole(@PathVariable("campaignId") UUID campaignId, @RequestBody NewRoleAPIInbound command, Principal currentUser) {
		
		this.campaignService.addRole(campaignId, command.getName(), command.getDescription(), currentUser.getName());
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	
	}	
	
	/**
	* Deletes a Role and all its associated data
	* @param roleId - Id of Role to delete
	* @param currentUser - currently logged in User
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@DeleteMapping(path="campaign/{campaignId}/role/{roleId}")
	public ResponseEntity<Void> deleteRole(@PathVariable("campaignId")UUID campaignId, @PathVariable("roleId")UUID roleId, Principal currentUser) {
		
		this.campaignService.deleteRole(roleId, currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	* Adds a new Participation at either Campaign or Role level.
	* @param participation - Details of the new Participation
	* @param currentUser   - Currently authenticated User
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PostMapping(path="campaign/participant")
	public ResponseEntity<Void> addParticipation(@RequestBody AddParticipationAPIInbound participation, Principal currentUser){
		
		this.campaignService.addParticipationToCampaign(participation.getContactId(), participation.getCampaignId(), participation.getRoleId().orElse(null), participation.getType(), currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	* Deletes an existing Participation from a Campaign or Role
	* @param participationId - Id of Participation to 
	* @param currentUser     - Currently authenticated User
	* @return Builder
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@DeleteMapping(path="campaign/participant/{participationId}")
	public ResponseEntity<Void> deleteParticipation(@PathVariable("participationId") UUID participationId, Principal currentUser){
		this.campaignService.deleteParticipation(participationId, currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	* Adds a Note to the Campaign or Role level
	* @param note			- Details of the Note
	* @param currentUser	- Currently authenticated User
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PostMapping(path="campaign/note")
	public ResponseEntity<Void> addNote(@RequestBody AddNoteAPIInbound note, Principal currentUser){
		this.campaignService.addNoteToCampaign(note.getCampaignId(), note.getRoleId().orElse(null), note.getTitle().orElse(null), note.getText(), currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PutMapping(path="campaign/note/{noteId}")
	public ResponseEntity<Void> updateNote(@PathVariable("noteId")UUID noteId, @RequestBody UpdateNoteAPIInbound note, Principal currentUser){
		this.campaignService.updateNote(noteId, note.getTitle().orElse(null), note.getText(), currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	* Deletes an existing Note
	* @param noteId			- Id of the Note to delete
	* @param currentUser	- Currently authenticated User
	* @return
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@DeleteMapping(path="campaign/note/{noteId}")
	public ResponseEntity<Void> deleteNote(@PathVariable("noteId") UUID noteId, Principal currentUser){
		this.campaignService.deleteNote(noteId, currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	* Adds an Appointment to the Campaign or Role level
	* @param appointment - Details of the Appointment
	* @param currentUser - Currently authenticated User
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PostMapping(path="campaign/appointment")
	public ResponseEntity<Void> addAppointment(@RequestBody AddAppointmentAPIInbound appointment, Principal currentUser){
		
		this.campaignService
			.addAppointment(
					appointment.getCampaignId(), 
					appointment.getRoleId().orElse(null), 
					appointment.getName(), 
					appointment.getDescription(), 
					appointment.getPhoneNumber().orElse(null),
					appointment.getVideoLink().orElse(null),
					appointment.getWhen(),
					currentUser.getName());
		
		return new ResponseEntity<>(HttpStatus.OK);
	
	}
	
	/**
	* Updates an existing Appointment
	* @param appointmentId 	- Id of the appointment to update
	* @param appointment   	- Details of the appointment
	* @param currentUser	- Currently authorized User
	* @return ResponeEntity
	 ù*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PutMapping(path="campaign/appointment/{appointmentId}")
	public ResponseEntity<Void> updateAppointment(@PathVariable("appointmentId")UUID appointmentId, @RequestBody UpdateAppointmentAPIInbound appointment, Principal currentUser){
		this.campaignService.updateAppointment(appointmentId, appointment.getName(), appointment.getDescription(), appointment.getPhoneNumber().orElse(null), appointment.getVideoLink().orElse(null), appointment.getWhen(), currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	* Deletes an existing Appointment
	* @param appointmentId - Id of the Appointment to delete
	* @param currentUser   - Currently authenticated User
	* @return Builder
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@DeleteMapping(path="campaign/appointment/{appointmentId}")
	public ResponseEntity<Void> deleteAppointment(@PathVariable("appointmentId") UUID appointmentId, Principal currentUser){
		this.campaignService.deleteAppointment(appointmentId, currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	* Adds a new Document at the Campaign or Role level
	* @param document		- Contains document metadata
	* @param documentBytes	- bytes of the Actual document 	
	* @param principal		- Currently authenticated User
	* @return ResponseENtity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PostMapping(path="campaign/document",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Void> addDocument(@RequestPart("document") AddDocumentAPIInbound document, @RequestPart("documentFile")MultipartFile documentFile, Principal principal) throws IOException{
		this.campaignService.addDocument(document.getCampaignId(), document.getRoleId().orElse(null), document.getTitle(), document.getType(), documentFile.getBytes(), principal.getName());
		return new ResponseEntity<>(HttpStatus.OK);	
	}
	
	/**
	* Deletes an existing Document
	* @param documentId		- Id of the Document to delete
	* @param currentUser	- Currently authenticated User
	* @return Builder
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@DeleteMapping(path="campaign/document/{documentId}")
	public ResponseEntity<Void> deleteDocument(@PathVariable("documentId") UUID documentId, Principal currentUser){
		this.campaignService.deleteDocument(documentId, currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);	
	}
	
	/**
	* Adds a Candidate to a Campaign
	* @param campaignId		- Id of the Campaign to add the Candidate to
	* @param candidateId	- Id of the Candidate to add to the Campaign
	* @return ReponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PutMapping(path="campaign/{campaignId}/candidate/{candidateId}")
	public ResponseEntity<Void> addCandidateToCampaign(@PathVariable("campaignId") UUID campaignId, @PathVariable("candidateId") String candidateId, Principal currentUser){
		this.campaignService.addCandidateToCampaign(campaignId, null, candidateId, currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);	
	}
	
	/**
	* Adds a Candidate to a Role
	* @param campaignId		- Id of the Campaign to add the Candidate to
	* @param roleId			- Id of the Role to add the Candidate to
	* @param candidateId	- Id of the Candidate to add to the Campaign
	* @return ReponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PutMapping(path="campaign/{campaignId}/role/{roleId}/candidate/{candidateId}")
	public ResponseEntity<Void> addCandidateToRole(@PathVariable("campaignId") UUID campaignId, @PathVariable("roleId") UUID roleId, @PathVariable("candidateId") String candidateId, Principal currentUser){
		this.campaignService.addCandidateToCampaign(campaignId, roleId, candidateId, currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);	
	}
	
	/**
	* Deletes a Candidate from a Campaign
	* @param campaignId		- Id of the Campaign to add the Candidate to
	* @param candidateId	- Id of the Candidate to add to the Campaign
	* @return ReponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@DeleteMapping(path="campaign/{campaignId}/candidate/{candidateId}")
	public ResponseEntity<Void> deleteCandidateFromCampaign(@PathVariable("campaignId") UUID campaignId, @PathVariable("candidateId") String candidateId, Principal currentUser){
		this.campaignService.deleteCandidateFromCampaign(campaignId, null, candidateId, currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);	
	}
	
	/**
	* Deletes a Candidate from a Role
	* @param campaignId		- Id of the Campaign to add the Candidate to
	* @param roleId			- Id of the Role to add the Candidate to
	* @param candidateId	- Id of the Candidate to add to the Campaign
	* @return ReponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@DeleteMapping(path="campaign/{campaignId}/role/{roleId}/candidate/{candidateId}")
	public ResponseEntity<Void> deleteCandidateFromRole(@PathVariable("campaignId") UUID campaignId, @PathVariable("roleId") UUID roleId, @PathVariable("candidateId") String candidateId, Principal currentUser){
		this.campaignService.deleteCandidateFromCampaign(campaignId, roleId, candidateId, currentUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);	
	}
	
	/**
	* Returns the bytes of a Document
	* @param documentId - Id of the Document to retrieve
	* @param principal  - Currently authenticated User
	* @return bytes of Document
	* @throws Exception
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@GetMapping(value = "/campaign/document/{documentId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public byte[] getDocumentAsPDF(@PathVariable("documentId")UUID documentId, Principal principal) throws Exception{
		
		Document 				document 		= this.campaignService.fetchDocumentById(documentId, principal.getName());
		byte[] 					fileBytes 		= null;
		ByteArrayOutputStream 	stream;
		
		fileBytes 	= document.getBytes();
		stream 		= new ByteArrayOutputStream(fileBytes.length);
		stream.write(fileBytes);
		
		return stream.toByteArray();
	}
	
}