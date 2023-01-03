package com.arenella.recruit.candidates.adapters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CandidateNoLongerAvailableEvent;
import com.arenella.recruit.curriculum.adapters.ExternalEventListener;
import com.arenella.recruit.emailservice.adapters.EmailServiceExternalEventListener;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;

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
	private ExternalEventListener 				eventListener;
	
	@Autowired
	private EmailServiceExternalEventListener 	emailServiceListener;
	
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
		
		eventListener.listenForSearchedSkillsEvent(skills);
	}
	
	/**
	* Refer to ExternalEventPublisher for details 
	*/
	public void publishPendingCurriculumDeletedEvent(UUID pendingCurriculumId) {
		eventListener.listenForPendingCurriculumDeletedEvent(pendingCurriculumId);
	}

	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publishCandidateNoLongerAvailableEvent(CandidateNoLongerAvailableEvent event) {
		eventListener.listenForCandidateNoLongerAvailableEvent(event);
		
	}

	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publishCandidateCreatedEvent(CandidateCreatedEvent event) {
		eventListener.listenForCandidateCreatedEvent(event);
	}

	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publishRequestSendAlertDailySummaryEmailCommand(RequestSendAlertDailySummaryEmailCommand command) {
		
		Map<String,Object> model = new HashMap<>();
		
		command.getMatchesByAlert().keySet().stream().forEach(key -> {
			model.put(key, command.getMatchesByAlert().get(key));
		});
		
		RequestSendEmailCommand c = 
				RequestSendEmailCommand
					.builder()
						.emailType(EmailType.EXTERN)
						.model(model)
						.persistable(false)
					//	.recipients(Set.of(new Recipient<String>(recruiter.getUserId(), RecipientType.RECRUITER, recruiter.getEmail())))
						.sender(new Sender<>(UUID.randomUUID(), SenderType.SYSTEM, "kparkings@gmail.com"))
						.title("Arenella-ICT - New Matching Candidates")
						.topic(EmailTopic.ALERT_MATCHES)
					.build();
		
		this.emailServiceListener.listenForSendEmailCommand(c);
		
	}

}
