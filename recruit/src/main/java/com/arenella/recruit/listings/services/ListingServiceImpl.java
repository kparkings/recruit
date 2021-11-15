package com.arenella.recruit.listings.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.dao.ListingDao;
import com.arenella.recruit.listings.dao.ListingEntity;

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
	public UUID addListing(Listing listing) {
		
		ListingEntity entity = ListingEntity.builder().build(); //TODO: Assign UUID to Entity before Save
		
		this.listingDao.save(entity);
		
		//TODO: Post on LinkedIN
		
		return UUID.randomUUID(); //TODO: Return UUID from new Entity
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

}