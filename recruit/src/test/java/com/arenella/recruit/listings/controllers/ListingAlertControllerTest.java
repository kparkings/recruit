package com.arenella.recruit.listings.controllers;

import java.security.Principal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.listings.beans.ListingAlert;
import com.arenella.recruit.listings.services.ListingAlertService;

/**
* Unit tests for the ListingAlertController class 
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class ListingAlertControllerTest {

	@Mock
	private ListingAlertService 	mockListingAlertService;
	
	@Mock
	private Principal 				mockPrincipal;
	
	@InjectMocks
	private ListingAlertController 	controller = new ListingAlertController();
	
	/**
	* Tests creation of ListingAlert
	* @throws Exception
	*/
	@Test
	void testAddListingAlert() {
	
		ListingAlertAPIIncoming alert = ListingAlertAPIIncoming.builder().build();
		
		controller.addListingAlert(alert, this.mockPrincipal);
		
		Mockito.verify(this.mockListingAlertService).addListingAlert(Mockito.any(ListingAlert.class), Mockito.any());
		
	}
	
	/**
	* Tests the Deletion of a Listing
	* @throws Exception
	*/
	@Test
	void testDeleteListing() {
		
		final UUID id = UUID.randomUUID();
		
		this.controller.deleteListingAlert(id);
		
		Mockito.verify(this.mockListingAlertService).deleteListingAlert(id);
		
	}
	
}
