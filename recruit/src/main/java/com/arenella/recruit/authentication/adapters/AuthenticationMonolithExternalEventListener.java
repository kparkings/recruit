package com.arenella.recruit.authentication.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.authentication.enums.AccountType;
import com.arenella.recruit.authentication.services.AccountService;

/**
* Implementation of ExternalEventListener optimised to 
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

}