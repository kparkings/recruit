package com.arenella.recruit.emailservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.EmailAttachment;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Status;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.emailservice.beans.EmailAttachment.FileType;
import com.arenella.recruit.emailservice.dao.EmailServiceDao;

/**
* Unit tests for the EmailServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

	private static final String 				BODY 						= "body";
	private static final LocalDateTime 			CREATED 					= LocalDateTime.of(2023,03,03, 10,10,10);
	private static final EmailType				EMAIL_TYPE 					= EmailType.INTERN;
	private static final UUID					ID 							= UUID.randomUUID();
	private static final boolean				PERSISTABLE 				= true;
	private static final LocalDateTime 			SEND_AFTER 					= LocalDateTime.of(2023,03,04, 10,10,10);
	private static final String 				TITLE 						= "title";
	private static final LocalDateTime 			SENT 						= LocalDateTime.of(2023,03,04, 10,10,20);
	private static final Email.Status			STATUS 						= Status.SENT_INTERN;
	private static final String 				SENDER_ID 					= "1234566788";
	private static final String 				CONTACT_ID 					= "kparkings";
	private static final SenderType 			CONTACT_TYPE 				= SenderType.RECRUITER;
	private static final String 				SENDER_EMAIL 				= "kparkings@gmail.com";
	private static final Sender<String>			SENDER 						= new Sender<>(SENDER_ID, CONTACT_ID, CONTACT_TYPE, SENDER_EMAIL);
	private static final UUID 					ATTACHMENT_ID 				= UUID.randomUUID();
	private static final FileType 				FILE_TYPE 					= FileType.doc;
	private static final byte[] 				FILE_BYTES 					= new byte[] {1,8,9};
	private static final String					ATTACHMENT_NAME				= "cv";
	private static final UUID					RECIPIENT_ID				= UUID.randomUUID();
	private static final String					RECIPIENT_CONTACT_ID		= "kparkings2";
	private static final ContactType			RECIPIENT_TYPE				= ContactType.RECRUITER;
	private static final EmailRecipient<UUID>	EMAIL_RECIPIENT				= new EmailRecipient<>(RECIPIENT_ID, RECIPIENT_CONTACT_ID, RECIPIENT_TYPE);
	private static final EmailAttachment 		EMAIL_ATTACHMENT 			= EmailAttachment
			.builder()
				.attachmentId(ATTACHMENT_ID)
				.emailId(ID)
				.fileBytes(FILE_BYTES)
				.fileType(FILE_TYPE)
				.name(ATTACHMENT_NAME)
			.build();
	
	@Mock
	private EmailServiceDao 	mockEmailServiceDao;
	
	@InjectMocks
	private EmailServiceImpl 	service 				= new EmailServiceImpl();
	
	/**
	* Tests retrieval of recipients Email's
	* @throws Exception
	*/
	@Test
	public void testFetchEmailsByRecipientId() throws Exception{
	
		Mockito.when(this.mockEmailServiceDao.fetchEmailsByRecipientId(RECIPIENT_CONTACT_ID, ContactType.RECRUITER)).thenReturn(Set.of(Email
				.builder()
				.body(BODY)
				.created(CREATED)
				.emailType(EMAIL_TYPE)
				.recipients(Set.of(EMAIL_RECIPIENT))
				.id(ID)
				.persistable(PERSISTABLE)
				.scheduledToBeSentAfter(SEND_AFTER)
				.sender(SENDER)
				.sent(SENT)
				.status(STATUS)
				.title(TITLE)
				.attachments(Set.of(EMAIL_ATTACHMENT))
				.build()));
		
		Set<Email> emails = this.service.fetchEmailsByRecipientId(RECIPIENT_CONTACT_ID);
		
		emails.stream().filter(e -> e.getId() == ID).findAny().orElseThrow();
		
	}
	
	/**
	* Test case that requested attachment not found
	* @throws Exception
	*/
	@Test
	public void testfetchAttachment_unknownAttachment() throws Exception{
		Mockito.when(this.mockEmailServiceDao
				.fetchAttachment(ID, ATTACHMENT_ID, RECIPIENT_CONTACT_ID)).thenReturn(Optional.empty());
	
		assertThrows(RuntimeException.class, () -> {
			this.service.fetchAttachment(ID, ATTACHMENT_ID, RECIPIENT_CONTACT_ID);
		});
		
	}
	
	/**
	* Tests retrieval of Attachment
	* @throws Exception
	*/
	@Test
	public void testfetchAttachment() throws Exception{
		Mockito.when(this.mockEmailServiceDao
				.fetchAttachment(ID, ATTACHMENT_ID, RECIPIENT_CONTACT_ID)).thenReturn(Optional.of(EMAIL_ATTACHMENT));
	
		assertEquals(EMAIL_ATTACHMENT, this.service.fetchAttachment(ID, ATTACHMENT_ID, RECIPIENT_CONTACT_ID));		
	}
	
}