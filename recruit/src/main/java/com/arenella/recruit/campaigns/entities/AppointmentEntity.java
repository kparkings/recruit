package com.arenella.recruit.campaigns.entities;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
* Entity representation of a Campaign appointment 
*/
@Entity
@Table(schema="campaigns", name="appointments")
public class AppointmentEntity {

	private UUID			appointmentId;
	private UUID			campaiginId;
	private UUID			roleId;
	private String 			name;
	private String 			description;
	private String 			videoLink;
	private String 			phoneNumber;
	private ZonedDateTime 	when;
	
	/**
	* Default constructir
	*/
	public AppointmentEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public AppointmentEntity(AppointmentEntityBuilder builder) {
		this.appointmentId	= builder.appointmentId;
		this.campaiginId	= builder.campaignId;
		this.roleId			= builder.roleId;
		this.name 			= builder.name;
		this.description 	= builder.description;
		this.videoLink 		= builder.videoLink;
		this.phoneNumber 	= builder.phoneNumber;
		this.when 			= builder.when;
	}
	
	/**
	* Returns the unique Id of the Appointment
	* @return Id of the Appointment
	*/
	public UUID getAppointmentId() {
		return this.appointmentId;
	}
	
	/**
	* Returns the Id of the Campaign the Appointment is for
	* @return Id of the Campaign
	*/
	public UUID getCampaignId() {
		return this.campaiginId;
	}
		
	/**
	* If an Appointment for a specific Role then the Id of the Role
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
	public static AppointmentEntityBuilder builder() {
		return new AppointmentEntityBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class AppointmentEntityBuilder {
		
		private UUID			appointmentId;
		private UUID			campaignId;
		private UUID			roleId;
		private String 			name;
		private String 			description;
		private String 			videoLink;
		private String 			phoneNumber;
		private ZonedDateTime 	when;
		
		/**
		* Sets the Id of the appointment
		* @param appointmentId - Unique id of the Appointment
		* @return Builder
		*/
		public AppointmentEntityBuilder appointmentId(UUID appointmentId) {
			this.appointmentId = appointmentId;
			return this;
		}
		
		/**
		* Sets the Id of the Campaign the Appointment is associated with 
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public AppointmentEntityBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* If Role level Appointment, sets the Id of the Role the Appointment is associated with 
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public AppointmentEntityBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the name of the Appointment
		* @param name - Name of appointment
		* @return Builder
		*/
		public AppointmentEntityBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		* Sets a description of what the appointment is for
		* @param description - description of the appointment
		* @return Builder
		*/
		public AppointmentEntityBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Sets the link to the video meeting
 		* @param videoLink - URL to video meeting
		* @return Builder
		*/
		public AppointmentEntityBuilder videoLink(String videoLink) {
			this.videoLink = videoLink;
			return this;
		}
		
		/**
		* Sets the phone number to call for the appointment 
		* @param phoneNumber - Phone number
		* @return Builder
		*/
		public AppointmentEntityBuilder phoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}
		
		/**
		* Sets when the Appointment is due to take place
		* @param when - When the Appointment is scheduled
		* @return Builder
		*/
		public AppointmentEntityBuilder when(ZonedDateTime when) { 
			this.when = when;
			return this;
		}
		
		/**
		* Returns an initialized Appointment
		* @return initialized appointment
		*/
		public AppointmentEntity build() {
			return new AppointmentEntity(this);
		}
		
	}
}
