package com.arenella.recruit.candidates.beans;

import java.util.UUID;

import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Class represents a Candidate that has uploaded their details but not 
* yet been processed into the system. A Candidate that 
* can be place on a project
* @author K Parkings
*/
public class PendingCandidate {

	private UUID 			pendingCandidateId;
	private String 			firstname;
	private String 			surname;
	private String 			email;
	private PERM 			perm;
	private FREELANCE 		freelance;
	
	/**
	* Constructor based upon a builder
	* @param builder = Contains initialization information
	*/
	public PendingCandidate(PendingCandidateBuilder builder) {
		
		this.pendingCandidateId			= builder.pendingCandidateId;
		this.firstname					= builder.firstname;
		this.surname					= builder.surname;
		this.email						= builder.email;
		this.perm 						= builder.perm;
		this.freelance 					= builder.freelance;
		
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
	public FREELANCE isFreelance() {
		return this.freelance;
	}
	
	/**
	* Returns whether or not the Candidate is interested in permanent roles
	* @return Whether or not the Candidate is interested in permanent roles
	*/
	public PERM isPerm() {
		return this.perm;
	}
	
	/**
	* Builder for the PendingCandidate class
	* @return A Builder for the PendingCandidate class
	*/
	public static final PendingCandidateBuilder builder() {
		return new PendingCandidateBuilder();
	}
	
	/**
	* Builder Class for the PendingCandidate Class
	* @author K Parkings
	*/
	public static class PendingCandidateBuilder {
		
		private UUID 			pendingCandidateId;
		private String 			firstname;
		private String			surname;
		private String 			email;
		private PERM 			perm;
		private FREELANCE 		freelance;
		
		/**
		* Sets the candidates Unique identifier in the System
		* @param candidateId - Unique identifier of the Candidate
		* @return Builder
		*/
		public PendingCandidateBuilder pendingCandidateId(UUID pendingCandidateId) {
			this.pendingCandidateId = pendingCandidateId;
			return this;
		}
		
		/**
		* Sets the First name of the Candidate
		* @param firstname - Candidates first name
		* @return Builder
		*/
		public PendingCandidateBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Surname of the Candidate
		* @param surname - Candidates Surname
		* @return Builder
		*/
		public PendingCandidateBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address of the Candidate
		* @param email - Email address
		* @return Builder
		*/
		public PendingCandidateBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for perm roles
		* @param freelance - Whether or not the Candidate is interested in perm roles
		* @return Builder
		*/
		public PendingCandidateBuilder perm(PERM perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param freelance - Whether or not the Candidate is interested in freelance roles
		* @return Builder
		*/
		public PendingCandidateBuilder freelance(FREELANCE freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Returns an instance of PendingCandidate initialized with the 
		* values in the builder
		* @return Initialized instance of PendingCandidate
		*/
		public PendingCandidate build() {
			return new PendingCandidate(this);
		}
	}
	
}