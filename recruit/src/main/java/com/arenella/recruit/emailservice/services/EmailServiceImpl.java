package com.arenella.recruit.emailservice.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Contact;
import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.EmailAttachment;
import com.arenella.recruit.emailservice.dao.ContactDao;
import com.arenella.recruit.emailservice.dao.EmailServiceDao;
import com.arenella.recruit.emailservice.entity.EmailEntity;

/**
* Services for interacting with Email's
* @author K Parkings
*/
@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
	private EmailServiceDao 		emailServiceDao;
	
	@Autowired
	private ContactDao 				contactDao;
	
	@Autowired
	private EmailDispatcherService 	emailService;
	
	/**
	* Refer to the EmailService interface for details 
	*/
	@Override
	public Set<Email> fetchEmailsByRecipientId(String recipientId) {
		return emailServiceDao.fetchEmailsByRecipientId(recipientId, ContactType.RECRUITER);		
	}

	/**
	* Refer to the EmailService interface for details 
	*/
	@Override
	public EmailAttachment fetchAttachment(UUID emailId, UUID attachmentId, String  recipientId) {
		return this.emailServiceDao.fetchAttachment(emailId, attachmentId, recipientId).orElseThrow(() -> new RuntimeException("Unknown Attachment"));
	}

	/**
	* Refer to the EmailService interface for details 
	*/
	@Override
	public void setEmailReadStatus(UUID emailId, boolean read, String authorisedUsersId) {
	
		Optional<Email> emailOpt = this.emailServiceDao.fetchEmailById(emailId);
		
		Email email = emailOpt.orElseThrow(() -> new RuntimeException("Unknown Email"));
		
		EmailRecipient<UUID> recipient = email.getRecipients().stream().filter(r -> r.getContactId().equals(authorisedUsersId)).findAny().orElseThrow(() -> new RuntimeException("Unknown Email."));
		
		recipient.setViewed(read);
		
		this.emailServiceDao.save(EmailEntity.convertToEntity(email));
		
	}

	/**
	* Refer to the EmailService interface for details 
	*/
	@Override
	public void handleReply(UUID emailId, String message, String authenticatedUserId) {
		
		Optional<Email> 	emailOpt 	= this.emailServiceDao.fetchEmailById(emailId);
		Email 				email 		= emailOpt.orElseThrow(() -> new RuntimeException("Unknown Email"));
		Contact 			contact 	= this.contactDao.getByIdAndType(ContactType.RECRUITER, authenticatedUserId).orElseThrow(() -> new RuntimeException("Unknown Contact"));
		StringBuilder 		body 		= new StringBuilder();
	
		body
			.append("Reply From: ").append(contact.getFirstName() + " " + contact.getSurname())
			.append("\n")
			.append("Reply Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm:ss")))
			.append("\n\n")
			.append(message)
			.append("\n\n")
			.append(" ************* ")
			.append("\n\n")
			.append(email.getBody());
		
		email.setBody(body.toString());
		
		Set<EmailRecipient<UUID>> recipients = new HashSet<>();
		
		recipients.addAll(email.getRecipients());
		
		/**
		* Once a reply is made Original Sender also becomes a recipient of the replies 
		*/
		if (recipients.stream().filter(r -> r.getContactId().equals(email.getSender().getContactId())).findAny().isEmpty()) {
			
			EmailRecipient<UUID> recipient = new  EmailRecipient<>(UUID.randomUUID(), email.getSender().getContactId(), ContactType.RECRUITER);

			recipient.setEmail(email.getSender().getEmail());
			
			recipients.add(recipient);
		}

		recipients.stream().filter(r -> !r.getContactId().equals(authenticatedUserId)).forEach(r -> r.setViewed(false));
		recipients.stream().filter(r -> r.getContactId().equals(authenticatedUserId)).forEach(r -> r.setViewed(true));

		email.setRecipients(recipients);
		
		this.emailServiceDao.saveEmail(email);
		
		email.getRecipients().stream().filter(r -> !r.getContactId().equals(authenticatedUserId)).forEach(r -> {
			
			RequestSendEmailCommand command = 
					RequestSendEmailCommand
						.builder()
							.emailType(EmailType.SYSTEM_EXTERN)
							.model(Map.of("recipientName",r.getFirstName() == null ? "na" : r.getFirstName(), "emailTitle", email.getTitle()))
							.persistable(false)
							.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(), r.getContactId(), ContactType.RECRUITER)))
							.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "kparkings@gmail.com"))
							.title("Arenella-ICT: Reply received for message " + email.getTitle())
							.topic(EmailTopic.REC_TO_REC_EMAIL_REPLY_NOTICICATION)
						.build();
			
			this.emailService.handleSendEmailCommand(command);
		});
		
	}

}
