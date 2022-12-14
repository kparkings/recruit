package com.arenella.recruit.candidates.adapters;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CandidateNoLongerAvailableEvent;
import com.arenella.recruit.curriculum.adapters.ExternalEventListener;

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
public class MonolithExternalEventPublisher implements ExternalEventPublisher{

	@Autowired
	private ExternalEventListener curriculumEventListener;
	
	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publishSearchedSkillsEvent(Set<String> skills) {
		
		boolean isAdminUser	= SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
		
		/**
		* We don't want to log these events for Admin users
		*/
		if (isAdminUser) {
			return;
		}
		
		curriculumEventListener.listenForSearchedSkillsEvent(skills);
	}
	
	/**
	* Refer to ExternalEventPublisher for details 
	*/
	public void publishPendingCurriculumDeletedEvent(UUID pendingCurriculumId) {
		curriculumEventListener.listenForPendingCurriculumDeletedEvent(pendingCurriculumId);
	}

	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publishCandidateNoLongerAvailableEvent(CandidateNoLongerAvailableEvent event) {
		curriculumEventListener.listenForCandidateNoLongerAvailableEvent(event);
		
	}

	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publishCandidateCreatedEvent(CandidateCreatedEvent event) {
		
		//TODO: [KP] When its built pass event to test against Alerts for Matching candidates.
		
	}

}
