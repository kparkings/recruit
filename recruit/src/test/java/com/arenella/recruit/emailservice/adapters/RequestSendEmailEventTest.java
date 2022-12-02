package com.arenella.recruit.emailservice.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Recipient;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Recipient.RecipientType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;

/**
* Unit tests for the RequestSendEmailEvent class
* @author K Parkings
*/
public class RequestSendEmailEventTest {

	final private UUID 					recip1Id 					= UUID.randomUUID();
	final private String 				recip2Id 					= "anId";
	final private String 				title						= "aTitle";
	final private EmailType 			emailType					= EmailType.EXTERN;
	final private Sender<?> 			sender						= new Sender<UUID>(UUID.randomUUID(), SenderType.SYSTEM, "norepy@renella-ict.com"); 
	final private Set<Recipient<?>> 	recipients					= Set.of(new Recipient<UUID>(recip1Id, RecipientType.RECRUITER, "norepy@renella-ict.com"),
																	 new Recipient<String>(recip2Id, RecipientType.SYSTEM, "norepy@renella-ict.com"));
	final private EmailTopic			topic						= EmailTopic.ACCOUNT_CREATED;
	final private Map<String,Object>	model						= new HashMap<>();
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		RequestSendEmailEvent event = RequestSendEmailEvent
				.builder()
					.emailType(this.emailType)
					.recipients(this.recipients)
					.sender(this.sender)
					.title(this.title)
					.topic(topic)
					.model(model)
				.build();
		
		assertEquals(title, 					event.getTitle());
		assertEquals(emailType, 				event.getEmailType());
		assertEquals(sender, 					event.getSender());
		assertEquals(topic, 					event.getTopic());
		assertEquals(model, 					event.getModel());
		
		UUID 	recipient1Id = (UUID) 	event.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.RECRUITER).findFirst().get().getId();
		String 	recipient2Id = (String) event.getRecipients().stream().filter(r -> r.getRecipientType() == RecipientType.SYSTEM).findFirst().get().getId();
		
		assertEquals(recip1Id, recipient1Id);
		assertEquals(recip2Id, recipient2Id);
		
	}
	
}