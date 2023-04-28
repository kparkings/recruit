package com.arenella.recruit.candidates.beans;

import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Rate;

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
	private boolean 		perm;
	private boolean 		freelance;
	private String 			introduction;
	private Rate			rate;
	private Photo			photo;
	
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
		this.introduction				= builder.introduction;
		this.rate						= builder.rate;
		this.photo						= builder.photo;
		
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
	* Returns the Candidates Rate information
	* @return Rate information
	*/
	public Optional<Rate> getRate() {
		return Optional.ofNullable(this.rate);
	}
	
	/**
	* Returns the Candidates introduction to themselves and what
	* they are looking for
	* @return Intro
	*/
	public String getIntroduction() {
		return this.introduction;
	}
	
	/**
	* If available returns a photo of the Candidate
	* @return Profile Photo
	*/
	public Optional<Photo> getPhoto(){
		return Optional.ofNullable(this.photo);
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
		private boolean			perm;
		private boolean 		freelance;
		private String 			introduction;
		private Rate			rate;
		private Photo			photo;
		
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
		public PendingCandidateBuilder perm(boolean perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param freelance - Whether or not the Candidate is interested in freelance roles
		* @return Builder
		*/
		public PendingCandidateBuilder freelance(boolean freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets the Candidates Rate information
		* @param rate - Rate information
		* @return Builder
		*/
		public PendingCandidateBuilder rate(Rate rate) {
			this.rate = rate;
			return this;
		}
		
		/**
		* Sets the Candidate introduction to themselves
		* @param introduction - Introduction
		* @return Builder
		 */
		public PendingCandidateBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Sets Profile Photo for the Candidate
		* @param photo - Photo of the Candidate
		* @return Builde
		*/
		public PendingCandidateBuilder photo(Photo photo) {
			this.photo = photo;
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