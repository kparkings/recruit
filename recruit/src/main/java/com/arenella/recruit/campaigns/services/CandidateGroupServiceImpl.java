package com.arenella.recruit.campaigns.services;

import java.util.Optional;
import java.util.Set;
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
	* DO NOT EXPOSE VIA API 
	* Constructor
	* @param candidateGroupDao  - Repository for CandidateGroups
	* @param campaignService	- Services for working with Campaigns
	*/
	public CandidateGroupServiceImpl(CandidateGroupEntityDao candidateGroupDao, CampaignService campaignService) {
		this.candidateGroupDao 	= candidateGroupDao;
		this.campaignService 	= campaignService;
	}
	
	/**
	* DO NOT EXPOSE VIA API
	* Refer to the CandidateGroupService interface for details 
	*/
	@Override
	public Set<CandidateGroup> fetchCandidateGroupsForCampaign(UUID campaignId) {
		return this.candidateGroupDao.findCandidateGroupsForCampaign(campaignId);
	}

	/**
	* DO NOT EXPOSE VIA API 
	* Refer to the CandidateGroupService interface for details 
	*/
	@Override
	public void deleteCandidateGroup(UUID id) {
		this.candidateGroupDao.deleteById(id);
	}

	/**
	* DO NOT EXPOSE VIA API 
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
 	
		Optional<CandidateGroup> candidateGroup = this.candidateGroupDao.fetchCandidateGroup(group.getCampaignId(), group.getRoleId(), group.getCandidateId());
		
		candidateGroup.ifPresent(cg -> {
			this.securityCheckHasAccessToCampaignOrRole(cg, currentUser);			
			this.candidateGroupDao.saveGrouping(CandidateGroup.builder().from(cg).group(group.getGroup()).build());
		});
		
	}
	
	/**
	* 
	* @param CandidateGroup - Group to perform security check on
	* @param currentUser 	- If request originated from an end user the id of the currently authenticated User
	*/
	private void securityCheckHasAccessToCampaignOrRole(CandidateGroup group, String currentUser) {
		
		if (Optional.ofNullable(currentUser).isEmpty()) {
			return;
		}
		
		Campaign 		campaign 	= this.campaignService.fetchCampaignById(group.getCampaignId(), currentUser);
		
		boolean authorizedAtCampaignLevel = campaign.getParticipations().stream().filter(p -> p.getContactId().equals(currentUser) && (p.getType().equals(ParticipantType.ADMIN) ||p.getType().equals(ParticipantType.EDIT)) ).findAny().isPresent();	
		boolean authorizedAtRoleLevel = false;
		
		if (group.getRoleId().isPresent()) {
			
			Role role = campaign.getRoles().stream().filter(r -> r.getId().equals(group.getRoleId().get())).findAny().orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_ROLE));
			
			authorizedAtRoleLevel = role.getParticipations().stream().filter(p -> p.getContactId().equals(currentUser) && (p.getType().equals(ParticipantType.ADMIN) ||p.getType().equals(ParticipantType.EDIT)) ).findAny().isPresent();
			
		}
		
		if (!authorizedAtCampaignLevel && !authorizedAtRoleLevel) {
			throw new  IllegalArgumentException(ERR_MSG_UNKNOWN_ROLE);
		}
		
	}

}