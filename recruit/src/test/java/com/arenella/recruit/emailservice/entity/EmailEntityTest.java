package com.arenella.recruit.emailservice.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.EmailAttachment;
import com.arenella.recruit.emailservice.beans.EmailAttachment.FileType;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Status;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;

/**
* Unit tests for the EmailEntity class
* @author K Parkings
*/
class EmailEntityTest {

	private final String						recip1Address				= "norepy1@renella-ict.com";
	private final String						recip2Address				= "norepy2@renella-ict.com";
	private final UUID 							recip1Id 					= UUID.randomUUID();
	private final UUID	 						recip2Id 					= UUID.randomUUID();
	private final UUID 							id							= UUID.randomUUID();
	private final String 						title						= "aTitle";
	private final EmailType 					emailType					= EmailType.EXTERN;
	private final String						firstNameRecip1				= "Kevin1";
	private final String						firstNameRecip2				= "Kevin2";
	private final SenderEntity 					sender						= SenderEntity.builder().id(UUID.randomUUID().toString()).contactType(SenderType.SYSTEM).emailAddress("norepy@renella-ict.com").emailId(id).build(); 
	private final Sender<?> 					senderDomain				= new Sender<UUID>(UUID.randomUUID(), "", SenderType.SYSTEM, "norepy@renella-ict.com"); 
	private final LocalDateTime 				created						= LocalDateTime.of(2022,11,17, 19,11,00);
	private final LocalDateTime 				scheduledToBeSentAfter		= LocalDateTime.of(2022,11,17, 19,11,10);
	private final LocalDateTime 				sent						= LocalDateTime.of(2022,11,17, 19,11,20);
	private final String 						body						= "aBody";
	private final Status 						status						= Status.DRAFT;
	private final EmailRecipient<UUID>			emailRecip1					= new EmailRecipient<UUID>(recip1Id, "rec1", ContactType.RECRUITER);
	private final EmailRecipient<UUID>			emailRecip2					= new EmailRecipient<UUID>(recip2Id, "rec2", ContactType.SYSTEM);
	private final Set<EmailRecipientEntity> 	recipients					= Set.of(EmailRecipientEntity.builder().id(recip1Id).contactType(ContactType.RECRUITER).emailAddress(recip1Address).firstName(firstNameRecip1).emailId(id).build(),
																	 			EmailRecipientEntity.builder().id(recip2Id).contactType(ContactType.SYSTEM).emailAddress(recip2Address).firstName(firstNameRecip2).emailId(id).build());
	private final Set<EmailRecipient<UUID>> 	recipientsDomain			= Set.of(emailRecip1,emailRecip2);
	private final EmailAttachmentEntity			attachment1					= EmailAttachmentEntity.builder().attachmentId(UUID.randomUUID()).emailId(UUID.randomUUID()).fileBytes(new byte[] {1}).fileType(FileType.doc).build();
	private final EmailAttachmentEntity			attachment2					= EmailAttachmentEntity.builder().attachmentId(UUID.randomUUID()).emailId(UUID.randomUUID()).fileBytes(new byte[] {2}).fileType(FileType.pdf).build();
	private final Set<EmailAttachmentEntity>	attachments					= Set.of(attachment1, attachment2);
	
