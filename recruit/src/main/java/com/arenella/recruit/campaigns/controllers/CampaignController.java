package com.arenella.recruit.campaigns.controllers;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
* Rest API for working with Campaign's 
*/
@RestController
public class CampaignController {

	/**
	* Returns a high level collection of Campaigns the authenticated user is a participant in
	* @param currentUser - Current authenticated user 
	* @return Campaigns the User is a participant in
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@GetMapping(path="campaign")
	public ResponseEntity<Set<CampaignOverviewAPIOutbound>> fetchCampaignsForUser(Principal currentUser) {
		return null;
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
		return null;
	}
	
	/**
	* Creates a new Campaign with the currentUser as the default admin
	* @param currentUser
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PostMapping(path="campaign")
	public ResponseEntity<Void> addNewCampaign(@RequestBody NewCampaignAPIInbound campaign, Principal currentUser) {
		return null;
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
		return null;
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
		return null;
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
		return null;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PutMapping(path="campaign/note/{noteId}")
	public ResponseEntity<Void> updateNote(@PathVariable("noteId")UUID noteId, @RequestBody UpdateNoteAPIInbound note, Principal currentUser){
		return null;
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
		return null;
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
		return null;
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
		return null;
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
		return null;
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
	public ResponseEntity<Void> addDocument(@RequestPart("document") AddDocumentAPIInbound document, @RequestPart("documentBytes")Optional<MultipartFile> documentBytes, Principal principal){
		return null;
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
		return null;
	}
	
	/**
	* Retrieves the bytes for a specific Document
	* @param documentId - Id of the document to retrieve
	* @param principal  - Currently authenticated User
	* @return Bytes of Document
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@GetMapping(path="campaign/document/{documentId}")
	public ResponseEntity<CampaignDocumentAPIOutbound> fetchCampaignDocument(@PathVariable("documentId") UUID documentId, Principal principal) {
		return null;
	}
	
}