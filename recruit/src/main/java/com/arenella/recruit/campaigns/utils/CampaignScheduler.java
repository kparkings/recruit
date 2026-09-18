package com.arenella.recruit.campaigns.utils;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.arenella.recruit.campaign.dao.CandidateEntityDao;
import com.arenella.recruit.campaigns.services.CampaignService;

/**
* Scheduler for Campaigns
* - If 7 days after External User is created no confirmation is received their details are anonymized
* - If 7 daya after 1 years reminder is sent their details are anonymized
* - If 1 year ( - 1 week ) has passed since external candidate last gave permission for their details to be stored new email request is sent
*/
@Service
public class CampaignScheduler {

	private CampaignService 				campaignService;
	
	/**
	* Constructor
	* @param candidateService - Services relating to Candidates including ExternalCandidate
	*/
	public CampaignScheduler(CampaignService campaignService, CandidateEntityDao candidateEntityDao) {
		this.campaignService 	= campaignService;
	}
	
	@Scheduled(fixedRate=10000)
	public void runScheduler() {
		
		final LocalDateTime cuttoffNoAuthroisationRecieved = LocalDateTime.now().minusDays(7);
		final LocalDateTime cuttoffAuthorizationEmailSent = LocalDateTime.now().minusDays(7);
		final LocalDateTime cuttoffForSendEmailLastAuthorizationReceived = LocalDateTime.now().minusDays(365);
		final LocalDateTime cuttoffForDeleteCandidateNoAuthorizationReceived = LocalDateTime.now().minusDays(372);
		
		campaignService.fetchExternalCandidatesThatMissedAuthorisationCuttoff(cuttoffNoAuthroisationRecieved).forEach(extCandidate -> {
			this.campaignService.rejectExternalCandidateConnectionRequest(UUID.fromString(extCandidate.getId()));
		});
		
		campaignService.fetchExternalCandidatesThatRequireAnnualRenewalAuthorizationEmail(cuttoffAuthorizationEmailSent, cuttoffForSendEmailLastAuthorizationReceived).forEach(extCandidate -> {
			this.campaignService.sendExternalCandiateDataRenentionRenewalMessageSendEmailCommand(extCandidate, extCandidate.getCampaignId().orElse(null), extCandidate.getRoleId().orElse(null));
		});
		
		campaignService.fetchExternalCandidatesThatMissedAuthorisationAnnualRenewalCuttoff(cuttoffAuthorizationEmailSent, cuttoffForDeleteCandidateNoAuthorizationReceived).forEach(extCandidate -> {
			this.campaignService.rejectExternalCandidateConnectionRequest(UUID.fromString(extCandidate.getId()));
		});
				
	}
	
}
