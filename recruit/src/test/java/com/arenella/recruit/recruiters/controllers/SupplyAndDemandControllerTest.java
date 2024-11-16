package com.arenella.recruit.recruiters.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.arenella.recruit.authentication.spring.filters.ClaimsUsernamePasswordAuthenticationToken;
import com.arenella.recruit.recruiters.beans.BlacklistedRecruiterAPIOutbound;
import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.beans.OpenPositionAPIInbound;
import com.arenella.recruit.recruiters.beans.OpenPositionAPIOutbound;
import com.arenella.recruit.recruiters.dao.SupplyAndDemandEventDao;
import com.arenella.recruit.recruiters.services.SupplyAndDemandService;

/**
* Unit tests for testing the SupplyAndDemandController API
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class SupplyAndDemandControllerTest {

	@InjectMocks
	private SupplyAndDemandController 							controller 									= new SupplyAndDemandController();
	
	@Mock
	private SupplyAndDemandService								mockSupplyAndDemandService;
	
	@Mock
	private ClaimsUsernamePasswordAuthenticationToken			mockUsernamePasswordAuthenticationToken;
	
	@Mock
	private SupplyAndDemandEventDao								mockSupplyAndDemandEventDao;
	
	@Mock
	private Principal											mockPrincipal;
	
	/**
	* Test Success
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testAddOpenPosition() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.TRUE));
	
		ResponseEntity<Void> response = controller.addOpenPosition(OpenPositionAPIInbound.builder().positionTitle("Java Dev").build(), mockUsernamePasswordAuthenticationToken);
		
		Mockito.verify(mockSupplyAndDemandService).addOpenPosition(Mockito.any(OpenPosition.class));
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testAddOpenPosition_recruiter_has_credits() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn("kevin");
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.TRUE));
		Mockito.doNothing().when(mockSupplyAndDemandService).useCredit("kevin");
	
		ResponseEntity<Void> response = controller.addOpenPosition(OpenPositionAPIInbound.builder().positionTitle("Java Dev").build(), mockUsernamePasswordAuthenticationToken);
		
		Mockito.verify(mockSupplyAndDemandService).addOpenPosition(Mockito.any(OpenPosition.class));
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testAddOpenPosition_recruiter_no_credits() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn("kevin");
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.TRUE));
		Mockito.doThrow(new IllegalStateException("")).when(mockSupplyAndDemandService).useCredit("kevin");
	
		OpenPositionAPIInbound op = OpenPositionAPIInbound.builder().positionTitle("Java Dev").build();
		
		assertThrows(IllegalStateException.class, () -> {
			controller.addOpenPosition(op, mockUsernamePasswordAuthenticationToken);
		});
		
	}
	
	
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	void testDeleteOpenPosition() throws Exception{
		
		ResponseEntity<Void> response = controller.deleteOpenPosition(UUID.randomUUID());
		
		Mockito.verify(mockSupplyAndDemandService).deleteOpenPosition(Mockito.any(UUID.class));
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	void testUpdateOpenPosition() throws Exception{
		ResponseEntity<Void> response = controller.updateOpenPosition(UUID.randomUUID(), OpenPositionAPIInbound.builder().positionTitle("Java Dev").build());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Mockito.verify(mockSupplyAndDemandService).updateOpenPosition(Mockito.any(UUID.class), Mockito.any(OpenPosition.class));
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	void testAddRecruiterToGlobalBlacklist() {
		ResponseEntity<Void> response = controller.addRecruiterToGlobalBlacklist("recruiter1Id");
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	void testDeleteRecruiterFromGlobalBlacklist() {
		ResponseEntity<Void> response = controller.deleteRecruiterFromGlobalBlacklist("recruiter1Id");
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	void testFetchBlacklistForRecruiter() {
		ResponseEntity<Set<BlacklistedRecruiterAPIOutbound>> response = controller.fetchBlacklistForRecruiter("recruiter1Id");
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	void testFetchAdvertisedPositions() {
		
		final UUID id1 = UUID.randomUUID();
		final UUID id2 = UUID.randomUUID();
		final UUID id3 = UUID.randomUUID();
		
		final LocalDateTime created1 = LocalDateTime.of(2001, 1, 1, 0, 0 ,0);
		final LocalDateTime created2 = LocalDateTime.of(2003, 1, 1, 0, 0 ,0);
		final LocalDateTime created3 = LocalDateTime.of(2002, 1, 1, 0, 0, 0);
		
		OpenPosition c1 = OpenPosition.builder().id(id1).created(created1).build();
		OpenPosition c2 = OpenPosition.builder().id(id2).created(created2).build();
		OpenPosition c3 = OpenPosition.builder().id(id3).created(created3).build();
		
		Mockito.when(this.mockSupplyAndDemandService.fetchOpenPositions()).thenReturn(Set.of(c1,c2,c3));
		
		ResponseEntity<Set<OpenPositionAPIOutbound>> response = controller.fetchOpenPositions(this.mockUsernamePasswordAuthenticationToken);
		
		response.getBody().stream().filter(c -> c.getId()== id1).findAny().orElseThrow();
		response.getBody().stream().filter(c -> c.getId()== id2).findAny().orElseThrow();
		response.getBody().stream().filter(c -> c.getId()== id3).findAny().orElseThrow();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	void testFetchAdvertisedPositions_forRecruiter() {
		
		final UUID id1 = UUID.randomUUID();
		final UUID id2 = UUID.randomUUID();
		final UUID id3 = UUID.randomUUID();
		
		final LocalDateTime created1 = LocalDateTime.of(2001, 1, 1, 0, 0, 0);
		final LocalDateTime created2 = LocalDateTime.of(2003, 1, 1, 0, 0, 0);
		final LocalDateTime created3 = LocalDateTime.of(2002, 1, 1, 0, 0, 0);
		
		OpenPosition c1 = OpenPosition.builder().id(id1).created(created1).build();
		OpenPosition c2 = OpenPosition.builder().id(id2).created(created2).build();
		OpenPosition c3 = OpenPosition.builder().id(id3).created(created3).build();
		
		Mockito.when(this.mockSupplyAndDemandService.fetchOpenPositions(Mockito.anyString())).thenReturn(Set.of(c1,c2,c3));
		
		ResponseEntity<Set<OpenPositionAPIOutbound>> response = controller.fetchOpenPositions("aRecruiterId", this.mockUsernamePasswordAuthenticationToken);
		
		response.getBody().stream().filter(c -> c.getId()== id1).findAny().orElseThrow();
		response.getBody().stream().filter(c -> c.getId()== id2).findAny().orElseThrow();
		response.getBody().stream().filter(c -> c.getId()== id3).findAny().orElseThrow();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests retrieval of unseen open position count for authenticated user
	* @throws Exception
	*/
	@Test
	void testFetchOpenPositionsCount_() {
		
		final UUID id1 = UUID.randomUUID();
		final UUID id2 = UUID.randomUUID();
		final UUID id3 = UUID.randomUUID();
		
		final LocalDateTime created1 = LocalDateTime.of(2001, 1, 1, 0, 0, 0);
		final LocalDateTime created2 = LocalDateTime.of(2003, 1, 1, 0, 0, 0);
		final LocalDateTime created3 = LocalDateTime.of(2002, 1, 1, 0, 0, 0);
		
		OpenPosition c1 = OpenPosition.builder().id(id1).created(created1).build();
		OpenPosition c2 = OpenPosition.builder().id(id2).created(created2).build();
		OpenPosition c3 = OpenPosition.builder().id(id3).created(created3).build();
		
		Mockito.when(this.mockSupplyAndDemandService.fetchViewedEventsByRecruiter(Mockito.any(), Mockito.any())).thenReturn(Set.of(c2.getId()));
		
		Mockito.when(this.mockSupplyAndDemandService.fetchOpenPositions()).thenReturn(Set.of(c1,c2,c3));
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn("kevin");
		ResponseEntity<Long> response = controller.fetchOpenPositionsCount(this.mockUsernamePasswordAuthenticationToken);
		
		assertEquals(2, response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests if request is received event is passed to service and correct 
	* status code returned
	* @throws Exception
	*/
	@Test
	void testRegisterOpenPositionViewedEvent() {
		
		ResponseEntity<Void> response = this.controller.registerOpenPositionViewedEvent(UUID.randomUUID());
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		Mockito.verify(this.mockSupplyAndDemandService).registerOpenPositionViewedEvent(Mockito.any(UUID.class));
	
	}
	
	/**
	* Tests if request is received event is passed to service and correct 
	* status code returned
	* @throws Exception
	*/
	@Test
	void testRegisterOfferedCandidateViewedEvent() {
		
		ResponseEntity<Void> response = this.controller.registerOfferedCandidateViewedEvent(UUID.randomUUID());
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		Mockito.verify(this.mockSupplyAndDemandService).registerOfferedCandidateViewedEvent(Mockito.any(UUID.class));
	
	}
	
	/**
	* Test end point for sending contact info to recruiter relating to an 
	* Open Position
	* @throws Exception
	*/
	@Test
	void testContactRecruiterForOpenPosition() {
		
		ResponseEntity<Void> response = this.controller.contactRecruiterForOpenPosition(UUID.randomUUID(), "message", mockUsernamePasswordAuthenticationToken);
		
		assertEquals(HttpStatus.OK,response.getStatusCode());
		
	}
	
	/**
	* Tests if admin returns true
	* @throws Exception
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testPassesCreditCheck_admin() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.TRUE));
		
		ResponseEntity<Boolean> response = this.controller.passesCreditCheck(mockUsernamePasswordAuthenticationToken);
		
		assertTrue(response.getBody());

		Mockito.verify(this.mockSupplyAndDemandService, Mockito.never()).doCreditsCheck(mockUsernamePasswordAuthenticationToken.getName());
		
	}
	
	/**
	* Tests if recruiter but no using credit based access return true
	* @throws Exception
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testPassesCreditCheck_recruiter_not_creditbased() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.FALSE));
		
		ResponseEntity<Boolean> response = this.controller.passesCreditCheck(mockUsernamePasswordAuthenticationToken);
		
		assertTrue(response.getBody());
		
		Mockito.verify(this.mockSupplyAndDemandService, Mockito.never()).doCreditsCheck(mockUsernamePasswordAuthenticationToken.getName());

	}
	
	/**
	* Tests if recruiter but no using credit based access return true
	* @throws Exception
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testPassesCreditCheck_recruiter_creditbased() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		Mockito.when(this.mockSupplyAndDemandService.doCreditsCheck(Mockito.any())).thenReturn(true);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.TRUE));
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn("userId");
		
		ResponseEntity<Boolean> response = this.controller.passesCreditCheck(mockUsernamePasswordAuthenticationToken);
		
		assertTrue(response.getBody());
		
		Mockito.verify(this.mockSupplyAndDemandService).doCreditsCheck(mockUsernamePasswordAuthenticationToken.getName());

	}
	
	/**
	* Tests Fetch of remaining credit count for a User
	* @throws Exception
	*/
	@Test
	void testFetchRemainingCreditCount() {
	
		final int count = 6;
		
		Mockito.when(mockPrincipal.getName()).thenReturn("rec22");
		Mockito.when(this.mockSupplyAndDemandService.getCreditCountForUser(Mockito.anyString())).thenReturn(count);
		
		ResponseEntity<Integer> response = this.controller.fetchRemainingCreditCount(mockPrincipal);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(count, response.getBody());
	
	}
	
	/**
	* Tests ordering
	* @throws Exception
	*/
	@Test
	void testPostsInReverseCreatedDateOrder() {
		
		final UUID id1 = UUID.randomUUID();
		final UUID id2 = UUID.randomUUID();
		final UUID id3 = UUID.randomUUID();
		
		final LocalDateTime created1 = LocalDateTime.of(2001, 1, 1, 0,0,2);
		final LocalDateTime created2 = LocalDateTime.of(2003, 1, 1, 0,0,1);
		final LocalDateTime created3 = LocalDateTime.of(2002, 1, 1, 0,0,3);
		
		OpenPosition c1 = OpenPosition.builder().id(id1).created(created1).build();
		OpenPosition c2 = OpenPosition.builder().id(id2).created(created2).build();
		OpenPosition c3 = OpenPosition.builder().id(id3).created(created3).build();
		
		Mockito.when(this.mockSupplyAndDemandService.fetchOpenPositions()).thenReturn(Set.of(c1,c2,c3));
		
		ResponseEntity<Set<OpenPositionAPIOutbound>> response = controller.fetchOpenPositions(this.mockUsernamePasswordAuthenticationToken);
		
		response.getBody().stream().filter(c -> c.getId()== id1).findAny().orElseThrow();
		response.getBody().stream().filter(c -> c.getId()== id2).findAny().orElseThrow();
		response.getBody().stream().filter(c -> c.getId()== id3).findAny().orElseThrow();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
}