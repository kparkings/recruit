package com.arenella.recruit.candidates.adapters;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.adapters.events.CreditsAssignedEvent;
import com.arenella.recruit.adapters.events.CreditsUsedEvent;
import com.arenella.recruit.adapters.events.CurriculumSkillsExtractionEvent;
import com.arenella.recruit.adapters.events.CurriculumUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterDeletedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.RecruiterCredit;
import com.arenella.recruit.candidates.beans.SkillUpdateStat;
import com.arenella.recruit.candidates.dao.SkillUpdateStatDao;
import com.arenella.recruit.candidates.repos.CandidateRepository;
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
	private ExternalEventPublisher		externalEventPublisher;
	
	@Autowired
	private CandidateRepository 		canidateRepo;
	
	@Autowired
	private SkillUpdateStatDao			SkillUpdateStatDao;
	
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
			this.candidateService.updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DISABLED_CREDITS, Optional.of(this.isPaidSubscription(event)));
		} else {
			this.candidateService.updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DEFAULT_CREDITS, Optional.of(false));
		}
	}

	/**
	* Returns whether the subscription is a paid subscription
	* @param event - Contains infor about the subscription
	* @return whether the subscription is a paid subscription
	*/
	private boolean isPaidSubscription(SubscriptionAddedEvent event) {
		
		return switch(event.getSubscriptionType()) {
			case ONE_MONTH_SUBSCRIPTION, 
				 THREE_MONTHS_SUBSCRIPTION, 
				 SIX_MONTHS_SUBSCRIPTION, 
				 YEAR_SUBSCRIPTION -> true;
			default -> false;
		};
		
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
	*/
	@Override
	public void listenForCurriculumSkillsExtractionEvent(CurriculumSkillsExtractionEvent event) {
		
		Optional<Candidate> candidateOpt = this.canidateRepo.findCandidateById(event.getCurriculumId());
		
		if (candidateOpt.isEmpty()) {
			return;
		}
		
		long originalSkillCount = candidateOpt.get().getSkills().size();
		
		candidateOpt.get().getSkills().addAll(event.getSkills());
		
		long updatedSkillCount = candidateOpt.get().getSkills().size();
		
		this.canidateRepo.saveCandidate(candidateOpt.get());
		this.SkillUpdateStatDao.saveSkillUpdateStat(new SkillUpdateStat(event.getCurriculumId(), LocalDate.now(), updatedSkillCount - originalSkillCount));
		
	}

	/**
	* Refer to CandidateExternalEventListener for details
	* [KP] Though I think this is okay, we are bouncing an event from curriculum -> candidate -> newsFeedItem. There is therefore
	* coupling but as its async ( or would be in a non Monolith implementation and NewsFeedItems can be eventual consistency
	* its probably an ok solution 
	*/
	@Override
	public void listenForCurriculumUpdatedEvent(CurriculumUpdatedEvent event) {
		
		Optional<Candidate> optCandidate = this.canidateRepo.findCandidateById(Integer.valueOf(event.getCurriculumId()));
				
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

	/**
	* Refer to CandidateExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterAccountDeletedEvent(RecruiterDeletedEvent recruiterDeletedEvent) {
		this.candidateService.deleteCandidatesForOwnedByRecruiter(recruiterDeletedEvent.getRecruiterId());
		this.candidateService.deleteSavedCandidatesForRecruiter(recruiterDeletedEvent.getRecruiterId());	
		this.candidateService.deleteCreditsForRecruiter(recruiterDeletedEvent.getRecruiterId());
		this.candidateService.deleteContactForRecruiter(recruiterDeletedEvent.getRecruiterId());
		this.candidateService.deleteSavedCandidateSearchesForUser(recruiterDeletedEvent.getRecruiterId());
	}

}