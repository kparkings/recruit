package com.arenella.recruit.recruiters.controllers;

import com.arenella.recruit.recruiters.beans.Recruiter;

/**
* Contains most basic details of a Recruiter 
*/
public class RecruiterBasicInfoAPIOutbound {

	private String recruiterId;
	private String firstname;
	private String surname;
	
	/**
	* Constructor
	* @param builder - Contains initialization values
	*/
	public RecruiterBasicInfoAPIOutbound(RecruiterBasicInfoAPIOutboundBuilder builder) {
		this.recruiterId 	= builder.recruiterId;
		this.firstname 		= builder.firstname;
		this.surname 		= builder.surname;
	}
	
	/**
	* Returns the unique Id of the Recruiter
	* @return Id
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns the firstName of the Recruiter
	* @return first name
	*/
	public String getFirstname() {
		return this.firstname;
	}
	
	/**
	* Returns the surname of the Recruiter
	* @return surname
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static RecruiterBasicInfoAPIOutboundBuilder builder() {
		return new RecruiterBasicInfoAPIOutboundBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class RecruiterBasicInfoAPIOutboundBuilder {
	
		private String recruiterId;
		private String firstname;
		private String surname;
		
		/**
		* Sets the Unique If of the Recruiter
		* @param recruiterId - Unique identifier
		* @return BUilder
		*/
		public RecruiterBasicInfoAPIOutboundBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Returns the firstName of the Recruiter
		* @param firstname - FirstName of the Recruiter
		* @return Builder
		*/
		public RecruiterBasicInfoAPIOutboundBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Recruiters surname
		* @param surname - surname of the Recruiter
		* @return  Builder
		*/
		public RecruiterBasicInfoAPIOutboundBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Returns initialized instance based on builder values
		* @return Initialized instance
		*/
		public RecruiterBasicInfoAPIOutbound build() {
			return new RecruiterBasicInfoAPIOutbound(this);
		}
		
	} 
	
	/**
	* Converts from Domain to API Outbound representation
	* @param recruiter - To convert
	* @return converted
	*/
	public static RecruiterBasicInfoAPIOutbound fromDomain(Recruiter recruiter) {
		return RecruiterBasicInfoAPIOutbound
				.builder()
					.recruiterId(recruiter.getUserId())
					.firstname(recruiter.getFirstName())
					.surname(recruiter.getSurname())
				.build();
	}
}