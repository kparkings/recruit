package com.arenella.recruit.emailservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Contact;
import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.EmailAttachment;
import com.arenella.recruit.emailservice.beans.EmailAttachment.FileType;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Status;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.emailservice.controllers.EmailAPIOutbound.EmailAttachmentAPIOutbound;
import com.arenella.recruit.emailservice.controllers.EmailAPIOutbound.SenderAPIOutbound;

/**
* Unit tests for the EmailAPIOutbound class
* @author K Parkings
*/
public class EmailAPIOutboundTest {

	private final UUID 						recip1Id 					= UUID.randomUUID();
	private final UUID 						recip2Id 					= UUID.randomUUID();
	private final UUID 						id							= UUID.randomUUID();
	private final String 					title						= "aTitle";
	private final EmailType 				emailType					= EmailType.EXTERN;
	private final String					senderContactId				= "scid1";
	private final String					senderFirstName				= "Kevin";
	private final String					senderSurname				= "Parkings";
	private final Sender<?> 				sender						= new Sender<UUID>(UUID.randomUUID(), senderContactId, SenderType.RECRUITER, "norepy@renella-ict.com"); 
	private final LocalDateTime 			created						= LocalDateTime.of(2022,11,17, 19,11,00);
	private final LocalDateTime 			scheduledToBeSentAfter		= LocalDateTime.of(2022,11,17, 19,11,10);;
	private final LocalDateTime 			sent						= LocalDateTime.of(2022,11,17, 19,11,20);;
	private final String 					body						= "aBody";
	private final Status 					status						= Status.DRAFT;
	private final String					contactId1					= "cid1";
	private final String					contactId2					= "cid2";
	private final EmailRecipient<UUID>		emailRecip1					= new EmailRecipient<UUID>(recip1Id, contactId1, ContactType.RECRUITER);
	private final EmailRecipient<UUID>		emailRecip2					= new EmailRecipient<UUID>(recip2Id, contactId2, ContactType.SYSTEM);
	private final Set<EmailRecipient<UUID>> recipients					= Set.of(emailRecip1,emailRecip2);
	private final Set<EmailAttachment>		attachments					= Set.of(EmailAttachment.builder().name("cv").fileType(FileType.doc).build());
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		final UUID 								id				= UUID.randomUUID();
		final String 							title			= "title";
		final SenderAPIOutbound 				sender			= new SenderAPIOutbound("recruiter1", SenderType.RECRUITER, "Kevin ", "sender@mail.com");	 
		final LocalDateTime 					created			= LocalDateTime.of(2023, 02, 26, 16, 23, 00);
		final String 							body		 	= "body";
		final Set<EmailAttachmentAPIOutbound>	attachments		= new LinkedHashSet<>();
		final boolean							viewed			= true;
		
		EmailAPIOutbound email = EmailAPIOutbound
				.builder()
					.attachments(attachments)
					.body(body)
					.created(created)
					.id(id)
					.sender(sender)
					.title(title)
					.viewed(viewed)
				.build();
		
		assertEquals(id, 					email.getId());
		assertEquals(title, 				email.getTitle());
		assertEquals(sender, 				email.getSender());
		assertEquals(created, 				email.getCreated());
		assertEquals(body, 					email.getBody());
		assertEquals(attachments, 			email.getAttachments());
		assertEquals(viewed, 				email.isViewed());
		assertEquals(SenderType.RECRUITER, 	email.getSender().getSenderType());
		
	}
	
	/**
	* Tests conversion from Domain to APIOutbound representation
	* @throws Exception
	*/
	@Test
	void testConvertFromDomain_sender_contact_exists() {
		
		final String expectedSender = senderFirstName + " " + senderSurname;
		
		Email email = Email
				.builder()
					.body(this.body)
					.created(this.created)
					.emailType(this.emailType)
					.id(this.id)
					.recipients(this.recipients)
					.scheduledToBeSentAfter(this.scheduledToBeSentAfter)
					.sender(this.sender)
					.sent(this.sent)
					.status(this.status)
					.title(this.title)
					.attachments(attachments)
				.build();
		
		Set<Contact> contacts = Set.of(new Contact(senderContactId, ContactType.RECRUITER, senderFirstName, senderSurname, ""));
		
		EmailAPIOutbound out = EmailAPIOutbound.convertFromDomain(email, contacts, contactId1);
		
		assertEquals(id, 					out.getId());
		assertEquals(title, 				out.getTitle());
		assertEquals(expectedSender, 		out.getSender().getSenderName());
		assertEquals(created, 				out.getCreated());
		assertEquals(body, 					out.getBody());
		assertEquals(attachments.size(), 	out.getAttachments().size());
		assertEquals(SenderType.RECRUITER, 	out.getSender().getSenderType());
		
	}
	
	/**
	* Tests conversion from Domain to APIOutbound representation
	* @throws Exception
	*/
	@Test
	void testConvertFromDomain_sender_contact_does_not_exists() {
		
		Email email = Email
				.builder()
					.body(this.body)
					.created(this.created)
					.emailType(this.emailType)
					.id(this.id)
					.recipients(this.recipients)
					.scheduledToBeSentAfter(this.scheduledToBeSentAfter)
					.sender(this.sender)
					.sent(this.sent)
					.status(this.status)
					.title(this.title)
					.attachments(attachments)
				.build();
		
		Set<Contact> contacts = Set.of();
		
		EmailAPIOutbound out = EmailAPIOutbound.convertFromDomain(email, contacts, contactId1);
		
		assertEquals(id, 					out.getId());
		assertEquals(title, 				out.getTitle());
		assertEquals(senderContactId, 		out.getSender().getSenderName());
		assertEquals(created, 				out.getCreated());
		assertEquals(body, 					out.getBody());
		assertEquals(attachments.size(), 	out.getAttachments().size());
		assertEquals(SenderType.RECRUITER, 	out.getSender().getSenderType());
		
	}
	
}