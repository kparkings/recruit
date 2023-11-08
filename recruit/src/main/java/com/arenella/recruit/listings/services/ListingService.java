package com.arenella.recruit.listings.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arenella.recruit.adapters.actions.GrantCreditCommand;
import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.CandidateListingContactRequest;
import com.arenella.recruit.listings.controllers.ListingContactRequest;

/**
* Defines services relating to Listings
* @author K Parkings
*/
public interface ListingService {

	/**
	* Adds a new Listing
	* @param listing 			- Details of new Listing
	* @param postToSocialMedia 	- Whether or not to post the Listing on Social Media
	* @return Unique Id for new Listing
	*/
	public UUID addListing(Listing listing, boolean postToSocialMedia);
	
	/**
	* Updates an Existing Listing
	* @param listingId 	- Unique Id of Listing to update
	* @param listing 	- Updated details of the Listing
	*/
	public void updateListing(UUID listingId, Listing listing);

	/**
	* Deletes an existing Listing
	* @param listingId
	*/
	public void deleteListing(UUID listingId);
	
	/**
	* Retrieves a Page of listings matching the filters
	* @return Page of Matching Listings
	*/
	public Page<Listing> fetchListings(ListingFilter filters, Pageable pageable);

	/**
	* Registers that a Listing was Viewed
	* @param event - Contains details of the viewing of the Listing
	*/
	public void registerListingViewedEvent(ListingViewedEvent event);

	/**
	* Sends a Contact request for a Listing to the owner of the Listing
	* @param contactRequest - Details of contactRequest to be Sent
	*/
	public void sendContactRequestToListingOwner(ListingContactRequest contactRequest);

	/**
	* Sends a Contact request for a Listing from an existing Candidate to the owner of the Listing
	* @param contactRequest - Details of contactRequest to be Sent
	*/
	public void sendContactRequestFomCandidateToListingOwner(CandidateListingContactRequest contactRequest);

	/**
	* Sets the Listings for the recruiter as being active
	* @param recruiterId - Id of the recruiter owning the Listings
	*/
	public void disableListingsForRecruiter(String recruiterId);

	/**
	* Sets the Listings for the recruiter as not being active
	* @param recruiterId - Id of the recruiter owning the Listings
	*/
	public void enableListingsForRecruiter(String recruiterId);

	/**
	* Sets Credits available to Recruiters
	* @param command - credits info
	*/
	public void updateCredits(GrantCreditCommand command);

	/**
	* Returns whether or not the user has Credits. If the User does not
	* have credit based access then this will be true. Otherwise true will 
	* be returned when the user has remaining credits and false where they
	* do not
	* @param userName - id of the user
	* @return Whether or not the User has remaining credits
	*/
	//public boolean hasCreditsLeft(String userName);

	/**
	* Returns whether the User either does not have Credit based access or does 
	* have credit based access to curriculums but has remaining credits
	* @param name - id of Authorized User
	* @return whether User can access Curriculums
	*/
	public Boolean doCreditsCheck(String name);

	/**
	* Updates a Recruiters credits. Decrementing them by 1
	* @param userId - id of User who used a Credit
	*/
	public void useCredit(String userId);


}