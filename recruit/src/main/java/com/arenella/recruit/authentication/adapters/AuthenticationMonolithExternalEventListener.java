package com.arenella.recruit.authentication.adapters;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CandidateAccountCreatedEvent;
import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidatePasswordUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterPasswordUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.authentication.beans.User.USER_ROLE;
import com.arenella.recruit.authentication.enums.AccountType;
import com.arenella.recruit.authentication.services.AccountService;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Implementation of ExternalEventListener optimized to 
* work for when the services are physically located in 
* the same Monolith.
* @author K Parkings
*/
@Service
public class AuthenticationMonolithExternalEventListener implements AuthenticationExternalEventListener{

	@Autowired
	private AccountService accountService;
	
	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event) {
		this.accountService.createAccount(event.getRecruiterId(), event.getEncryptedPassord(), AccountType.RECRUITER);
	}

	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent event) {
		accountService.replaceRolesForUser(event.geRecruiterId(), Set.of(USER_ROLE.recruiterNoSubscrition));
		accountService.updateUsersCreditStatus(event.geRecruiterId(), false);
	}

	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForSubscriptionAddedEvent(SubscriptionAddedEvent event) {
		accountService.replaceRolesForUser(event.getRecruiterId(), Set.of(USER_ROLE.recruiter));
		
		if (event.getSubscriptionType() == subscription_type.CREDIT_BASED_SUBSCRIPTION) {
			accountService.updateUsersCreditStatus(event.getRecruiterId(), true);
		} else {
			accountService.updateUsersCreditStatus(event.getRecruiterId(), false);
		}
		
	}

	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterPasswordUpdatedEvent(RecruiterPasswordUpdatedEvent event) {
		this.accountService.updateUserPassword(event.getRecruiterId(), event.getNewPassword());
	}

	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForCandidateAccountCreatedEvent(CandidateAccountCreatedEvent event) {
		this.accountService.createAccount(event.getCandidateId(), event.getEncryptedPassword(), AccountType.CANDIDATE);
	}

	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForCandidatePasswordUpdatedEvent(CandidatePasswordUpdatedEvent event) {
		this.accountService.updateUserPassword(event.getCandidateId(), event.getNewPassword());
	}

	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForCandidteDeletedEvent(CandidateDeletedEvent candidateDeletedEvent) {
		this.accountService.deleteAccount(candidateDeletedEvent.getCandidateId());
		
	}

}