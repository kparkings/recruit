package com.arenella.recruit.campaign.dao;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.CandidateGroup;
import com.arenella.recruit.campaigns.entities.CandidateGroupEntity;

/**
* Repository for CandidateGroup Entities
*/
@Repository
public interface CandidateGroupEntityDao extends ListCrudRepository<CandidateGroupEntity, UUID>{

	@Query("from CandidateGroupEntity where campaignId = :campaignId")
	Set<CandidateGroupEntity> fetchGroupEntituesForCampaigin(UUID campaignId);
	
	/**
	* Returns the Grouping matching the Id
	* @param id - Id of the Grouping
	* @return Grouping if present
	*/
	default Optional<CandidateGroup> fetchGroupingById(UUID id) {
		return this.findById(id).stream().map(CandidateGroupEntity::fromEntity).findFirst();
	}
	
	/**
	* Persists the Group
	* @param group - Group to persist
	*/
	default void saveGrouping(CandidateGroup group) {
		this.save(CandidateGroupEntity.toEntity(group));
	}
	
	/**
	* Returns Groupings for a given Campaigin
	* @param campaignId - Id of the Campaign
	* @return Groupings for the campaign
	*/
	default Set<CandidateGroup> findCandidateGroupsForCampaign(UUID campaignId) {
		return this.fetchGroupEntituesForCampaigin(campaignId).stream().map(CandidateGroupEntity::fromEntity).collect(Collectors.toSet());
	}
	
}