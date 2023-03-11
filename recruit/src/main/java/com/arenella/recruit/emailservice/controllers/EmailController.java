package com.arenella.recruit.emailservice.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.emailservice.beans.Contact;
import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.EmailAttachment;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.emailservice.services.ContactService;
import com.arenella.recruit.emailservice.services.EmailService;

/**
* REST API for working with Email's
* @author K Parkings
*/
@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ContactService	contactService;
	
	/**
	* Returns the users email's
	* @return Email's for the authenticated User
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@GetMapping(path="email", produces="application/json")
	public ResponseEntity<Set<EmailAPIOutbound>> getEmailsForUser(Principal principal){
	
		Set<Email> 		emails 		= this.emailService.fetchEmailsByRecipientId(principal.getName());
		Set<String> 	contactIds 	= emails.stream().filter(e -> e.getSender().getContactType() == SenderType.RECRUITER).map(e -> e.getSender().getContactId()).collect(Collectors.toSet());
		Set<Contact> 	contacts 	= this.contactService.fetchContacts(contactIds);
		
		Set<EmailAPIOutbound> emailsAPIOutbound =  this.emailService.fetchEmailsByRecipientId(principal.getName()).stream().map(e -> EmailAPIOutbound.convertFromDomain(e, contacts)).collect(Collectors.toCollection(LinkedHashSet::new));
	
		return new ResponseEntity<>(emailsAPIOutbound, HttpStatus.OK);
		
	}
	
	/**
	* 
	* @param emailId		- Unique id of the email the attachment belongs to
	* @param attachmentId	- Unique id of the attachment
	* @param principal		- Authenticated User
	* @return Attachment 
	* @throws IOException
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@GetMapping(value = "/email/{emailId}/attachment/{attachmentId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<ByteArrayResource> getEmailAttachment(@PathVariable("emailId")UUID emailId, @PathVariable("attachmentId") UUID attachmentId, Principal principal) throws IOException {
		
		EmailAttachment 		attachment 	= this.emailService.fetchAttachment(emailId, attachmentId, principal.getName());
		ByteArrayOutputStream 	stream 		= new ByteArrayOutputStream(attachment.getFileBytes().length);
		HttpHeaders 			header 		= new HttpHeaders();
		
		header.setContentType(new MediaType("application", "force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+attachment.getName()+"." + attachment.getFileType());
		
		stream.write(attachment.getFileBytes());
		
		return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), header, HttpStatus.OK);
	
	}
	
	/**
	* Marks an email as having been read by the use
	* @param emailId - id of the Email
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PutMapping(value="/email/{emailId}/read", produces="application/json")
	public void markAsRead(@PathVariable("emailId") UUID emailId, Principal principal) {
		this.emailService.setEmailReadStatus(emailId, true, principal.getName());
	}
	
	/**
	* Marks an email as not having been read 
	* @param emailId
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PutMapping(value="/email/{emailId}/unread", produces="application/json")
	public void markAsUndread(@PathVariable("emailId") UUID emailId, Principal principal) {
		this.emailService.setEmailReadStatus(emailId, false, principal.getName());
	}
	
	/**
	* Deletes an email
	* @param emailId - id of the email to delete
	*/
	public void deleteEmail(UUID emailId) {
		
	}
	
}