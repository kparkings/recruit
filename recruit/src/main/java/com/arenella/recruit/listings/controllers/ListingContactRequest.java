package com.arenella.recruit.listings.controllers;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

/**
* API In-bound representation of a Contact Request 
* from a Candidate applying for to a Listing
* @author K Parkings
*/
public class ListingContactRequest {

	private UUID			listingId;
	private MultipartFile 	attachment;
	private String 			senderName;
	private String 			senderEmail;
	private String 			message;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public ListingContactRequest(ListingContactRequestBuilder builder) {
		this.listingId		= builder.listingId;
		this.attachment		= builder.attachment;
		this.senderName 	= builder.senderName;
		this.senderEmail 	= builder.senderEmail;
		this.message 		= builder.message;
	}
	
	/**
	* Return the attachment
	* @return attachment
	*/
	public UUID getListingId() {
		return this.listingId;
	}
	
	/**
	* Return the attachment
	* @return attachment
	*/
	public MultipartFile getAttachment() {
		return this.attachment;
	}
	
	/**
	* Return the name of the Sender
	* @return Senders name
	*/
	public String getSenderName() {
		return this.senderName;
	}
	
	/**
	* Returns the email address of the sender
	* @return senders email address
	*/
	public String getSenderEmail() {
		return this.senderEmail;
	}
	
	/**
	* Returns senders message to Listing owner
	* @return message to Listing owner
	*/
	public String getMessage() {
		return this.message;
	}
	
	/**
	* Return a Builder for the ListingContactRequest class
	* @return
	*/
	public static ListingContactRequestBuilder builder() {
		return new ListingContactRequestBuilder();
	}
	
	/**
	* Builder for the ListingContactRequestAPIInbound class
	* @author K Parkings
	*/
	public static class ListingContactRequestBuilder{
		
		private UUID			listingId;
		private MultipartFile	attachment;
		private String 			senderName;
		private String 			senderEmail;
		private String 			message;
		
		/**
		* Sets the unique identifier of the Listing
		* @param listingId - id of the Listing
		* @return Builder
		*/
		public ListingContactRequestBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the attachment to send to Listing owner
		* @param attachment - attachment
		* @return Builder
		*/
		public ListingContactRequestBuilder attachment(MultipartFile attachment) {
			this.attachment = attachment;
			return this;
		}
		
		/**
		* Sets the name of the Sender of the Contact Request
		* @param senderName - Name of Sender
		* @return Builder
		*/
		public ListingContactRequestBuilder senderName(String senderName) {
			this.senderName = senderName;
			return this;
		}
		
		/**
		* Sets the senders email
		* @param senderEmail - Email address of the Sender
		* @return Builder
		*/
		public ListingContactRequestBuilder senderEmail(String senderEmail) {
			this.senderEmail = senderEmail;
			return this;
		}
		
		/**
		* Sets the message to be sent to the Listing owner
		* @param message - Message to Listing owner
		* @return Builder
		*/
		public ListingContactRequestBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Returns initialized ListingContactRequest
		* @return Initialized ListingContactRequest
		*/
		public ListingContactRequest build() {
			return new ListingContactRequest(this);
		}
		
	}
	
}