	private final EmailAttachment				attachment1Domain			= EmailAttachment.builder().attachmentId(UUID.randomUUID()).emailId(UUID.randomUUID()).fileBytes(new byte[] {1}).fileType(FileType.doc).build();
	private final EmailAttachment				attachment2Domain			= EmailAttachment.builder().attachmentId(UUID.randomUUID()).emailId(UUID.randomUUID()).fileBytes(new byte[] {2}).fileType(FileType.pdf).build();
	private final Set<EmailAttachment>			attachmentsDomain			= Set.of(attachment1Domain, attachment2Domain);
	
	
	/**
	* Sets up test environment 
	*/
	@BeforeEach
	void init() {
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
	void testConstructor() {
		
		EmailEntity email = EmailEntity
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
		assertEquals(sender.getEmailAddress(), 	email.getSender().getEmailAddress());
		assertEquals(sender.getContactType(), 	email.getSender().getContactType());
		
		UUID 	recipient1Id = (UUID) email.getRecipients().stream().filter(r -> r.getContactType() == ContactType.RECRUITER).findFirst().get().getId();
		UUID 	recipient2Id = (UUID) email.getRecipients().stream().filter(r -> r.getContactType() == ContactType.SYSTEM).findFirst().get().getId();
		
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
		
		assertTrue(email.getAttachments().contains(attachment1));
		assertTrue(email.getAttachments().contains(attachment2));
		
	}
	
	/**
	* Tests conversion of Domain to Entity representation
	* @throws Exception
	*/
	@Test
	void testConvertFromEntity() {
		
		Email email = Email
				.builder()
					.body(this.body)
					.created(this.created)
					.emailType(this.emailType)
					.id(this.id)
					.recipients(this.recipientsDomain)
					.scheduledToBeSentAfter(this.scheduledToBeSentAfter)
					.sender(this.senderDomain)
					.sent(this.sent)
					.status(this.status)
					.title(this.title)
					.attachments(attachmentsDomain)
				.build();
		
		EmailEntity entity = EmailEntity.convertToEntity(email);
		
		assertEquals(id, 						entity.getId());
		assertEquals(title, 					entity.getTitle());
		assertEquals(emailType, 				entity.getEmailType());
		assertEquals(created, 					entity.getCreated());
		assertEquals(scheduledToBeSentAfter, 	entity.getScheduledToBeSentAfter());
		assertEquals(sent, 						entity.getSent());
		assertEquals(body, 						entity.getBody());
		assertEquals(status, 					entity.getStatus());
		
		assertEquals(senderDomain.getId().toString(), 	entity.getSender().getId());
		assertEquals(senderDomain.getEmail(), 			entity.getSender().getEmailAddress());
		assertEquals(senderDomain.getContactType(), 	entity.getSender().getContactType());
		
		UUID 	recipient1Id = (UUID) entity.getRecipients().stream().filter(r -> r.getContactType() == ContactType.RECRUITER).findFirst().get().getId();
		UUID 	recipient2Id = (UUID) entity.getRecipients().stream().filter(r -> r.getContactType() == ContactType.SYSTEM).findFirst().get().getId();
		
		assertEquals(recip1Id, recipient1Id);
		assertEquals(recip2Id, recipient2Id);
		
		String 	recipient1Address = (String) entity.getRecipients().stream().filter(r -> r.getContactType() == ContactType.RECRUITER).findFirst().get().getEmailAddress();
		String 	recipient2Address = (String) entity.getRecipients().stream().filter(r -> r.getContactType() == ContactType.SYSTEM).findFirst().get().getEmailAddress();
		
		assertEquals(recip1Address, recipient1Address);
		assertEquals(recip2Address, recipient2Address);
		
		String 	recipient1FirstName = (String) email.getRecipients().stream().filter(r -> r.getContactType() == ContactType.RECRUITER).findFirst().get().getFirstName();
		String 	recipient2FirstName = (String) email.getRecipients().stream().filter(r -> r.getContactType() == ContactType.SYSTEM).findFirst().get().getFirstName();
		
		assertEquals(firstNameRecip1, recipient1FirstName);
		assertEquals(firstNameRecip2, recipient2FirstName);
		
		entity.getAttachments().stream().filter(a -> 
				a.getAttachmentId() == attachment1Domain.getAttachmentId() 
			&&  a.getEmailId()		== attachment1Domain.getEmailId()
			&&  a.getFileBytes()	== attachment1Domain.getFileBytes()
			&&  a.getFileType()		== attachment1Domain.getFileType()
			).findAny().orElseThrow();
		
		entity.getAttachments().stream().filter(a -> 
				a.getAttachmentId() == attachment2Domain.getAttachmentId() 
			&&  a.getEmailId()		== attachment2Domain.getEmailId()
			&&  a.getFileBytes()	== attachment2Domain.getFileBytes()
			&&  a.getFileType()		== attachment2Domain.getFileType()
			).findAny().orElseThrow();
		
	}
	
	/**
	* Tests conversion of Domain to Entity representation
	* @throws Exception
	*/
	@Test
	void testConvertToEntity() {
		
		EmailEntity entity = EmailEntity
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
		
		Email email = EmailEntity.convertFromEntity(entity);
		
		assertEquals(id, 						email.getId());
		assertEquals(title, 					email.getTitle());
		assertEquals(emailType, 				email.getEmailType());
		assertEquals(created, 					email.getCreated());
		assertEquals(scheduledToBeSentAfter, 	email.getScheduledToBeSentAfter());
		assertEquals(sent, 						email.getSent());
		assertEquals(body, 						email.getBody());
		assertEquals(status, 					email.getStatus());
		
		assertEquals(sender.getId(), 				email.getSender().getId().toString());
		assertEquals(sender.getEmailAddress(), 		email.getSender().getEmail());
		assertEquals(sender.getContactType(), 		email.getSender().getContactType());
		
		UUID 	recipient1Id = (UUID) email.getRecipients().stream().filter(r -> r.getContactType() == ContactType.RECRUITER).findFirst().get().getId();
		UUID 	recipient2Id = (UUID) email.getRecipients().stream().filter(r -> r.getContactType() == ContactType.SYSTEM).findFirst().get().getId();
		
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
		
		entity.getAttachments().stream().filter(a -> 
			a.getAttachmentId() == attachment1.getAttachmentId() 
		&&  a.getEmailId()		== attachment1.getEmailId()
		&&  a.getFileBytes()	== attachment1.getFileBytes()
		&&  a.getFileType()		== attachment1.getFileType()
		).findAny().orElseThrow();
	
		entity.getAttachments().stream().filter(a -> 
				a.getAttachmentId() == attachment2.getAttachmentId() 
			&&  a.getEmailId()		== attachment2.getEmailId()
			&&  a.getFileBytes()	== attachment2.getFileBytes()
			&&  a.getFileType()		== attachment2.getFileType()
			).findAny().orElseThrow();

	}
}
