package com.arenella.recruit.emailservice.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.dao.EmailServiceDao;

/**
* Services for interacting with Emails
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

}
