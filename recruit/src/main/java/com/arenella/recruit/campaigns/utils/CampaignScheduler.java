package com.arenella.recruit.campaigns.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidates.services.CandidateService;

/**
* Scheduler for Campaigns
* - If 7 days after External User is created no confirmation is received their details are anonymized
* - If 7 daya after 1 years reminder is sent their details are anonymized
* - If 1 year ( - 1 week ) has passed since external candidate last gave permission for their details to be stored new email request is sent
*/
@Service
public class CampaignScheduler {

	private CandidateService candidateService;
	
	/**
	* Constructor
	* @param candidateService - Services relating to Candidates including ExternalCandidate
	*/
	public CampaignScheduler(CandidateService candidateService) {
		this.candidateService = candidateService;
	}
	
	@Scheduled(fixedRate=10000)
	public void runScheduler() {
		
		//1. Fetch cadidates ( external , deleted=false, created before today-7 days, lastCandidateDataRetentionConfirmation=null 
			//Deelete
		
		//2. Fetch cadidates ( external , deleted=false,  lastCandidateDataRetentionConfirmation < today - 1 year + 7 days
		//Send Email to ask for permission
		
		//3. Fetch cadidates ( external , deleted=false,  lastCandidateDataRetentionConfirmation < today - 1 year
			//delete
				
	}
	
}
