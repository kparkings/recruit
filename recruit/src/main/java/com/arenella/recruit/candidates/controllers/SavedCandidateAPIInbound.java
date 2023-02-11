package com.arenella.recruit.candidates.controllers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* API In-bound representation of SavedCandidate a Candidate
* a user has chosen to add to their saved Candidates list
* @author K Parkings
*/
@JsonDeserialize(builder=SavedCandidateAPIInbound.SavedCandidateAPIInboundBuilder.class)
public class SavedCandidateAPIInbound {

	private long 	candidateId;
	private String 	notes;
	
	/**
	* Constructor based upon a builder
	* @param builder - contains initialization values
	*/
	public SavedCandidateAPIInbound(SavedCandidateAPIInboundBuilder builder) {
		this.candidateId 	= builder.candidateId;
		this.notes 			= builder.notes;
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
	* Returns a Builder for the SavedCandidateAPIInbound class
	* @return Builder
	*/
	public static SavedCandidateAPIInboundBuilder builder() {
		return new SavedCandidateAPIInboundBuilder();
	}
	
	/**
	* Builder class for SavedCandidateAPIInbound
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class SavedCandidateAPIInboundBuilder {
	
		private long 	candidateId;
		private String 	notes;
		
		/**
		* Sets the Unique Id of the Candidate who is being saved
		* by the User
		* @param candidateId - Unique id of the Candidate
		* @return Builder
		*/
		public SavedCandidateAPIInboundBuilder candidateId(long candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets notes made by the User about the Candidate
		* @param notes	- Notes relating to the Candidate
		* @return Builder
		*/
		public SavedCandidateAPIInboundBuilder	notes(String notes){
			this.notes = notes;
			return this;
		}
		
		/**
		* Returns an initialized instance of SavedCandidateAPIInbound
		* @return INstance of SavedCandidateAPIInbound
		*/
		public SavedCandidateAPIInbound build() {
			return new SavedCandidateAPIInbound(this);
		} 
		
	}
	
	/**
	* Converts from API In-bound representation of SavedCandidate to Domain representation
	* @param savedCandidateAPIInbound - API In-bound representation
	* @return Domain representation
	*/
	public static SavedCandidate convertToDomain(SavedCandidateAPIInbound savedCandidateAPIInbound, String userId) {
		return SavedCandidate
				.builder()
					.userId(userId)
					.candidateId(savedCandidateAPIInbound.getCandidateId())
					.notes(savedCandidateAPIInbound.getNotes())
				.build();
	} 
	
}