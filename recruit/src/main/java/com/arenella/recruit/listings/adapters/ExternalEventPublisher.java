package com.arenella.recruit.listings.adapters;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;

/**
* Defines functionality for publishing Events to external services 
* @author K Parkings
*/
public interface ExternalEventPublisher {

	/**
	* Requests an email is sent to the recruiter who owns the listing 
	* @param command - Command containing details for email
	*/
	public void publicRequestSendListingContactEmailCommand(RequestListingContactEmailCommand command);

	/**
	* Requests an email is sent to the recruiter who owns the listing from an authenticated Candidate
	* @param command - Command containing details for email
	*/
	public void publicRequestSendListingContactEmailCommand(CandidateRequestListingContactEmailCommand command);
	
	/**
	* Sends command to send email to Candidate to inform them their is a new listing Matching 
	* the Alert the created
	* @param command - contains details about the Hit
	*/
	public void RequestSendListingAlertHitEmailCommand(RequestSendEmailCommand command);

}
