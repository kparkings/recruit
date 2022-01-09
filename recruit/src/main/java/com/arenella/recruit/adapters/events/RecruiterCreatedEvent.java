package com.arenella.recruit.adapters.events;

/**
* Event informing other services that a Recruiter has been created.
* @author K Parkings
*/
public class RecruiterCreatedEvent {

	private String recruiterId;
	private String encryptedPassword;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public RecruiterCreatedEvent(RecruiterCreatedEventBuilder builder) {
		
		this.recruiterId 		= builder.recruiterId;
		this.encryptedPassword 	= builder.encryptedPassword;
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
		* Returns an initialized instance of RecruiterCreatedEvent
		* @return Initailzed instance of RecruiterCreatedEvent
		*/
		public RecruiterCreatedEvent build() {
			return new RecruiterCreatedEvent(this);
		}
		
	}
		
}