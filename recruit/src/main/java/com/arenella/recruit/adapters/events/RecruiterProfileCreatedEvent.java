package com.arenella.recruit.adapters.events;

import java.util.Optional;

/**
* Event informing that a new RecruiterProfile 
* was created 
*/
public class RecruiterProfileCreatedEvent {

	private String 	recruiterId;
	private Photo 	profileImage;
	
	/**
	* Constructor based upon a builder
	* @param builder - contains initialization data
	*/
	public RecruiterProfileCreatedEvent(RecruiterProfileCreatedEventBuilder builder) {
		this.recruiterId 	= builder.recruiterId;
		this.profileImage 	= builder.profileImage;
	}
	/**
	* Returns unique id of the Recruiter
	* @return Id
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns the recruiters profile image
	* @return profile image
	*/
	public Optional<Photo> getProfileImage() {
		return Optional.ofNullable(this.profileImage);
	}
	
	/**
	* Returns a builder for the class
	* @return
	*/
	public static RecruiterProfileCreatedEventBuilder builder() {
		return new RecruiterProfileCreatedEventBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class RecruiterProfileCreatedEventBuilder {
		
		private String 	recruiterId;
		private Photo 	profileImage;
		
		/**
		* Sets the Id of the recruiter whose profile was created
		* @param recruiterId - Unique id
		* @return Builder
		*/
		public RecruiterProfileCreatedEventBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the profile image
		* @param profileImage - profile image
		* @return Builder
		*/
		public RecruiterProfileCreatedEventBuilder profileImage(Photo profileImage) {
			this.profileImage = profileImage;
			return this;
		}
		
		/**
		* Returns initialized instance
		* @return initialized instance
		*/
		public RecruiterProfileCreatedEvent build() {
			return new RecruiterProfileCreatedEvent(this);
		}
		
	}
}