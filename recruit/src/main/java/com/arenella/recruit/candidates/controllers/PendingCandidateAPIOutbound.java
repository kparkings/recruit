package com.arenella.recruit.candidates.controllers;

import java.util.UUID;

import com.arenella.recruit.candidates.beans.PendingCandidate;

/**
* Representation of a PendingCandidate. This is a specialist 
* version meant to be made available to consumers via the 
* REST Api.
* @author K Parkings
*/
public class PendingCandidateAPIOutbound {

	private UUID 			pendingCandidateId;
	private String 			firstname;
	private String 			surname;
	private String 			email;
	private boolean 		perm;
	private boolean 		freelance;
	
	/**
	* Constructor based upon a builder
	* @param builder = Contains initialization information
	*/
	public PendingCandidateAPIOutbound(PendingCandidateAPIOutboundBuilder builder) {
		
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
	public boolean  isFreelance() {
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
	* Builder for the PendingCandidateAPIOutbound class
	* @return A Builder for the Candidate class
	*/
	public static final PendingCandidateAPIOutboundBuilder builder() {
		return new PendingCandidateAPIOutboundBuilder();
	}
	
	/**
	* Builder Class for the PendingCandidateAPIOutnbound Class
	* @author K Parkings
	*/
	public static class PendingCandidateAPIOutboundBuilder {
		
		private UUID 			pendingCandidateId;
		private String 			firstname;
		private String			surname;
		private String 			email;
		private boolean 		perm;
		private boolean 		freelance;
		
		/**
		* Sets the candidates Unique identifier in the System
		* @param candidateId - Unique identifier of the Candidate
		* @return Builder
		*/
		public PendingCandidateAPIOutboundBuilder pendingCandidateId(UUID pendingCandidateId) {
			this.pendingCandidateId = pendingCandidateId;
			return this;
		}
		
		/**
		* Sets the First name of the Candidate
		* @param firstname - Candidates first name
		* @return Builder
		*/
		public PendingCandidateAPIOutboundBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Surname of the Candidate
		* @param surname - Candidates Surname
		* @return Builder
		*/
		public PendingCandidateAPIOutboundBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address of the Candidate
		* @param email - Email address
		* @return Builder
		*/
		public PendingCandidateAPIOutboundBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		
		/**
		* Sets whether the Candidate is looking for perm roles
		* @param freelance - Whether or not the Candidate is interested in perm roles
		* @return Builder
		*/
		public PendingCandidateAPIOutboundBuilder perm(boolean perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param freelance - Whether or not the Candidate is interested in freelance roles
		* @return Builder
		*/
		public PendingCandidateAPIOutboundBuilder freelance(boolean freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Returns an instance of Candidate initialized with the 
		* values in the builder
		* @return Initialized instance of Candidate
		*/
		public PendingCandidateAPIOutbound build() {
			return new PendingCandidateAPIOutbound(this);
		}
	}
	
	/**
	* Converts an incoming API representation of a Canidate to the 
	* Domain version
	* @param candiateAPIInbound - API Incoming version of the Candidate
	* @return Domain version of the Candidate
	*/
	public static PendingCandidateAPIOutbound convertFromPendingCandidate(PendingCandidate pendingCandidate) {
		
		return PendingCandidateAPIOutbound
				.builder()
					.pendingCandidateId(pendingCandidate.getPendingCandidateId())
					.firstname(pendingCandidate.getFirstname())
					.surname(pendingCandidate.getSurname())
					.email(pendingCandidate.getEmail())
					.freelance(pendingCandidate.isFreelance())
					.perm(pendingCandidate.isPerm())
				.build();
		
	}
}
