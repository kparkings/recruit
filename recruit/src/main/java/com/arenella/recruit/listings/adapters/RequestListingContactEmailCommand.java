package com.arenella.recruit.listings.adapters;

/**
* Command to send a request for an email to be sent
* to the owner of a Listing
* @author K Parkings
*/
public class RequestListingContactEmailCommand {

	private String		recruiterId;
	private String		listingName;
	private String		senderName;
	private String		senderEmail;
	private String		message;
	private byte[]		file;
	private String		fileType;
	
	/**
	* Constructor based upon a Builder
	* @param builder Contains initialization 
	*/
	public RequestListingContactEmailCommand(RequestListingContactEmailCommandBuilder builder) {
		
		this.recruiterId 	=	builder.recruiterId; 
		this.listingName 	=	builder.listingName;
		this.senderName 	=	builder.senderName;
		this.senderEmail 	=	builder.senderEmail;
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
	* Returns the name of the person who 
	* is sending the contact request to the recruiter
	* @return name of person
	*/
	public String getSenderName(){
		return this.senderName;
	}
	
	/**
	* Returns the email of the person who 
	* is sending the contact request to the recruiter
	* @return email of person
	*/
	public String getSenderEmail(){
		return this.senderEmail;
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
	public static RequestListingContactEmailCommandBuilder builder() {
		return new RequestListingContactEmailCommandBuilder();
	}
	
	/**
	* Builder for the RequestListingContactEmailCommand class
	* @author K Parkings
	*/
	public static class RequestListingContactEmailCommandBuilder {
	
		private String		recruiterId;
		private String		listingName;
		private String		senderName;
		private String		senderEmail;
		private String		message;
		private byte[]		file;
		private String		fileType;
		
		/**
		* Sets the id of the Recruiter the Message is destined for
		* @param recruiterId - Id of the recruiter
		* @return Builder
		*/
		public RequestListingContactEmailCommandBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the name/title of the listing the message relates to
		* @param listingName - name of the listing
		* @return Builder
		*/
		public RequestListingContactEmailCommandBuilder listingName(String listingName) {
			this.listingName = listingName;
			return this;
		}
		
		/**
		* Sets the name of the person sending the message
		* @param senderName - name of the person
		* @return Builder
		*/
		public RequestListingContactEmailCommandBuilder senderName(String senderName) {
			this.senderName = senderName;
			return this;
		}
		
		/**
		* Sets the email of the person sending the message
		* @param senderEmail - email address
		* @return Builder
		*/
		public RequestListingContactEmailCommandBuilder senderEmail(String senderEmail) {
			this.senderEmail = senderEmail;
			return this;
		}
		
		/**
		* Sets the message being sent
		* @param message - message to the recruiter
		* @return Builder
		*/
		public RequestListingContactEmailCommandBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Sets the attachment file
		* @param file - bytes of the attachment file
		* @return Builder
		*/
		public RequestListingContactEmailCommandBuilder file(byte[] file) {
			this.file = file;
			return this;
		}
		
		/**
		* Sets the file type of the attachment
		* @param fileType - type of the attachement
		* @return Builder
		*/
		public RequestListingContactEmailCommandBuilder fileType(String fileType) {
			this.fileType = fileType;
			return this;
		}
		
		/**
		* Returns an initialized instance of the RequestListingContactEmailCommand class 
		* @return initialized instance
		*/
		public RequestListingContactEmailCommand build() {
			return new RequestListingContactEmailCommand(this);
		}
		
	}
	
}