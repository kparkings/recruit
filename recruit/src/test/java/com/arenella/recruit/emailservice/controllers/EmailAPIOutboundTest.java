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

	final private UUID 						recip1Id 					= UUID.randomUUID();
	final private UUID 						recip2Id 					= UUID.randomUUID();
	final private UUID 						id							= UUID.randomUUID();
	final private String 					title						= "aTitle";
	final private EmailType 				emailType					= EmailType.EXTERN;
	final private String					senderContactId				= "scid1";
	final private String					senderFirstName				= "Kevin";
	final private String					senderSurname				= "Parkings";
	final private Sender<?> 				sender						= new Sender<UUID>(UUID.randomUUID(), senderContactId, SenderType.RECRUITER, "norepy@renella-ict.com"); 
	final private LocalDateTime 			created						= LocalDateTime.of(2022,11,17, 19,11,00);
	final private LocalDateTime 			scheduledToBeSentAfter		= LocalDateTime.of(2022,11,17, 19,11,10);;
	final private LocalDateTime 			sent						= LocalDateTime.of(2022,11,17, 19,11,20);;
	final private String 					body						= "aBody";
	final private Status 					status						= Status.DRAFT;
	final private String					contactId1					= "cid1";
	final private String					contactId2					= "cid2";
	final private EmailRecipient<UUID>		emailRecip1					= new EmailRecipient<UUID>(recip1Id, contactId1, ContactType.RECRUITER);
	final private EmailRecipient<UUID>		emailRecip2					= new EmailRecipient<UUID>(recip2Id, contactId2, ContactType.SYSTEM);
	final private Set<EmailRecipient<UUID>> recipients					= Set.of(emailRecip1,emailRecip2);
	final private Set<EmailAttachment>		attachments					= Set.of(EmailAttachment.builder().name("cv").fileType(FileType.doc).build());
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
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
	public void testConvertFromDomain_sender_contact_exists() throws Exception{
		
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
	public void testConvertFromDomain_sender_contact_does_not_exists() throws Exception{
		
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
