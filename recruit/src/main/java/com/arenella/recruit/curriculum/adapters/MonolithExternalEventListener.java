package com.arenella.recruit.curriculum.adapters;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CandidateNoLongerAvailableEvent;
import com.arenella.recruit.candidates.dao.PendingCandidateDao;
import com.arenella.recruit.curriculum.dao.SkillsDao;
import com.arenella.recruit.curriculum.entity.SkillEntity;
import com.arenella.recruit.curriculum.services.CurriculumService;

/**
* Implementation of ExternalEventListener optimised to 
* work for when the services are physically located in 
* the same Monolith.
* @author K Parkings
*/
@Service
public class MonolithExternalEventListener implements ExternalEventListener{

	@Autowired
	private SkillsDao 			skillsDao;
	
	@Autowired
	private PendingCandidateDao pendingCandidateDao;
	
	@Autowired
	private CurriculumService 	curriculumService;
	
	/**
	* Refer to the ExternalEventListener for details 
	*/
	//TODO: [KP] Need to convert to Event instead of Set
	@Override
	public void listenForSearchedSkillsEvent(Set<String> skills) {
		skillsDao.saveAll(skills.stream().map(skill -> new SkillEntity(preprocessSkill(skill))).collect(Collectors.toSet()));
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
	* Performs pre-processing of skill to ensure the skill
	* is represented correctly in the system reardless of 
	* case and whitespace
	* @param skill - raw skill value
	* @return PreProcessed skill
	*/
	//TODO: [KP] This is duplicate logic. Should be done via shared preprocessing object
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
		this.curriculumService.deleteCurriculum(event.getCandidateId());
	}

}
