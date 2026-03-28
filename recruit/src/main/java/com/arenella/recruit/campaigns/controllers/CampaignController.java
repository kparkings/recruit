package com.arenella.recruit.campaigns.controllers;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<CampaignAPIOutbound> fetchCampaign(@PathVariable("campaignId") UUID campaiginId, Principal currentUser) {
		return null;
	}
	
	/**
	* Creates a new Campaign with the currentUser as the default admin
	* @param currentUser
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PostMapping(path="campaign")
	public ResponseEntity<Void> addNewCampaign(@RequestBody NewCampaignAPIInbound campaigin, Principal currentUser) {
		return null;
	}
	
}