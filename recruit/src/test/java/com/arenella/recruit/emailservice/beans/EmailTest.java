package com.arenella.recruit.emailservice.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.emailservice.beans.Email.Status;

/**
* Unit tests for the Email class
* @author K Parkings
*/
public class EmailTest {

	final private String					recip1Address				= "norepy1@renella-ict.com";
	final private String					recip2Address				= "norepy2@renella-ict.com";
	final private UUID 						recip1Id 					= UUID.randomUUID();
	final private UUID 						recip2Id 					= UUID.randomUUID();
	final private UUID 						id							= UUID.randomUUID();
	final private String 					title						= "aTitle";
	final private EmailType 				emailType					= EmailType.EXTERN;
	final private String					firstNameRecip1				= "Kevin1";
	final private String					firstNameRecip2				= "Kevin2";
	final private String					senderContactId				= "scid1";
	final private Sender<?> 				sender						= new Sender<UUID>(UUID.randomUUID(), senderContactId, SenderType.SYSTEM, "norepy@renella-ict.com"); 
	final private LocalDateTime 			created						= LocalDateTime.of(2022,11,17, 19,11,00);
	final private LocalDateTime 			scheduledToBeSentAfter		= LocalDateTime.of(2022,11,17, 19,11,10);;
	final private LocalDateTime 			sent						= LocalDateTime.of(2022,11,17, 19,11,20);;
	final private String 					body						= "aBody";
	final private Status 					status						= Status.DRAFT;
	final private String					contactId1					= "cid1";
	final private String					contactId2					= "cid2";
	final private EmailRecipient<UUID>		emailRecip1					= new EmailRecipient<UUID>(recip1Id,   contactId1, ContactType.RECRUITER);
	final private EmailRecipient<UUID>		emailRecip2					= new EmailRecipient<UUID>(recip2Id, contactId2, ContactType.SYSTEM);
	final private Set<EmailRecipient<UUID>> recipients					= Set.of(emailRecip1,emailRecip2);
	final private Set<EmailAttachment>		attachments					= Set.of(EmailAttachment.builder().build());
	
	/**
	* Sets up test env
	*/
	@BeforeEach
	public void init() {
		emailRecip1.setEmail(recip1Address);
		emailRecip1.setFirstName(firstNameRecip1);
		emailRecip2.setEmail(recip2Address);
		emailRecip2.setFirstName(firstNameRecip2);
	}
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
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
		
		assertEquals(id, 						email.getId());
		assertEquals(title, 					email.getTitle());
		assertEquals(emailType, 				email.getEmailType());
		assertEquals(sender, 					email.getSender());
		assertEquals(created, 					email.getCreated());
		assertEquals(scheduledToBeSentAfter, 	email.getScheduledToBeSentAfter());
		assertEquals(sent, 						email.getSent());
		assertEquals(body, 						email.getBody());
		assertEquals(status, 					email.getStatus());
		
		assertEquals(sender.getId(), 			email.getSender().getId());
		assertEquals(sender.getEmail(), 		email.getSender().getEmail());
		assertEquals(sender.getContactType(), 	email.getSender().getContactType());
		assertEquals(sender.getContactId(), 	email.getSender().getContactId());
		
		UUID 	recipient1Id = (UUID) 	email.getRecipients().stream().filter(r -> r.getContactType() == ContactType.RECRUITER).findFirst().get().getId();
		UUID 	recipient2Id = (UUID) 	email.getRecipients().stream().filter(r -> r.getContactType() == ContactType.SYSTEM).findFirst().get().getId();
		
		assertEquals(recip1Id, recipient1Id);
		assertEquals(recip2Id, recipient2Id);
		
		String 	recipient1Address = (String) email.getRecipients().stream().filter(r -> r.getContactType() == ContactType.RECRUITER).findFirst().get().getEmailAddress();
		String 	recipient2Address = (String) email.getRecipients().stream().filter(r -> r.getContactType() == ContactType.SYSTEM).findFirst().get().getEmailAddress();
		
		assertEquals(recip1Address, recipient1Address);
		assertEquals(recip2Address, recipient2Address);
		
		String 	recipient1FirstName = (String) email.getRecipients().stream().filter(r -> r.getContactType() == ContactType.RECRUITER).findFirst().get().getFirstName();
		String 	recipient2FirstName = (String) email.getRecipients().stream().filter(r -> r.getContactType() == ContactType.SYSTEM).findFirst().get().getFirstName();
		
		assertEquals(firstNameRecip1, recipient1FirstName);
		assertEquals(firstNameRecip2, recipient2FirstName);
		
		assertEquals(attachments, email.getAttachments());
		
	}
	
	/**
	* Tests updating of the state to SENT_EXTERN
	* @throws Exception
	*/
	@Test
	public void testMarkAsSentExternal() throws Exception{
		
		Email email = Email
				.builder()
				.build();
		
		assertEquals(Status.DRAFT, email.getStatus());
		
		email.markSentExternal();
		
		assertEquals(Status.SENT_EXTERN, email.getStatus());
		
	}
	
	/**
	* Tests updating of the body of the Email
	* @throws Exception
	*/
	@Test
	public void testSetBody() throws Exception{
		
		final String body 			= "Some text";
		final String updatedBody 	= "Some new text";
		
		Email email = Email
				.builder()
				.body(body)
				.build();
		
		assertEquals(body, email.getBody());
		
		email.setBody(updatedBody);;
		
		assertEquals(updatedBody, email.getBody());
		
	}
	
	/**
	* Tests updating of Email Recipients
	* @throws Exception
	*/
	@Test
	public void testSetRecipients() throws Exception{
		
		final EmailRecipient<UUID> 		recipient1 	= new EmailRecipient<>(UUID.randomUUID(), "", ContactType.RECRUITER);
		final EmailRecipient<UUID> 		recipient2 	= new EmailRecipient<>(UUID.randomUUID(), "", ContactType.RECRUITER);
		final Set<EmailRecipient<UUID>> original 	= Set.of(recipient1);
		final Set<EmailRecipient<UUID>> updates 	= Set.of(recipient2);
		
		Email email = Email
				.builder()
				.recipients(original)
				.build();
		
		assertEquals(recipient1, email.getRecipients().stream().findFirst().get());
		
		email.setRecipients(updates);
		
		assertEquals(recipient2, email.getRecipients().stream().findFirst().get());
		
	}
	
	/**
	* Tests updating of the state to FAILURE
	* @throws Exception
	*/
	@Test
	public void testMarkAsSentFailure() throws Exception{
		
		Email email = Email
				.builder()
				.build();
		
		assertEquals(Status.DRAFT, email.getStatus());
		
		email.markSentFailure();;
		
		assertEquals(Status.FAILURE, email.getStatus());
		
		
	}
	
	/**
	* Tests setting of persistable. Must be false by default to 
	* prevent sensitive data being stored in the DB
	* @throws Exception
	*/
	@Test
	public void testIsPersistable() throws Exception{
		
		Email email = Email
				.builder()
				.build();
		
		/**
		* Default must be false 
		*/
		assertFalse(email.isPersistable());
		
		email = Email
				.builder()
					.persistable(true)
				.build();
		
		assertTrue(email.isPersistable());
		
	}
	
}