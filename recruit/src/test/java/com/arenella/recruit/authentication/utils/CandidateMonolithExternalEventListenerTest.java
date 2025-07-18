package com.arenella.recruit.authentication.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.CreditsUsedEvent;
import com.arenella.recruit.adapters.events.CurriculumSkillsExtractionEvent;
import com.arenella.recruit.adapters.events.CurriculumUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterDeletedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.candidates.adapters.CandidateMonolithExternalEventListener;
import com.arenella.recruit.candidates.adapters.ExternalEventPublisher;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.RecruiterCredit;
import com.arenella.recruit.candidates.beans.SkillUpdateStat;
import com.arenella.recruit.candidates.dao.SkillUpdateStatDao;
import com.arenella.recruit.candidates.repos.CandidateRepository;
import com.arenella.recruit.candidates.services.CandidateService;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Unit tests for the CandidateMonolithExternalEventListener class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class CandidateMonolithExternalEventListenerTest {

	private static final String EMAIL 			= "a@b.cd";
	private static final String FIRSTNAME 		= "fred";
	private static final String SURNAME 		= "smith";
	private static final String RECRUITER_ID 	= "rec44";
	
	@InjectMocks
	private CandidateMonolithExternalEventListener listener;
	
	@Mock
	private CandidateService 						mockCandidateService;
	
	@Mock
	private ExternalEventPublisher					mockExternalEventPublisher;
	
	@Mock
	private CandidateRepository 					mockCandidateRepo;
	
	@Mock
	private SkillUpdateStatDao						mockSkillUpdateStatDao;
	
	/**
	* Tests handling of RecruiterCreatedEvent persists contact
	* @throws Exception
	*/
	@Test
	void testListenForRecruiterCreatedEvent() {
		
		RecruiterCreatedEvent event = RecruiterCreatedEvent
			.builder()
				.email(EMAIL)
				.firstName(FIRSTNAME)
				.recruiterId(RECRUITER_ID)
				.surname(SURNAME)
			.build();
		
		listener.listenForRecruiterCreatedEvent(event);
		
		verify(mockCandidateService).updateContact(RECRUITER_ID, EMAIL, FIRSTNAME, SURNAME);
		
	}
	
	/**
	* Tests handling of RecruiterCreatedEvent persists contact
	* @throws Exception
	*/
	@Test
	void testListenForRecruiterUpdatedEvent() {
		
		RecruiterUpdatedEvent event = RecruiterUpdatedEvent
			.builder()
				.email(EMAIL)
				.firstName(FIRSTNAME)
				.recruiterId(RECRUITER_ID)
				.surname(SURNAME)
			.build();
		
		listener.listenForRecruiterUpdatedEvent(event);
		
		verify(mockCandidateService).updateContact(RECRUITER_ID, EMAIL, FIRSTNAME, SURNAME);
		
	}
	
	/**
	* Tests handling of the Event
	* @throws Exception
	*/
	@Test
	void testListenForCreditsUsedEvent() {
		
		final String 	userId 		= "kparkings";
		final int 		credits 	= 20;
		
		CreditsUsedEvent event = new CreditsUsedEvent(userId, credits);
		
		doNothing().when(this.mockCandidateService).updateCreditsForUser(userId, credits);
		
		this.listener.listenForCreditsUsedEvent(event);
		
		verify(this.mockCandidateService).updateCreditsForUser(userId, credits);
		
	}
	
	/**
	* Tests correct action is carried out when a RecruiterHasOpenSubscriptionEvent is received
	* @throws Exception
	*/
	@Test
	void testListenForRecruiterHasOpenSubscriptionEvent() {
	
		final String 			recruiterId 		= "kparkings";
		
		SubscriptionAddedEvent event = new SubscriptionAddedEvent(recruiterId, subscription_type.ONE_MONTH_SUBSCRIPTION);
		
		this.listener.listenForSubscriptionAddedEvent(event);
	
		verify(this.mockCandidateService).updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DISABLED_CREDITS, Optional.of(true));
		
	}

	/**
	* Tests if no subscription credits are disabled. 
	* @throws Exception
	*/
	@Test
	void testListenForRecruiterNoOpenSubscriptionsEvent() {
		
		final String recruiterId = "r55";
		
		this.listener.listenForRecruiterNoOpenSubscriptionsEvent(new RecruiterNoOpenSubscriptionEvent(recruiterId));
	
		verify(this.mockCandidateService).updateCreditsForUser(recruiterId, RecruiterCredit.DISABLED_CREDITS);
		
	}
	
	/**
	* If no Candidates with id matching the curriculumId then we can't match the Curriculum to a Candidate and 
	* therefore there is no need to create a NewsFeedItem so silently fail
	* @throws Exception
	*/
	@Test
	void testListenForCurriculumUpdatedEvent_no_corresponding_candidate() throws Exception{
		
		final String id = "1000";
		
		when(this.mockCandidateRepo.findCandidateById(Long.valueOf(id))).thenReturn(Optional.empty());
		
		this.listener.listenForCurriculumUpdatedEvent(new CurriculumUpdatedEvent(id));
		
		verify(this.mockExternalEventPublisher, never()).publishCandidateUpdateEvent(any());
		
	}
	
	/**
	* If Candidates with id matching the curriculumId then we can match the Curriculum to a Candidate and 
	* therefore  create a NewsFeedItem 
	* @throws Exception
	*/
	@Test
	void testListenForCurriculumUpdatedEvent_corresponding_candidate() throws Exception{
		
		final String id = "1000";
		
		when(this.mockCandidateRepo.findCandidateById(Long.valueOf(id))).thenReturn(Optional.of(Candidate.builder()
				.candidateId(id)
				.build()));
		
		this.listener.listenForCurriculumUpdatedEvent(new CurriculumUpdatedEvent(id));
		
		verify(this.mockExternalEventPublisher).publishCandidateUpdateEvent(any());
		
	}
	
	/**
	* Test case that the Subscription is a credit based subscriptiio
	* @throws Exception
	*/
	@Test
	void testSubscriptionAddedEvent_creditBased() {
	
		final String userId = "user1";
		
		this.listener.listenForSubscriptionAddedEvent(new SubscriptionAddedEvent(userId, subscription_type.CREDIT_BASED_SUBSCRIPTION));
		
	}
	
	/**
	* Tests case that the Subscription is a paid Subscription
	* @throws Exception
	*/
	@Test
	void testSubscriptionAddedEvent_paidSubscription() {
		
		final String userId = "user1";
		
		this.listener.listenForSubscriptionAddedEvent(new SubscriptionAddedEvent(userId, subscription_type.ONE_MONTH_SUBSCRIPTION));
	
		verify(this.mockCandidateService).updateCreditsForUser(userId, RecruiterCredit.DISABLED_CREDITS, Optional.of(true));
	
	}
	
	/**
	* Tests case in which event is received for an unknown Candidate
	* @throws Exception
	*/
	@Test
	void testListenForCurriculumSkillsExtractionEvent_candidateUnknown() {
		
		when(this.mockCandidateRepo.findCandidateById(111L)).thenReturn(Optional.empty());
		
		this.listener.listenForCurriculumSkillsExtractionEvent(new CurriculumSkillsExtractionEvent(111L, Set.of()));
		
		verify(this.mockCandidateRepo, never()).saveCandidate(any());
		verify(this.mockSkillUpdateStatDao, never()).saveSkillUpdateStat(any());
	}
	
	/**
	* Tests case in which event is received for a known Candidate
	* @throws Exception
	*/
	@Test
	void testListenForCurriculumSkillsExtractionEvent() {
	
		ArgumentCaptor<Candidate> 		argCapt 	= ArgumentCaptor.forClass(Candidate.class);
		ArgumentCaptor<SkillUpdateStat> argStatCapt = ArgumentCaptor.forClass(SkillUpdateStat.class);
		
		when(this.mockCandidateRepo.findCandidateById(111L)).thenReturn(Optional.of(Candidate.builder().skills(Set.of("java", "php")).build()));
		
		this.listener.listenForCurriculumSkillsExtractionEvent(new CurriculumSkillsExtractionEvent(111L, Set.of("c#", "java")));
		
		verify(this.mockCandidateRepo).saveCandidate(argCapt.capture());
		
		assertEquals(3, argCapt.getValue().getSkills().size());
		assertTrue(argCapt.getValue().getSkills().contains("java"));
		assertTrue(argCapt.getValue().getSkills().contains("c#"));
		assertTrue(argCapt.getValue().getSkills().contains("php"));
		
		verify(this.mockSkillUpdateStatDao).saveSkillUpdateStat(argStatCapt.capture());
		
		assertEquals(1, 	argStatCapt.getValue().getAddedSkillsCount());
		assertEquals(111L, 	argStatCapt.getValue().getCandidateId());
		
	}
	
	/**
	* Tests handling even sends instruction to delete candidates owned by the Recruiter
	* @throws Exception
	*/
	@Test
	void testListenForRecruiterAccountDeletedEvent() {
		
		final String recruiterId = "rec1";
		
		this.listener.listenForRecruiterAccountDeletedEvent(new RecruiterDeletedEvent(recruiterId));
		
		verify(this.mockCandidateService).deleteCandidatesForOwnedByRecruiter(recruiterId);
		verify(this.mockCandidateService).deleteSavedCandidatesForRecruiter(recruiterId);
		verify(this.mockCandidateService).deleteCreditsForRecruiter(recruiterId);
		verify(this.mockCandidateService).deleteContactForRecruiter(recruiterId);
		verify(this.mockCandidateService).deleteSavedCandidatesForRecruiter(recruiterId);
		
	}

}