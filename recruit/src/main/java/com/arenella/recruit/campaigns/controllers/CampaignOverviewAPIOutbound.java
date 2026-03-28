package com.arenella.recruit.campaigns.controllers;

import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;

/**
* Representation of Campaign containing just basic overview details 
*/
public class CampaignOverviewAPIOutbound {

	private UUID 				id;
	private String 				name;
	private String 				description;
	private CampaignLogo 		logo;

	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public CampaignOverviewAPIOutbound(CampaignOverviewAPIOutboundBuilder builder) {
		this.id 			= builder.id;
		this.name 			= builder.name;
		this.description 	= builder.description;
		this.logo 			= builder.logo;
	}
	
	/**
	* Returns the unique Id of the Campaign
	* @return id
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns the name of the Campaign
	* @return Campaign name
	*/
	public String getName() {
		return this.name;
	}
	
	/**
	* Returns a description of the Campaign
	* @return description
	*/
	public String getDescription() {
		return this.description;
	}
	
	/**
	* Returns the Campaigns logo if one has been provided
	* @return campaign logo
	*/
	public Optional<CampaignLogo> getLogo() {
		return Optional.ofNullable(this.logo);
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static CampaignOverviewAPIOutboundBuilder builder() {
		return new CampaignOverviewAPIOutboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class CampaignOverviewAPIOutboundBuilder {
		
		private UUID 				id;
		private String 				name;
		private String 				description;
		private CampaignLogo 		logo;
		
		/**
		* Populates builder with the values from the Campaign
		* @param campaign - Existing Campaign
		* @return Builder
		*/
		public CampaignOverviewAPIOutboundBuilder from(Campaign campaign) {
			
			this.id 			= campaign.getId();
			this.name 			= campaign.getName();
			this.description 	= campaign.getDescription();
			
			campaign.getLogo().ifPresent(campaignLogo -> this.logo = campaignLogo);
			
			return this;
		}
		
		/**
		* Returns an initialized instance of the Class
		* @return Initialized instance
		*/
		public CampaignOverviewAPIOutbound build() {
			return new CampaignOverviewAPIOutbound(this);
		}
		
	}

}