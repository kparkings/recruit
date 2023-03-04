package com.arenella.recruit.listings.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.listings.beans.Listing.listing_type;

import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.beans.ListingFilter.ListingFilterBuilder;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.ListingContactRequest.ListingContactRequestBuilder;
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
	public Page<ListingAPIOutboundPublic> fetchListingsPubilc(	@RequestParam(required = false) listing_type listingType, 
																Pageable pageable){
		
		ListingFilterBuilder filterBuilder = ListingFilter.builder();
		
		if (Optional.ofNullable(listingType).isPresent()) {
			filterBuilder.type(listingType);
		}
		
		return service.fetchListings(filterBuilder.build(), pageable).map(listing -> ListingAPIOutboundPublic.convertFromListing(listing));	
		
	}
	
	/**
	* Logs a viewed Listing event
	* @param listingId
	*/
	@PostMapping(value="/listing/public/viewedEvent/{listingId}")
	public ResponseEntity<Void> registerListingViewedEvent(@PathVariable UUID listingId) {
		
		this.service.registerListingViewedEvent(ListingViewedEvent.builder().listingId(listingId).build());
		
		return ResponseEntity.status(HttpStatus.OK).build();
		
	}
	
	/**
	* Posts a response from a Candidate to the Listing
	* @return ResponeEntity
	*/
	@PostMapping(value="/listing/public/{listingId}/contact-recruiter",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Void> sendContactRequestToListingOwner(@PathVariable("listingId") UUID listingId, @RequestPart("senderName")String senderName, @RequestPart("senderEmail")String senderEmail, @RequestPart("message")String message, @RequestPart("attachment") Optional<MultipartFile> file){
		
		ListingContactRequestBuilder builder = ListingContactRequest.builder();
		
		builder
			.listingId(listingId)
			.message(message)
			.senderEmail(senderEmail)
			.senderName(senderName);
		
			if (file.isPresent()) {
				builder.attachment(file.get());
			}
		
		this.service.sendContactRequestToListingOwner(builder.build());
		
		return ResponseEntity.ok().build();	
		
	}
	
}