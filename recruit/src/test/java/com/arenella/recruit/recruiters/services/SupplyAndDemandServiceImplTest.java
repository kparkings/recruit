package com.arenella.recruit.recruiters.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.arenella.recruit.adapters.events.OfferedCandidateContactRequestEvent;
import com.arenella.recruit.adapters.events.OpenPositionContactRequestEvent;
import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
import com.arenella.recruit.recruiters.beans.OfferedCandidate;
import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent.EventType;
import com.arenella.recruit.recruiters.dao.OfferedCandidateDao;
import com.arenella.recruit.recruiters.dao.OpenPositionDao;
import com.arenella.recruit.recruiters.dao.SupplyAndDemandEventDao;

/**
* Unit tests for the SupplyAndDemandServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class SupplyAndDemandServiceImplTest {

	private static final String RECRUITER_ID = "recruiterId1";
	
	@InjectMocks
	private SupplyAndDemandServiceImpl 			service;
	
	@Mock
	private OpenPositionDao 					mockOpenPositionDao;
	
	@Mock
	private OfferedCandidateDao					mockOfferedCandidateDao;
	
	@Mock
	private	Authentication						mockAuthentication;
	
	@Mock
	private RecruitersExternalEventPublisher	mockEventPublisher;
	
	@Mock
	private SupplyAndDemandEventDao			 	mockSupplyAndDemandEventDao;
	
	/**
	* Sets up test environment
	* @throws Exception
	*/
	@BeforeEach
	public void init() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
	}
	
	/**
	* Tests happy path for persisting an OpenPosition
	* @throws Exception
	*/
	@Test
	public void testAddOpenPosition() throws Exception{
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(RECRUITER_ID);
		
		ArgumentCaptor<OpenPosition> captor = ArgumentCaptor.forClass(OpenPosition.class);
		
		OpenPosition openPosition = OpenPosition.builder().positionTitle("aTitle").build();
		
		service.addOpenPosition(openPosition);
		
		Mockito.verify(mockOpenPositionDao).persistOpenPositiion(captor.capture());
		
		assertEquals(RECRUITER_ID, captor.getValue().getRecruiterId());
		
	}
	
	/**
	* Happy path for deleting an OpenPosition
	* @throws Exception
	*/
	@Test
	public void testDeleteOpenPosition() throws Exception{
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(RECRUITER_ID);
		
		UUID 			openPositionId 	= UUID.randomUUID();
		OpenPosition 	openPosition 	= OpenPosition.builder().positionTitle("aTitle").recruiterId(RECRUITER_ID).build();
		
		Mockito.when(this.mockOpenPositionDao.findByOpenPositionId(openPositionId)).thenReturn(openPosition);
		
		service.deleteOpenPosition(openPositionId);
		
	}
	
	/**
	* Failure path where OpenPosition does not exist
	* @throws Exception
	*/
	@Test
	public void testDeleteOpenPosition_unknownOpenPosition() throws Exception{
		
		UUID 			openPositionId 	= UUID.randomUUID();
		
		Assertions.assertThrows(RuntimeException.class, () -> {
			service.deleteOpenPosition(openPositionId);
		});
		
	}
	
	/**
	* Failure position where an attempt is made to delete another users 
	* OpenPosition
	* @throws Exception
	*/
	@Test
	public void testDeleteOpenPosition_wrongUser() throws Exception{
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(RECRUITER_ID);
		
		UUID 			openPositionId 	= UUID.randomUUID();
		OpenPosition 	openPosition 	= OpenPosition.builder().recruiterId("AnotherRecruitersId").build();
		
		Mockito.when(this.mockOpenPositionDao.findByOpenPositionId(openPositionId)).thenReturn(openPosition);
		
		Assertions.assertThrows(IllegalAccessException.class, () -> {
			service.deleteOpenPosition(openPositionId);
		});
		
	}
	
	@Test
	public void testUpdateOpenPosition() throws Exception{
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(RECRUITER_ID);
		
		UUID 			openPositionId 	= UUID.randomUUID();
		OpenPosition 	openPosition 	= OpenPosition.builder().positionTitle("aTitle").recruiterId(RECRUITER_ID).build();
		
		Mockito.when(this.mockOpenPositionDao.findByOpenPositionId(openPositionId)).thenReturn(openPosition);
		
		service.updateOpenPosition(openPositionId, openPosition);
		
		Mockito.verify(this.mockOpenPositionDao).updateExistingOpenPosition(openPositionId, openPosition);
	}
	
	@Test
	public void testUpdateOpenPosition_wrongUser() throws Exception{
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(RECRUITER_ID);
		
		UUID 			openPositionId 	= UUID.randomUUID();
		OpenPosition 	openPosition 	= OpenPosition.builder().positionTitle("aTitle").recruiterId("AnotherRecruitersId").build();
		
		Mockito.when(this.mockOpenPositionDao.findByOpenPositionId(openPositionId)).thenReturn(openPosition);
		
		Assertions.assertThrows(IllegalAccessException.class, () -> {
			service.updateOpenPosition(openPositionId, openPosition);
		});
		
	}
	
	/**
	* Tests persisting of an OfferedCandidate
	* @throws Exception
	*/
	@Test
	public void testAddOfferedCandidate() throws Exception{
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(RECRUITER_ID);
		
		ArgumentCaptor<OfferedCandidate> captor = ArgumentCaptor.forClass(OfferedCandidate.class);
		
		OfferedCandidate offeredCandidate = OfferedCandidate.builder().candidateRoleTitle("aRole").build();
		
		service.addOfferedCandidate(offeredCandidate);
		
		Mockito.verify(mockOfferedCandidateDao).persistOfferedCandidate(captor.capture());
		
		assertEquals(RECRUITER_ID, captor.getValue().getRecruiterId());
		
	}
	
	/**
	* Happy path for deleting an OfferedCandidate
	* @throws Exception
	*/
	@Test
	public void testDeleteOfferedCandidate() throws Exception{
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(RECRUITER_ID);
		
		UUID 				offeredCandidateId 		= UUID.randomUUID();
		OfferedCandidate 	offeredCandidate 		= OfferedCandidate.builder().candidateRoleTitle("aRole").recruiterId(RECRUITER_ID).build();
		
		Mockito.when(this.mockOfferedCandidateDao.findByOfferedCandidateId(offeredCandidateId)).thenReturn(offeredCandidate);
		
		service.deleteOfferedCandidate(offeredCandidateId);
		
	}
	
	/**
	* Failure path where OfferedCandidate does not exist
	* @throws Exception
	*/
	@Test
	public void testDeleteOfferedCandidate_unknownOpenPosition() throws Exception{
		
		UUID offeredCandidateId 	= UUID.randomUUID();
		
		Assertions.assertThrows(RuntimeException.class, () -> {
			service.deleteOfferedCandidate(offeredCandidateId);
		});
		
	}
	
	/**
	* Failure position where an attempt is made to delete another users 
	* OfferedCandidate
	* @throws Exception
	*/
	@Test
	public void testDeleteOfferedCandidate_wrongUser() throws Exception{

		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(RECRUITER_ID);
		
		UUID 				offeredCandidateId 	= UUID.randomUUID();
		OfferedCandidate 	offeredCandidate 	= OfferedCandidate.builder().candidateRoleTitle("aRole").recruiterId("AnotherRecruitersId").build();
		
		Mockito.when(this.mockOfferedCandidateDao.findByOfferedCandidateId(offeredCandidateId)).thenReturn(offeredCandidate);
		
		Assertions.assertThrows(IllegalAccessException.class, () -> {
			service.deleteOfferedCandidate(offeredCandidateId);
		});
		
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
		
		OfferedCandidate c1 = OfferedCandidate.builder().id(id1).candidateRoleTitle("aRole").created(created1).build();
		OfferedCandidate c2 = OfferedCandidate.builder().id(id2).candidateRoleTitle("aRole").created(created2).build();
		OfferedCandidate c3 = OfferedCandidate.builder().id(id3).candidateRoleTitle("aRole").created(created3).build();
		
		Mockito.when(this.mockOfferedCandidateDao.findAllOfferedCandidates()).thenReturn(Set.of(c1,c2,c3));
		
		Set<OfferedCandidate> candidates = this.service.fetchOfferedCandidates();
		
		candidates.stream().filter(c -> c.getId()== id1).findAny().orElseThrow();
		candidates.stream().filter(c -> c.getId()== id2).findAny().orElseThrow();
		candidates.stream().filter(c -> c.getId()== id3).findAny().orElseThrow();
		
	}
	
	/**
	* Tests retrieval of OfferedCandidates for a specific Recruiter
	* @throws Exception
	*/
	@Test
	public void testFetchOfferedCandidatesByRecruiterId() throws Exception{
		
		final UUID id1 = UUID.randomUUID();
		final UUID id2 = UUID.randomUUID();
		final UUID id3 = UUID.randomUUID();
		
		final LocalDate created1 = LocalDate.of(2001, 1, 1);
		final LocalDate created2 = LocalDate.of(2003, 1, 1);
		final LocalDate created3 = LocalDate.of(2002, 1, 1);
		
		OfferedCandidate c1 = OfferedCandidate.builder().id(id1).created(created1).build();
		OfferedCandidate c2 = OfferedCandidate.builder().id(id2).created(created2).build();
		OfferedCandidate c3 = OfferedCandidate.builder().id(id3).created(created3).build();
		
		Mockito.when(this.mockOfferedCandidateDao.findAllOfferedCandidatesByRecruiterId(RECRUITER_ID)).thenReturn(Set.of(c1,c2,c3));
		
		Set<OfferedCandidate> candidates = this.service.fetchOfferedCandidates(RECRUITER_ID);
		
		candidates.stream().filter(c -> c.getId()== id1).findAny().orElseThrow();
		candidates.stream().filter(c -> c.getId()== id2).findAny().orElseThrow();
		candidates.stream().filter(c -> c.getId()== id3).findAny().orElseThrow();
		
	}
	
	/**
	* Tests retrieval of OpenPositions
	* @throws Exception
	*/
	@Test
	public void testFetchOpenPositions() throws Exception{
		
		final UUID id1 = UUID.randomUUID();
		final UUID id2 = UUID.randomUUID();
		final UUID id3 = UUID.randomUUID();
		
		final LocalDate created1 = LocalDate.of(2001, 1, 1);
		final LocalDate created2 = LocalDate.of(2003, 1, 1);
		final LocalDate created3 = LocalDate.of(2002, 1, 1);
		
		OpenPosition c1 = OpenPosition.builder().id(id1).created(created1).build();
		OpenPosition c2 = OpenPosition.builder().id(id2).created(created2).build();
		OpenPosition c3 = OpenPosition.builder().id(id3).created(created3).build();
		
		Mockito.when(this.mockOpenPositionDao.findAllOpenPositions()).thenReturn(Set.of(c1,c2,c3));
		
		Set<OpenPosition> openPositions = this.service.fetchOpenPositions();
		
		openPositions.stream().filter(c -> c.getId()== id1).findAny().orElseThrow();
		openPositions.stream().filter(c -> c.getId()== id2).findAny().orElseThrow();
		openPositions.stream().filter(c -> c.getId()== id3).findAny().orElseThrow();
		
	}
	
	/**
	* Tests retrieval of OpenPositions for a specific Recruiter
	* @throws Exception
	*/
	@Test
	public void testFetchOpenPositionsByRecruiter() throws Exception{
		
		final UUID id1 = UUID.randomUUID();
		final UUID id2 = UUID.randomUUID();
		final UUID id3 = UUID.randomUUID();
		
		final LocalDate created1 = LocalDate.of(2001, 1, 1);
		final LocalDate created2 = LocalDate.of(2003, 1, 1);
		final LocalDate created3 = LocalDate.of(2002, 1, 1);
		
		OpenPosition c1 = OpenPosition.builder().id(id1).created(created1).build();
		OpenPosition c2 = OpenPosition.builder().id(id2).created(created2).build();
		OpenPosition c3 = OpenPosition.builder().id(id3).created(created3).build();
		
		Mockito.when(this.mockOpenPositionDao.findAllOpenPositionsByRecruiterId(RECRUITER_ID)).thenReturn(Set.of(c1,c2,c3));
		
		Set<OpenPosition> openPositions = this.service.fetchOpenPositions(RECRUITER_ID);
		
		openPositions.stream().filter(c -> c.getId()== id1).findAny().orElseThrow();
		openPositions.stream().filter(c -> c.getId()== id2).findAny().orElseThrow();
		openPositions.stream().filter(c -> c.getId()== id3).findAny().orElseThrow();
		
	}
	
	/**
	* Tests case the Offered Candidate the contact request is for
	* does not exist
	* @throws Exception
	*/
	@Test
	public void testSendOfferedCandidateContactEmail_unknownOfferedCandidate() throws Exception{
		
		final UUID 		offeredCandidateId 	= UUID.randomUUID();
		final String 	message				= "aMessage";
		final String 	authenticatedUserId	= "recriter22";
		
		Mockito.when(this.mockOfferedCandidateDao.existsById(offeredCandidateId)).thenReturn(false);
		
		assertThrows(RuntimeException.class, () -> {
			this.service.sendOfferedCandidateContactEmail(offeredCandidateId, message, authenticatedUserId);
		});
		
	}
	
	/**
	* Tests Happy Path
	* @throws Exception
	*/
	@Test
	public void testSendOfferedCandidateContactEmail() throws Exception{
		
		final UUID 		offeredCandidateId 	= UUID.randomUUID();
		final String 	message				= "aMessage";
		final String 	authenticatedUserId	= "recriter22";
		final String	recruiterId			= "recruiter33";
		final String 	candidateRoleTitle	= "Java Developer";
		final OfferedCandidate offeredCandidate = OfferedCandidate
				.builder()
					.recruiterId(recruiterId)
					.candidateRoleTitle(candidateRoleTitle)
				.build();
		
		ArgumentCaptor<OfferedCandidateContactRequestEvent> argCapt = ArgumentCaptor.forClass(OfferedCandidateContactRequestEvent.class);
		
		Mockito.when(this.mockOfferedCandidateDao.existsById(offeredCandidateId)).thenReturn(true);
		Mockito.when(this.mockOfferedCandidateDao.findByOfferedCandidateId(offeredCandidateId)).thenReturn(offeredCandidate);
		
		Mockito.doNothing().when(this.mockEventPublisher).publishOffereedCandidateRequestEvent(argCapt.capture());
		
		this.service.sendOfferedCandidateContactEmail(offeredCandidateId, message, authenticatedUserId);
		
		Mockito.verify(this.mockEventPublisher).publishOffereedCandidateRequestEvent(Mockito.any());
		
		OfferedCandidateContactRequestEvent event = argCapt.getValue();
		
		assertEquals(message, 				event.getMessage());
		assertEquals(offeredCandidateId, 	event.getOfferedCandidateId());
		assertEquals(candidateRoleTitle, 	event.getOfferedCandidateTitle());
		assertEquals(recruiterId, 			event.getRecipientId());
		assertEquals(authenticatedUserId, 	event.getSenderId());
		
	}
	
	/**
	* Tests case the Open Position the contact request is for
	* does not exist
	* @throws Exception
	*/
	@Test
	public void testSendOpenPositionContactEmail_unknownOPenPosition() throws Exception{
		
		final UUID 		openPositionId 		= UUID.randomUUID();
		final String 	message				= "aMessage";
		final String 	authenticatedUserId	= "recriter22";
		
		Mockito.when(this.mockOpenPositionDao.existsById(openPositionId)).thenReturn(false);
		
		assertThrows(RuntimeException.class, () -> {
			this.service.sendOpenPositionContactEmail(openPositionId, message, authenticatedUserId);
		});
		
	}
	
	/**
	* Tests Happy Path
	* @throws Exception
	*/
	@Test
	public void testSendOpenPositionContactEmail() throws Exception{
		
		final UUID 			openPositionId 		= UUID.randomUUID();
		final String 		message				= "aMessage";
		final String 		authenticatedUserId	= "recriter22";
		final String		recruiterId			= "recruiter33";
		final String 		openPositionTitle	= "Java Developer";
		final OpenPosition 	openPosition 		= OpenPosition
				.builder()
					.recruiterId(recruiterId)
					.positionTitle(openPositionTitle)
				.build();
		
		ArgumentCaptor<OpenPositionContactRequestEvent> argCapt = ArgumentCaptor.forClass(OpenPositionContactRequestEvent.class);
		
		Mockito.when(this.mockOpenPositionDao.existsById(openPositionId)).thenReturn(true);
		Mockito.when(this.mockOpenPositionDao.findByOpenPositionId(openPositionId)).thenReturn(openPosition);
		
		Mockito.doNothing().when(this.mockEventPublisher).publishOpenPositionContactRequestEvent(argCapt.capture());
		
		this.service.sendOpenPositionContactEmail(openPositionId, message, authenticatedUserId);
		
		Mockito.verify(this.mockEventPublisher).publishOpenPositionContactRequestEvent(Mockito.any());
		
		OpenPositionContactRequestEvent event = argCapt.getValue();
		
		assertEquals(message, 				event.getMessage());
		assertEquals(openPositionId, 		event.getOpenPositionId());
		assertEquals(openPositionTitle, 	event.getOpenPositionTitle());
		assertEquals(recruiterId, 			event.getRecipientId());
		assertEquals(authenticatedUserId, 	event.getSenderId());
		
	}
	
	/**
	* Tests fetching a USers events
	* @throws Exception
	*/
	@Test
	public void testFetchViewedEventsByRecruiter() throws Exception{
		
		final String 	recruiterId 	= "kparkings";
		final UUID		id1 			= UUID.randomUUID();
		final UUID		id2 			= UUID.randomUUID();
		
		Mockito.when(this.mockSupplyAndDemandEventDao.fetchEventsForRecruiter(EventType.OFFERED_CANDIDATE, recruiterId)).thenReturn(Set.of(id1, id2));
		
		Set<UUID> ids = this.service.fetchViewedEventsByRecruiter(EventType.OFFERED_CANDIDATE, recruiterId);
		
		assertTrue(ids.contains(id1));
		assertTrue(ids.contains(id2));
		
	}
	
	/**
	* Tests disabling of OpenPositions and OfferedCandidates belonging to a Recruiter
	* @throws Exception
	*/
	@SuppressWarnings("unchecked")
	@Test
	public void testDisableSupplyAndDemandPostsForRecruiter() throws Exception{
		
		final String recruiterId = "rec222";
		final Set<OfferedCandidate> ocs = Set.of(OfferedCandidate.builder().active(true).build(), OfferedCandidate.builder().active(true).build());
		final Set<OpenPosition> 	ops = Set.of(OpenPosition.builder().active(true).build(), OpenPosition.builder().active(true).build(), OpenPosition.builder().active(true).build());
		
		ArgumentCaptor<Set<OfferedCandidate>> 	argCaptOcs = ArgumentCaptor.forClass(Set.class);
		ArgumentCaptor<Set<OpenPosition>> 		argCaptOps = ArgumentCaptor.forClass(Set.class);
		
		Mockito.when(this.mockOfferedCandidateDao.findAllOfferedCandidatesByRecruiterId(Mockito.anyString())).thenReturn(ocs);
		Mockito.when(this.mockOpenPositionDao.findAllOpenPositionsByRecruiterId(Mockito.anyString())).thenReturn(ops);
		
		Mockito.doNothing().when(this.mockOfferedCandidateDao).persistOfferedCandidates(argCaptOcs.capture());
		Mockito.doNothing().when(this.mockOpenPositionDao).persistOpenPositions(argCaptOps.capture());
		
		this.service.disableSupplyAndDemandPostsForRecruiter(recruiterId);
		
		Mockito.verify(this.mockOfferedCandidateDao).persistOfferedCandidates(Mockito.anySet());
		Mockito.verify(this.mockOpenPositionDao).persistOpenPositions(Mockito.anySet());
		
		assertEquals(2, argCaptOcs.getValue().size());
		assertEquals(3, argCaptOps.getValue().size());
		
		argCaptOcs.getValue().stream().forEach(oc -> Assertions.assertFalse(oc.isActive()));
		argCaptOps.getValue().stream().forEach(op -> Assertions.assertFalse(op.isActive()));
		
	}
	
	/**
	* Tests disabling of OpenPositions and OfferedCandidates belonging to a Recruiter
	* @throws Exception
	*/
	@SuppressWarnings("unchecked")
	@Test
	public void testEnableSupplyAndDemandPostsForRecruiter() throws Exception{
		
		final String recruiterId = "rec222";
		final Set<OfferedCandidate> ocs = Set.of(OfferedCandidate.builder().active(false).build(), OfferedCandidate.builder().active(false).build());
		final Set<OpenPosition> 	ops = Set.of(OpenPosition.builder().active(false).build(), OpenPosition.builder().active(false).build(), OpenPosition.builder().active(false).build());
		
		ArgumentCaptor<Set<OfferedCandidate>> 	argCaptOcs = ArgumentCaptor.forClass(Set.class);
		ArgumentCaptor<Set<OpenPosition>> 		argCaptOps = ArgumentCaptor.forClass(Set.class);
		
		Mockito.when(this.mockOfferedCandidateDao.findAllOfferedCandidatesByRecruiterId(Mockito.anyString())).thenReturn(ocs);
		Mockito.when(this.mockOpenPositionDao.findAllOpenPositionsByRecruiterId(Mockito.anyString())).thenReturn(ops);
		
		Mockito.doNothing().when(this.mockOfferedCandidateDao).persistOfferedCandidates(argCaptOcs.capture());
		Mockito.doNothing().when(this.mockOpenPositionDao).persistOpenPositions(argCaptOps.capture());
		
		this.service.enableSupplyAndDemandPostsForRecruiter(recruiterId);
		
		Mockito.verify(this.mockOfferedCandidateDao).persistOfferedCandidates(Mockito.anySet());
		Mockito.verify(this.mockOpenPositionDao).persistOpenPositions(Mockito.anySet());
		
		assertEquals(2, argCaptOcs.getValue().size());
		assertEquals(3, argCaptOps.getValue().size());
		
		argCaptOcs.getValue().stream().forEach(oc -> Assertions.assertTrue(oc.isActive()));
		argCaptOps.getValue().stream().forEach(op -> Assertions.assertTrue(op.isActive()));
		
	}
	
}