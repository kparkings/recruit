package com.arenella.recruit.adapters.events;

/**
* Event informing other services that a Recruiter has been created.
* @author K Parkings
*/
public class RecruiterCreatedEvent {

	private String recruiterId;
	private String encryptedPassword;
	private String email;
	private String firstName;
	private String surname;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public RecruiterCreatedEvent(RecruiterCreatedEventBuilder builder) {
		
		this.recruiterId 		= builder.recruiterId;
		this.encryptedPassword 	= builder.encryptedPassword;
		this.email				= builder.email;
		this.firstName			= builder.firstName;
		this.surname			= builder.surname;
	}
	
	/**
	* Returns the Unique id of the recruiter
	* @return unique id of the recruiter (userId0
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns the Recruiters encrypted password
	* @return Encryted password of the Recruiter
	*/
	public String getEncryptedPassord() {
		return this.encryptedPassword;
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
	* Returns an instance of a Builder for the RecruiterCreatedEvent class
	* @return Builder for the Class
	*/
	public static RecruiterCreatedEventBuilder builder() {
		return new RecruiterCreatedEventBuilder();
	}
	
	/**
	* Builder for the RecruiterCreatedEvent class
	* @author K Parkings
	*/
	public static class RecruiterCreatedEventBuilder {
		
		private String recruiterId;
		private String encryptedPassword;
		private String email;
		private String firstName;
		private String surname;
		
		/**
		* Sets the unique Id of the Recruiter which has been created
		* @param recruiterId - Unique Id of recruiter
		* @return Builder
		*/
		public RecruiterCreatedEventBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the Recruiters password (encrypted)
		* @param encryptedPassword - Already encrypted version of the Password
		* @return Builder
		*/
		public RecruiterCreatedEventBuilder encryptedPassword(String encryptedPassword) {
			this.encryptedPassword = encryptedPassword;
			return this;
		}
		
		/**
		* Sets the firstname of the Recruiter
		* @param firstName - Recruiters firstname
		* @return Builder
		*/
		public RecruiterCreatedEventBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the surname of the Recruiter
		* @param surname - Recruiters surname
		* @return Builder
		*/
		public RecruiterCreatedEventBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the email address of the Recruiter
		* @param email - Email address of the Recruiter
		* @return Builder
		*/
		public RecruiterCreatedEventBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Returns an initialized instance of RecruiterCreatedEvent
		* @return Initailzed instance of RecruiterCreatedEvent
		*/
		public RecruiterCreatedEvent build() {
			return new RecruiterCreatedEvent(this);
		}
		
	}
		
}