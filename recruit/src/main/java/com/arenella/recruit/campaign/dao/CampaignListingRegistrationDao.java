package com.arenella.recruit.campaign.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.CampaignListingRegistration;
import com.arenella.recruit.campaigns.entities.CampaignListingRegistrationEntity;

/**
* Repository for interacting with CampaignListingRegistrations 
*/
@Repository
public interface CampaignListingRegistrationDao extends ListCrudRepository<CampaignListingRegistrationEntity, UUID>{

	/**
	* Persists a Registration
	* @param registration - New Listing to be registered
	*/
	default void saveCampaignListingRegistration(CampaignListingRegistrationEntity registration) {
		this.save(CampaignListingRegistrationEntity
				.builder()
					.listingId(registration.getListingId())
					.listingOwnerId(registration.getListingOwnerId())
				.build());
	}
	
	/**
	* If found returns the Listing associated with the given Id
	*/
	default Optional<CampaignListingRegistration>findRegistrationById(UUID listingId) {
		return this.findById(listingId).stream().map(entity -> CampaignListingRegistrationEntity.fromEntity(entity)).findAny();
	}
	
}
