package com.arenella.recruit.campaigns.services;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.CandidateGroup;

/**
* Defines Services for interacting with CandidateGroup's 
*/
public interface CandidateGroupService {

	/**
	* Fetches all groupings for a given Campaign
	* @param campaignId - Id of the 
	* @return CandidateGroups for the Campaign
	*/
	public Set<CandidateGroup> fetchCandidateGroupsForCampaign(UUID campaignId);
	
	/**
	* Deletes a grouping with the given Id
	* @param id - Id of the group to delete
	*/
	public void deleteCandidateGroup(UUID id);
	
	/**
	* Returns a specific group based upon its Id
	* @param id - Id of the Grouping
	* @return grouping id found
	*/
	public Optional<CandidateGroup> findGroupById(UUID id);
	
	/**
	* Persists the CandidateGroup
	* @param group 			- To be persisted
	* @param currentUser 	- id of currently authenticated user
	*/
	public void saveGandidateGroup(CandidateGroup group, String currentUser);
	
}
