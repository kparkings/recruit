package com.arenella.recruit.emailservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.emailservice.beans.Recipient;
import com.arenella.recruit.emailservice.dao.RecipientDao;

/**
* Services for interacting with Email Recipients
* @author K Parkings
*/
@Service
public class RecipentServiceImpl implements RecipientService{

	@Autowired
	private RecipientDao recipientDao;
	
	/**
	* refer to the RecipientService for details
	*/
	@Override
	public void addRecipient(Recipient recipient) {
		this.recipientDao.addRecipient(recipient);
		
	}
	
	/**
	* refer to the RecipientService for details
	*/
	@Override
	public void updateRecipient(Recipient recipient) {
		this.recipientDao.updateRecipient(recipient);
		
	}

}
