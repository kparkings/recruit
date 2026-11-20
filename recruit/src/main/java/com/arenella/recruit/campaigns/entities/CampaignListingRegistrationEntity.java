package com.arenella.recruit.campaigns.entities;

import java.util.UUID;

import com.arenella.recruit.campaigns.beans.CampaignListingRegistration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Represents a Listing that exists in the System. This information is 
* required so that we can check that a User trying to associate a 
* Listing with a Campaign is the owner of that Listing. 
*/
@Entity
@Table(schema="emails", name="listing_registrations")
public class CampaignListingRegistrationEntity {

	@Id
	@Column(name="listing_id")
	private UUID listingId;
	
	@Column(name="listing_owner_id")
	private String listingOwnerId;
	
	/**
	* Default constructor 
	*/
	public CampaignListingRegistrationEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder 
	* @param builder - Contains the initialization values
	*/
	public CampaignListingRegistrationEntity(CampaignListingRegistrationEntityBuilder builder) {
		this.listingId 		= builder.listingId;
		this.listingOwnerId = builder.listingOwnerId;
	}
	
	/**
	* Returns the Id of the Listing
	* @return Id of the Listing
	*/
	public UUID getListingId() {
		return this.listingId;
	}
	
	/**
	* Return the Id of the User who is the owner of the Listing
	* @return Id of the Listings owner
	*/
	public String getListingOwnerId() {
		return this.listingOwnerId;
	}
	
	/**
	* Builder for the class 
	* @return Builder
	*/
	public static CampaignListingRegistrationEntityBuilder builder() {
		return new CampaignListingRegistrationEntityBuilder();
	}
	
	/**
	* Builder for the class
	*/
	public static class CampaignListingRegistrationEntityBuilder {
	
		private UUID 	listingId;
		private String 	listingOwnerId;
		
		/**
		* Sets the Id of a Listing
		* @param listingId - Unique Id of a Listing
		* @return Builder
		*/
		public CampaignListingRegistrationEntityBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the Id of the User who owns the Listing
		* @param listingOwnerId - Unique Id of the Listing
		* @return Builder
		*/
		public CampaignListingRegistrationEntityBuilder listingOwnerId(String listingOwnerId) {
			this.listingOwnerId = listingOwnerId;
			return this;
		}
		
		
		/**
		* Returns an initialized instance of the class
		* @return instance
		*/
		public CampaignListingRegistrationEntity build() {
			return new CampaignListingRegistrationEntity(this);
		}
		
		
	}

	/**
	* Converts from Entity to Domain representation
	* @param registration - To be converted
	* @return converted
	*/
	public static CampaignListingRegistration fromEntity(CampaignListingRegistrationEntity registration) {
		return new CampaignListingRegistration(registration.getListingId(), registration.getListingOwnerId());
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param registration - To be converted
	* @return converted
	*/
	public static CampaignListingRegistrationEntity toEntity(CampaignListingRegistration registration) {
		return CampaignListingRegistrationEntity
				.builder()
					.listingId(registration.listingId())
					.listingOwnerId(registration.listingOwnerId())
				.build();
	}
	
}