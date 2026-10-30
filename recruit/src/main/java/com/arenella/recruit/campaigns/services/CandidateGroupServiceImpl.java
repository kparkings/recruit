package com.arenella.recruit.campaigns.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.arenella.recruit.campaign.dao.CandidateGroupEntityDao;
import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CandidateGroup;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;
import com.arenella.recruit.campaigns.beans.Role;

/**
* Implementation of the CandidateGroupService interface 
*/
@Service
public class CandidateGroupServiceImpl implements CandidateGroupService{

	public static final String ERR_MSG_USER_NOT_PARTICIPANT 				= "You are not a participant in the Campaign.";
	public static final String ERR_MSG_UNKNOWN_ROLE							= "Unknown Role";
	public static final String ERR_MSG_UNKNOWN_GROUP						= "Unknown Group";
	
	private CandidateGroupEntityDao candidateGroupDao;
	private CampaignService 		campaignService;
	
	/**
	* Constructor
	* @param candidateGroupDao  - Repository for CandidateGroups
	* @param campaignService	- Services for working with Campaigns
	*/
	public CandidateGroupServiceImpl(CandidateGroupEntityDao candidateGroupDao, CampaignService campaignService) {
		this.candidateGroupDao 	= candidateGroupDao;
		this.campaignService 	= campaignService;
	}
	
	/**
	* Refer to the CandidateGroupService interface for details 
	*/
	@Override
	public void fetchCandidateGroupsForCampaign(UUID campaignId) {
		this.candidateGroupDao.findCandidateGroupsForCampaign(campaignId);
	}

	/**
	* Refer to the CandidateGroupService interface for details 
	*/
	@Override
	public void deleteCandidateGroup(UUID id) {
		this.candidateGroupDao.deleteById(id);
	}

	/**
	* Refer to the CandidateGroupService interface for details 
	*/
	@Override
	public Optional<CandidateGroup> findGroupById(UUID id) {
		return this.candidateGroupDao.fetchGroupingById(id);
	}

	/**
	* Refer to the CandidateGroupService interface for details 
	*/
	@Override
	public void saveGandidateGroup(CandidateGroup group, String currentUser) {
 	
		this.securityCheckHasAccessToCampaignOrRole(group.getId(), currentUser);
		
		this.candidateGroupDao.saveGrouping(group);
		
	}
	
	/**
	* 
	* @param groupId - Id of the Group to perform security check on
	* @param currentUser - If request originated from an end user the id of the currently authenticated User
	*/
	private void securityCheckHasAccessToCampaignOrRole(UUID groupId, String currentUser) {
		
		if (Optional.ofNullable(currentUser).isEmpty()) {
			return;
		}
		
		CandidateGroup 	group 		= this.candidateGroupDao.fetchGroupingById(groupId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_GROUP));
		Campaign 		campaign 	= this.campaignService.fetchCampaignById(group.getCampaignId(), currentUser);
		
		if (group.getRoleId().isEmpty()) {
			campaign.getParticipations().stream().filter(p -> p.getContactId().equals(currentUser) && (p.getType().equals(ParticipantType.ADMIN) ||p.getType().equals(ParticipantType.EDIT)) ).findAny().orElseThrow(() -> new IllegalArgumentException(ERR_MSG_USER_NOT_PARTICIPANT));	
		}
		
		if (group.getRoleId().isPresent()) {
			
			Role role = campaign.getRoles().stream().filter(r -> r.getId().equals(group.getRoleId().get())).findAny().orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_ROLE));
			
			role.getParticipations().stream().filter(p -> p.getContactId().equals(currentUser) && (p.getType().equals(ParticipantType.ADMIN) ||p.getType().equals(ParticipantType.EDIT)) ).findAny().orElseThrow(() -> new IllegalArgumentException(ERR_MSG_USER_NOT_PARTICIPANT));
			
		}
		
	}

}