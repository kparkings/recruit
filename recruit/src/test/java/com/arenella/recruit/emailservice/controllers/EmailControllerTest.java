package com.arenella.recruit.emailservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Status;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.EmailAttachment;
import com.arenella.recruit.emailservice.beans.EmailAttachment.FileType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.emailservice.services.EmailService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
* Unit tests for the EmailController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {

	private static final String 				BODY 						= "body";
	private static final LocalDateTime 			CREATED 					= LocalDateTime.of(2023,03,03, 10,10,10);
	private static final EmailType				EMAIL_TYPE 					= EmailType.INTERN;
	private static final UUID					ID 							= UUID.randomUUID();
	private static final boolean				PERSISTABLE 				= true;
	private static final LocalDateTime 			SEND_AFTER 					= LocalDateTime.of(2023,03,04, 10,10,10);
	private static final String 				TITLE 						= "title";
	private static final LocalDateTime 			SENT 						= LocalDateTime.of(2023,03,04, 10,10,20);
	private static final Email.Status			STATUS 						= Status.SENT_INTERN;
	private static final String 				SENDER_ID 					= "1234566788";
	private static final String 				CONTACT_ID 					= "kparkings";
	private static final SenderType 			CONTACT_TYPE 				= SenderType.RECRUITER;
	private static final String 				SENDER_EMAIL 				= "kparkings@gmail.com";
	private static final Sender<String>			SENDER 						= new Sender<>(SENDER_ID, CONTACT_ID, CONTACT_TYPE, SENDER_EMAIL);
	private static final UUID 					ATTACHMENT_ID 				= UUID.randomUUID();
	private static final FileType 				FILE_TYPE 					= FileType.doc;
	private static final byte[] 				FILE_BYTES 					= new byte[] {1,8,9};
	private static final String					ATTACHMENT_NAME				= "cv";
	private static final UUID					RECIPIENT_ID				= UUID.randomUUID();
	private static final String					RECIPIENT_CONTACT_ID		= "kparkings2";
	private static final ContactType			RECIPIENT_TYPE				= ContactType.RECRUITER;
	private static final EmailRecipient<UUID>	EMAIL_RECIPIENT				= new EmailRecipient<>(RECIPIENT_ID, RECIPIENT_CONTACT_ID, RECIPIENT_TYPE);
	private static final EmailAttachment 		EMAIL_ATTACHMENT 			= EmailAttachment
			.builder()
				.attachmentId(ATTACHMENT_ID)
				.emailId(ID)
				.fileBytes(FILE_BYTES)
				.fileType(FILE_TYPE)
				.name(ATTACHMENT_NAME)
			.build();
	
	@Mock
	private EmailService 		mockEmailService;
	
	@Mock
	private Principal			mockPrincipal;
	
	@InjectMocks
	private EmailController 	emailController;
	
	/**
	* Tests retrieval of email's
	* @throws Exception
	*/
	@Test
	public void testGetEmailsForUser() throws Exception{
		
		Mockito.when(this.mockPrincipal.getName()).thenReturn(RECIPIENT_CONTACT_ID);
		
		Mockito.when(this.mockEmailService.fetchEmailsByRecipientId(Mockito.anyString()))
			.thenReturn(Set.of(Email
				.builder()
					.body(BODY)
					.created(CREATED)
					.emailType(EMAIL_TYPE)
					.recipients(Set.of(EMAIL_RECIPIENT))
					.id(ID)
					.persistable(PERSISTABLE)
					.scheduledToBeSentAfter(SEND_AFTER)
					.sender(SENDER)
					.sent(SENT)
					.status(STATUS)
					.title(TITLE)
					.attachments(Set.of(EMAIL_ATTACHMENT))
					.build()));
		
		ResponseEntity<Set<EmailAPIOutbound>> response = this.emailController.getEmailsForUser(mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		EmailAPIOutbound email = response.getBody().stream().findFirst().get();
		
		assertEquals(BODY, 								email.getBody());
		assertEquals(CREATED, 							email.getCreated());
		assertEquals(ID, 								email.getId());
		assertEquals(TITLE, 							email.getTitle());
		assertEquals(SENDER_EMAIL, 						email.getSender().getEmail());
		assertEquals(SENDER_ID, 						email.getSender().getId());
		assertEquals(CONTACT_ID, 						email.getSender().getSenderName());
		assertEquals(ATTACHMENT_ID, 					email.getAttachments().stream().findFirst().get().getId());
		assertEquals(ATTACHMENT_NAME+ "." + FILE_TYPE, 	email.getAttachments().stream().findFirst().get().getName());
		
	} 
	
	/**
	* Tests retrieval of an Attachment for an Email
	* @throws Exception
	*/
	@Test
	public void testGetEmailAttachment() throws Exception{
		
		Mockito.when(this.mockPrincipal.getName()).thenReturn(RECIPIENT_CONTACT_ID);
		Mockito.when(this.mockEmailService.fetchAttachment(ID, ATTACHMENT_ID, RECIPIENT_CONTACT_ID)).thenReturn(EMAIL_ATTACHMENT);
		
		ResponseEntity<ByteArrayResource> response = this.emailController.getEmailAttachment(ID, ATTACHMENT_ID, mockPrincipal);
		
		assertEquals(new MediaType("application", "force-download"), response.getHeaders().getContentType());
		
		assertEquals(FILE_BYTES.length, response.getBody().getByteArray().length);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		
	}
	
	/**
	* Test response from marking Email as read
	* @throws Exception
	*/
	@Test
	public void testMarkAsRead() throws Exception{
		
		final UUID emailId = UUID.randomUUID();
		
		Mockito.when(this.mockPrincipal.getName()).thenReturn(RECIPIENT_CONTACT_ID);
		Mockito.doNothing().when(this.mockEmailService).setEmailReadStatus(emailId, true, RECIPIENT_CONTACT_ID);
		
		this.emailController.markAsRead(emailId, this.mockPrincipal);
		
		Mockito.verify(this.mockEmailService).setEmailReadStatus(emailId, true, RECIPIENT_CONTACT_ID);
		
	}
	
	/**
	* Test response from marking Email as unread
	* @throws Exception
	*/
	@Test
	public void testMarkAsUnRead() throws Exception{
		
		final UUID emailId = UUID.randomUUID();
		
		Mockito.when(this.mockPrincipal.getName()).thenReturn(RECIPIENT_CONTACT_ID);
		Mockito.doNothing().when(this.mockEmailService).setEmailReadStatus(emailId, false, RECIPIENT_CONTACT_ID);
		
		this.emailController.markAsUndread(emailId, mockPrincipal);
		
		Mockito.verify(this.mockEmailService).setEmailReadStatus(emailId, false, RECIPIENT_CONTACT_ID);
		
	}
	
}