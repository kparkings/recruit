package com.arenella.recruit.campaign.dao;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.entities.CampaignEntity;

/**
* Repository for Campaign's 
*/
public interface CampaignDao extends ListCrudRepository<CampaignEntity, UUID>{

	@Query("FROM CampaignEntity where :participantId member of participations")
	Set<CampaignEntity> fetchCampaignsWhereUserIsParticipant(String userId);
	
	/**
	* Returns Campaigns in which the User is a Participant. Only the basic information 
	* is loaded.
	* @param userId - Id of the User to fetch Campaigns for
	* @return Campaigns in which the user is a Participant
	*/
	default Set<Campaign> fetchCampaignsForUser(String userId) {
		return this.fetchCampaignsWhereUserIsParticipant(userId).stream().map(c -> CampaignEntity.fromEntityLazy(c)).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Returns a Campaign based upon its Id
	* @param campaignId - Id of the Campaign to retrieve
	* @return Campaign if present
	*/
	default Optional<Campaign> fetchCampaign(UUID campaignId) {
		return this.findById(campaignId).map(CampaignEntity::fromEntityLazy);
	}

	/**
	* Saves a Campaign
	* @param campaign - Campaign to be persisted
	*/
	default void saveCampaign(Campaign campaign) {
		this.save(CampaignEntity.toEntity(campaign));
	}

}
