package com.arenella.recruit.listings.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.candidates.controllers.CandidateAPIOutbound;
import com.arenella.recruit.listings.services.ListingService;

/**
* REST API for interacting with Listings. A Listing is a type of role 
* a recruiter can post. That can be a permanent job, a freelance roles etc
* @author K Parkings
*/
@RestController
public class ListingController {

	@Autowired
	private ListingService service;
	
	/**
	* Adds a new listing
	* @param recruiterId - Id of the recruiter who is the owner of the listing
	* @param listing	 - Details of the listing to be created
	* @return Unique identifier of the Listing
	*/
	@PreAuthorize("hasRole('ROLE_RECRUITER')")
	@PostMapping(value="/listing/")
	public ResponseEntity<UUID> addListing(@RequestBody ListingAPIInbound listing){
		return ResponseEntity.status(HttpStatus.CREATED).body(service.addListing(ListingAPIInbound.convertToListing(listing), listing.isPostToSocialMedia()));
	}
	
	/**
	* Updates an Existing Listing
	* @param listing - New version of listing
	* @return Status of update attempt
	*/
	@PreAuthorize("hasRole('ROLE_RECRUITER')")
	@PutMapping(value="/listing/{listingId}")
	public ResponseEntity<Void> updateListing(@PathVariable("listingId") UUID listingId, @RequestBody ListingAPIInbound listing){
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/**
	* Deletes an existing Listing 
	* @param listingId - Unique id of the Listing to delete
	* @return Status of deletion attempt
	*/
	@PreAuthorize("hasRole('ROLE_RECRUITER')")
	@DeleteMapping(value="/listing/{listingId}")
	public ResponseEntity<Void> deleteListing(@PathVariable("listingId") UUID listingId){
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/**
	* Returns all Listings. Unprotected URL available to 
	* unregistered users
	* @return All available listings
	*/
	@GetMapping(value="/listing")
	public ResponseEntity<Page<CandidateAPIOutbound>> fetchListings(Pageable pageable, @RequestParam(required=false)String recruiterId){
		return ResponseEntity.status(HttpStatus.OK).body(Page.empty());
	}
	
}