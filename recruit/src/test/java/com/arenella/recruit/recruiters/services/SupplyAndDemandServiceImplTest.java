package com.arenella.recruit.recruiters.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
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

import com.arenella.recruit.adapters.actions.GrantCreditCommand;
import com.arenella.recruit.adapters.events.OpenPositionContactRequestEvent;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.recruiters.beans.RecruiterCredit;
import com.arenella.recruit.recruiters.dao.RecruiterCreditDao;
import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
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
	
	@Mock
	private RecruiterCreditDao					mockCreditDao;
	
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
		final Set<OpenPosition> 	ops = Set.of(OpenPosition.builder().active(true).build(), OpenPosition.builder().active(true).build(), OpenPosition.builder().active(true).build());
		
		ArgumentCaptor<Set<OpenPosition>> 		argCaptOps = ArgumentCaptor.forClass(Set.class);
		
		Mockito.when(this.mockOpenPositionDao.findAllOpenPositionsByRecruiterId(Mockito.anyString())).thenReturn(ops);
		Mockito.doNothing().when(this.mockOpenPositionDao).persistOpenPositions(argCaptOps.capture());
		
		this.service.disableSupplyAndDemandPostsForRecruiter(recruiterId);
	
		Mockito.verify(this.mockOpenPositionDao).persistOpenPositions(Mockito.anySet());
		
		assertEquals(3, argCaptOps.getValue().size());
		
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
		final Set<OpenPosition> 	ops = Set.of(OpenPosition.builder().active(false).build(), OpenPosition.builder().active(false).build(), OpenPosition.builder().active(false).build());
		
		ArgumentCaptor<Set<OpenPosition>> 		argCaptOps = ArgumentCaptor.forClass(Set.class);
		
		Mockito.when(this.mockOpenPositionDao.findAllOpenPositionsByRecruiterId(Mockito.anyString())).thenReturn(ops);
		Mockito.doNothing().when(this.mockOpenPositionDao).persistOpenPositions(argCaptOps.capture());
		
		this.service.enableSupplyAndDemandPostsForRecruiter(recruiterId);
		
		Mockito.verify(this.mockOpenPositionDao).persistOpenPositions(Mockito.anySet());
		
		assertEquals(3, argCaptOps.getValue().size());
		
		argCaptOps.getValue().stream().forEach(op -> Assertions.assertTrue(op.isActive()));
		
	}
	
	/**
	* Test updating the RecruiterCredits
	* @throws Exception
	*/
	@Test
	public void testUpdateCredits() throws Exception{
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<RecruiterCredit>> argCapt = ArgumentCaptor.forClass(Set.class);
		
		RecruiterCredit rc1 = RecruiterCredit.builder().recruiterId("recruiter1").credits(2).build();
		RecruiterCredit rc2 = RecruiterCredit.builder().recruiterId("recruiter1").credits(5).build();
		
		Mockito.when(this.mockCreditDao.fetchRecruiterCredits()).thenReturn(Set.of(rc1,rc2));
		Mockito.doNothing().when(this.mockCreditDao).saveAll(argCapt.capture());
		
		this.service.updateCredits(new GrantCreditCommand());
		
		if (argCapt.getValue().stream().filter(rc -> rc.getCredits() != RecruiterCredit.DEFAULT_CREDITS).findAny().isPresent()) {
			throw new RuntimeException();
		}
		
		Mockito.verify(this.mockEventPublisher, Mockito.times(2)).publishSendEmailCommand(Mockito.any(RequestSendEmailCommand.class));
		
	}
	
	/**
	* Test false returned if User not known
	* @throws Exception
	*/
	@Test
	public void testDoCreditsCheck_unknownUser() throws Exception{
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.empty());
		
		assertFalse(service.doCreditsCheck("recruiter33"));
		
	}
	
	/**
	* Test false returned if User has no remaining credits
	* @throws Exception
	*/
	@Test
	public void testDoCreditsCheck_knownUser_no_credits() throws Exception{
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(0).build();
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.of(rc));
		
		assertFalse(service.doCreditsCheck("recruiter33"));
		
	} 
	
	/**
	* Test false returned if User has no remaining credits
	* @throws Exception
	*/
	@Test
	public void testDoCreditsCheck_knownUser_has_credits() throws Exception{
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(1).build();
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.of(rc));
		
		assertTrue(service.doCreditsCheck("recruiter33"));
		
	}
	
	/**
	* Happy path
	* @throws Exception
	*/
	@Test
	public void testUpdateCreditsForUser() throws Exception{
		
		final String 	userId 		= "kparkings";
		final int 		credits 	= 20;

		ArgumentCaptor<RecruiterCredit> argCapt = ArgumentCaptor.forClass(RecruiterCredit.class);
		
		RecruiterCredit recCredits = RecruiterCredit.builder().recruiterId(userId).credits(30).build();
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.of(recCredits));
		Mockito.doNothing().when(this.mockCreditDao).persist(argCapt.capture());
		
		this.service.updateCreditsForUser(userId, credits);
	
		Mockito.verify(this.mockCreditDao).persist(Mockito.any());
		
		assertEquals(credits, argCapt.getValue().getCredits());
		
	}
	
	/**
	* If no credits for user does nothing
	* @throws Exception
	*/
	@Test
	public void testUpdateCreditsForUser_unknownRecruiter() throws Exception{
		
		final String 	userId 		= "kparkings";
		final int 		credits 	= 20;

		Mockito.when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.empty());
		
		this.service.updateCreditsForUser(userId, credits);
	
		Mockito.verify(this.mockCreditDao, Mockito.never()).persist(Mockito.any());
		
	}
	
	/**
	* Tests case no credits
	* @throws Exception
	*/
	@Test
	public void testGetCreditCountForUser_unknownUser() throws Exception{
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.getCreditCountForUser("rec22");
		});
		
	}
	
	/**
	* Tests case no credits
	* @throws Exception
	*/
	@Test
	public void testGetCreditCountForUser() throws Exception{
		
		final int credits = 5;
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(credits).build();
		
		Mockito.when(this.mockCreditDao.getByRecruiterId(Mockito.anyString())).thenReturn(Optional.of(rc));
		
		assertEquals(credits, this.service.getCreditCountForUser("rec22"));
		
	}
}