package com.arenella.recruit.curriculum.adapters;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidateNoLongerAvailableEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;
import com.arenella.recruit.candidates.dao.PendingCandidateDao;
import com.arenella.recruit.candidates.services.AlertTestUtil;
import com.arenella.recruit.curriculum.dao.CurriculumSkillsDao;
import com.arenella.recruit.curriculum.services.CurriculumService;
import com.arenella.recruit.curriculum.beans.RecruiterCredit;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Implementation of ExternalEventListener optimized to 
* work for when the services are physically located in 
* the same Monolith.
* @author K Parkings
*/
@Service
public class MonolithExternalEventListener implements CurriculumExternalEventListener{

	@Autowired
	private CurriculumSkillsDao 	skillsDao;
	
	@Autowired
	private PendingCandidateDao 	pendingCandidateDao;
	
	@Autowired
	private CurriculumService 		curriculumService;
	
	@Autowired
	private AlertTestUtil			alertTestUtil;
	
	/**
	* Refer to the ExternalEventListener for details 
	*/
	//TODO: [KP] Need to convert to Event instead of Set
	@Override
	public void listenForSearchedSkillsEvent(Set<String> skills) {
		extractAndPersistNewSkills(skills);
	}
	
	/**
	* Adds any new skills searched on to the DB
	* @param skills - Skills from current search
	*/
	//TODO:[KP] Move to service
	private void extractAndPersistNewSkills(Set<String> skills){
	
		Set<String> newSkills = new HashSet<>();
		
		skills.stream().map(skill -> preprocessSkill(skill)).forEach(skill -> {
			
			if(!this.skillsDao.existsById(skill)) {
				newSkills.add(skill);
			}
		});
		
		if (!newSkills.isEmpty()) {
			this.skillsDao.persistSkills(newSkills);
		}
		
	}

	/**
	* Refer to the ExternalEventListener for details 
	*/
	//TODO: [KP] Need to convert to Event instead of UUID
	@Override
	public void listenForPendingCurriculumDeletedEvent(UUID pendingCandidateId) {
		this.pendingCandidateDao.deleteById(pendingCandidateId);
	}
	
	/**
	* Refer to the ExternalEventListener for details 
	*/
	//TODO: [KP] Need to convert to Event instead of UUID
	@Override
	public void listenForCandidateCreatedEvent(CandidateCreatedEvent event) {
		this.alertTestUtil.testAgainstCandidateSearchAlerts(event);
	}
	
	/**
	* Performs pre-processing of skill to ensure the skill
	* is represented correctly in the system regardless of 
	* case and whitespace
	* @param skill - raw skill value
	* @return PreProcessed skill
	*/
	//TODO: [KP] This is duplicate logic. Should be done via shared pre-processing object
	private static String preprocessSkill(String skill) {
		
		skill = skill.toLowerCase();
		skill = skill.trim();
		
		return skill;
		
	}

	/**
	* Refer to the ExternalEventListener for details 
	*/
	@Override
	public void listenForCandidateNoLongerAvailableEvent(CandidateNoLongerAvailableEvent event) {
		//this.curriculumService.deleteCurriculum(event.getCandidateId());
	}

	/**
	* Refer to the ExternalEventListener for details 
	*/
	@Override
	public void listenForCandidteDeletedEvent(CandidateDeletedEvent candidateDeletedEvent) {
		this.curriculumService.deleteCurriculum(Long.valueOf(candidateDeletedEvent.getCandidateId()));
	}
	
	/**
	* Refer to the ListingsExternalEventListener interface for details
	*/
	@Override
	public void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent event) {
		this.curriculumService.updateCreditsForUser(event.geRecruiterId(), RecruiterCredit.DISABLED_CREDITS);
	}

	/**
	* Refer to the ListingsExternalEventListener interface for details
	*/
	@Override
	public void listenForSubscriptionAddedEvent(SubscriptionAddedEvent event) {
		
		if (event.getSubscriptionType() != subscription_type.CREDIT_BASED_SUBSCRIPTION) {
			this.curriculumService.updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DISABLED_CREDITS);
		} else {
			this.curriculumService.updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DEFAULT_CREDITS);
		}
		
	}

	/**
	* Refer to the ListingsExternalEventListener interface for details
	*/
	@Override
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event) {
		this.curriculumService.addCreditsRecordForUser(event.getRecruiterId());
	}

}
