package com.arenella.recruit.emailservice.controllers;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;

/**
* REST API for working with Emails
* @author K Parkings
*/
@RestController
public class EmailController {

	/**
	* Returns the users email's
	* @return Emails for the authenticated User
	*/
	public Set<EmailAPIOutbound> getEmailsForUser(){
		return null;
	}
	
	/**
	* Returns an attachment
	* @param attachmntId - Id of attachment to return
	* @return attachment
	*/
	public Void getEmailAttachment(UUID attachmentId, Principal principal) {
		return null;
	}
	
	/**
	* Marks an email as having been read by the use
	* @param emailId - id of the Email
	*/
	public void markAsRead(UUID emailId) {
		
	}
	
	/**
	* Marks and email as not having been read 
	* @param emailId
	*/
	public void markAsUndread(UUID emailId) {
		
	}
	
	/**
	* Deletes an email
	* @param emailId - id of the email to delete
	*/
	public void deleteEmail(UUID emailId) {
		
	}
	
}