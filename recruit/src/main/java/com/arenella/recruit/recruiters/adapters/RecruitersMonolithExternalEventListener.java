package com.arenella.recruit.recruiters.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.actions.GrantCreditCommand;
import com.arenella.recruit.adapters.events.RecruiterHasOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUserAccountCreatedEvent;
import com.arenella.recruit.recruiters.services.SupplyAndDemandService;

/**
* Implementation of ExternalEventListener optimised to 
* work for when the services are physically located in 
* the same Monolith.
* @author K Parkings
*/
@Service
public class RecruitersMonolithExternalEventListener implements RecruitersExternalEventListener{
	
	@Autowired
	private SupplyAndDemandService supplyAndDemandService;
	
	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterAccountCreatedEvent(RecruiterUserAccountCreatedEvent event) {
		// TODO Auto-generated method stub
		
	}

	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent recruiterNoOpenSubscriptionEvent) {
		this.supplyAndDemandService.disableSupplyAndDemandPostsForRecruiter(recruiterNoOpenSubscriptionEvent.geRecruiterId());
		
	}

	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterHasOpenSubscriptionsEvent(RecruiterHasOpenSubscriptionEvent recruiterHasOpenSubscriptionEvent) {
		this.supplyAndDemandService.enableSupplyAndDemandPostsForRecruiter(recruiterHasOpenSubscriptionEvent.geRecruiterId());
	}

	@Override
	public void listenForGrantCreditCommand(GrantCreditCommand command) {
		this.supplyAndDemandService.updateCredits(command);
	}

}