package com.arenella.recruit.adapters.events;

import java.util.Optional;

/**
* Even represents an update to a Recruiters profile 
*/
public class RecruiterProfileUpdatedEvent {
	
	private String 	recruiterId;
	private Photo 	profileImage;
	
	/**
	* Constructor based upon a builder
	* @param builder - contains initialization data
	*/
	public RecruiterProfileUpdatedEvent(RecruiterProfileUpdatedEventBuilder builder) {
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
	public static RecruiterProfileUpdatedEventBuilder builder() {
		return new RecruiterProfileUpdatedEventBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class RecruiterProfileUpdatedEventBuilder {
		
		private String 	recruiterId;
		private Photo 	profileImage;
		
		/**
		* Sets the Id of the recruiter whose profile was created
		* @param recruiterId - Unique id
		* @return Builder
		*/
		public RecruiterProfileUpdatedEventBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the profile image
		* @param profileImage - profile image
		* @return Builder
		*/
		public RecruiterProfileUpdatedEventBuilder profileImage(Photo profileImage) {
			this.profileImage = profileImage;
			return this;
		}
		
		/**
		* Returns initialized instance
		* @return initialized instance
		*/
		public RecruiterProfileUpdatedEvent build() {
			return new RecruiterProfileUpdatedEvent(this);
		}
		
	}
}