package com.arenella.recruit.campaign.dao;

import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.CampaignListing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Entry associates a Listing with a Campaign. 
* This is used to track which Listing(s) are related to the 
* Campaign or Role 
*/
@Entity
@Table(schema="campaigns", name="campaign_listings")
public class CampaignListingEntity {

	@Id
	@Column(name="id")
	private UUID id;
	
	@Column(name="listing_id")
	private UUID listingId;
	
	@Column(name="campaign_id")
	private UUID campaignId;
	
	@Column(name="role_id")
	private UUID roleId;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public CampaignListingEntity(CampaignListingEntityBuilder builder) {
		this.id = builder.id;
		this.listingId = builder.listingId;
		this.campaignId = builder.campaignId;
		this.roleId = builder.roleId;
	}
	
	/**
	* Returns the id of the association between a Listing 
	* and a Campaign or Role
	* @return Id of the association
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns the Id of the Listing
	* @return Id of the Listing
	*/
	public UUID getListingId() {
		return this.listingId;
	}
	
	/**
	* Returns the Id of the Campaign
	* @return Id of the Campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	}
	
	/**
	* If Role level association returns the Id
	* of the Role
	* @return Id of the Role
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
	}
	
	/**
	* Returns a Builder for the class
	* @return BUilder
	*/
	public static CampaignListingEntityBuilder builder() {
		return new CampaignListingEntityBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class CampaignListingEntityBuilder {
		
		private UUID id;
		private UUID listingId;
		private UUID campaignId;
		private UUID roleId;
		
		/**
		* Sets the Unique id of the association
		* @param id - Id
		* @return Builder
		*/
		public CampaignListingEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the Id of the Listing to associate with the Campaign/Role
		* @param listingId - Id of the Listing
		* @return Builder
		*/
		public CampaignListingEntityBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the Id of the Campaign associated with the Listing
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public CampaignListingEntityBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
	
		/**
		* Sets the Id of the Role associated with the Listing if the association
		* is at Role level
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public CampaignListingEntityBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Returns an initialized isntance 
		* @return initialized instance
		*/
		public CampaignListingEntity build() {
			return new CampaignListingEntity(this);
		}
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - to be converted
	* @return converted
	*/
	public static CampaignListing fromEntity(CampaignListingEntity entity) {
		return CampaignListing
				.builder()
					.id(entity.getId())
					.listingId(entity.getListingId())
					.campaignId(entity.getCampaignId())
					.roleId(entity.getRoleId().orElse(null))
				.build();
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param entity - to be converted
	* @return converted
	*/
	public static CampaignListingEntity toEntity(CampaignListing listing) {
		return CampaignListingEntity
				.builder()
					.id(listing.getId())
					.listingId(listing.getListingId())
					.campaignId(listing.getCampaignId())
					.roleId(listing.getRoleId().orElse(null))
				.build();
	}
	
}