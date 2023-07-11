package com.arenella.recruit.adapters.events;

import com.arenella.recruit.recruiters.beans.Recruiter.language;

/**
* Event with details of Recruiters updated
* account details
* @author K Parkings
*/
public class RecruiterUpdatedEvent {

	private String 		recruiterId;
	private String 		firstName;
	private String 		surname;
	private String 		email;
	private String		companyName;
	private language 	language;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public RecruiterUpdatedEvent(RecruiterUpdatedEventBuilder builder) {
		this.recruiterId				= builder.recruiterId;
		this.firstName					= builder.firstName;
		this.surname					= builder.surname;
		this.email						= builder.email;
		this.companyName				= builder.companyName;
		
		this.language		= builder.language;
	}
	
	/**
	* Returns the Unique id of the recruiter
	* @return unique id of the recruiter (userId0
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns the Email address of the Recruiter
	* @return email address of the Recruiter
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* Returns the firstname of the Recruiter
	* @return firstname of the Recruiter
	*/
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns the surname of the Recruiter
	* @return surname of the Recruiter
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns the name of the Recruiter's company
	* @return name of the company
	*/
	public String getCompanyName() {
		return this.companyName;
	}
	
	/**
	* Returns the primary language of the Recruiter
	* @return recruiters main language
	*/
	public language getLanguage() {
		return this.language;
	}
	
	/**
	* Returns an instance of a Builder for the RecruiterCreatedEvent class
	* @return Builder for the Class
	*/
	public static RecruiterUpdatedEventBuilder builder() {
		return new RecruiterUpdatedEventBuilder();
	}
	
	/**
	* Builder for the RecruiterCreatedEvent class
	* @author K Parkings
	*/
	public static class RecruiterUpdatedEventBuilder {
		
		private String 		recruiterId;
		private String 		firstName;
		private String 		surname;
		private String 		email;
		private String		companyName;
		private language 	language;
		
		/**
		* Sets the unique Id of the Recruiter which has been created
		* @param recruiterId - Unique Id of recruiter
		* @return Builder
		*/
		public RecruiterUpdatedEventBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the firstname of the Recruiter
		* @param firstName - Recruiters firstname
		* @return Builder
		*/
		public RecruiterUpdatedEventBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the surname of the Recruiter
		* @param surname - Recruiters surname
		* @return Builder
		*/
		public RecruiterUpdatedEventBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the email address of the Recruiter
		* @param email - Email address of the Recruiter
		* @return Builder
		*/
		public RecruiterUpdatedEventBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the email address of the Recruiter
		* @param email - Email address of the Recruiter
		* @return Builder
		*/
		public RecruiterUpdatedEventBuilder companyName(String companyName) {
			this.companyName = companyName;
			return this;
		}
		
		/**
		* Sets the email address of the Recruiter
		* @param email - Email address of the Recruiter
		* @return Builder
		*/
		public RecruiterUpdatedEventBuilder language(language language) {
			this.language = language;
			return this;
		}
		
		/**
		* Returns an initialized instance of RecruiterCreatedEvent
		* @return Initailzed instance of RecruiterCreatedEvent
		*/
		public RecruiterUpdatedEvent build() {
			return new RecruiterUpdatedEvent(this);
		}
		
	}
	
}