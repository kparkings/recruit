package com.arenella.recruit.campaigns.controllers;

import com.arenella.recruit.campaigns.beans.Candidate;
import com.arenella.recruit.campaigns.beans.Candidate.Type;

/**
* APIOutbound representation of a Candidate 
*/
public class CandidateAPIOutbound {

	private String 	id;
	private Type 	type;
	private String 	firstName;
	private String 	surname;
	private String  countryCode;
	private String 	jobTitle;
	private String 	email;
	private boolean deletedFromSystem;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public CandidateAPIOutbound(CandidateAPIOutboundBuilder builder) {
		this.id 				= builder.id;
		this.type 				= builder.type;
		this.firstName 			= builder.firstName;
		this.surname 			= builder.surname;
		this.countryCode 		= builder.countryCode;
		this.jobTitle 			= builder.jobTitle;
		this.email 				= builder.email;
		this.deletedFromSystem 	= builder.deletedFromSystem;
	}
	
	/**
	* Returns the unique Id of the Candidate
	* @return
	*/
	public String getId() {
		return this.id;
	}
	
	/**
	* Returns whether the Candidate is a Registered Candidate 
	* in the System or a reference to one of the Recruiters 
	* external Candidates
	* @return Candidate Type
	*/
	public Type getType() {
		return this.type;
	}
	
	/**
	* Returns the firstName of the Candidate
	* @return first name
	*/
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns the surname of the Candidate
	* @return surname
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns the code of the Country where the 
	* Candidate is based
	* @return Country code 
	*/
	public String getCountryCode() {
		return this.countryCode;
	}
	
	/**
	* Returns the Job title of the Candidate
	* @return job title
	*/
	public String getJobTitle() {
		return this.jobTitle;
	}
	
	/**
	* Returns the candidates email address
	* @return Candidates email address
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* If a Candidate has deleted their profile form the 
	* system we need to delete their details. Their Candidate 
	* record will be updated to remove identifying details but the 
	* Id will remain to allow Recruiters to see that the Candidate 
	* they had previously added to a Campaign/Role is no 
	* longer in the system
	* @return is the Candidate has been deleted from the System
	*/
	public boolean isDeletedFromSystem() {
		return deletedFromSystem;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder for the class
	*/
	public static CandidateAPIOutboundBuilder builder() {
		return new CandidateAPIOutboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class CandidateAPIOutboundBuilder {
		
		private String 	id;
		private Type 	type;
		private String 	firstName;
		private String 	surname;
		private String  countryCode;
		private String 	jobTitle;
		private String 	email;
		private boolean deletedFromSystem;
		
		/**
		* Populates the Builder with values from a Candidate 
		* @param candiate - Initialization values
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder from (Candidate candidate) {
			
			this.id 				= candidate.getId();
			this.type 				= candidate.getType();
			this.firstName 			= candidate.getFirstName();
			this.surname 			= candidate.getSurname();
			this.countryCode 		= candidate.getCountryCode();
			this.jobTitle 			= candidate.getJobTitle();
			this.email 				= candidate.getEmail();
			this.deletedFromSystem 	= candidate.isDeleteFromSystem();
			
			return this;
		}
		
		/**
		* Returns an initialized instance 
		* @return initialized instance
		*/
		public CandidateAPIOutbound build() {
			return new CandidateAPIOutbound(this);
		}
		
	}
	
}
