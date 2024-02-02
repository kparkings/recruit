package com.arenella.recruit.curriculum.adapters;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.CreditsAssignedEvent;
import com.arenella.recruit.adapters.events.CreditsUsedEvent;
import com.arenella.recruit.adapters.events.CurriculumUpdatedEvent;
import com.arenella.recruit.candidates.adapters.CandidateExternalEventListener;

/**
* Unit tests for the MonolithExternalEventPublisher class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CurriculumMonolithExternalEventPublisherTest {

	@Mock
	private CandidateExternalEventListener 				mockCandiditeExternalEventListener;
	
	@Mock
	private CurriculumExternalEventListener 			mockCurriculumEventListener;
	
	@InjectMocks
	private CurriculumMonolithExternalEventPublisher 	publisher = new CurriculumMonolithExternalEventPublisher();
	
	/**
	* Test sending of Event
	* @throws Exception
	*/
	@Test
	public void testPublishCreditsAssignedEvent() throws Exception{
		
		this.publisher.publishCreditsAssignedEvent(new CreditsAssignedEvent("kparkings", 3));	
		
		Mockito.verify(this.mockCandiditeExternalEventListener).listenForCreditsAssignedEvent(Mockito.any(CreditsAssignedEvent.class));
		
	}
	
	/**
	* Tests listener is called when Event is published
	* @throws Exception
	*/
	@Test
	public void testPublishPendingCurriculumDeletedEvent() throws Exception {
		
		Mockito.doNothing().when(mockCurriculumEventListener).listenForPendingCurriculumDeletedEvent(Mockito.any());
		
		publisher.publishPendingCurriculumDeletedEvent(UUID.randomUUID());
		
		Mockito.verify(mockCurriculumEventListener).listenForPendingCurriculumDeletedEvent(Mockito.any());
	
	}
	
	/**
	* Test sending of Event
	* @throws Exception
	*/
	@Test
	public void testPublishCreditsUsedEvent() throws Exception{
		
		this.publisher.publishCreditsUsedEvent(new CreditsUsedEvent("kparkings", 20));	
		
		Mockito.verify(this.mockCandiditeExternalEventListener).listenForCreditsUsedEvent(Mockito.any(CreditsUsedEvent.class));
		
	}
	
	/**
	* Test sending of Event
	* @throws Exception
	*/
	@Test
	public void testPublishCurriculumUpdates() throws Exception{
		
		this.publisher.publishCurriculumUpdates(new CurriculumUpdatedEvent("1222"));
		
		Mockito.verify(this.mockCandiditeExternalEventListener).listenForCurriculumUpdatedEvent(Mockito.any(CurriculumUpdatedEvent.class));
		
	}
}