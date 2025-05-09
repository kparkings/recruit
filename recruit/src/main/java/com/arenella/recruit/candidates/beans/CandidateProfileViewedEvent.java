package com.arenella.recruit.candidates.beans;

import java.time.LocalDateTime;
import java.util.UUID;

public class CandidateProfileViewedEvent {

	private UUID 			id;
	private String 			recruiterId;
	private String 			candidateId;
	private LocalDateTime 	viewed;
	
	/**
	* Construtor based upon a builder 
	* @param builder - contains initialization values
	*/
	public CandidateProfileViewedEvent(CandidateProfileViewedEventBuilder builder) {
		this.id 			= builder.id;
		this.recruiterId 	= builder.recruiterId;
		this.candidateId 	= builder.candidateId;
		this.viewed 		= builder.viewed;
	}
	
	/**
	* Returns the unique identifier of the event
	* @return id of the event
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns the id of the Recruiter that viewed the candidates profile
	* @return Recruiters unique id
	*/
	public String recruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns the id of the Candidate whose profile was viewed
	* @return Candidates unique Id
	*/
	public String candidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns when the Recruiter viewed the Candidates profile
	* @return When profile was viewed
	*/
	public LocalDateTime getViewed() {
		return this.viewed;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static CandidateProfileViewedEventBuilder builder() {
		return new CandidateProfileViewedEventBuilder();
	}
	
	/**
	* Builder for the class
	*/
	public static class CandidateProfileViewedEventBuilder {
	
		private UUID 			id;
		private String 			recruiterId;
		private String 			candidateId;
		private LocalDateTime 	viewed;
		
		/**
		* Sets the unique identifier of the event
		* @param id - Unique ID
		* @return Builder
		*/
		public CandidateProfileViewedEventBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the unique identifier of the Recruiter
		* @param recruiterId - Recruiter Id
		* @return Builder
		*/
		public CandidateProfileViewedEventBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the unique identifier of the Candidate
		* @param candidateId - Candidate Id
		* @return Builder
		*/
		public CandidateProfileViewedEventBuilder candidateId(String candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets when the Recruiter viewed the Candidates profile
		* @param viewed - When profile was viewed
		* @return Builder
		*/
		public CandidateProfileViewedEventBuilder viewed(LocalDateTime viewed) {
			this.viewed = viewed;
			return this;
		}
		
		/**
		* Returns initialized instance
		* @return initialized instance
		*/
		public CandidateProfileViewedEvent build() {
			return new CandidateProfileViewedEvent(this);
		}
		
	}
}
