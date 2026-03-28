package com.arenella.recruit.campaigns.controllers;

import java.util.Optional;

import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Class represents a new Campaign. It is the representation 
* sent to the API to request the initial creation of the 
* Campaign 
*/
@JsonDeserialize(builder=NewCampaignAPIInbound.NewCampaignAPIInboundBuilder.class)
public class NewCampaignAPIInbound {

	private String 				name;
	private String 				description;
	private CampaignLogo 		logo;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains the initialization values
	*/
	public NewCampaignAPIInbound(NewCampaignAPIInboundBuilder builder) {
		this.name 			= builder.name;
		this.description 	= builder.description;
		this.logo 			= builder.logo;
	}
	
	/**
	* Returns the name of the Campaign
	* @return name of the Campaign
	*/
	public String getName() {
		return this.name;
	}
	
	/**
	* Returns the Campaign description
	* @return description
	*/
	public String getDescription() {
		return this.description;
	}
	
	/**
	* Returns the Logo of the Campaign
	* @return Logo
	*/
	public Optional<CampaignLogo> getLogo() {
		return Optional.ofNullable(this.logo);
	}

	/**
	* Returns a Builder for the class
	* @return Builder
	*/
	public static NewCampaignAPIInboundBuilder builder() {
		return new NewCampaignAPIInboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class NewCampaignAPIInboundBuilder {
		
		private String 				name;
		private String 				description;
		private CampaignLogo 		logo;
		
		/**
		* Sets the name of the Campaign
		* @param name - Name of the Campaign
		* @return Builder
		*/
		public NewCampaignAPIInboundBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		* Sets the Campaign description
		* @param description - Description of the Campaign 
		* @return Builder
		*/
		public NewCampaignAPIInboundBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Sets the Campaign logo
		* @param logo - A logo for the Campaign
		* @return Builder
		*/
		public NewCampaignAPIInboundBuilder logo(CampaignLogo logo) {
			this.logo = logo;
			return this;
		}
		
		/**
		* Returns a new initialized instance of the class
		* @return Initialized instance
		*/
		public NewCampaignAPIInbound build() {
			return new NewCampaignAPIInbound(this);
		}
		
	}
	
}