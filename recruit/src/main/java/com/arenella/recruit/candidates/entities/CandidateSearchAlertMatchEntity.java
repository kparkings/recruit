package com.arenella.recruit.candidates.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.candidates.beans.CandidateSearchAlertMatch;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Entity representation of a Match for a CandidateSearchAlert with 
* a Candidate
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="candidate_search_alert_match")
public class CandidateSearchAlertMatchEntity {

	@Id
	@Column(name="id")
	private UUID 					id;
	
	@Column(name="alertId")
	private UUID					alertId;
	
	@Column(name="recruiter_id")
	private String 					recruiterId;
	
	@Column(name="alert_name")
	private String 					alertName;
	
	@Column(name="candidate_id")
	private Long 					candidateId;
	
	@Column(name="role_sought")
	private String					roleSought;
	
	@Column(name="accuracy")
	@Enumerated(EnumType.STRING)
	private suggestion_accuracy 	accuracy;
	
	/**
	* Default constructor 
	*/
	public CandidateSearchAlertMatchEntity() {
		//Hibernate
	}
	
	/**
	* Constructor
	* @param builder - Contains initialization details
	*/
	public CandidateSearchAlertMatchEntity(CandidateSearchAlertMatchEntityBuilder builder) {
		this.id				= builder.id;
		this.recruiterId	= builder.recruiterId;
		this.alertId		= builder.alertId;
		this.alertName		= builder.alertName;
		this.candidateId	= builder.candidateId;
		this.roleSought		= builder.roleSought;
		this.accuracy		= builder.accuracy;
	}
	
	/**
	* Returns the unique id of the Match
	* @return Unique Id
	*/
	public UUID getId() {
		return this.id;
	};
	
	/**
	* Returns the Unique Id of the recruiter that owns the Alert
	* @return recruiter Id
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns the Unique Id of the Alert that
	* produced the Match
	* @return id of the Alert
	*/
	public UUID getAlertId() {
		return this.alertId;
	}
	
	/**
	* Returns the name of the Alert
	* @return Alert name
	*/
	public String  getAlertName() {
		return this.alertName;
	}
	
	/**
	* Returns the unique id of the Candidate that matched
	* @return id of Candidate
	*/
	public Long getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns the name of the role sought by the candidate
	* @return candidates role
	*/
	public String getRoleSought() {
		return this.roleSought;
	}
	
	/**
	* Returns how accurate a match the Candidate is for the Alert
	* @return accuracy
	*/
	public suggestion_accuracy getAccuracy() {
		return this.accuracy;
	}
	
	/**
	* Returns a Builder for the CandidateSearchAlertMatchEntity class
	* @return Builder
	*/
	public static CandidateSearchAlertMatchEntityBuilder builder() {
		return new CandidateSearchAlertMatchEntityBuilder();
	}
	
	/**
	* Builder for the CandidateSearchAlertMatchEntity class
	* @author K Parkings
	*/
	public static class CandidateSearchAlertMatchEntityBuilder{
	
		private UUID 					id;
		private String 					recruiterId;
		private UUID					alertId;
		private String 					alertName;
		private Long 					candidateId;
		private String					roleSought;
		private suggestion_accuracy 	accuracy;
		
		/**
		* Sets the Unique Id of the Match
		* @param id - Id of the Match
		* @return Builder
		*/
		public CandidateSearchAlertMatchEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the Id of the recruiter owning the Alert
		* @param recruiterId - Id of the recruiter
		* @return Builder
		*/
		public CandidateSearchAlertMatchEntityBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the id of the Alert that generated the Match
		* @param alertId - unique if of the Alert
		* @return Builder
		*/
		public CandidateSearchAlertMatchEntityBuilder alertId(UUID alertId) {
			this.alertId = alertId;
			return this;
		}
		
		/**
		* Sets the name of the Alert
		* @param alertName - name of the Alert
		* @return Builder
		*/
		public CandidateSearchAlertMatchEntityBuilder alertName(String alertName) {
			this.alertName = alertName;
			return this;
		}
		
		/**
		* Sets the Unique id of the Candidate that was matched on
		* @param candidateId - id of the Candidate
		* @return Builder
		*/
		public CandidateSearchAlertMatchEntityBuilder candidateId(Long candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* 
		* @param roleSought
		* @return Builder
		*/
		public CandidateSearchAlertMatchEntityBuilder roleSought(String roleSought) {
			this.roleSought = roleSought;
			return this;
		}
		
		/**
		* Sets how accurately the Candidate met the Alerts requirements
		* @param accuracy - Accuracy of the Match
		* @return Builder
		*/
		public CandidateSearchAlertMatchEntityBuilder accuracy(suggestion_accuracy accuracy) {
			this.accuracy = accuracy;
			return this;
		}
		
		/**
		* Returns an instance of CandidateSearchAlertMatch initialized
		* with the values in the Builder
		* @return CandidateSearchAlertMatch
		*/
		public CandidateSearchAlertMatchEntity build() {
			return new CandidateSearchAlertMatchEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to Domain representation of Match
	* @param entity - Entity representation
	* @return Domain representation
	*/
	public static CandidateSearchAlertMatch convertFromEntity(CandidateSearchAlertMatchEntity entity) {
		return CandidateSearchAlertMatch
				.builder()
					.alertName(entity.getAlertName())
					.candidateId(entity.getCandidateId())
					.id(entity.getId())
					.recruiterId(entity.getRecruiterId())
					.roleSought(entity.getRoleSought())
					.accuracy(entity.getAccuracy())
					.alertId(entity.getAlertId()) //Need to add alertId to table and pass it in here. Als
				.build();
	}
	
	/**
	* Converts from Domain to Entity representation of Match
	* @param match - Domain representation
	* @return Entity representation
	*/
	public static CandidateSearchAlertMatchEntity convertToEntity(CandidateSearchAlertMatch match) {
		return CandidateSearchAlertMatchEntity
				.builder()
					.alertName(match.getAlertName())
					.candidateId(match.getCandidateId())
					.id(match.getId())
					.recruiterId(match.getRecruiterId())
					.roleSought(match.getRoleSought())
					.accuracy(match.getAccuracy())
					.alertId(match.getAlertId())
				.build();
	}
	
}