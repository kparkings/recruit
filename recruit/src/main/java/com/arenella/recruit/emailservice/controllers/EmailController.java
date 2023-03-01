package com.arenella.recruit.emailservice.controllers;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.emailservice.services.EmailService;

/**
* REST API for working with Emails
* @author K Parkings
*/
@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;
	
	/**
	* Returns the users email's
	* @return Email's for the authenticated User
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@GetMapping(path="email", produces="application/json")
	public Set<EmailAPIOutbound> getEmailsForUser(Principal principal){
		return this.emailService.fetchEmailsByRecipientId(principal.getName()).stream().map(e -> EmailAPIOutbound.convertFromDomain(e)).collect(Collectors.toCollection(LinkedHashSet::new));
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