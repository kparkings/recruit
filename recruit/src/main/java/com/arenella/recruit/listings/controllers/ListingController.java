package com.arenella.recruit.listings.controllers;

import java.security.Principal;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.Listing.LISTING_AGE;
import com.arenella.recruit.listings.beans.Listing.language;
import com.arenella.recruit.authentication.spring.filters.ClaimsUsernamePasswordAuthenticationToken;

import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.CandidateListingContactRequest.CandidateListingContactRequestBuilder;
import com.arenella.recruit.listings.controllers.ListingContactRequest.ListingContactRequestBuilder;
import com.arenella.recruit.listings.services.ListingService;
import com.arenella.recruit.listings.utils.ListingAlertHitTesterUtil;

/**
* REST API for interacting with Listings. A Listing is a type of role 
* a recruiter can post. That can be a permanent job, a freelance roles etc
* @author K Parkings
*/
@RestController
public class ListingController {

	@Autowired
	private ListingService 				service;
	
	@Autowired
	private ListingAlertHitTesterUtil 	listingAlertHitUtil;
	
	/**
	* Adds a new listing
	* @param recruiterId - Id of the recruiter who is the owner of the listing
	* @param listing	 - Details of the listing to be created
	* @param principal	 - Contains logged in User id
	* @return Unique identifier of the Listing
	*/
	@PreAuthorize("hasRole('ROLE_RECRUITER')")
	@PostMapping(value="/listing/")
	public ResponseEntity<UUID> addListing(@RequestBody ListingAPIInbound listingAPIInbound, Principal principal){

		UUID id = service.addListing(ListingAPIInbound.convertToListing(listingAPIInbound), listingAPIInbound.isPostToSocialMedia());
		
		this.performCreditCheck(principal); //TODO: [KP] Check this should not be above the first line. Im sure it was working ok but potentially this allows unlimited listings ot be posted
		
		Listing listing = ListingAPIInbound.convertToListing(listingAPIInbound);
		listing.setListingId(id);
		
		this.listingAlertHitUtil.registerListing(listing);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(id);
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
	public Page<ListingAPIOutbound> fetchListings(	@RequestParam(required=false)String recruiterId, 
													@RequestParam(required=false)LISTING_AGE listingAge,
													Pageable pageable){

		ListingFilter filters = 
				ListingFilter
				.builder()
					.ownerId(recruiterId)
					.listingAge(listingAge)
					.active(true)
				.build();
		
		return service.fetchListings(filters, pageable).map(ListingAPIOutbound::convertFromListing);	
		
	}
	
	/**
	* Returns all Listings. Unprotected URL available to 
	* unregistered users
	* @return All available listings
	*/
	@PostMapping(value="/listing/public/")
	public Page<ListingAPIOutboundPublic> fetchListingsPubilc(	@RequestBody(required = false) ListingSearchRequestAPIInbound searchRequest, 
																Pageable pageable){
		
		ListingFilter filters = 
				ListingFilter
				.builder()
					.searchTerm(searchRequest.getSearchTerm().orElse(null))
					.type(searchRequest.getContractType().orElse(null))
					.listingAge(searchRequest.getMaxAgeOfPost().orElse(null))
					.geoZones(searchRequest.getGeoZones())
					.countries(searchRequest.getCountries())
					.active(true)
				.build();

		return service.fetchListings(filters, pageable).map(ListingAPIOutboundPublic::convertFromListing);	
		
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
	
	/**
	* Allows an Authenticated Candidate to send a message to the Recruiter owning the Listing
	* @param listingId 	- Id of the listing
	* @param message	- Message to be sent
	* @param file		- Optional file containing CV. If no CV send will use candidates default
	* @param principal	- Authenticated Candidate User
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_CANDIDATE')")
	@PostMapping(value="/listing/{listingId}/candidate-contact-recruiter",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Void> sendContactRequestToListingOwnerFromCandidate(@PathVariable("listingId") UUID listingId, @RequestPart("message")String message, @RequestPart("attachment") Optional<MultipartFile> file, Principal principal){
		
		CandidateListingContactRequestBuilder builder = CandidateListingContactRequest.builder();
		
		builder
			.listingId(listingId)
			.candidateId(principal.getName()) 
			.message(message);
		
			if (file.isPresent()) {
				builder.attachment(file.get());
			}
		
		this.service.sendContactRequestFomCandidateToListingOwner(builder.build());
		
		return ResponseEntity.ok().build();	
		
	}
	
	/**
	* Performs a check to see if the User passes the credit check. That is. If the users access to 
	* curriculums is via credits does the User have remaining credits. If the Users access is not 
	* dependent upon credits or the User has remaining credits then returns true else false
	* @param principal - Authroized User
	* @return whether the creditCheck passed for the User
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER')")
	@GetMapping(value="/listing/creditCheck")
	public ResponseEntity<Boolean> passesCreditCheck(Principal principal){
		
		ClaimsUsernamePasswordAuthenticationToken 	user 			= (ClaimsUsernamePasswordAuthenticationToken)principal;
		boolean 									isRecruiter 	= user.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_RECRUITER")).findAny().isPresent();
		boolean 									useCredits 		= (Boolean)user.getClaim("useCredits").get();
		
		if (isRecruiter && useCredits) {
			return ResponseEntity.ok(this.service.doCreditsCheck(principal.getName()));
		}
		
		return ResponseEntity.ok(true);
	}
	
	/**
	* Returns the number of credits the Candidate has left
	* @return
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@GetMapping(path="listing/_credits")
	public ResponseEntity<Integer> fetchRemainingCreditCount(Principal principal){
		return ResponseEntity.ok(this.service.getCreditCountForUser(principal.getName()));
	}
	
	/**
	* Returns a list of Supported languages that a role can require the candiadte 
	* to speak
	* @return supported languages
	*/
	@GetMapping(path="/listing/public/languages")
	public ResponseEntity<Set<language>> fetchSupportedLanguages(){
		return ResponseEntity.ok(Arrays.stream(language.values()).collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Performs check to ensure user either doesnt use credit based access or 
	* has enough credits to perform an operation
	* @param principal - currently logged in user
	*/
	private void performCreditCheck(Principal principal) {
		
		ClaimsUsernamePasswordAuthenticationToken 	user 			= (ClaimsUsernamePasswordAuthenticationToken)principal;
		boolean 									isRecruiter 	= user.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_RECRUITER")).findAny().isPresent();
		boolean 									useCredits 		= (Boolean)user.getClaim("useCredits").get();
		
		if (isRecruiter && useCredits) {
			this.service.useCredit(user.getName());
		}
		
	}
}