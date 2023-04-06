package com.arenella.recruit.listings.adapters;

/**
* Command to send a request for an email to be sent
* to the owner of a Listing
* @author K Parkings
*/
public class CandidateRequestListingContactEmailCommand {

	private String		recruiterId;
	private String		listingName;
	private String		candidateId;
	private String		message;
	private byte[]		file;
	private String		fileType;
	
	/**
	* Constructor based upon a Builder
	* @param builder Contains initialization 
	*/
	public CandidateRequestListingContactEmailCommand(CandidateRequestListingContactEmailCommandBuilder builder) {
		
		this.recruiterId 	=	builder.recruiterId; 
		this.listingName 	=	builder.listingName;
		this.candidateId 	=	builder.candidateId;
		this.message		=	builder.message;
		this.file 			=	builder.file;
		this.fileType 		=	builder.fileType;
		
	}
	
	/**
	* Returns the unique id of the recruiter the 
	* email is to be sent to
	* @return id of recruiter
	*/
	public String getRecruiterId(){
		return this.recruiterId;
	}
	
	/**
	* Returns the name/title of the listing the 
	* email relates to
	* @return - name of the listing
	*/
	public String getListingName(){
		return this.listingName;
	}
	
	/**
	* Returns the id of the candidate who 
	* is sending the contact request to the recruiter
	* @return name of person
	*/
	public String getCandidateId(){
		return this.candidateId;
	}
	
	/**
	* Returns the message being sent to the Recruiter
	* @return body of message
	*/
	public String getMessage(){
		return this.message;
	}
	
	/**
	* Returns the attachment file bytes
	* @return bytes of the file
	*/
	public byte[] getFile(){
		return this.file;
	}
	
	/**
	* Returns the type for the attachment file
	* @return file type
	*/
	public String getFileType(){
		return this.fileType;
	}
	
	/**
	* Returns a builder for the RequestListingContactEmailCommand class
	* @return
	*/
	public static CandidateRequestListingContactEmailCommandBuilder builder() {
		return new CandidateRequestListingContactEmailCommandBuilder();
	}
	
	/**
	* Builder for the RequestListingContactEmailCommand class
	* @author K Parkings
	*/
	public static class CandidateRequestListingContactEmailCommandBuilder {
	
		private String		recruiterId;
		private String		listingName;
		private String		candidateId;
		private String		message;
		private byte[]		file;
		private String		fileType;
		
		/**
		* Sets the id of the Recruiter the Message is destined for
		* @param recruiterId - Id of the recruiter
		* @return Builder
		*/
		public CandidateRequestListingContactEmailCommandBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the name/title of the listing the message relates to
		* @param listingName - name of the listing
		* @return Builder
		*/
		public CandidateRequestListingContactEmailCommandBuilder listingName(String listingName) {
			this.listingName = listingName;
			return this;
		}
		
		/**
		* Sets the id of the candidate sending the message
		* @param candidateId - id of the Candidate
		* @return Builder
		*/
		public CandidateRequestListingContactEmailCommandBuilder candidateId(String candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets the message being sent
		* @param message - message to the recruiter
		* @return Builder
		*/
		public CandidateRequestListingContactEmailCommandBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Sets the attachment file
		* @param file - bytes of the attachment file
		* @return Builder
		*/
		public CandidateRequestListingContactEmailCommandBuilder file(byte[] file) {
			this.file = file;
			return this;
		}
		
		/**
		* Sets the file type of the attachment
		* @param fileType - type of the attachment
		* @return Builder
		*/
		public CandidateRequestListingContactEmailCommandBuilder fileType(String fileType) {
			this.fileType = fileType;
			return this;
		}
		
		/**
		* Returns an initialized instance of the RequestListingContactEmailCommand class 
		* @return initialized instance
		*/
		public CandidateRequestListingContactEmailCommand build() {
			return new CandidateRequestListingContactEmailCommand(this);
		}
		
	}
	
}