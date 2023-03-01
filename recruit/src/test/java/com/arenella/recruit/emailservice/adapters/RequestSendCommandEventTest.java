package com.arenella.recruit.emailservice.adapters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;

/**
* Unit tests for the RequestSendEmailEvent class
* @author K Parkings
*/
public class RequestSendCommandEventTest {

	final private UUID 						recip1Id 					= UUID.randomUUID();
	final private UUID 						recip2Id 					= UUID.randomUUID();
	final private String 					title						= "aTitle";
	final private EmailType 				emailType					= EmailType.EXTERN;
	final private Sender<?> 				sender						= new Sender<UUID>(UUID.randomUUID(), "", SenderType.SYSTEM, "norepy@renella-ict.com"); 
	final private Set<EmailRecipient<UUID>> recipients					= Set.of(new EmailRecipient<UUID>(recip1Id, "rec1", ContactType.RECRUITER),
																	 	new EmailRecipient<UUID>(recip2Id, "rec2", ContactType.SYSTEM));
	final private EmailTopic				topic						= EmailTopic.ACCOUNT_CREATED;
	final private Map<String,Object>		model						= new HashMap<>();
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		RequestSendEmailCommand command = RequestSendEmailCommand
				.builder()
					.emailType(this.emailType)
					.recipients(this.recipients)
					.sender(this.sender)
					.title(this.title)
					.topic(topic)
					.model(model)
					.persistable(true)
				.build();
		
		assertEquals(title, 					command.getTitle());
		assertEquals(emailType, 				command.getEmailType());
		assertEquals(sender, 					command.getSender());
		assertEquals(topic, 					command.getTopic());
		assertEquals(model, 					command.getModel());
		assertTrue(command.isPersistable()); 
		
		UUID 	recipient1Id = (UUID) 	command.getRecipients().stream().filter(r -> r.getContactType() == ContactType.RECRUITER).findFirst().get().getId();
		UUID 	recipient2Id = (UUID) command.getRecipients().stream().filter(r -> r.getContactType() == ContactType.SYSTEM).findFirst().get().getId();
		
		assertEquals(recip1Id, recipient1Id);
		assertEquals(recip2Id, recipient2Id);
		
	}
	
	/**
	* Tests by Default false so sensitive data not persisted by accident
	* @throws Exception
	*/
	@Test
	public void testDefaultIsPersistable() throws Exception{
		
		RequestSendEmailCommand command = RequestSendEmailCommand
				.builder()
				.build();
		
		assertFalse(command.isPersistable()); 
		
	}
	
}