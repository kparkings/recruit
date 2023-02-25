package com.arenella.recruit.emailservice.services;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.RecipientType;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.Status;
import com.arenella.recruit.emailservice.beans.Recipient;
import com.arenella.recruit.emailservice.dao.EmailServiceDao;
import com.arenella.recruit.emailservice.dao.RecipientDao;

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
	private RecipientDao				mockRecipientDao;
	
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
	public void testHandleSendEmailCommand() throws Exception{
		
		Email.EmailRecipient<?> recipient1 = new Email.EmailRecipient("one", RecipientType.RECRUITER);
		Email.EmailRecipient<?> recipient2 = new Email.EmailRecipient("two", RecipientType.RECRUITER);
		
		Mockito.when(this.mockRecipientDao.getByIdAndType(Mockito.any(), Mockito.any())).thenReturn(Optional.of(new Recipient("", RecipientType.RECRUITER, "", "")));
		
		this.service.handleSendEmailCommand(RequestSendEmailCommand.builder().recipients(Set.of(recipient1, recipient2)).topic(EmailTopic.ACCOUNT_CREATED).build());
		
		Mockito.verify(this.emailDaoMock, Mockito.times(2)).saveEmail(Mockito.any(Email.class));
		
	}
	
	
}
