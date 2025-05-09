package com.arenella.recruit.candidates.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.arenella.recruit.candidates.beans.CandidateProfileViewedEvent;

/**
* Event representing a Recruiter viewing the profile 
* of a Candidate 
*/
@Entity
@Table(name = "event_candidate_profile_views")
public class CandidateProfileViewedEventEntity {

	@Id
	@Column(name="id")
	private UUID 			id;
	
	@Column(name="recruiter_id")
	private String 			recruiterId;
	
	@Column(name="candidate_id")
	private String 			candidateId;
	
	@Column(name="viewed")
	private LocalDateTime 	viewed;
	
	/**
	* Default constructor 
	*/
	public CandidateProfileViewedEventEntity() {
		//Hibernate
	}
	
	/**
	* Construtor based upon a builder 
	* @param builder - contains initialization values
	*/
	public CandidateProfileViewedEventEntity(CandidateProfileViewedEventEntityBuilder builder) {
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
	public static CandidateProfileViewedEventEntityBuilder builder() {
		return new CandidateProfileViewedEventEntityBuilder();
	}
	
	/**
	* Builder for the class
	*/
	public static class CandidateProfileViewedEventEntityBuilder {
	
		private UUID 			id;
		private String 			recruiterId;
		private String 			candidateId;
		private LocalDateTime 	viewed;
		
		/**
		* Sets the unique identifier of the event
		* @param id - Unique ID
		* @return Builder
		*/
		public CandidateProfileViewedEventEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the unique identifier of the Recruiter
		* @param recruiterId - Recruiter Id
		* @return Builder
		*/
		public CandidateProfileViewedEventEntityBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the unique identifier of the Candidate
		* @param candidateId - Candidate Id
		* @return Builder
		*/
		public CandidateProfileViewedEventEntityBuilder candidateId(String candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets when the Recruiter viewed the Candidates profile
		* @param viewed - When profile was viewed
		* @return Builder
		*/
		public CandidateProfileViewedEventEntityBuilder viewed(LocalDateTime viewed) {
			this.viewed = viewed;
			return this;
		}
		
		/**
		* Returns initialized instance
		* @return initialized instance
		*/
		public CandidateProfileViewedEventEntity build() {
			return new CandidateProfileViewedEventEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity representation to Domain representation
	* @param entity - To convert
	* @return Domain representation
	*/
	public static CandidateProfileViewedEvent toDomain(CandidateProfileViewedEventEntity entity) {
		return CandidateProfileViewedEvent
				.builder()
				.id(entity.getId())
				.candidateId(entity.candidateId)
				.recruiterId(entity.recruiterId())
				.viewed(entity.getViewed())
				.build();
	}
	
	/**
	 * Converts from Domain representation to Entity representation
	 * @param event - To convert
	 * @return Entity representation
	 */
	public static CandidateProfileViewedEventEntity fromDomain(CandidateProfileViewedEvent event) {
		return CandidateProfileViewedEventEntity
				.builder()
				.id(event.getId())
				.candidateId(event.candidateId())
				.recruiterId(event.recruiterId())
				.viewed(event.getViewed())
				
				.build();
	}

}