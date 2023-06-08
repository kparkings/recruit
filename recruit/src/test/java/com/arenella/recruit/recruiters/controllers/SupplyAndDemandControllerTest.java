package com.arenella.recruit.recruiters.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Principal;
import java.time.LocalDate;
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

import com.arenella.recruit.recruiters.beans.BlacklistedRecruiterAPIOutbound;
import com.arenella.recruit.recruiters.beans.OfferedCandidate;
import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIInbound;
import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIOutbound;
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
public class SupplyAndDemandControllerTest {

	@InjectMocks
	private SupplyAndDemandController 	controller 					= new SupplyAndDemandController();
	
	@Mock
	private SupplyAndDemandService		supplyAndDemandService;
	
	@Mock
	private Principal					mockPrincipal;
	
	@Mock
	private SupplyAndDemandEventDao		mockSupplyAndDemandEventDao;
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testAddOpenPosition() throws Exception{
		
		ResponseEntity<Void> response = controller.addOpenPosition(OpenPositionAPIInbound.builder().positionTitle("Java Dev").build());
		
		Mockito.verify(supplyAndDemandService).addOpenPosition(Mockito.any(OpenPosition.class));
		
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testDeleteOpenPosition() throws Exception{
		
		ResponseEntity<Void> response = controller.deleteOpenPosition(UUID.randomUUID());
		
		Mockito.verify(supplyAndDemandService).deleteOpenPosition(Mockito.any(UUID.class));
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testUpdateOpenPosition() throws Exception{
		ResponseEntity<Void> response = controller.updateOpenPosition(UUID.randomUUID(), OpenPositionAPIInbound.builder().positionTitle("Java Dev").build());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Mockito.verify(supplyAndDemandService).updateOpenPosition(Mockito.any(UUID.class), Mockito.any(OpenPosition.class));
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testAddOfferedCandidate() throws Exception{
		ResponseEntity<Void> response = controller.addOfferedCandidate(OfferedCandidateAPIInbound.builder().candidateRoleTitle("Java Dev").build());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testDeleteOfferedCandidate() throws Exception{
		ResponseEntity<Void> response = controller.deleteOfferedCandidate(UUID.randomUUID());
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testUpdateOfferedCandidate() throws Exception{
		
		ResponseEntity<Void> response = controller.updateOfferedCandidate(UUID.randomUUID(), OfferedCandidateAPIInbound.builder().candidateRoleTitle("Java Dev").build());
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		Mockito.verify(supplyAndDemandService).updateOfferedCandidate(Mockito.any(UUID.class), Mockito.any(OfferedCandidate.class));
		
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testAddRecruiterToGlobalBlacklist() throws Exception{
		ResponseEntity<Void> response = controller.addRecruiterToGlobalBlacklist("recruiter1Id");
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testDeleteRecruiterFromGlobalBlacklist() throws Exception{
		ResponseEntity<Void> response = controller.deleteRecruiterFromGlobalBlacklist("recruiter1Id");
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testFetchBlacklistForRecruiter() throws Exception{
		ResponseEntity<Set<BlacklistedRecruiterAPIOutbound>> response = controller.fetchBlacklistForRecruiter("recruiter1Id");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testFetchAdvertisedPositions() throws Exception{
		
		final UUID id1 = UUID.randomUUID();
		final UUID id2 = UUID.randomUUID();
		final UUID id3 = UUID.randomUUID();
		
		final LocalDate created1 = LocalDate.of(2001, 1, 1);
		final LocalDate created2 = LocalDate.of(2003, 1, 1);
		final LocalDate created3 = LocalDate.of(2002, 1, 1);
		
		OpenPosition c1 = OpenPosition.builder().id(id1).created(created1).build();
		OpenPosition c2 = OpenPosition.builder().id(id2).created(created2).build();
		OpenPosition c3 = OpenPosition.builder().id(id3).created(created3).build();
		
		Mockito.when(this.supplyAndDemandService.fetchOpenPositions()).thenReturn(Set.of(c1,c2,c3));
		
		ResponseEntity<Set<OpenPositionAPIOutbound>> response = controller.fetchOpenPositions(this.mockPrincipal);
		
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
	public void testFetchAdvertisedPositions_forRecruiter() throws Exception{
		
		final UUID id1 = UUID.randomUUID();
		final UUID id2 = UUID.randomUUID();
		final UUID id3 = UUID.randomUUID();
		
		final LocalDate created1 = LocalDate.of(2001, 1, 1);
		final LocalDate created2 = LocalDate.of(2003, 1, 1);
		final LocalDate created3 = LocalDate.of(2002, 1, 1);
		
		OpenPosition c1 = OpenPosition.builder().id(id1).created(created1).build();
		OpenPosition c2 = OpenPosition.builder().id(id2).created(created2).build();
		OpenPosition c3 = OpenPosition.builder().id(id3).created(created3).build();
		
		Mockito.when(this.supplyAndDemandService.fetchOpenPositions(Mockito.anyString())).thenReturn(Set.of(c1,c2,c3));
		
		ResponseEntity<Set<OpenPositionAPIOutbound>> response = controller.fetchOpenPositions("aRecruiterId", this.mockPrincipal);
		
		response.getBody().stream().filter(c -> c.getId()== id1).findAny().orElseThrow();
		response.getBody().stream().filter(c -> c.getId()== id2).findAny().orElseThrow();
		response.getBody().stream().filter(c -> c.getId()== id3).findAny().orElseThrow();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests retrieval of OfferedCandidates
	* @throws Exception
	*/
	@Test
	public void testFetchOfferedCandidates() throws Exception{
		
		final UUID id1 = UUID.randomUUID();
		final UUID id2 = UUID.randomUUID();
		final UUID id3 = UUID.randomUUID();
		
		final LocalDate created1 = LocalDate.of(2001, 1, 1);
		final LocalDate created2 = LocalDate.of(2003, 1, 1);
		final LocalDate created3 = LocalDate.of(2002, 1, 1);
		
		OfferedCandidate c1 = OfferedCandidate.builder().id(id1).created(created1).build();
		OfferedCandidate c2 = OfferedCandidate.builder().id(id2).created(created2).build();
		OfferedCandidate c3 = OfferedCandidate.builder().id(id3).created(created3).build();
		
		Mockito.when(this.supplyAndDemandService.fetchOfferedCandidates()).thenReturn(Set.of(c1,c2,c3));
		
		Mockito.when(mockPrincipal.getName()).thenReturn("kparkings");
		
		ResponseEntity<Set<OfferedCandidateAPIOutbound>> response = controller.fetchOfferedCandidates(mockPrincipal);
		
		response.getBody().stream().filter(c -> c.getId()== id1).findAny().orElseThrow();
		response.getBody().stream().filter(c -> c.getId()== id2).findAny().orElseThrow();
		response.getBody().stream().filter(c -> c.getId()== id3).findAny().orElseThrow();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests retrieval of OfferedCandidates
	* @throws Exception
	*/
	@Test
	public void testFetchOfferedCandidatesForRecruiter() throws Exception{
		
		final UUID id1 = UUID.randomUUID();
		final UUID id2 = UUID.randomUUID();
		final UUID id3 = UUID.randomUUID();
		
		final LocalDate created1 = LocalDate.of(2001, 1, 1);
		final LocalDate created2 = LocalDate.of(2003, 1, 1);
		final LocalDate created3 = LocalDate.of(2002, 1, 1);
		
		OfferedCandidate c1 = OfferedCandidate.builder().id(id1).created(created1).build();
		OfferedCandidate c2 = OfferedCandidate.builder().id(id2).created(created2).build();
		OfferedCandidate c3 = OfferedCandidate.builder().id(id3).created(created3).build();
		
		Mockito.when(this.supplyAndDemandService.fetchOfferedCandidates(Mockito.anyString())).thenReturn(Set.of(c1,c2,c3));
		
		ResponseEntity<Set<OfferedCandidateAPIOutbound>> response = controller.fetchOfferedCandidates("aRecruiterId", this.mockPrincipal);
		
		response.getBody().stream().filter(c -> c.getId()== id1).findAny().orElseThrow();
		response.getBody().stream().filter(c -> c.getId()== id2).findAny().orElseThrow();
		response.getBody().stream().filter(c -> c.getId()== id3).findAny().orElseThrow();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests if request is received event is passed to service and correct 
	* status code returned
	* @throws Exception
	*/
	@Test
	public void testRegisterOpenPositionViewedEvent() throws Exception {
		
		ResponseEntity<Void> response = this.controller.registerOpenPositionViewedEvent(UUID.randomUUID());
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		Mockito.verify(this.supplyAndDemandService).registerOpenPositionViewedEvent(Mockito.any(UUID.class));
	
	}
	
	/**
	* Tests if request is received event is passed to service and correct 
	* status code returned
	* @throws Exception
	*/
	@Test
	public void testRegisterOfferedCandidateViewedEvent() throws Exception {
		
		ResponseEntity<Void> response = this.controller.registerOfferedCandidateViewedEvent(UUID.randomUUID());
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		Mockito.verify(this.supplyAndDemandService).registerOfferedCandidateViewedEvent(Mockito.any(UUID.class));
	
	}

	/**
	* Test end point for sending contact info to recruiter relating to an 
	* Offered candidate
	* @throws Exception
	*/
	@Test
	public void testContactRecruiterForOfferedCandidate() throws Exception{
		
		ResponseEntity<Void> response = this.controller.contactRecruiterForOfferedCandidate(UUID.randomUUID(), "message", mockPrincipal);
		
		assertEquals(HttpStatus.OK,response.getStatusCode());
		
	}
	
	/**
	* Test end point for sending contact info to recruiter relating to an 
	* Open Position
	* @throws Exception
	*/
	@Test
	public void testContactRecruiterForOpenPosition() throws Exception{
		
		ResponseEntity<Void> response = this.controller.contactRecruiterForOpenPosition(UUID.randomUUID(), "message", mockPrincipal);
		
		assertEquals(HttpStatus.OK,response.getStatusCode());
		
	}
	
}