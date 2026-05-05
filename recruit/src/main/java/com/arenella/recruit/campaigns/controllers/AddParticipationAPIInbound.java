package com.arenella.recruit.campaigns.controllers;

import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Class represents a command to add a new Participation to 
* either a Campaign or Campaign Role 
*/
@JsonDeserialize(builder=AddParticipationAPIInbound.AddParticipationAPIInboundBuilder.class)
public class AddParticipationAPIInbound {

	private String 			contactId;
	private UUID 			campaignId; 
	private UUID 			roleId;
	private ParticipantType type;

	/**
	* Constructor 
	* @param builder - Contains initialization values
	*/
	public AddParticipationAPIInbound(AddParticipationAPIInboundBuilder builder) {
		this.contactId 		= builder.contactId;
		this.campaignId 	= builder.campaignId;
		this.roleId 		= builder.roleId;
		this.type 			= builder.type;
	}
	
	/**
	* Returns the Id of the Contact to be added to the Campaign 
	* as a Participant
	* @return Id of the Contact
	*/
	public String getContactId() {
		return this.contactId;
	}
	
	/**
	* Returns the Id of the Campaign to add the 
	* Participation to
	* @return Id of the Campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	} 
	
	/**
	* Returns, if this is a participation at Role level the Id of the Role
	* the Participation is for
	* @return Id of the Role
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
	}
	
	/**
	* Returns the type of the Participation
	* @return Participation Type
	*/
	public ParticipantType getType() {
		return this.type;
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder
	*/
	public static AddParticipationAPIInboundBuilder builder() {
		return new AddParticipationAPIInboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class AddParticipationAPIInboundBuilder {
		
		private String 			contactId;
		private UUID 			campaignId; 
		private UUID 			roleId;
		private ParticipantType type;
		
		/**
		* Sets the id of the Contact that will be participation in the Campaign/Role
		* @param contactId - Id of the Contact
		* @return Builder
		*/
		public AddParticipationAPIInboundBuilder contactId(String contactId) {
			this.contactId = contactId;
			return this;
		}
		
		/**
		* Sets the Id of the Campaign the Participation is related to
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public AddParticipationAPIInboundBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		} 
		
		/**
		* If this is a Role level participation sets the Id of the Role
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public AddParticipationAPIInboundBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the type of the Participation the Contact has in the 
		* Campaign/Role
		* @param type - Type of the Participation
		* @return Builder
		*/
		public AddParticipationAPIInboundBuilder type(ParticipantType type) {
			this.type = type;
			return this;
		}
		
		/**
		* Returns an initialized instance 
		* @return Initialized instance
		*/
		public AddParticipationAPIInbound build() {
			return new AddParticipationAPIInbound(this);
		}
	}
	
}