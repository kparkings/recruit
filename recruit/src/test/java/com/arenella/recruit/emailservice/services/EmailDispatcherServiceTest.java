package com.arenella.recruit.emailservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Contact;
import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Status;
import com.arenella.recruit.emailservice.dao.ContactDao;
import com.arenella.recruit.emailservice.dao.EmailServiceDao;

/**
* Unit tests for the EmailDispatcherService class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class EmailDispatcherServiceTest {

	@InjectMocks
	private EmailDispatcherService 		service 			= new EmailDispatcherService();
	
	@Mock
	private JavaMailSender 				senderMock;
	
	@Mock
	private EmailServiceDao 			emailDaoMock;
	
	@Mock
	private ScheduledExecutorService 	schedulerMock;
	
	@Mock
	private ContactDao					mockContactDao;
	
	@Spy
	private EmailTemplateFactory 		templateFacotry 	= new EmailTemplateFactory();
	
	/**
	* Sets up test environment
	* @throws Exception
	*/
	@BeforeEach
	public void init() throws Exception{
		ReflectionTestUtils.setField(service, "scheduler", schedulerMock);
	}
	
	/**
	* Tests that each email stored in the DB but not yet sent is rescheduled when 
	* the service starts.
	* @throws Exception
	*/
	@Test
	public void testSendingOfPreviouslyPersistedButUnsendEmails() throws Exception{
		
		final Set<Email> emails = 
				Set.of(
					Email.builder().build(),
					Email.builder().build(),
					Email.builder().build());
	
		Mockito.when(this.emailDaoMock.fetchEmailsByStatus(Status.TO_OUTBOX)).thenReturn(emails);
		
		this.service.init();
	
		Mockito.verify(schedulerMock, Mockito.times(emails.size())).schedule(Mockito.any(Runnable.class), Mockito.anyLong(), Mockito.any());
		
	}
	
	/**
	* Test command is converted to Email and Email is persisted
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testHandleSendEmailCommand_system_external() throws Exception{
		
		Email.EmailRecipient<UUID> recipient1 = new Email.EmailRecipient("one", "rec1", ContactType.RECRUITER);
		Email.EmailRecipient<UUID> recipient2 = new Email.EmailRecipient("two", "rec2", ContactType.RECRUITER);
		
		Mockito.when(this.mockContactDao.getByIdAndType(Mockito.any(), Mockito.any())).thenReturn(Optional.of(new Contact("", ContactType.RECRUITER, "", "")));
		
		this.service.handleSendEmailCommand(RequestSendEmailCommand.builder().emailType(EmailType.SYSTEM_EXTERN).recipients(Set.of(recipient1, recipient2)).topic(EmailTopic.ACCOUNT_CREATED).build());
		
		Mockito.verify(this.emailDaoMock, Mockito.times(2)).saveEmail(Mockito.any(Email.class));
		
	}
	
	/**
	* Test command is converted to Email and Email is persisted
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testHandleSendEmailCommand_system_Internal() throws Exception{
		
		Email.EmailRecipient<UUID> recipient1 = new Email.EmailRecipient("one", "rec1", ContactType.RECRUITER);
		Email.EmailRecipient<UUID> recipient2 = new Email.EmailRecipient("two", "rec2", ContactType.RECRUITER);
		
		Mockito.when(this.mockContactDao.getByIdAndType(Mockito.any(), Mockito.any())).thenReturn(Optional.of(new Contact("", ContactType.RECRUITER, "", "")));
		
		this.service.handleSendEmailCommand(RequestSendEmailCommand.builder().emailType(EmailType.SYSTEM_INTERN).recipients(Set.of(recipient1, recipient2)).topic(EmailTopic.ACCOUNT_CREATED).build());
		
		Mockito.verify(this.emailDaoMock, Mockito.times(2)).saveEmail(Mockito.any(Email.class));
		
	}
	
	/**
	* Test command is converted to Email and Email is persisted
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testHandleSendEmailCommand_external() throws Exception{
		
		Email.EmailRecipient<UUID> recipient1 = new Email.EmailRecipient("one", "rec1", ContactType.RECRUITER);
		Email.EmailRecipient<UUID> recipient2 = new Email.EmailRecipient("two", "rec2", ContactType.RECRUITER);
		
		Mockito.when(this.mockContactDao.getByIdAndType(Mockito.any(), Mockito.any())).thenReturn(Optional.of(new Contact("", ContactType.RECRUITER, "", "")));
		
		this.service.handleSendEmailCommand(RequestSendEmailCommand.builder().emailType(EmailType.EXTERN).recipients(Set.of(recipient1, recipient2)).topic(EmailTopic.ACCOUNT_CREATED).build());
		
		Mockito.verify(this.emailDaoMock, Mockito.times(2)).saveEmail(Mockito.any(Email.class));
		
	}
	
	/**
	* Test command is converted to Email and Email is persisted
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testHandleSendEmailCommand_Internal() throws Exception{
		
		Email.EmailRecipient<UUID> recipient1 = new Email.EmailRecipient("one", "rec1", ContactType.RECRUITER);
		Email.EmailRecipient<UUID> recipient2 = new Email.EmailRecipient("two", "rec2", ContactType.RECRUITER);
		
		Mockito.when(this.mockContactDao.getByIdAndType(Mockito.any(), Mockito.any())).thenReturn(Optional.of(new Contact("", ContactType.RECRUITER, "", "")));
		
		this.service.handleSendEmailCommand(RequestSendEmailCommand.builder().emailType(EmailType.INTERN).recipients(Set.of(recipient1, recipient2)).topic(EmailTopic.ACCOUNT_CREATED).build());
		
		Mockito.verify(this.emailDaoMock, Mockito.times(2)).saveEmail(Mockito.any(Email.class));
		
	}
	
	/**
	* Test status for external
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testHandleSendEmailCommand_status_extern() throws Exception{
		
		ArgumentCaptor<Email> emailAc = ArgumentCaptor.forClass(Email.class);
		
		Email.EmailRecipient<UUID> recipient1 = new Email.EmailRecipient("two", "rec2", ContactType.RECRUITER);
		
		Mockito.when(this.mockContactDao.getByIdAndType(Mockito.any(), Mockito.any())).thenReturn(Optional.of(new Contact("", ContactType.RECRUITER, "", "")));
		Mockito.doNothing().when(this.emailDaoMock).saveEmail(emailAc.capture());
		
		this.service.handleSendEmailCommand(RequestSendEmailCommand.builder().emailType(EmailType.EXTERN).recipients(Set.of(recipient1)).topic(EmailTopic.ACCOUNT_CREATED).build());
		
		assertEquals(Status.TO_OUTBOX, emailAc.getValue().getStatus());
		
	}
	
	/**
	* Test status for system external
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testHandleSendEmailCommand_status_system_extern() throws Exception{
		
		ArgumentCaptor<Email> emailAc = ArgumentCaptor.forClass(Email.class);
		
		Email.EmailRecipient<UUID> recipient1 = new Email.EmailRecipient("two", "rec2", ContactType.RECRUITER);
		
		Mockito.when(this.mockContactDao.getByIdAndType(Mockito.any(), Mockito.any())).thenReturn(Optional.of(new Contact("", ContactType.RECRUITER, "", "")));
		Mockito.doNothing().when(this.emailDaoMock).saveEmail(emailAc.capture());
		
		this.service.handleSendEmailCommand(RequestSendEmailCommand.builder().emailType(EmailType.EXTERN).recipients(Set.of(recipient1)).topic(EmailTopic.ACCOUNT_CREATED).build());
		
		assertEquals(Status.TO_OUTBOX, emailAc.getValue().getStatus());
		
	}
	
	/**
	* Test status for system internal
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testHandleSendEmailCommand_status_system_intern() throws Exception{
		
		ArgumentCaptor<Email> emailAc = ArgumentCaptor.forClass(Email.class);
		
		Email.EmailRecipient<UUID> recipient1 = new Email.EmailRecipient("two", "rec2", ContactType.RECRUITER);
		
		Mockito.when(this.mockContactDao.getByIdAndType(Mockito.any(), Mockito.any())).thenReturn(Optional.of(new Contact("", ContactType.RECRUITER, "", "")));
		Mockito.doNothing().when(this.emailDaoMock).saveEmail(emailAc.capture());
		
		this.service.handleSendEmailCommand(RequestSendEmailCommand.builder().emailType(EmailType.SYSTEM_INTERN).recipients(Set.of(recipient1)).topic(EmailTopic.ACCOUNT_CREATED).build());
		
		assertEquals(Status.SENT_INTERN, emailAc.getValue().getStatus());
		
	}
	
	/**
	* Test status for internal
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testHandleSendEmailCommand_status_intern() throws Exception{
		
		ArgumentCaptor<Email> emailAc = ArgumentCaptor.forClass(Email.class);
		
		Email.EmailRecipient<UUID> recipient1 = new Email.EmailRecipient("two", "rec2", ContactType.RECRUITER);
		
		Mockito.when(this.mockContactDao.getByIdAndType(Mockito.any(), Mockito.any())).thenReturn(Optional.of(new Contact("", ContactType.RECRUITER, "", "")));
		Mockito.doNothing().when(this.emailDaoMock).saveEmail(emailAc.capture());
		
		this.service.handleSendEmailCommand(RequestSendEmailCommand.builder().emailType(EmailType.INTERN).recipients(Set.of(recipient1)).topic(EmailTopic.ACCOUNT_CREATED).build());
		
		assertEquals(Status.SENT_INTERN, emailAc.getValue().getStatus());
		
	}
	
}
