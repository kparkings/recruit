package com.arenella.recruit.recruiters.listings.controllers;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.arenella.recruit.candidates.controllers.CandidateAPIOutbound;
import com.arenella.recruit.listings.controllers.ListingAPIInbound;
import com.arenella.recruit.listings.controllers.ListingController;
import com.arenella.recruit.listings.services.ListingService;

/**
* Unit tests for the ListingController Class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ListingControllerTest {
	
	@Mock
	private Pageable 			mockPageable;
	
	@Mock
	private ListingService 		mockListingService;
	
	@Mock
	private	Authentication		mockAuthentication;
	
	@InjectMocks
	private ListingController 	controller 				= new ListingController();
	
	/**
	* Sets up test environment
	* @throws Exception
	*/
	@BeforeEach
	public void init() throws Exception {
	
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
		
	}
	
	/**
	* Test successful addition of a new Listing
	* @throws Exception
	*/
	@Test
	public void testAddListing() throws Exception{
		
		ListingAPIInbound 	listing 		= ListingAPIInbound.builder().build();
		
		Mockito.when(mockListingService.addListing(Mockito.any(), Mockito.anyBoolean())).thenReturn(UUID.randomUUID());
		
		ResponseEntity<UUID> response = controller.addListing(listing);
		
		if (!(response.getBody() instanceof UUID)) {
			throw new RuntimeException("Expected UUID");
		}
		
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		
	}
	
	/**
	* Tests successful update of an Existing Listing
	* @throws Exception
	*/
	@Test
	public void testUpdateListing() throws Exception{
		
		UUID				listingId		= UUID.randomUUID();
		ListingAPIInbound 	listing 		= ListingAPIInbound.builder().build();
		
		ResponseEntity<Void> response = controller.updateListing(listingId, listing);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
	}
	
	/**
	* Tests successful deletion of an existing Listing
	* @throws Exception
	*/
	@Test
	public void testDeleteListing() throws Exception{
		
		UUID				listingId		= UUID.randomUUID();
		
		ResponseEntity<Void> response = controller.deleteListing(listingId);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
	}
	
	/**
	* Tests fetch of Page of listings
	* @throws Exception
	*/
	@Test
	public void testFetchListing() throws Exception{
		
		ResponseEntity<Page<CandidateAPIOutbound>> response = controller.fetchListings(mockPageable, null);
		
		if (!(response.getBody() instanceof Page)) {
			throw new RuntimeException("Expected UUID");
		}
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
	}
	
	/**
	* Tests fetch of Page of listings for a specific Recruiter
	* @throws Exception
	*/
	@Test
	public void testFetchListingWithRecruiterFilter() throws Exception{
		
		final String recruiterId = "kparking";
		
		ResponseEntity<Page<CandidateAPIOutbound>> response = controller.fetchListings(mockPageable, recruiterId);
		
		if (!(response.getBody() instanceof Page)) {
			throw new RuntimeException("Expected UUID");
		}
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
	}
		
}