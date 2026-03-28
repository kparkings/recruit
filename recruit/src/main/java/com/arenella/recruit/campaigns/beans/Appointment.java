package com.arenella.recruit.campaigns.beans;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
* Class represents an Appointment relating to a Campaign or Role such 
* as a call with a Candidate 
*/
public class Appointment {
	
	private String 			name;
	private String 			description;
	private String 			videoLink;
	private String 			phoneNumber;
	private ZonedDateTime 	when;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public Appointment(AppointmentBuilder builder) {
		this.name 			= builder.name;
		this.description 	= builder.description;
		this.videoLink 		= builder.videoLink;
		this.phoneNumber 	= builder.phoneNumber;
		this.when 			= builder.when;
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
	public static AppointmentBuilder builder() {
		return new AppointmentBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class AppointmentBuilder {
		
		private String 			name;
		private String 			description;
		private String 			videoLink;
		private String 			phoneNumber;
		private ZonedDateTime 	when;
		
		/**
		* Sets the name of the Appointment
		* @param name - Name of appointment
		* @return Builder
		*/
		public AppointmentBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		* Sets a description of what the appointment is for
		* @param description - description of the appointment
		* @return Builder
		*/
		public AppointmentBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Sets the link to the video meeting
 		* @param videoLink - URL to video meeting
		* @return Builder
		*/
		public AppointmentBuilder videoLink(String videoLink) {
			this.videoLink = videoLink;
			return this;
		}
		
		/**
		* Sets the phone number to call for the appointment 
		* @param phoneNumber - Phone number
		* @return Builder
		*/
		public AppointmentBuilder phoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}
		
		/**
		* Sets when the Appointment is due to take place
		* @param when - When the Appointment is scheduled
		* @return Builder
		*/
		public AppointmentBuilder when(ZonedDateTime when) { 
			this.when = when;
			return this;
		}
		
		/**
		* Returns an initialized Appointment
		* @return initialized appointment
		*/
		public Appointment build() {
			return new Appointment(this);
		}
		
	}
	
	
}
