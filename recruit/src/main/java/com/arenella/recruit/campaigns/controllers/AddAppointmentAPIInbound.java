package com.arenella.recruit.campaigns.controllers;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Command to add a new Appointment to a Campaign 
*/
@JsonDeserialize(builder=AddAppointmentAPIInbound.AddAppointmentAPIInboundBuilder.class)
public class AddAppointmentAPIInbound {

	private UUID 			campaignId;
	private UUID			roleId;
	private String 			name;
	private String 			description;
	private String 			videoLink;
	private String 			phoneNumber;
	private ZonedDateTime 	when;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public AddAppointmentAPIInbound(AddAppointmentAPIInboundBuilder builder) {
		this.campaignId 	= builder.campaignId;
		this.roleId 		= builder.roleId;
		this.name 			= builder.name;
		this.description 	= builder.description;
		this.videoLink 		= builder.videoLink;
		this.phoneNumber 	= builder.phoneNumber;
		this.when 			= builder.when;
	}
	
	/**
	* Returns the Id of the Campaign the Appointment is associated with
	* @return Id of the Campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	}
	
	/**
	* If this is a Role level Appointment returns the id of the Role
	* @return Id of the Role
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
	}
	
	/**
	* Returns the name of the Appointment
	* @return Appointment name
	*/
	public String getName(){
		return this.name;
	}
	
	/**
	* Returns a description of the Appointment
	* @return description
	*/
	public String getDescription(){
		return this.description;
	}
	
	/**
	* Returns the link to for example a Teams or GoogleMeet video session
	* @return link to online video meeting
	*/
	public Optional<String> getVideoLink(){
		return Optional.ofNullable(this.videoLink);
	}
	
	/**
	* Returns the phone number to call for the appointment
	* @return Phone number
	*/
	public Optional<String> getPhoneNumber(){
		return Optional.ofNullable(this.phoneNumber);
	}
	
	/**
	* Returns the date/time that the appointment is due to 
	* take place
	* @return when appointment id due to take place
	*/
	public ZonedDateTime getWhen() {
		return this.when;
	}
	
	/**
	* Returns a Builder for the class
	* @return Builder
	*/
	public static AddAppointmentAPIInboundBuilder builder() {
		return new AddAppointmentAPIInboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class AddAppointmentAPIInboundBuilder {
		
		private UUID 			campaignId;
		private UUID			roleId;
		private String 			name;
		private String 			description;
		private String 			videoLink;
		private String 			phoneNumber;
		private ZonedDateTime 	when;
		
		/**
		* Sets the id of the Campaign the Appointment is associated with 
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public AddAppointmentAPIInboundBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}

		/**
		* If Appointment is at Role level sets the Id of the Role
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public AddAppointmentAPIInboundBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the name of the Appointment
		* @param name - Name of appointment
		* @return Builder
		*/
		public AddAppointmentAPIInboundBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		* Sets a description of what the appointment is for
		* @param description - description of the appointment
		* @return Builder
		*/
		public AddAppointmentAPIInboundBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Sets the link to the video meeting
 		* @param videoLink - URL to video meeting
		* @return Builder
		*/
		public AddAppointmentAPIInboundBuilder videoLink(String videoLink) {
			this.videoLink = videoLink;
			return this;
		}
		
		/**
		* Sets the phone number to call for the appointment 
		* @param phoneNumber - Phone number
		* @return Builder
		*/
		public AddAppointmentAPIInboundBuilder phoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}
		
		/**
		* Sets when the Appointment is due to take place
		* @param when - When the Appointment is scheduled
		* @return Builder
		*/
		public AddAppointmentAPIInboundBuilder when(ZonedDateTime when) { 
			this.when = when;
			return this;
		}
		
		/**
		* Returns an initialized Appointment
		* @return initialized appointment
		*/
		public AddAppointmentAPIInbound build() {
			return new AddAppointmentAPIInbound(this);
		}
		
	}
	
}