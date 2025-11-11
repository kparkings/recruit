package com.arenella.recruit.listings.services;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.arenella.recruit.authentication.spring.filters.ClaimsUsernamePasswordAuthenticationToken;
import com.arenella.recruit.listings.beans.ListingAlert;
import com.arenella.recruit.listings.beans.ListingAlertFilterOptions;
import com.arenella.recruit.listings.beans.ListingAlertSentEvent;
import com.arenella.recruit.listings.dao.ListingAlertDao;
import com.arenella.recruit.listings.dao.ListingAlertSentEventDao;

/**
* Services for interacting with ListingAlert's
* @author K Parkings
*/
@Service
public class ListingAlertServiceImpl implements ListingAlertService{

	private ListingAlertDao listingAlertDao;
	private ListingAlertSentEventDao listingAlertSentEventDao;
	
	/**
	* Constructor
	* @param listingAlertDao 			- DAO access to Listings
	* @param listingAlertSentEventDao	- DAO for events relating to alerts 
	*/
	public ListingAlertServiceImpl(ListingAlertDao listingAlertDao, ListingAlertSentEventDao listingAlertSentEventDao) {
		this.listingAlertDao 			= listingAlertDao;
		this.listingAlertSentEventDao 	= listingAlertSentEventDao;
	}
	
	/**
	* Refer to the ListingAlertService for details 
	*/
	@Override
	public void addListingAlert(ListingAlert alert, Principal currentUser) {
		
		alert.initializeAlert();
		
		if (currentUser != null && this.isCandidate(currentUser)) {
			alert.setUserId(Long.valueOf(currentUser.getName()));
		}
		
		this.listingAlertDao.addAlert(alert);
		
	}

	/**
	* Refer to the ListingAlertService for details 
	*/
	@Override
	public Set<ListingAlert> fetchListingAlerts(ListingAlertFilterOptions filters) {
		return this.listingAlertDao.findAll(filters);
	}

	/**
	* Refer to the ListingAlertService for details 
	*/
	@Override
	public void deleteListingAlert(UUID id) {
		this.listingAlertDao.deleteById(id);
		
	}
	
	/**
	* Refer to the ListingAlertService for details 
	*/
	@Override
	public void registerListingAlertEmailSent(UUID listingId) {
		this.listingAlertSentEventDao.saveEvent(new ListingAlertSentEvent(UUID.randomUUID(), listingId, LocalDate.now()));
	}
	
	/**
	* Returns whether or not the user is a Candidate
	* @return whether or not the user is a Candidate
	*/
	private boolean isCandidate(Principal principal) {
		return this.hasRole(principal, "ROLE_CANDIDATE");
	}
	
	/**
	* Returns whether or not the user has a given Role
	* @return whether or not the user has a given Role
	*/
	private boolean hasRole(Principal principal, String role) {
		
		ClaimsUsernamePasswordAuthenticationToken user = (ClaimsUsernamePasswordAuthenticationToken)principal;
		
		return user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
		
	}
	
}