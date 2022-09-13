package com.arenella.recruit.recruiters.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIInbound;
import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIOutbound;
import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.beans.OpenPositionAPIInbound;
import com.arenella.recruit.recruiters.beans.OpenPositionAPIOutbound;
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
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testAddOpenPosition() throws Exception{
		ResponseEntity<Void> response = controller.addOpenPosition(OpenPositionAPIInbound.builder().build());
		
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
	};
	
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testUpdateOpenPosition() throws Exception{
		ResponseEntity<Void> response = controller.updateOpenPosition(UUID.randomUUID(), OpenPositionAPIInbound.builder().build());
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		Mockito.verify(supplyAndDemandService).updateOpenPosition(Mockito.any(UUID.class), Mockito.any(OpenPosition.class));
	};
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testAddOfferedCandidate() throws Exception{
		ResponseEntity<Void> response = controller.addOfferedCandidate(OfferedCandidateAPIInbound.builder().build());
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
	};
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testDeleteOfferedCandidate() throws Exception{
		ResponseEntity<Void> response = controller.deleteOfferedCandidate(UUID.randomUUID());
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	};
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testUpdateOfferedCandidate() throws Exception{
		ResponseEntity<Void> response = controller.updateOfferedCandidate(UUID.randomUUID(), OfferedCandidateAPIInbound.builder().build());
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	};
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testAddRecruiterToGlobalBlacklist() throws Exception{
		ResponseEntity<Void> response = controller.addRecruiterToGlobalBlacklist("recruiter1Id");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	};
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testDeleteRecruiterFromGlobalBlacklist() throws Exception{
		ResponseEntity<Void> response = controller.deleteRecruiterFromGlobalBlacklist("recruiter1Id");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	};
	
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
	public void testFetchOfferedCandidates() throws Exception{
		ResponseEntity<Set<OfferedCandidateAPIOutbound>> response = controller.fetchOfferedCandidates();
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	};
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testFetchAdvertisedPositions() throws Exception{
		ResponseEntity<Set<OpenPositionAPIOutbound>> response = controller.fetchOpenPositions();
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	};
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testFetchOfferedCandidates_forRecruiter() throws Exception{
		ResponseEntity<Set<OfferedCandidateAPIOutbound>> response = controller.fetchOfferedCandidates("recruiter1Id");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	};
	
	/**
	* Test Success
	* @throws Exception
	*/
	@Test
	public void testFetchAdvertisedPositions_forRecruiter() throws Exception{
		ResponseEntity<Set<OpenPositionAPIOutbound>> response = controller.fetchOpenPositions("recruiter1Id");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	};
	
}