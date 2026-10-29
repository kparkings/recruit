package com.arenella.recruit.campaigns.controllers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Class represents a request to add a new External Candidate to a Campaign or Role
* An External Candidte is a Candidate with no profile in the system. It allows the 
* Recruiters to use the platform to manage a complete campaign including with Candidates 
* they work with that have not registered with Arenella-ICT
*/
@JsonDeserialize(builder=NewExternalCandidateAPIInbound.NewExternalCandidateAPIInboundBuilder.class)
public class NewExternalCandidateAPIInbound {

	private String firstName;
	private String surname;
	private String countryCode;
	private String jobTitle;
	private String email;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public NewExternalCandidateAPIInbound(NewExternalCandidateAPIInboundBuilder builder) {
		this.firstName 		= builder.firstName;
		this.surname 		= builder.surname;
		this.countryCode 	= builder.countryCode;
		this.jobTitle 		= builder.jobTitle;
		this.email 			= builder.email;
	}
	
	/**
	* Returns the Candidates first name 
	* @return first name
	*/
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns the Candidates surname
	* @return surname
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns the country code of where the 
	* Candidate is based
	* @return country code
	*/
	public String getCountryCode() {
		return this.countryCode;
	}
	
	/**
	* Returns the Job title of the roles the Candidate
	* performs
	* @return Job title of the Candidate
	*/
	public String getJobTitle() {
		return this.jobTitle;
	}
	
	/**
	* Returns the email of the Candidate
	* @return candidates email
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* Returns a Builder for the class
	* @return Builder
	*/
	public static NewExternalCandidateAPIInboundBuilder builder() {
		return new NewExternalCandidateAPIInboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class NewExternalCandidateAPIInboundBuilder {
		
		private String firstName;
		private String surname;
		private String countryCode;
		private String jobTitle;
		private String email;
		
		/**
		* Sets the First name of the Candidate
		* @param firstName - First Name of the Candidate
		* @return Builder
		*/
		public NewExternalCandidateAPIInboundBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the Candidates surname
		* @param surname - Candidate surname
		* @return Builder
		*/
		public NewExternalCandidateAPIInboundBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Country code of where the Candidate is based
		* @param countryCode - Code of Candidates country
		* @return Builder
		*/
		public NewExternalCandidateAPIInboundBuilder countryCode(String countryCode) {
			this.countryCode = countryCode;
			return this;
		}
		
		/**
		* Sets the Candidates Job title
		* @param jobTitle - Function the Candidate performs
		* @return Builder
		*/
		public NewExternalCandidateAPIInboundBuilder jobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
			return this;
		}
		
		/**
		* Sets the email address of the Candidate
		* @param email - Email address of the Candidate
		* @return Builder
		*/
		public NewExternalCandidateAPIInboundBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Returns an initialized instance of the Class
		* @return initialized instance of the Class
		*/
		public NewExternalCandidateAPIInbound build() {
			return new NewExternalCandidateAPIInbound(this);
		}
		
	}
	
}
