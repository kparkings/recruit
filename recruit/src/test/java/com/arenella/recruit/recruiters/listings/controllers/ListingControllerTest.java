package com.arenella.recruit.recruiters.listings.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.listings.beans.Listing.LISTING_AGE;
import com.arenella.recruit.listings.beans.Listing.listing_type;
import com.arenella.recruit.authentication.spring.filters.ClaimsUsernamePasswordAuthenticationToken;
import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.CandidateListingContactRequest;
import com.arenella.recruit.listings.controllers.ListingAPIInbound;
import com.arenella.recruit.listings.controllers.ListingAPIOutbound;
import com.arenella.recruit.listings.controllers.ListingAPIOutboundPublic;
import com.arenella.recruit.listings.controllers.ListingContactRequest;
import com.arenella.recruit.listings.controllers.ListingController;
import com.arenella.recruit.listings.services.ListingService;

/**
* Unit tests for the ListingController Class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ListingControllerTest {
	
	@Mock
	private Pageable 									mockPageable;
	
	@Mock
	private ListingService 								mockListingService;
	
	@Mock
	private	Authentication								mockAuthentication;
	
	@Mock
	private ClaimsUsernamePasswordAuthenticationToken 	mockUsernamePasswordAuthenticationToken;
	
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testAddListing() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
	//	Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn("kevin");
		
		
		ListingAPIInbound 	listing 		= ListingAPIInbound.builder().build();
		
		Mockito.when(mockListingService.addListing(Mockito.any(), Mockito.anyBoolean())).thenReturn(UUID.randomUUID());
	//	Mockito.doThrow(new IllegalStateException("")).when(mockListingService).useCredit("kevin");
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.TRUE));
		//Mockito.when(this.mockListingService.hasCreditsLeft(Mockito.anyString())).thenReturn(true);
		
		ResponseEntity<UUID> response = controller.addListing(listing, mockUsernamePasswordAuthenticationToken);
		
		if (!(response.getBody() instanceof UUID)) {
			throw new RuntimeException("Expected UUID");
		}
		
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		
	}
	
	/**
	* Test successful addition of a new Listing
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testAddListing_recruiter_has_credits() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn("kevin");
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.TRUE));
		
		ListingAPIInbound 	listing 		= ListingAPIInbound.builder().build();
		
		Mockito.when(mockListingService.addListing(Mockito.any(), Mockito.anyBoolean())).thenReturn(UUID.randomUUID());
		
		ResponseEntity<UUID> response = controller.addListing(listing, mockUsernamePasswordAuthenticationToken);
		
		if (!(response.getBody() instanceof UUID)) {
			throw new RuntimeException("Expected UUID");
		}
		
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		
	}
	
	/**
	* Test successful addition of a new Listing
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testAddListing_recruiter_has_no_credits() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn("kevin");
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.TRUE));
//		Mockito.when(this.mockListingService.hasCreditsLeft(Mockito.anyString())).thenReturn(false);
		Mockito.doThrow(new IllegalStateException("")).when(mockListingService).useCredit("kevin");
		
		ListingAPIInbound 	listing 		= ListingAPIInbound.builder().build();
		
		assertThrows(IllegalStateException.class, () -> {
			controller.addListing(listing, mockUsernamePasswordAuthenticationToken);
		});
		
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
		
		Mockito.verify(mockListingService).updateListing(Mockito.eq(listingId), Mockito.any());
		
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
		
		Mockito.verify(mockListingService).deleteListing(Mockito.eq(listingId));
		
	}
	
	/**
	* Tests fetch of Page of listings
	* @throws Exception
	*/
	@Test
	public void testFetchListing_noRecruiterId() throws Exception{
		
		ArgumentCaptor<ListingFilter> filterArgCapt = ArgumentCaptor.forClass(ListingFilter.class);
		
		Mockito.when(this.mockListingService.fetchListings(filterArgCapt.capture(), Mockito.any())).thenReturn(Page.empty());
		
		Page<ListingAPIOutbound> response = controller.fetchListings(null, LISTING_AGE.ALL, mockPageable);
	
		assertTrue(response instanceof Page);
		
		assertTrue(filterArgCapt.getValue().getOwnerId().isEmpty());
		
	}
	
	/**
	* Tests fetch of Page of listings
	* @throws Exception
	*/
	@Test
	public void testFetchListingPublic() throws Exception{
		
		ArgumentCaptor<ListingFilter> filterArgCapt = ArgumentCaptor.forClass(ListingFilter.class);
		
		Mockito.when(this.mockListingService.fetchListings(filterArgCapt.capture(), Mockito.any())).thenReturn(Page.empty());
		
		Page<ListingAPIOutboundPublic> response = controller.fetchListingsPubilc(null, LISTING_AGE.ALL, mockPageable);
	
		assertTrue(response instanceof Page);
		
	}
	
	/**
	* Tests fetch of Page of listings when Contract type has been specified 
	* to filter on
	* @throws Exception
	*/
	@Test
	public void testFetchListingPublic_withContractTypeFilter() throws Exception{
		
		final listing_type listingType = listing_type.CONTRACT_ROLE;
		
		ArgumentCaptor<ListingFilter> 	filterArgCapt = ArgumentCaptor.forClass(ListingFilter.class);
		
		Mockito.when(this.mockListingService.fetchListings(filterArgCapt.capture(), Mockito.any())).thenReturn(Page.empty());
		
		Page<ListingAPIOutboundPublic> response = controller.fetchListingsPubilc(listingType, LISTING_AGE.ALL, mockPageable);
	
		assertTrue(response instanceof Page);
		
		assertEquals(filterArgCapt.getValue().getType().get(), listingType);
		
	}
	
	/**
	* Tests fetch of Page of listings for a specific Recruiter
	* @throws Exception
	*/
	@Test
	public void testFetchListingWithRecruiterFilter() throws Exception{
	
		ArgumentCaptor<ListingFilter> filterArgCapt = ArgumentCaptor.forClass(ListingFilter.class);
		
		Mockito.when(this.mockListingService.fetchListings(filterArgCapt.capture(), Mockito.any())).thenReturn(Page.empty());
		
		final String recruiterId = "kparking";
		
		Page<ListingAPIOutbound> response = controller.fetchListings(recruiterId, LISTING_AGE.ALL, mockPageable);
		
		assertTrue(response instanceof Page);
		
		assertEquals(filterArgCapt.getValue().getOwnerId().get(), recruiterId);
		
	}
	
	/**
	* Tests correct response returned when Event registered
	* @throws Exception
	*/
	@Test
	@WithAnonymousUser
	public void testRegisterListingViewedEvent()  throws Exception {
		
		final UUID listingId	= UUID.randomUUID();
		
		ArgumentCaptor<ListingViewedEvent> captor = ArgumentCaptor.forClass(ListingViewedEvent.class);
		
		Mockito.doNothing().when(this.mockListingService).registerListingViewedEvent(captor.capture());
		
		controller.registerListingViewedEvent(listingId);
		
		Mockito.verify(this.mockListingService).registerListingViewedEvent(Mockito.any(ListingViewedEvent.class));
	
		assertTrue(captor.getValue().getEventId() instanceof UUID);
		assertTrue(captor.getValue().getCreated() instanceof LocalDateTime);
		assertEquals(listingId, captor.getValue().getListingId());
		
	}
	
	/**
	* Test sending of request for contact to Owner of the Listing
	* @throws Exception
	*/
	@Test
	public void testSendContactRequestToListingOwner()  throws Exception {
		
		final UUID				listingId		= UUID.randomUUID();
		final MultipartFile 	attachment		= Mockito.mock(MultipartFile.class);
		final String 			senderName		= "Kevin Parkings";
		final String 			senderEmail		= "kparkings@gmail.com";
		final String 			message			= "some message";
		
		ArgumentCaptor<ListingContactRequest> argCaptContactRequest = ArgumentCaptor.forClass(ListingContactRequest.class);
		
		Mockito.doNothing().when(this.mockListingService).sendContactRequestToListingOwner(argCaptContactRequest.capture());
		
		ResponseEntity<Void> response = this.controller.sendContactRequestToListingOwner(listingId, senderName, senderEmail, message, Optional.of(attachment));
		
		Mockito.verify(this.mockListingService).sendContactRequestToListingOwner(Mockito.any(ListingContactRequest.class));
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		assertEquals(listingId, 	argCaptContactRequest.getValue().getListingId());
		assertEquals(attachment, 	argCaptContactRequest.getValue().getAttachment());
		assertEquals(senderName, 	argCaptContactRequest.getValue().getSenderName());
		assertEquals(senderEmail, 	argCaptContactRequest.getValue().getSenderEmail());
		assertEquals(message, 		argCaptContactRequest.getValue().getMessage());
		
	}
	
	/**
	* Test sending of request for contact from Candidate to Owner of the Listing
	* @throws Exception
	*/
	@Test
	public void testSndContactRequestToListingOwnerFromCandidate() throws Exception{
		
		final UUID				listingId		= UUID.randomUUID();
		final MultipartFile 	attachment		= Mockito.mock(MultipartFile.class);
		final String 			candidateId		= "Kevin Parkings";
		final String 			message			= "some message";
		
		ArgumentCaptor<CandidateListingContactRequest> argCaptContactRequest = ArgumentCaptor.forClass(CandidateListingContactRequest.class);
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn(candidateId);
		Mockito.doNothing().when(this.mockListingService).sendContactRequestFomCandidateToListingOwner(argCaptContactRequest.capture());
		
		ResponseEntity<Void> response = this.controller.sendContactRequestToListingOwnerFromCandidate(listingId, message, Optional.of(attachment), this.mockUsernamePasswordAuthenticationToken);
		
		Mockito.verify(this.mockListingService).sendContactRequestFomCandidateToListingOwner(Mockito.any(CandidateListingContactRequest.class));
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		assertEquals(listingId, 	argCaptContactRequest.getValue().getListingId());
		assertEquals(attachment, 	argCaptContactRequest.getValue().getAttachment().get());
		assertEquals(candidateId, 	argCaptContactRequest.getValue().getCandidateId());
		assertEquals(message, 		argCaptContactRequest.getValue().getMessage());
		
	}
	
	/**
	* Tests if admin returns true
	* @throws Exception
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testPassesCreditCheck_admin() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.TRUE));
		
		ResponseEntity<Boolean> response = this.controller.passesCreditCheck(mockUsernamePasswordAuthenticationToken);
		
		assertTrue(response.getBody());

		Mockito.verify(this.mockListingService, Mockito.never()).doCreditsCheck(mockUsernamePasswordAuthenticationToken.getName());
		
	}
	
	/**
	* Tests if recruiter but no using credit based access return true
	* @throws Exception
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testPassesCreditCheck_recruiter_not_creditbased() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.FALSE));
		
		ResponseEntity<Boolean> response = this.controller.passesCreditCheck(mockUsernamePasswordAuthenticationToken);
		
		assertTrue(response.getBody());
		
		Mockito.verify(this.mockListingService, Mockito.never()).doCreditsCheck(mockUsernamePasswordAuthenticationToken.getName());

	}
	
	/**
	* Tests if recruiter but no using credit based access return true
	* @throws Exception
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testPassesCreditCheck_recruiter_creditbased() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		Mockito.when(this.mockListingService.doCreditsCheck(Mockito.any())).thenReturn(true);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.TRUE));
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn("userId");
		
		ResponseEntity<Boolean> response = this.controller.passesCreditCheck(mockUsernamePasswordAuthenticationToken);
		
		assertTrue(response.getBody());
		
		Mockito.verify(this.mockListingService).doCreditsCheck(mockUsernamePasswordAuthenticationToken.getName());

	}
		
}