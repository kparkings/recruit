package com.arenella.recruit.candidates.adapters;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.adapters.events.CreditsAssignedEvent;
import com.arenella.recruit.adapters.events.CreditsUsedEvent;
import com.arenella.recruit.adapters.events.CurriculumUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.RecruiterCredit;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.services.CandidateService;
import com.arenella.recruit.newsfeed.beans.NewsFeedItem.NEWSFEED_ITEM_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Monolith implementation of the listener. In monolith setup just calls services.
* @author K parkings
*/
@Service
public class CandidateMonolithExternalEventListener implements CandidateExternalEventListener {

	@Autowired
	private CandidateService 			candidateService;
	
	@Autowired
	private CandidateDao 				canidateDao;
	
	@Autowired
	private ExternalEventPublisher		externalEventPublisher;
	
	/**
	* Refer to CandidateExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event) {
		this.candidateService.updateContact(event.getRecruiterId(), event.getEmail(), event.getFirstName(), event.getSurname());
		this.candidateService.addCreditsRecordForUser(event.getRecruiterId());
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
		if(event.getSubscriptionType() != subscription_type.CREDIT_BASED_SUBSCRIPTION) {
			this.candidateService.updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DISABLED_CREDITS);
		} else {
			this.candidateService.updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DEFAULT_CREDITS);
		}
	}

	/**
	* Refer to CandidateExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent event) {
		this.candidateService.updateCreditsForUser(event.geRecruiterId(), RecruiterCredit.DISABLED_CREDITS);
		
	}

	/**
	* Refer to CandidateExternalEventListener for details
	* [KP] Though I think this is okay, we are bouncing an event from curriculum -> candidate -> newsFeedItem. There is therefore
	* coupling but as its async ( or would be in a non Monolith implementation and NewsFeedItems can be eventual consistency
	* its probably an ok solution 
	*/
	@Override
	public void listenForCurriculumUpdatedEvent(CurriculumUpdatedEvent event) {
		
		Optional<Candidate> optCandidate = this.canidateDao.findCandidateById(Integer.valueOf(event.getCurriculumId()));
				
		if (optCandidate.isPresent()) {
			
			Candidate candidate = optCandidate.get();
			
			CandidateUpdateEvent updateEvent = CandidateUpdateEvent
					.builder()
						.itemType(NEWSFEED_ITEM_TYPE.CANDIDATE_CV_UPDATED)
						.candidateId(Integer.valueOf(candidate.getCandidateId()))
						.firstName(candidate.getFirstname())
						.surname(candidate.getSurname())
						.roleSought(candidate.getRoleSought())
					.build(); 
			
			externalEventPublisher.publishCandidateUpdateEvent(updateEvent);
			
		}
		
		
	}

}
