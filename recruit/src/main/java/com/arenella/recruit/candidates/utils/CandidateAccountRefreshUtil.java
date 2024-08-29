package com.arenella.recruit.candidates.utils;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arenella.recruit.adapters.actions.RequestSkillsForCurriculumCommand;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.repos.CandidateRepository;
import com.arenella.recruit.curriculum.adapters.CurriculumExternalEventListener;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

/**
* Scheduler to periodically perform tasks to update the a Candidates account. 
* 
* Tasks include
* 
* 1. Update Skill's against current known Skill's in the system
* 2. Send email to ask for availability confirmation <NOT YET IMPLEMENTED>
* @author K Parkings
*/
@Component
@EnableScheduling
public class CandidateAccountRefreshUtil {

	//TODO: [KP] Setting this out for the time being because I want the new candidates to already start getting their lastAccountRefresh values assigned
	
	@Autowired
	private CandidateRepository 				candidateRepository;
	
	@Autowired
	private ElasticsearchClient 				esClient;
	
	@Autowired
	private CurriculumExternalEventListener 	eventPublisher;
	
	/**
	* Triggers refresh actions for Candidates that are outdated
	*/
	@Scheduled(cron = "0 */10 * ? * *")
	public void performRefreshOnOutdatedAccounts() {
		try {
			this.runSkillUpdateRefresh();	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Task to periodically update a Candidate's skills
	*/
	private void runSkillUpdateRefresh() {
		
		//TODO: 1 - Admin available screen to change from 2 weeks to 2 weeks and 3 days to give candidates chance to react to email
		
		CandidateFilterOptions filters = CandidateFilterOptions.builder().lastAccountRefreshLtEq(LocalDate.now().minusWeeks(2)).build();
		
		try {
			candidateRepository.findCandidates(filters, esClient, 10).forEach(candidate -> {
				candidate.setLastAccountRefresh(LocalDate.now().plusDays(1));
				this.candidateRepository.saveCandidate(candidate);
				eventPublisher.listenForRequestSkillsForCurriculumCommand(new RequestSkillsForCurriculumCommand(Long.valueOf(candidate.getCandidateId())));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
