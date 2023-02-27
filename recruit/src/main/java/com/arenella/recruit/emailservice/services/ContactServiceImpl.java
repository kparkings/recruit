package com.arenella.recruit.emailservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.emailservice.beans.Contact;
import com.arenella.recruit.emailservice.dao.ContactDao;

/**
* Services for interacting with Email Contact's
* @author K Parkings
*/
@Service
public class ContactServiceImpl implements ContactService{

	@Autowired
	private ContactDao contactDao;
	
	/**
	* refer to the ContactService for details
	*/
	@Override
	public void addContact(Contact contact) {
		this.contactDao.addContact(contact);
		
	}
	
	/**
	* refer to the ContactService for details
	*/
	@Override
	public void updateContact(Contact contact) {
		this.contactDao.updateContact(contact);
		
	}

}
