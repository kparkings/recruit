package com.arenella.recruit.candidates.utils;

//import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.repos.CandidateRepository;

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
	private CandidateRepository 	candidateRepository;
	
	@Autowired
	private ElasticsearchClient 	esClient;
	
	/**
	* Triggers refresh actions for Candidates that are outdated
	*/
	@Scheduled(cron = "* */1 * * * *")
	public void performRefreshOnOutdatedAccounts() {
		try {
			//this.runAssignMissingLastAccountRefreshDates();
			//this.runSkillUpdateRefresh();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Task to periodically update a Candidate's skills
	*/
	//private void runSkillUpdateRefresh() {
		
	//	CandidateFilterOptions filters = CandidateFilterOptions.builder().lastAccountRefreshLtEq(LocalDate.now().minusWeeks(2)).build();
		
	//	try {
	//		candidateRepository.findCandidates(filters, esClient).forEach(candidate -> {
	//			System.out.println("Sending Command CmdFetchCandidateSkills " + candidate.getCandidateId());
	//		});
	//	} catch (Exception e) {
	//		e.printStackTrace();
	//	}
		
	//}
	
}
