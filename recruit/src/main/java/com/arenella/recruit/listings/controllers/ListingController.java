package com.arenella.recruit.listings.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import com.arenella.recruit.candidates.controllers.CandidateAPIOutbound;
import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.beans.ListingFilter.ListingFilterBuilder;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
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
		
		this.service.updateListing(listingId, ListingAPIInbound.convertToListing(listing));
		
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
		
		this.service.deleteListing(listingId);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/**
	* Returns all Listings. Unprotected URL available to 
	* unregistered users
	* @return All available listings
	*/
	@GetMapping(value="/listing")
	public Page<ListingAPIOutbound> fetchListings(Pageable pageable, @RequestParam(required=false)String recruiterId){
		
		ListingFilterBuilder filterBuilder = ListingFilter.builder();
		
		if (StringUtils.hasText(recruiterId)) {
			filterBuilder.ownerId(recruiterId);
		}
		
		return service.fetchListings(filterBuilder.build(), pageable).map(listing -> ListingAPIOutbound.convertFromListing(listing));	
		
	}
	
	/**
	* Returns all Listings. Unprotected URL available to 
	* unregistered users
	* @return All available listings
	*/
	@GetMapping(value="/listing/public/")
	public Page<ListingAPIOutbound> fetchListingsPubilc(Pageable pageable){
		
		ListingFilterBuilder filterBuilder = ListingFilter.builder();
		
		//TODO: [KP] Make new outbound with only info (no views) relevant to the public jobboard view
		
		return service.fetchListings(filterBuilder.build(), pageable).map(listing -> ListingAPIOutbound.convertFromListing(listing));	
		
	}
	
	/**
	* Returns single Listing based upon the ListingId
	* @return Listing associated with listingId
	*/
	//@GetMapping(value="/listing/{listingId}")
	//public ResponseEntity<CandidateAPIOutbound> fetchListing(@PathVariable UUID listingId){
	//	return ResponseEntity.status(HttpStatus.OK).body(null);
	//}
	
	/**
	* Logs a viewed Listing event
	* @param listingId
	*/
	@PostMapping(value="/listing/public/viewedEvent/{listingId}")
	public ResponseEntity<Void> registerListingViewedEvent(@PathVariable UUID listingId) {
		
		this.service.registerListingViewedEvent(ListingViewedEvent.builder().listingId(listingId).build());
		
		return ResponseEntity.status(HttpStatus.OK).build();
		
	}
	
}