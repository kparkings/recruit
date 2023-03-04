package com.arenella.recruit.emailservice.services;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.EmailAttachment;
import com.arenella.recruit.emailservice.dao.EmailServiceDao;
import com.arenella.recruit.emailservice.entity.EmailEntity;

/**
* Services for interacting with Email's
* @author K Parkings
*/
@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
	private EmailServiceDao emailServiceDao;
	
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

}
