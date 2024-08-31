package com.arenella.recruit.candidates.adapters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CandidateAccountCreatedEvent;
import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidateNoLongerAvailableEvent;
import com.arenella.recruit.adapters.events.CandidatePasswordUpdatedEvent;
import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.adapters.events.CandidateUpdatedEvent;
import com.arenella.recruit.adapters.events.ContactRequestEvent;
import com.arenella.recruit.adapters.events.RecruiterPasswordUpdatedEvent;
import com.arenella.recruit.authentication.adapters.AuthenticationExternalEventListener;
import com.arenella.recruit.curriculum.adapters.CurriculumExternalEventListener;
import com.arenella.recruit.emailservice.adapters.EmailServiceExternalEventListener;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.newsfeed.adapters.NewsFeedExternalEventListener;

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
	private CurriculumExternalEventListener 		curriculumExternalEventListener;
	
	@Autowired
	private AuthenticationExternalEventListener 	authenticationExternalEventListener;
	
	@Autowired
	private EmailServiceExternalEventListener 		emailServiceExternalEventListener;
	
	@Autowired
	private NewsFeedExternalEventListener			newsFeedExternalEventListener;
	
	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publishSearchedSkillsEvent(Set<String> skills) {		
		curriculumExternalEventListener.listenForSearchedSkillsEvent(skills);
	}

	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publishCandidateNoLongerAvailableEvent(CandidateNoLongerAvailableEvent event) {
		curriculumExternalEventListener.listenForCandidateNoLongerAvailableEvent(event);
		
	}

	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publishCandidateCreatedEvent(CandidateCreatedEvent event) {
		this.curriculumExternalEventListener.listenForCandidateCreatedEvent(event);
		this.emailServiceExternalEventListener.listenForCandidateCreatedEvent(event);
	}

	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publishRequestSendAlertDailySummaryEmailCommand(RequestSendAlertDailySummaryEmailCommand command) {
		
		Map<String,Object> alerts = new HashMap<>();
		
		command.getMatchesByAlert().keySet().stream().forEach(key -> {
			alerts.put(key, command.getMatchesByAlert().get(key));
		});
	
		Map<String, Object> model = new HashMap<>();
		model.put("alerts", alerts);
		model.put("recipientName", command.getRecruiterId());
		
		RequestSendEmailCommand c = 
				RequestSendEmailCommand
					.builder()
						.emailType(EmailType.EXTERN)
						.model(model)
						.persistable(false)
						.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(), command.getRecruiterId(), ContactType.RECRUITER)))
						.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "kparkings@gmail.com"))
						.title("Arenella-ICT - New Matching Candidates")
						.topic(EmailTopic.ALERT_MATCHES)
					.build();
		
		this.emailServiceExternalEventListener.listenForSendEmailCommand(c);
		
	}

	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publishCandidateAccountCreatedEvent(CandidateAccountCreatedEvent event) {
		authenticationExternalEventListener.listenForCandidateAccountCreatedEvent(event);
	}
	
	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishCandidatePasswordUpdated(CandidatePasswordUpdatedEvent event) {
		this.authenticationExternalEventListener.listenForCandidatePasswordUpdatedEvent(event);
	}

	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishCandidateAccountUpdatedEvent(CandidateUpdatedEvent event) {
		this.emailServiceExternalEventListener.listenForCandidateUpdatedEvent(event);
	}

	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishSendEmailCommand(RequestSendEmailCommand command) {
		this.emailServiceExternalEventListener.listenForSendEmailCommand(command);
		
	}
	
	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishCandidateDeletedEvent(CandidateDeletedEvent candidateDeletedEvent) {
		this.emailServiceExternalEventListener.listenForCandidteDeletedEvent(candidateDeletedEvent);
		this.curriculumExternalEventListener.listenForCandidteDeletedEvent(candidateDeletedEvent);
		this.authenticationExternalEventListener.listenForCandidteDeletedEvent(candidateDeletedEvent);
		
	}

	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishContactRequestEvent(ContactRequestEvent event) {
		
		Map<String, Object> modelExt = new HashMap<>();
		modelExt.put("message", 	event.getMessage()); 
		modelExt.put("title", 		event.getTitle());
		
		Map<String, Object> modelInt = new HashMap<>();
		modelInt.put("message", 	event.getMessage());
		modelInt.put("title",	 	event.getTitle());
		
		RequestSendEmailCommand cInt = 
				RequestSendEmailCommand
					.builder()
						.emailType(EmailType.INTERN)
						.model(modelInt)
						.persistable(true)
						.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(), event.getRecipientId(), ContactType.RECRUITER)))
						.sender(new Sender<>(UUID.randomUUID(), event.getSenderRecruiterId(), SenderType.RECRUITER, "get in email service"))
						.title(event.getTitle())
						.topic(EmailTopic.REC_TO_REC_CONTACT_REQUEST)
					.build();
		
		this.emailServiceExternalEventListener.listenForSendEmailCommand(cInt);
		
		RequestSendEmailCommand cExt = 
				RequestSendEmailCommand
					.builder()
						.emailType(EmailType.EXTERN)
						.model(modelExt)
						.persistable(false)
						.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(), event.getRecipientId(), ContactType.RECRUITER)))
						.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "kparkings@gmail.com"))
						.title("Arenella-ICT - Contact Request From A Recruiter")
						.topic(EmailTopic.REC_TO_REC_CONTACT_REQUEST)
					.build();
		
		this.emailServiceExternalEventListener.listenForSendEmailCommand(cExt);
		
	}

	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishCandidateUpdateEvent(CandidateUpdateEvent candidateUpdateEvent) {
		
		this.newsFeedExternalEventListener.listenForEventCandidateUpdate(candidateUpdateEvent);
		
	}
	
	
}