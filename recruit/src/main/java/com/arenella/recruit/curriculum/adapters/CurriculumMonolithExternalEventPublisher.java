package com.arenella.recruit.curriculum.adapters;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CreditsAssignedEvent;
import com.arenella.recruit.adapters.events.CreditsUsedEvent;
import com.arenella.recruit.adapters.events.CurriculumUpdatedEvent;
import com.arenella.recruit.candidates.adapters.CandidateExternalEventListener;

/**
* An implementation of ExternalEventPublisher optimised to work when the 
* external services are physically located in the same Monolith.
* 
* Here we are calling we are connecting to the other services directly. If the 
* application is ever split middleware will be needed and this direct dependency
* will no longer exist. As this is a monolith we can connect directly but by adding 
* all the external dependencies into EventPublishers/Listeners it will be easy 
* to split the services at a later date
* 
* @author K Parkings
*/
@Service
public class CurriculumMonolithExternalEventPublisher implements ExternalEventPublisher{

	@Autowired
	private CandidateExternalEventListener 	candiditeExternalEventListener;
	
	@Autowired
	private CurriculumExternalEventListener curriculumExternalEventListener;
	
	/**
	* Refer to the ExternalEventPublisher interface for details
	*/
	@Override
	public void publishCreditsAssignedEvent(CreditsAssignedEvent event) {
		this.candiditeExternalEventListener.listenForCreditsAssignedEvent(event);
	}
	
	/**
	* Refer to ExternalEventPublisher for details 
	*/
	public void publishPendingCurriculumDeletedEvent(UUID pendingCurriculumId) {
		curriculumExternalEventListener.listenForPendingCurriculumDeletedEvent(pendingCurriculumId);
	}

	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publishCreditsUsedEvent(CreditsUsedEvent event) {
		this.candiditeExternalEventListener.listenForCreditsUsedEvent(event);
		
	}

	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publishCurriculumUpdates(CurriculumUpdatedEvent event) {
		candiditeExternalEventListener.listenForCurriculumUpdatedEvent(event);
	}
	
}