package com.arenella.recruit.emailservice.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;

/**
* Unit tests for the RequestSendEmailCommand class
* @author K Parkings
*/
public class RequestSendEmailCommandTest {

	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	public void testConstructionViaBuilder() throws Exception{
		
		final EmailType 				emailType 	= EmailType.EXTERN;
		final Map<String,Object> 		model 		= Map.of();
		final boolean 					persistable = true;
		final Set<EmailRecipient<?>> 	recipients 	= new HashSet<>();
		final Sender<String> 			sender 		= new Sender<>("id", SenderType.SYSTEM,"kparkings@gmail.com");
		final String 					title 		= "aTitle";
		final EmailTopic 				topic 		= EmailTopic.ACCOUNT_CREATED;
		
		RequestSendEmailCommand command = 
				RequestSendEmailCommand
					.builder()
						.emailType(emailType)
						.model(model)
						.persistable(persistable)
						.recipients(recipients)
						.sender(sender)
						.title(title)
						.topic(topic)
					.build();
		
		assertEquals(emailType, 	command.getEmailType());
		assertEquals(model, 		command.getModel());
		assertEquals(persistable, 	command.isPersistable());
		assertEquals(recipients,	command.getRecipients());
		assertEquals(sender, 		command.getSender());
		assertEquals(title, 		command.getTitle());
		assertEquals(topic, 		command.getTopic());
		
	}
	
}
