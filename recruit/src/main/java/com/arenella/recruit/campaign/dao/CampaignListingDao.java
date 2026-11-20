package com.arenella.recruit.campaign.dao;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.CampaignListing;
import com.arenella.recruit.campaigns.entities.CampaignEntity;
import com.arenella.recruit.campaigns.entities.CampaignListingEntity;

/**
* Repository for associating Listings with Campaigns and Roles
*/
@Repository
public interface CampaignListingDao extends ListCrudRepository<CampaignListingEntity, UUID>{

	@Query("FROM CampaignListingEntity where campaignId = :campaignId and roleId is null and listingId = :listingId")
	Set<CampaignListingEntity> fetchAssociationsForCampaign(UUID campaignId, UUID listingId);
	
	@Query("FROM CampaignListingEntity where campaignId = :campaignId and roleId = :roleId and listingId = :listingId")
	Set<CampaignListingEntity> fetchAssociationsForRole(UUID campaignId, UUID roleId, UUID listingId);
	
	/**
	* Returns whether or not the association already exists
	* @param campaignId - Id of the Campaign
	* @param listingId  - Id of the Listing
	* @return Whether or not the association exists
	*/
	default boolean campaignListingAssociationExists(UUID campaignId, UUID listingId) {
		return !this.fetchAssociationsForCampaign(campaignId, listingId).isEmpty();
	}
	
	/**
	* Returns whether or not the association already exists
	* @param campaignId - Id of the Campaign
	* @param listingId  - Id of the Listing
	* @param roleId		- Id of the Role
	* @return Whether or not the association exists
	*/
	default boolean campaignListingAssociationExists(UUID campaignId, UUID roleId, UUID listingId) {
		return !this.fetchAssociationsForRole(campaignId, roleId, listingId).isEmpty();
	}
	
	/**
	* Persists an Association between a Campaign|Role and a Listing
	* @param association
	*/
	default void saveCampaignListing(CampaignListing association) {
		this.save(CampaignListingEntity.toEntity(association));
	}
	
}