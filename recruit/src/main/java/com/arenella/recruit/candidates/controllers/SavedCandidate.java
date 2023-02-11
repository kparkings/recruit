package com.arenella.recruit.candidates.controllers;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Domain representation of SavedCandidate
* @author K Parkings
*/
public class SavedCandidate {

	private String 	userId;
	private long 	candidateId;
	private String 	notes;
	
	/**
	* Constructor based upon a builder
	* @param builder - contains initialization values
	*/
	public SavedCandidate(SavedCandidateBuilder builder) {
		this.userId 		= builder.userId;
		this.candidateId 	= builder.candidateId;
		this.notes 			= builder.notes;
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
	* Returns a Builder for the SavedCandidate class
	* @return Builder
	*/
	public static SavedCandidateBuilder builder() {
		return new SavedCandidateBuilder();
	}
	
	/**
	* Builder class for SavedCandidate
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class SavedCandidateBuilder {
	
		private String 	userId;
		private long 	candidateId;
		private String 	notes;
		
		/**
		* Sets the Unique Id of the User who saved the Candidate
		* @param userId - Unique Id of the User
		* @return Builder
		*/
		public SavedCandidateBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets the Unique Id of the Candidate who is being saved
		* by the User
		* @param candidateId - Unique id of the Candidate
		* @return Builder
		*/
		public SavedCandidateBuilder candidateId(long candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets notes made by the User about the Candidate
		* @param notes	- Notes relating to the Candidate
		* @return Builder
		*/
		public SavedCandidateBuilder	notes(String notes){
			this.notes = notes;
			return this;
		}
		
		/**
		* Returns an initialized instance of SavedCandidate
		* @return Instance of SavedCandidate
		*/
		public SavedCandidate build() {
			return new SavedCandidate(this);
		} 
		
	}
	
}