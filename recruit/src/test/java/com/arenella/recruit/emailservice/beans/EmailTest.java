package com.arenella.recruit.emailservice.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Recipient;
import com.arenella.recruit.emailservice.beans.Email.Recipient.RecipientType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.emailservice.beans.Email.Status;

/**
* Unit tests for the Email class
* @author K Parkings
*/
public class EmailTest {

	final private String				recip1Address				= "norepy1@renella-ict.com";
	final private String				recip2Address				= "norepy2@renella-ict.com";
	final private UUID 					recip1Id 					= UUID.randomUUID();
	final private String 				recip2Id 					= "anId";
	final private UUID 					id							= UUID.randomUUID();
	final private String 				title						= "aTitle";
	final private EmailType 			emailType					= EmailType.EXTERN;
	final private Sender<?> 			sender						= new Sender<UUID>(UUID.randomUUID(), SenderType.SYSTEM, "norepy@renella-ict.com"); 
	final private LocalDateTime 		created						= LocalDateTime.of(2022,11,17, 19,11,00);
	final private LocalDateTime 		scheduledToBeSentAfter		= LocalDateTime.of(2022,11,17, 19,11,10);;
	final private LocalDateTime 		sent						= LocalDateTime.of(2022,11,17, 19,11,20);;
	final private String 				body						= "aBody";
	final private Status 				status						= Status.DRAFT;
	final private Set<Recipient<?>> 	recipients					= Set.of(new Recipient<UUID>(recip1Id, RecipientType.RECRUITER, recip1Address),
																	 new Recipient<String>(recip2Id, RecipientType.SYSTEM, recip2Address));
	
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
		assertEquals(sender.getSenderType(), 	email.getSender().getSenderType());
		
		UUID 	recipient1Id = (UUID) 	email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.RECRUITER).findFirst().get().getId();
		String 	recipient2Id = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.SYSTEM).findFirst().get().getId();
		
		assertEquals(recip1Id, recipient1Id);
		assertEquals(recip2Id, recipient2Id);
		
		String 	recipient1Address = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.RECRUITER).findFirst().get().getEmailAddress();
		String 	recipient2Address = (String) email.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.SYSTEM).findFirst().get().getEmailAddress();
		
		assertEquals(recip1Address, recipient1Address);
		assertEquals(recip2Address, recipient2Address);
		
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
	
}
