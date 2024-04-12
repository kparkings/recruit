package com.arenella.recruit.candidates.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.arenella.recruit.candidates.controllers.SavedCandidate;

/**
* Entity representation of SavedCandidate
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="saved_candidates")
public class SavedCandidateEntity {
 
	@EmbeddedId
	private SavedCandidateEntityId id = new SavedCandidateEntityId();
	
	@Column(name="notes")
	private String 	notes;
	
	/**
	* Required for Hibernate 
	*/
	public SavedCandidateEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a builder
	* @param builder - contains initialization values
	*/
	public SavedCandidateEntity(SavedCandidateEntityBuilder builder) {
		this.id.setUserId(builder.userId);
		this.id.setCandidateId(builder.candidateId);
		this.notes = builder.notes;
	}
	
	/**
	* Returns the Id of the User who has saved the Candidate
	* @return Unique Id of the User
	*/
	public String getUserId() {
		return this.id.getUserId();
	}
	
	/**
	* Returns the Id of the Candidate who has been saved by the User
	* @return Unique Id of the Candidate
	*/
	public long getCandidateId() {
		return this.id.getCandidateId();
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
	public static SavedCandidateEntityBuilder builder() {
		return new SavedCandidateEntityBuilder();
	}
	
	/**
	* Builder class for SavedCandidate
	* @author K Parkings
	*/
	public static class SavedCandidateEntityBuilder {
	
		private String 	userId;
		private long 	candidateId;
		private String 	notes;
		
		/**
		* Sets the Unique Id of the User who saved the Candidate
		* @param userId - Unique Id of the User
		* @return Builder
		*/
		public SavedCandidateEntityBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets the Unique Id of the Candidate who is being saved
		* by the User
		* @param candidateId - Unique id of the Candidate
		* @return Builder
		*/
		public SavedCandidateEntityBuilder candidateId(long candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets notes made by the User about the Candidate
		* @param notes	- Notes relating to the Candidate
		* @return Builder
		*/
		public SavedCandidateEntityBuilder	notes(String notes){
			this.notes = notes;
			return this;
		}
		
		/**
		* Returns an initialized instance of SavedCandidate
		* @return Instance of SavedCandidate
		*/
		public SavedCandidateEntity build() {
			return new SavedCandidateEntity(this);
		} 
		
	}

	/**
	* Converts from Entity representation to Domain representation
	* @param entity - Entity to convert
	* @return Domain representation
	*/
	public static SavedCandidate convertFromEntity(SavedCandidateEntity entity) {
		return SavedCandidate
				.builder()
					.candidateId(entity.getCandidateId())
					.userId(entity.getUserId())
					.notes(entity.getNotes())
				.build();
	}

	/**
	* Converts from Domain representation to Entity representation
	* @param entity - Entity to convert
	* @return Domain representation
	*/
	public static SavedCandidateEntity convertToEntity(SavedCandidate savedCandidate) {
		return SavedCandidateEntity
				.builder()
					.candidateId(savedCandidate.getCandidateId())
					.userId(savedCandidate.getUserId())
					.notes(savedCandidate.getNotes())
				.build();
	}
	
}