package com.arenella.recruit.listings.controllers;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
	@GetMapping(path="/public/listing-alert/{alertId}/_delete")
	public String deleteListingAlert(@PathVariable("alertId")UUID alertId) {
	
		this.listingAlertService.deleteListingAlert(alertId);
		
		return "You have been successfully unsubscribed";
	}
	
	
	
	
	
}
