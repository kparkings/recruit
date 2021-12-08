package com.arenella.recruit.listings.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.dao.ListingDao;
import com.arenella.recruit.listings.dao.ListingEntity;
import com.arenella.recruit.listings.exceptions.ListingValidationException;
import com.arenella.recruit.listings.exceptions.ListingValidationException.ListingValidationExceptionBuilder;

/**
* Services for working with Listings
* @author K Parkings
*/
@Service
public class ListingServiceImpl implements ListingService{

	@Autowired
	private ListingDao listingDao;
	
	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public UUID addListing(Listing listing, boolean postToSocialMedia) {
		
		listing.initializeAsNewListing();
		listing.setOwnerId(SecurityContextHolder.getContext().getAuthentication().getName());
		
		this.performValidation(listing);
		
		ListingEntity entity = ListingEntity.convertToEntity(listing, Optional.empty()); 
		
		this.listingDao.save(entity);
		
		if (postToSocialMedia) {
			//TODO: Post on LinkedIN 
		}
		
		return listing.getListingId(); 
	}

	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public void updateListing(UUID listingId, Listing listing) {
		
		ListingEntity 	entity 			= this.listingDao.findById(listingId).orElseThrow(() -> new IllegalArgumentException("Cannot update unknown listing: " + listingId));
		String 			currentUser		= SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		//Check Recruiter is owner of the Listing
		
	}

	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public void deleteListing(UUID listingId) {
		
		ListingEntity 	entity 			= this.listingDao.findById(listingId).orElseThrow(() -> new IllegalArgumentException("Cannot delete unknown listing: " + listingId));
		String 			currentUser		= SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		//Check Recruiter is owner of the Listing
	}

	/**
	* Refer to the Listing interface for details
	*/
	@Override
	public Page<Listing> fetchListings(ListingFilter filters, Pageable pageable) {
		return null;
	}
	
	/**
	* Listing to Validate
	* @param listing
	*/
	private void performValidation(Listing listing) {
		
		ListingValidationExceptionBuilder exception = ListingValidationException.builder();
		
		if (!StringUtils.hasText(listing.getOwnerName())) {
			exception.addFailedValidationField("ownerName", "An name must be provided");
		}
		
		if (!StringUtils.hasText(listing.getOwnerEmail())) {
			exception.addFailedValidationField("ownerEmail", "An Email Address must be provided");
		}
		
		if (!StringUtils.hasText(listing.getOwnerCompany())) {
			exception.addFailedValidationField("ownerCompanyName", "An Company name must be provided");
		}
		
		if (!StringUtils.hasText(listing.getTitle())) {
			exception.addFailedValidationField("title", "A Title must be provided");
		}
		
		if (!StringUtils.hasText(listing.getDescription())) {
			exception.addFailedValidationField("description", "A Description must be provided");
		}	
		
		ListingValidationException builtException = exception.build();
		
		if (builtException.hasFailedFields()) {
			throw builtException;
		}
		
	}
	
}