package com.arenella.recruit.emailservice.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Status;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.RecipientType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;

/**
* Unit tests for the EmailEntity class
* @author K Parkings
*/
public class EmailEntityTest {

	final private String						recip1Address				= "norepy1@renella-ict.com";
	final private String						recip2Address				= "norepy2@renella-ict.com";
	final private UUID 							recip1Id 					= UUID.randomUUID();
	final private String 						recip2Id 					= "anId";
	final private UUID 							id							= UUID.randomUUID();
	final private String 						title						= "aTitle";
	final private EmailType 					emailType					= EmailType.EXTERN;
	final private String						firstNameRecip1				= "Kevin1";
	final private String						firstNameRecip2				= "Kevin2";
	final private SenderEntity 					sender						= SenderEntity.builder().id(UUID.randomUUID().toString()).senderType(SenderType.SYSTEM).emailAddress("norepy@renella-ict.com").emailId(id).build(); 
	final private Sender<?> 					senderDomain				= new Sender<UUID>(UUID.randomUUID(), SenderType.SYSTEM, "norepy@renella-ict.com"); 
	final private LocalDateTime 				created						= LocalDateTime.of(2022,11,17, 19,11,00);
	final private LocalDateTime 				scheduledToBeSentAfter		= LocalDateTime.of(2022,11,17, 19,11,10);;
	final private LocalDateTime 				sent						= LocalDateTime.of(2022,11,17, 19,11,20);;
	final private String 						body						= "aBody";
	final private Status 						status						= Status.DRAFT;
	final private EmailRecipient<UUID>			emailRecip1					= new EmailRecipient<UUID>(recip1Id, RecipientType.RECRUITER);
	final private EmailRecipient<String>		emailRecip2					= new EmailRecipient<String>(recip2Id, RecipientType.SYSTEM);
	final private Set<EmailRecipientEntity> 	recipients					= Set.of(EmailRecipientEntity.builder().id(recip1Id.toString()).recipientType(RecipientType.RECRUITER).emailAddress(recip1Address).firstName(firstNameRecip1).emailId(id).build(),
																	 			EmailRecipientEntity.builder().id(recip2Id.toString()).recipientType(RecipientType.SYSTEM).emailAddress(recip2Address).firstName(firstNameRecip2).emailId(id).build());
	final private Set<EmailRecipient<?>> 		recipientsDomain			= Set.of(emailRecip1,emailRecip2);
	
	/**
	* Sets up test environment 
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
		assertEquals(sender.getSenderType(), 	email.getSender().getSenderType());
		
		String 	recipient1Id = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.RECRUITER).findFirst().get().getId();
		String 	recipient2Id = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.SYSTEM).findFirst().get().getId();
		
		assertEquals(recip1Id.toString(), recipient1Id);
		assertEquals(recip2Id.toString(), recipient2Id);
		
		String 	recipient1Address = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.RECRUITER).findFirst().get().getEmailAddress();
		String 	recipient2Address = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.SYSTEM).findFirst().get().getEmailAddress();
		
		assertEquals(recip1Address, recipient1Address);
		assertEquals(recip2Address, recipient2Address);
		
		String 	recipient1FirstName = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.RECRUITER).findFirst().get().getFirstName();
		String 	recipient2FirstName = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.SYSTEM).findFirst().get().getFirstName();
		
		assertEquals(firstNameRecip1, recipient1FirstName);
		assertEquals(firstNameRecip2, recipient2FirstName);
		
	}
	
	/**
	* Tests conversion of Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception{
		
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
		assertEquals(senderDomain.getSenderType(), 		entity.getSender().getSenderType());
		
		String 	recipient1Id = (String) entity.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.RECRUITER).findFirst().get().getId();
		String 	recipient2Id = (String) entity.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.SYSTEM).findFirst().get().getId();
		
		assertEquals(recip1Id.toString(), recipient1Id);
		assertEquals(recip2Id.toString(), recipient2Id);
		
		String 	recipient1Address = (String) entity.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.RECRUITER).findFirst().get().getEmailAddress();
		String 	recipient2Address = (String) entity.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.SYSTEM).findFirst().get().getEmailAddress();
		
		assertEquals(recip1Address, recipient1Address);
		assertEquals(recip2Address, recipient2Address);
		
		String 	recipient1FirstName = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.RECRUITER).findFirst().get().getFirstName();
		String 	recipient2FirstName = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.SYSTEM).findFirst().get().getFirstName();
		
		assertEquals(firstNameRecip1, recipient1FirstName);
		assertEquals(firstNameRecip2, recipient2FirstName);
		
	}
	
	/**
	* Tests conversion of Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception{
		
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
		
		assertEquals(sender.getId().toString(), 	email.getSender().getId().toString());
		assertEquals(sender.getEmailAddress(), 		email.getSender().getEmail());
		assertEquals(sender.getSenderType(), 		email.getSender().getSenderType());
		
		String 	recipient1Id = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.RECRUITER).findFirst().get().getId();
		String 	recipient2Id = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.SYSTEM).findFirst().get().getId();
		
		assertEquals(recip1Id.toString(), recipient1Id);
		assertEquals(recip2Id.toString(), recipient2Id);
		
		String 	recipient1Address = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.RECRUITER).findFirst().get().getEmailAddress();
		String 	recipient2Address = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.SYSTEM).findFirst().get().getEmailAddress();
		
		assertEquals(recip1Address, recipient1Address);
		assertEquals(recip2Address, recipient2Address);
		
		String 	recipient1FirstName = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.RECRUITER).findFirst().get().getFirstName();
		String 	recipient2FirstName = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.SYSTEM).findFirst().get().getFirstName();
		
		assertEquals(firstNameRecip1, recipient1FirstName);
		assertEquals(firstNameRecip2, recipient2FirstName);
		
	}
}
