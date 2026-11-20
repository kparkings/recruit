package com.arenella.recruit.campaigns.services;

import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.CampaignEmail;

/**
* Defines services for interacting with Campaign Email's 
*/
public interface CampaignEmailService {

	/**
	* Retrieves Email's relating to A given Campaign. This will also include
	* the email's for the child roles within the Campaign
	* @param campaiginId 	- Id of the Campaign
	* @param loggedInUserId - Id of currently authenticated User
	* @return Email's for Campaign and its child Roles
	*/
	Set<CampaignEmail> fetchEmailsForCampaign(UUID campaiginId, String loggedInUserId);
	
}
