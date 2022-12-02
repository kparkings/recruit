package com.arenella.recruit.emailservice.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailEvent;
import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Recipient;
import com.arenella.recruit.emailservice.beans.Email.Recipient.RecipientType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.emailservice.beans.Email.Status;
import com.arenella.recruit.emailservice.dao.EmailServiceDao;

/**
* Component responsible for sending Email
* @author K Parkings
*/
@Component
public class EmailDispatcherService {
	
	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	private EmailServiceDao emailDao;
	
	@Autowired
	private EmailTemplateFactory templateFacotry;
	
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
	
	/**
	* Loads any pending emails. Required for application restarts to no emails
	* are lost
	*/
	@PostConstruct
	public void init() {
		
		this.emailDao.fetchEmailsByStatus(Status.TO_OUTBOX).stream().forEach(email -> {
			this.dispatchEmail(email, this.emailDao);
		});
		
		//WART START
		Recipient<UUID> recipient1 	= new Recipient<>(UUID.randomUUID(), RecipientType.RECRUITER, 	"kparkings@gmail.com");
		Recipient<UUID> recipient2	= new Recipient<>(UUID.randomUUID(), RecipientType.RECRUITER, 	"kevin_parkings@hotmail.com");
		Sender<UUID> 	sender 		= new Sender<>(UUID.randomUUID(), 	 SenderType.SYSTEM, 		"kparkings@gmail.com");
		
		Map<String,Object> model = new HashMap<>();

		//TODO: This needs to be passed from the event
		model.put("user", "Kevin Parkings");
		
		
		//TODO: Event needs to send model
		
		RequestSendEmailEvent event = RequestSendEmailEvent
				.builder()
					.emailType(EmailType.EXTERN)
					.recipients(Set.of(recipient1, recipient2))
					.sender(sender)
					.title("Arenella Test email")
					.topic(EmailTopic.ACCOUNT_CREATED)
					.model(model)
				.build();
		
		this.handleSendEmailEvent(event);		
		
		//WART END
	}
	
	/**
	* Schedules the dispatching of an email to the Recipients
	* @param event - Contains details to construct email
	*/
	public void dispatchEmail(Email email, EmailServiceDao emailDao) {
		
		Runnable task = new Runnable(){

			@Override
			public void run() {
				
				try {
					dispatchEmailToRecipients(email);
					email.markSentExternal();
				}catch(Exception e) {
					email.markSentFailure();
				}
				
				emailDao.saveEmail(email);
				
			}
		};
		
		scheduler.schedule(task, 0, TimeUnit.MINUTES);
		
	}
	
	/**
	* Event handler for RequestSendEmailEvent
	* @param event - contains details of email to be sent
	*/
	public void handleSendEmailEvent(RequestSendEmailEvent event){
		
		Email email = Email
				.builder()
					.body(this.templateFacotry.fetchTemplate(event))
					.created(LocalDateTime.now())
					.emailType(event.getEmailType())
					.id(UUID.randomUUID())
					.recipients(event.getRecipients())
					.scheduledToBeSentAfter(LocalDateTime.now().minusDays(1))
					.sender(event.getSender())
					.status(Status.TO_OUTBOX)
					.title(event.getTitle())
				.build();
		
		this.emailDao.saveEmail(email);
		
		dispatchEmail(email, this.emailDao);
		
	}
	
	/**
	* Performs sending of an email
	* @param email - Email to send
	* @throws MessagingException
	*/
	private  void dispatchEmailToRecipients(Email email) throws MessagingException {

		if (email.getScheduledToBeSentAfter().isAfter(LocalDateTime.now())) {
			return;
		}
		
		MimeMessage 		mimeMessage = sender.createMimeMessage();
		MimeMessageHelper 	helper 		= new MimeMessageHelper(mimeMessage, "utf-8");
	
		String[] recipientEmails = email
				.getRecipients()
				.stream()
				.map(r -> r.getEmailAddress())
				.toArray(String[]::new);
		
		
		mimeMessage.setContent(email.getBody(), "text/html");
		helper.setFrom(email.getSender().getEmail());
		helper.setBcc(recipientEmails);
		helper.setSubject(email.getTitle());

		sender.send(mimeMessage);
	}
	
}