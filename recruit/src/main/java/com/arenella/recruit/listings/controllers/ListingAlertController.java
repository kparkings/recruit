package com.arenella.recruit.listings.controllers;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.listings.services.ListingAlertService;

/**
* REST API For ListingAlerts
* @author K Parkings
*/
@RestController
public class ListingAlertController {

	@Autowired
	private ListingAlertService listingAlertService;
	
	/**
	* Adds a new ListingAlert
	* @param alert - Details of ListingAlert to create
	*/
	//@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER') or hasRole('ROLE_CANDIDATE')")
	@PostMapping(path="/public/listing-alert", consumes="application/json", produces="application/json")
	public void addListingAlert(@RequestBody ListingAlertAPIIncoming alert, Principal principal) {
		this.listingAlertService.addListingAlert(ListingAlertAPIIncoming.convertToDomain(alert), principal);
	}
	
	/**
	* Deletes an existing ListingAlert
	* 
	* NOTE: Security - As User does not need to be registered to create an Alert and can delete 
	* via a link in an email without logging in anyone will be able to delete an Alert.
	* 
	* The fact that we are using UUIDs as identifiers minimise's the risk as it will not be easy to 
	* discovers the UUIDs in use also the impact of an Alert being deleted by an attacker is minimal.
	* 
	* @param alertId - Unique id of the Alert to delete
	*/
	@DeleteMapping(path="/public/listing-alert/{alertId}", consumes="application/json", produces="application/json")
	public void deleteListingAlert(@PathVariable("alertId")UUID alertId) {
		this.listingAlertService.deleteListingAlert(alertId);
	}
	
	
	
	
	
}
