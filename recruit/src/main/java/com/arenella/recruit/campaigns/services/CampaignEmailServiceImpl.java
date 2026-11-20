package com.arenella.recruit.campaigns.services;

import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.arenella.recruit.campaign.dao.CampaignDao;
import com.arenella.recruit.campaign.dao.CampaignEmailEntityDao;
import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignEmail;

/**
* Services for interacting with Campaign Email's 
*/
@Service
public class CampaignEmailServiceImpl implements CampaignEmailService{

	private final CampaignEmailEntityDao 	campaignEmailDao;
	private final CampaignDao				campaignDao;
	private final CampaignAuthUtility		authUtility;
	
	/**
	* Constructor
	* @param campaignEmailDao - Repo for persisting / retrieving Campaigin Email's
	* @param authUtility	  - Utilities for determining if user can access Campaign/Role items
	*/
	public CampaignEmailServiceImpl(CampaignEmailEntityDao campaignEmailDao, CampaignDao campaignDao, CampaignAuthUtility authUtility) {
		this.campaignEmailDao 	= campaignEmailDao;
		this.campaignDao 		= campaignDao;
		this.authUtility 		= authUtility;
	}
	
	//TODO: [KP] 1. Tests for this service
	//TODO: [KP] 2. API Outbound objects
	//TODO: [KP] 3. In controller fill the outbound objects
	//TODO: [KP] 4. Events for Listing ( Add / Delete )
	//TODO: [KP] 5. Events for Emails (Add Delete)
	//TODO: [KP] 6. Command form email viewed by owner
	//TODO: [KP] 7. Display emails in FE
	
	/**
	* Refer to the CampaignEmailService interface for details 
	*/
	@Override
	public Set<CampaignEmail> fetchEmailsForCampaign(UUID campaignId, String loggedInUserId) {
		
		this.authUtility.fetchAndValidateContactForCurrentUser(loggedInUserId);
		
		Campaign campaign = this.campaignDao.fetchCampaign(campaignId).orElseThrow(() -> new IllegalArgumentException(CampaignAuthUtility.ERR_MSG_UNKNOWN_CAMPAIGN));
		
		this.authUtility.checkLoggedInUserIsParticipantForCampaignOrRole(campaign, null, loggedInUserId);
		
		return this.campaignEmailDao.fetchEmailsForCampaign(campaignId);
	}

}