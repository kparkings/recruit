package com.arenella.recruit.campaigns.beans;

import java.util.Optional;
import java.util.UUID;

/**
* Class represents and association between a Listing
* and a Campaign or Role 
*/
public class CampaignListing {

	private UUID id;
	private UUID listingId;
	private UUID campaignId;
	private UUID roleId;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public CampaignListing(CampaignListingBuilder builder) {
		this.id 		= builder.id;
		this.listingId 	= builder.listingId;
		this.campaignId = builder.campaignId;
		this.roleId 	= builder.roleId;
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
	public static CampaignListingBuilder builder() {
		return new CampaignListingBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class CampaignListingBuilder {
		
		private UUID id;
		private UUID listingId;
		private UUID campaignId;
		private UUID roleId;
		
		/**
		* Sets the Unique id of the association
		* @param id - Id
		* @return Builder
		*/
		public CampaignListingBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the Id of the Listing to associate with the Campaign/Role
		* @param listingId - Id of the Listing
		* @return Builder
		*/
		public CampaignListingBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the Id of the Campaign associated with the Listing
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public CampaignListingBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
	
		/**
		* Sets the Id of the Role associated with the Listing if the association
		* is at Role level
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public CampaignListingBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Returns an initialized isntance 
		* @return initialized instance
		*/
		public CampaignListing build() {
			return new CampaignListing(this);
		}
	}
	
}