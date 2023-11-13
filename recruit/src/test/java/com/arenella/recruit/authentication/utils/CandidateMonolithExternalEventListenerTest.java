package com.arenella.recruit.authentication.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.CreditsUsedEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.authentication.beans.User.USER_ROLE;
import com.arenella.recruit.candidates.adapters.CandidateMonolithExternalEventListener;
import com.arenella.recruit.candidates.beans.RecruiterCredit;
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
	private CandidateService mockCandidateService;
	
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
	
		Mockito.verify(this.mockCandidateService).updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DISABLED_CREDITS);
		
	}
}
