package com.arenella.recruit.listings.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

/**
* Contact Request from an Authenticated Candidate
* @author K Parkings
*/
public class CandidateListingContactRequest {

	private String 			candidateId;
	private UUID			listingId;
	private String			message;
	private MultipartFile	attachment;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public CandidateListingContactRequest(CandidateListingContactRequestBuilder builder) {
		this.candidateId	= builder.candidateId;
		this.listingId		= builder.listingId;
		this.message		= builder.message;
		this.attachment		= builder.attachment;;
		
	}
	
	/**
	* Returns the unique id of the candidate sending
	* the request
	* @return id of the Candidate
	*/
	public String getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns the unique if of the listing
	* @return id of listing
	*/
	public UUID	getListingId() {
		return this.listingId;
	}
	
	/**
	* Returns the message to the owner of the
	* Listing
	* @return message to owning Recruiter
	*/
	public String getMessage() {
		return this.message;
	}
	
	/**
	* Returns the uploaded attachment if provides
	* @return File
	*/
	public Optional<MultipartFile> getAttachment(){
		return Optional.ofNullable(this.attachment);
	}
	
	/**
	* Returns a Builder for the class
	* @return builder
	*/
	public static CandidateListingContactRequestBuilder builder() {
		return new CandidateListingContactRequestBuilder();
	}
	
	/**
	* Builder for the Class
	* @author K Parkings
	*/
	public static class CandidateListingContactRequestBuilder{
	
		private String 			candidateId;
		private UUID			listingId;
		private String			message;
		private MultipartFile	attachment;
		
		/**
		* Sets the id of the Candidate sending the request
		* @param candidateId - Id of the candidate
		* @return Builder
		*/
		public CandidateListingContactRequestBuilder candidateId(String candidateId){
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets the unique id of the Listing the Candidate is contacting the 
		* Recruiter about
		* @param listingId - Unique id of the Listing
		* @return Builder
		*/
		public CandidateListingContactRequestBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the message to the Recruiter
		* @param message from Candidate to Recruiter
		* @return Builder
		*/
		public CandidateListingContactRequestBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Sets the attachment uploaded by the Candidate
		* @param attachment - uploaded attachment
		* @return Builder
		 */
		public CandidateListingContactRequestBuilder attachment(MultipartFile attachment) {
			this.attachment = attachment;
			return this;
		}
		
		/**
		* Returns an initialized instance
		* @return
		*/
		public CandidateListingContactRequest build() {
			return new CandidateListingContactRequest(this);
		}
		
	}
}