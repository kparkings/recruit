package com.arenella.recruit.emailservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.emailservice.controllers.EmailAPIOutbound.EmailAttachmentAPIOutbound;
import com.arenella.recruit.emailservice.controllers.EmailAPIOutbound.SenderAPIOutbound;

/**
* Unit tests for the EmailAPIOutbound class
* @author K Parkings
*/
public class EmailAPIOutboundTest {

	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		final UUID 								id				= UUID.randomUUID();
		final String 							title			= "title";
		final SenderAPIOutbound 				sender			= new SenderAPIOutbound("recruiter1", SenderType.RECRUITER, "Kevin ");	 
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
		
		assertEquals(id, 			email.getId());
		assertEquals(title, 		email.getTitle());
		assertEquals(sender, 		email.getSender());
		assertEquals(created, 		email.getCreated());
		assertEquals(body, 			email.getBody());
		assertEquals(attachments, 	email.getAttachments());
		assertEquals(viewed, 		email.isViewed());
		
	}
	
}
