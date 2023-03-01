package com.arenella.recruit.emailservice.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Contact;
import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Status;
import com.arenella.recruit.emailservice.beans.EmailAttachment;
import com.arenella.recruit.emailservice.beans.EmailAttachment.FileType;
import com.arenella.recruit.emailservice.dao.ContactDao;
import com.arenella.recruit.emailservice.dao.EmailServiceDao;

/**
* Component responsible for sending Email
* @author K Parkings
*/
@Component
public class EmailDispatcherService {
	
	@Autowired
	private JavaMailSender 					sender;
	
	@Autowired
	private EmailServiceDao 				emailDao;
	
	@Autowired
	private ContactDao						contactDao;
	
	@Autowired
	private EmailTemplateFactory 			templateFacotry;
	
	private final ScheduledExecutorService 	scheduler 			= Executors.newScheduledThreadPool(5);
	
	/**
	* Loads any pending emails. Required for application restarts to no email's
	* are lost
	*/
	//TODO: [KP] In the situation where multiple instances of the service are started
	//           this will lead to the email's being sent multiple times. This will need 
	//			 to be resolved if the monolith is split into microservices.
	@PostConstruct
	public void init() {
		
		this.emailDao.fetchEmailsByStatus(Status.TO_OUTBOX).stream().forEach(email -> {
			this.dispatchEmail(email, this.emailDao);
		});

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
		
		scheduler.schedule(task, 0L, TimeUnit.MINUTES);
		
	}
	
	/**
	* Event handler for RequestSendEmailEvent
	* @param command - contains details of email to be sent
	*/
	public void handleSendEmailCommand(RequestSendEmailCommand command){
		try {
			Set<EmailRecipient<UUID>> recipients = new HashSet<>();
			
			recipients.addAll(command.getRecipients());
			
			recipients.stream().forEach(r -> {
				
				Optional<Contact> 	recipientOpt 	= this.contactDao.getByIdAndType(r.getContactType(), r.getContactId());
				Map<String, Object> model 			= new HashMap<>(command.getModel());
				
				//Ugly but just to see if there is an issue until I have time to design proper error handling
				if (recipientOpt.isEmpty()) {
					r.setEmail("kparkings@gmail.com");
					r.setFirstName("Failed for " + r.getId() + " " + r.getId());
					model.put("recipientName", "na");
				} else {
					r.setEmail(recipientOpt.get().getEmail());
					r.setFirstName(recipientOpt.get().getFirstName());
					
					model.put("recipientName", recipientOpt.get().getFirstName());
				
				}
		
				UUID emailId = UUID.randomUUID();
				
				Email email = Email
						.builder()
							.body(this.templateFacotry.fetchTemplate(command, model))
							.created(LocalDateTime.now())
							.emailType(command.getEmailType())
							.id(emailId)
							.recipients(recipients)
							.scheduledToBeSentAfter(LocalDateTime.now().minusDays(1))
							.sender(command.getSender())
							.status(Status.TO_OUTBOX)
							.title(command.getTitle())
							.persistable(command.isPersistable())
							.attachments(convertAttachments(command, emailId))
						.build();
				
				this.emailDao.saveEmail(email);
				
				if (command.getEmailType() == EmailType.EXTERN || command.getEmailType() == EmailType.SYSTEM_EXTERN) {
					dispatchEmail(email, this.emailDao);
				}
				
			});
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	* Generated Attachments from the Command
	* @param command - Contains details of Attachments
	* @return EmailAttachment representation 
	*/
	private Set<EmailAttachment> convertAttachments(RequestSendEmailCommand command, UUID emailId){
		
		return command.getAttachments().stream().map(a -> 
			EmailAttachment
				.builder()
					.attachmentId(UUID.randomUUID())
					.emailId(emailId)
					.name(a.getName())
					.fileBytes(a.getFileBytes())
					.fileType(FileType.valueOf(a.getFileType()))
				.build()).collect(Collectors.toCollection(HashSet::new));
		
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