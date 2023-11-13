package com.arenella.recruit.candidates.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CreditsAssignedEvent;
import com.arenella.recruit.adapters.events.CreditsUsedEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.candidates.beans.RecruiterCredit;
import com.arenella.recruit.candidates.services.CandidateService;

/**
* Monolith implementation of the listener. In monolith setup just calls services.
* @author K parkings
*/
@Service
public class CandidateMonolithExternalEventListener implements CandidateExternalEventListener {

	@Autowired
	private CandidateService candidateService;
	
	/**
	* Refer to CandidateExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event) {
		this.candidateService.updateContact(event.getRecruiterId(), event.getEmail(), event.getFirstName(), event.getSurname());
	}

	/**
	* Refer to CandidateExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterUpdatedEvent(RecruiterUpdatedEvent event) {
		this.candidateService.updateContact(event.getRecruiterId(), event.getEmail(), event.getFirstName(), event.getSurname());
	}

	/**
	* Refer to CandidateExternalEventListener for details 
	*/
	@Override
	public void listenForCreditsAssignedEvent(CreditsAssignedEvent event) {
		this.candidateService.updateCreditsForUser(event.getUserId(), event.getCurrentCreditCount());
		
	}

	/**
	* Refer to CandidateExternalEventListener for details 
	*/
	@Override
	public void listenForCreditsUsedEvent(CreditsUsedEvent event) {
		this.candidateService.updateCreditsForUser(event.getUserId(), event.getCredits());
	}

	/**
	* Refer to CandidateExternalEventListener for details 
	*/
	@Override
	public void listenForSubscriptionAddedEvent(SubscriptionAddedEvent event) {
		this.candidateService.updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DISABLED_CREDITS);
	}

	/**
	* Refer to CandidateExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent event) {
		this.candidateService.updateCreditsForUser(event.geRecruiterId(), RecruiterCredit.DISABLED_CREDITS);
		
	}

}
