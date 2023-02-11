package com.arenella.recruit.candidates.controllers;

import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Outbound representation of SavedCandidate
* @author K Parkings
*/
public class SavedCandidateAPIOutbound {

	private String 							userId;
	private long 							candidateId;
	private String 							notes;
	private CandidateSuggestionAPIOutbound 	candidate;
	
	/**
	* Constructor based upon a builder
	* @param builder - contains initialization values
	*/
	public SavedCandidateAPIOutbound(SavedCandidateAPIOutboundBuilder builder) {
		this.userId 		= builder.userId;
		this.candidateId 	= builder.candidateId;
		this.notes 			= builder.notes;
		this.candidate		= builder.candidate;
	}
	
	/**
	* Returns the Id of the User who has saved the Candidate
	* @return Unique Id of the User
	*/
	public String getUserId() {
		return this.userId;
	}
	
	/**
	* Returns the Id of the Candidate who has been saved by the User
	* @return Unique Id of the Candidate
	*/
	public long getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns notes by the User relating to the Candidate
	* @return Notes
	*/
	public String getNotes() {
		return this.notes;
	}
	
	/**
	* Returns the Saved Candidate
	* @return
	*/
	public CandidateSuggestionAPIOutbound getCandidate() {
		return this.candidate;
	} 
	
	/**
	* Returns a Builder for the SavedCandidateAPIOutbound class
	* @return Builder
	*/
	public static SavedCandidateAPIOutboundBuilder builder() {
		return new SavedCandidateAPIOutboundBuilder();
	}
	
	/**
	* Builder class for SavedCandidateAPIOutbound
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class SavedCandidateAPIOutboundBuilder {
	
		private String 							userId;
		private long 							candidateId;
		private String 							notes;
		private CandidateSuggestionAPIOutbound 	candidate;
		
		/**
		* Sets the Unique Id of the User who saved the Candidate
		* @param userId - Unique Id of the User
		* @return Builder
		*/
		public SavedCandidateAPIOutboundBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets the Unique Id of the Candidate who is being saved
		* by the User
		* @param candidateId - Unique id of the Candidate
		* @return Builder
		*/
		public SavedCandidateAPIOutboundBuilder candidateId(long candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets notes made by the User about the Candidate
		* @param notes	- Notes relating to the Candidate
		* @return Builder
		*/
		public SavedCandidateAPIOutboundBuilder	notes(String notes){
			this.notes = notes;
			return this;
		}
		
		/**
		* Sets the actual Candidate that has been saved
		* @param candidate - Actual Candidate
		* @return Builder
		*/
		public SavedCandidateAPIOutboundBuilder	candidate(CandidateSuggestionAPIOutbound candidate){
			this.candidate = candidate;
			return this;
		}
		
		/**
		* Returns an initialized instance of SavedCandidateAPIOutbound
		* @return INstance of SavedCandidateAPIInbound
		*/
		public SavedCandidateAPIOutbound build() {
			return new SavedCandidateAPIOutbound(this);
		} 
		
	}
	
	/**
	* Converts from domain representation of SavedCandidate to the API Out-bound representation
	* @param savedCandidate - domain representation
	* @return API Out-bound representation
	*/
	public static SavedCandidateAPIOutbound convertFromDomain(SavedCandidate savedCandidate, CandidateSearchAccuracyWrapper candidate) {
		return SavedCandidateAPIOutbound
				.builder()
					.userId(savedCandidate.getUserId())
					.candidateId(savedCandidate.getCandidateId())
					.notes(savedCandidate.getNotes())
					.candidate(CandidateSuggestionAPIOutbound.convertFromCandidate(candidate))
				.build();
	} 
	
}