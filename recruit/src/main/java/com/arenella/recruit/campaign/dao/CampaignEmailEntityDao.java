package com.arenella.recruit.campaign.dao;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.CampaignEmail;
import com.arenella.recruit.campaigns.entities.CampaignEmailEntity;

/**
* Repositories for accessing Emails associated with Campaigns and Roles 
*/
@Repository
public interface CampaignEmailEntityDao extends ListCrudRepository<CampaignEmailEntity, UUID>{

	@Query("FROM CampaignEmailEntity where campaignId = :campaignId")
	Set<CampaignEmailEntity> fetchEmailsEntitiesForCampaign(UUID campaignId);
	
	/**
	* Persists the Email
	* @param email - Email to persist
	*/
	default void saveEmail(CampaignEmail email) {
		this.save(CampaignEmailEntity.toEntity(email));
	}
	
	/**
	* Returns the email's associated with a specific Campaign
	* @param roleId - Id of the Campaign
	* @return Email's
	*/
	default Set<CampaignEmail> fetchEmailsForCampaign(UUID campaignId) {
		return this.fetchEmailsEntitiesForCampaign(campaignId).stream().map(CampaignEmailEntity::fromEntity).collect(Collectors.toSet());
	}

}
