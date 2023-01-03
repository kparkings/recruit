package com.arenella.recruit.candidates.beans;

import java.util.UUID;

import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Represents a Match between a Candidate and a CandidateSearchAlert
* specification
* @author K Parkings
*/
public class CandidateSearchAlertMatch {

	private UUID 					id;
	private String 					recruiterId;
	private UUID					alertId;
	private String 					alertName;
	private Long 					candidateId;
	private String					roleSought;
	private suggestion_accuracy 	accuracy;
	
	/**
	* Constructor
	* @param builder - Contains initialization details
	*/
	public CandidateSearchAlertMatch(CandidateSearchAlertMatchBuilder builder) {
		this.id				= builder.id;
		this.recruiterId	= builder.recruiterId;
		this.alertId		= builder.alertId;
		this.alertName		= builder.alertName;
		this.candidateId	= builder.candidateId;
		this.roleSought		= builder.roleSought;
		this.accuracy	 	= builder.accuracy;
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
	* Returns the name of the Alert
	* @return Alert name
	*/
	public String  getAlertName() {
		return this.alertName;
	}
	
	/**
	* Returns the id of the Alert
	* @return Alert id
	*/
	public UUID  getAlertId() {
		return this.alertId;
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
	* Returns a Builder for the CandidateSearchAlertMatch class
	* @return Builder
	*/
	public static CandidateSearchAlertMatchBuilder builder() {
		return new CandidateSearchAlertMatchBuilder();
	}
	
	/**
	* Builder for the CandidateSearchAlertMatch class
	* @author K Parkings
	*/
	public static class CandidateSearchAlertMatchBuilder{
	
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
		public CandidateSearchAlertMatchBuilder  id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the Id of the recruiter owning the Alert
		* @param recruiterId - Id of the recruiter
		* @return Builder
		*/
		public CandidateSearchAlertMatchBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the id of the Alert
		* @param alertId - Unique id of the Alert
		* @return Builder
		*/
		public CandidateSearchAlertMatchBuilder alertId(UUID alertId) {
			this.alertId = alertId;
			return this;
		}
		
		/**
		* Sets the name of the Alert
		* @param alertName - name of the Alert
		* @return Builder
		*/
		public CandidateSearchAlertMatchBuilder alertName(String alertName) {
			this.alertName = alertName;
			return this;
		}
		
		/**
		* Sets the Unique id of the Candidate that was matched on
		* @param candidateId - id of the Candidate
		* @return Builder
		*/
		public CandidateSearchAlertMatchBuilder candidateId(Long candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets the Role the Candidate is looking for
		* @param roleSought - Role sought by the Candidate
		* @return Builder
		*/
		public CandidateSearchAlertMatchBuilder roleSought(String roleSought) {
			this.roleSought = roleSought;
			return this;
		}
		
		/**
		* Sets how accurately the Candidate met the Alerts requirements
		* @param accuracy - Accuracy of the Match
		* @return Builder
		*/
		public CandidateSearchAlertMatchBuilder accuracy(suggestion_accuracy accuracy) {
			this.accuracy = accuracy;
			return this;
		}
		
		/**
		* Returns an instance of CandidateSearchAlertMatch initialized
		* with the values in the Builder
		* @return CandidateSearchAlertMatch
		*/
		public CandidateSearchAlertMatch build() {
			return new CandidateSearchAlertMatch(this);
		}
		
	}
	
}