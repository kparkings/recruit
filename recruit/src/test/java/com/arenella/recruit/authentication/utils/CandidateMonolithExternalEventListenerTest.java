package com.arenella.recruit.authentication.utils;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.CreditsUsedEvent;
import com.arenella.recruit.adapters.events.CurriculumUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.candidates.adapters.CandidateMonolithExternalEventListener;
import com.arenella.recruit.candidates.adapters.ExternalEventPublisher;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.RecruiterCredit;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.repos.CandidateRepository;
import com.arenella.recruit.candidates.services.CandidateService;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Unit tests for the CandidateMonolithExternalEventListener class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateMonolithExternalEventListenerTest {

	private static final String EMAIL 			= "a@b.cd";
	private static final String FIRSTNAME 		= "fred";
	private static final String SURNAME 		= "smith";
	private static final String RECRUITER_ID 	= "rec44";
	
	@InjectMocks
	private CandidateMonolithExternalEventListener listener;
	
	@Mock
	private CandidateService 						mockCandidateService;
	
	@Mock
	private CandidateDao 							mockCandidateDao;
	
	@Mock
	private ExternalEventPublisher					mockExternalEventPublisher;
	
	@Mock
	private CandidateRepository 					mockCandidateRepo;
	
	/**
	* Tests handling of RecruiterCreatedEvent persists contact
	* @throws Exception
	*/
	@Test
	public void testListenForRecruiterCreatedEvent() throws Exception{
		
		RecruiterCreatedEvent event = RecruiterCreatedEvent
			.builder()
				.email(EMAIL)
				.firstName(FIRSTNAME)
				.recruiterId(RECRUITER_ID)
				.surname(SURNAME)
			.build();
		
		listener.listenForRecruiterCreatedEvent(event);
		
		Mockito.verify(mockCandidateService).updateContact(RECRUITER_ID, EMAIL, FIRSTNAME, SURNAME);
		
	}
	
	/**
	* Tests handling of RecruiterCreatedEvent persists contact
	* @throws Exception
	*/
	@Test
	public void testListenForRecruiterUpdatedEvent() throws Exception{
		
		RecruiterUpdatedEvent event = RecruiterUpdatedEvent
			.builder()
				.email(EMAIL)
				.firstName(FIRSTNAME)
				.recruiterId(RECRUITER_ID)
				.surname(SURNAME)
			.build();
		
		listener.listenForRecruiterUpdatedEvent(event);
		
		Mockito.verify(mockCandidateService).updateContact(RECRUITER_ID, EMAIL, FIRSTNAME, SURNAME);
		
	}
	
	/**
	* Tests handling of the Event
	* @throws Exception
	*/
	@Test
	public void testListenForCreditsUsedEvent() throws Exception{
		
		final String 	userId 		= "kparkings";
		final int 		credits 	= 20;
		
		CreditsUsedEvent event = new CreditsUsedEvent(userId, credits);
		
		
		Mockito.doNothing().when(this.mockCandidateService).updateCreditsForUser(userId, credits);
		
		this.listener.listenForCreditsUsedEvent(event);
		
		Mockito.verify(this.mockCandidateService).updateCreditsForUser(userId, credits);
		
	}
	
	/**
	* Tests correct action is carried out when a RecruiterHasOpenSubscriptionEvent is received
	* @throws Exception
	*/
	@Test
	public void testListenForRecruiterHasOpenSubscriptionEvent() throws Exception {
	
		final String 			recruiterId 		= "kparkings";
		
		SubscriptionAddedEvent event = new SubscriptionAddedEvent(recruiterId, subscription_type.ONE_MONTH_SUBSCRIPTION);
		
		this.listener.listenForSubscriptionAddedEvent(event);
	
		Mockito.verify(this.mockCandidateService).updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DISABLED_CREDITS, Optional.of(true));
		
	}

	/**
	* Tests if no subscription credits are disabled. 
	* @throws Exception
	*/
	@Test
	public void testListenForRecruiterNoOpenSubscriptionsEvent() throws Exception{
		
		final String recruiterId = "r55";
		
		this.listener.listenForRecruiterNoOpenSubscriptionsEvent(new RecruiterNoOpenSubscriptionEvent(recruiterId));
	
		Mockito.verify(this.mockCandidateService).updateCreditsForUser(recruiterId, RecruiterCredit.DISABLED_CREDITS);
		
	}
	
	/**
	* If no Candidates with id matching the curriculumId then we can't match the Curriculum to a Candidate and 
	* therefore there is no need to create a NewsFeedItem so silently fail
	* @throws Exception
	*/
	@Test
	public void testListenForCurriculumUpdatedEvent_no_corresponding_candidate() throws Exception{
		
		final String id = "1000";
		
		Mockito.when(this.mockCandidateRepo.findCandidateById(Long.valueOf(id))).thenReturn(Optional.empty());
		
		this.listener.listenForCurriculumUpdatedEvent(new CurriculumUpdatedEvent(id));
		
		Mockito.verify(this.mockExternalEventPublisher, Mockito.never()).publishCandidateUpdateEvent(Mockito.any());
		
	}
	
	/**
	* If Candidates with id matching the curriculumId then we can match the Curriculum to a Candidate and 
	* therefore  create a NewsFeedItem 
	* @throws Exception
	*/
	@Test
	public void testListenForCurriculumUpdatedEvent_corresponding_candidate() throws Exception{
		
		final String id = "1000";
		
		Mockito.when(this.mockCandidateRepo.findCandidateById(Long.valueOf(id))).thenReturn(Optional.of(Candidate.builder()
				.candidateId(id)
				.build()));
		
		this.listener.listenForCurriculumUpdatedEvent(new CurriculumUpdatedEvent(id));
		
		Mockito.verify(this.mockExternalEventPublisher).publishCandidateUpdateEvent(Mockito.any());
		
	}
	
	/**
	* Test case that the Subscription is a credit based subscriptiio
	* @throws Exception
	*/
	@Test
	public void testSubscriptionAddedEvent_creditBased() throws Exception{
	
		final String userId = "user1";
		
		this.listener.listenForSubscriptionAddedEvent(new SubscriptionAddedEvent(userId, subscription_type.CREDIT_BASED_SUBSCRIPTION));
		
	}
	
	/**
	* Tests case that the Subscription is a non paid Subscription
	* @throws Exception
	*/
	@Test
	public void testSubscriptionAddedEvent_unaidSubscription() throws Exception{
		
		final String userId = "user1";
		
		this.listener.listenForSubscriptionAddedEvent(new SubscriptionAddedEvent(userId, subscription_type.FIRST_GEN));
		
		Mockito.verify(this.mockCandidateService).updateCreditsForUser(userId, RecruiterCredit.DISABLED_CREDITS, Optional.of(false));
		
	}
	
	/**
	* Tests case that the Subscription is a paid Subscription
	* @throws Exception
	*/
	@Test
	public void testSubscriptionAddedEvent_paidSubscription() throws Exception{
		
		final String userId = "user1";
		
		this.listener.listenForSubscriptionAddedEvent(new SubscriptionAddedEvent(userId, subscription_type.ONE_MONTH_SUBSCRIPTION));
	
		Mockito.verify(this.mockCandidateService).updateCreditsForUser(userId, RecruiterCredit.DISABLED_CREDITS, Optional.of(true));
	
	}

}