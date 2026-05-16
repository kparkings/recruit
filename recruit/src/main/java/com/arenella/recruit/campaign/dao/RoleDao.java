package com.arenella.recruit.campaign.dao;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.Role;
import com.arenella.recruit.campaigns.entities.CampaignEntity;
import com.arenella.recruit.campaigns.entities.RoleEntity;

/**
* Repository for Roles 
*/
@Repository
public interface RoleDao extends ListCrudRepository<RoleEntity, UUID>{

	@Query("SELECT c FROM CampaignEntity c JOIN c.participations p where p.contactId = :userId")
	Set<CampaignEntity> fetchCampaignsWhereUserIsParticipant(String userId);
	
	/**
	* Saves a Role
	* @param role - Role to be persisted
	*/
	default void saveRole(Role role, UUID campaignId) {
		this.save(RoleEntity.toEntity(role, campaignId));
	}

}
