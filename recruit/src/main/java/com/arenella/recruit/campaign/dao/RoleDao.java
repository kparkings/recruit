package com.arenella.recruit.campaign.dao;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
	
	@Query("SELECT r FROM RoleEntity r JOIN r.candidates c where c.id = :candidateId")
	Set<RoleEntity> fetchRoleEntitiesForCandidate(String candidateId);
	
	/**
	* Saves a Role
	* @param role - Role to be persisted
	*/
	default void saveRole(Role role, UUID campaignId) {
		this.save(RoleEntity.toEntity(role, campaignId));
	}
	
	/**
	* If present returns the Role for the corresponding Id
	* @param roleId - Id of Role to fetch
	* @return if present the associated Role
	*/
	default Optional<Role> fetchRoleById(UUID roleId) {
		return this.findById(roleId).map(RoleEntity::fromEntity);
	}

	/**
	* Returns Roles in which the Candidates one of the associated Candidates
	* @param candidateId - Id of the Candidate
	* @return Associated Roles
	*/
	default Set<Role> fetchRolesForCandidate(String candidateId) {
		return this.fetchRoleEntitiesForCandidate(candidateId).stream().map(RoleEntity::fromEntity).collect(Collectors.toSet());
	}
	

}
