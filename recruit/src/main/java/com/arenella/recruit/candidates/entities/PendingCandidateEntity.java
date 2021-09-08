package com.arenella.recruit.candidates.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.candidates.beans.PendingCandidate;

/**
* Represents a Candidate that has uploaded their details 
* but is not yet processed and added as a Candidate
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="pending_candidate")
public class PendingCandidateEntity {

	@Id
	@Column(name="pending_candidate_id")
	private UUID		pendingCandidateId;
	
	@Column(name="firstname")
	private String 		firstname;
	
	@Column(name="surname")
	private String 		surname;
	
	@Column(name="email")
	private String 		email;
	
	@Column(name="perm")
	private boolean 	perm;
	
	@Column(name="freelance")
	private boolean 	freelance;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization parameters
	*/
	public PendingCandidateEntity(PendingCandidateEntityBuilder builder) {
		
		this.pendingCandidateId 		= builder.pendingCandidateId;
		this.firstname					= builder.firstname;
		this.surname					= builder.surname;
		this.email						= builder.email;
		this.perm 						= builder.perm;
		this.freelance 					= builder.freelance;
		
	}
	
	/**
	* Default constructor 
	*/
	@SuppressWarnings("unused")
	private PendingCandidateEntity() {
		//Hibernate
	}
	
	/**
	* Returns the unique identifier of the Candidate
	* @return unique Id of the candidate
	*/
	public UUID getPendingCandidateId() {
		return this.pendingCandidateId;
	}
	
	/***
	* Returns the name of the Candidate
	* @return Candidates name
	*/
	public String getFirstname() {
		return this.firstname;
	}
	
	/***
	* Returns the surname of the Candidate
	* @return Candidates name
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns the Candidates Email address
	* @return Email address
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* Returns whether or not the Candidate is looking for
	* freelance projects
	* @return Whether or not the Candidate is interested in freelance roles
	*/
	public boolean isFreelance() {
		return this.freelance;
	}
	
	/**
	* Returns whether or not the Candidate is interested in permanent roles
	* @return Whether or not the Candidate is interested in permanent roles
	*/
	public boolean isPerm() {
		return this.perm;
	}
	
	/**
	* Returns a Builder for the PendingCandidateEntity class
	* @return Builder for the PendingCandidateEntity class
	*/
	public static PendingCandidateEntityBuilder builder() {
		return new PendingCandidateEntityBuilder();
	}
	
	/**
	* Builder for the PendingCandidateEntity class
	* @author K Parkings
	*/
	public static class PendingCandidateEntityBuilder {
	
		private UUID 			pendingCandidateId;
		private String 			firstname;
		private String			surname;
		private String 			email;
		private boolean	 		perm;
		private boolean 		freelance;
		
		/**
		* Sets the Unique Identifier of the Candidate
		* @param candidateId - UniqueId of the Candidate
		* @return Builder
		*/
		public PendingCandidateEntityBuilder pendingCandidateId(UUID pendingCandidateId) {
			this.pendingCandidateId = pendingCandidateId;
			return this;
		}
		
		/**
		* Sets the First name of the Candidate
		* @param firstname - Candidates first name
		* @return Builder
		*/
		public PendingCandidateEntityBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Surname of the Candidate
		* @param surname - Candidates Surname
		* @return Builder
		*/
		public PendingCandidateEntityBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address of the Candidate
		* @param email - Email address
		* @return Builder
		*/
		public PendingCandidateEntityBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for Perm roles
		* @param perm - Whether candidate is looking for Perm roles
		* @return Builder
		*/
		public PendingCandidateEntityBuilder perm(boolean perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param perm - Whether candidate is looking for freelance roles
		* @return Builder
		*/
		public PendingCandidateEntityBuilder freelance(boolean freelance) {
			this.freelance = freelance;
			return this;
		}
		
		
		/**
		* Returns a CandidateEntity instance initialized with 
		* the values in the Builder
		* @return Instance of CandidateEntity
		*/
		public PendingCandidateEntity build() {
			return new PendingCandidateEntity(this);
		}
		
	}
	
	/**
	* Converts a Domain representation of a Candidate Object to an Entity 
	* representation
	* @param candidate - Instance of Candidate to convert to an Entity
	* @return Entity representation of a Candidate object
	*/
	public static PendingCandidateEntity convertToEntity(PendingCandidate pendingCandidate) {
		
		return PendingCandidateEntity
					.builder()
						.pendingCandidateId(pendingCandidate.getPendingCandidateId())
						.firstname(pendingCandidate.getFirstname())
						.surname(pendingCandidate.getSurname())
						.email(pendingCandidate.getEmail())
						.freelance(pendingCandidate.isFreelance())
						.perm(pendingCandidate.isPerm())
						.build();
		
	}

	/**
	* Converts a Entity representation of a Candidate Object to an Domain 
	* representation
	* @param candidate - Instance of Candidate to convert
	* @return Domain representation of a Candidate object
	*/
	public static PendingCandidate convertFromEntity(PendingCandidateEntity pendingCandidateEntity) {
		
		return PendingCandidate
					.builder()
						.pendingCandidateId(pendingCandidateEntity.getPendingCandidateId())
						.firstname(pendingCandidateEntity.getFirstname())
						.surname(pendingCandidateEntity.getSurname())
						.email(pendingCandidateEntity.getEmail())
						.freelance(pendingCandidateEntity.isFreelance())
						.perm(pendingCandidateEntity.isPerm())
						.build();
		
	}
	
}