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
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.emailservice.beans.EmailAttachment;
import com.arenella.recruit.emailservice.services.EmailService;

/**
* REST API for working with Email's
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
	 * @throws IOException 
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@GetMapping(
			  value = "/email/{emailId}/attachment/{attachmentId}",
			  produces = MediaType.APPLICATION_PDF_VALUE
			)
	public ResponseEntity<ByteArrayResource> getEmailAttachment(@PathVariable("emailId")UUID emailId, @PathVariable("attachmentId") UUID attachmentId, Principal principal) throws IOException {
		
		EmailAttachment attachment = this.emailService.fetchAttachment(emailId, attachmentId, principal.getName());
		
		ByteArrayOutputStream 	stream 		= new ByteArrayOutputStream(attachment.getFileBytes().length);
		
		stream.write(attachment.getFileBytes());
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+attachment.getName()+"." + attachment.getFileType());
		
		return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), header, HttpStatus.OK);
	
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