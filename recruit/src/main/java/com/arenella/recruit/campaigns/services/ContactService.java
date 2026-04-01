package com.arenella.recruit.campaigns.services;

import java.util.Set;

import com.arenella.recruit.campaigns.beans.Contact;

/**
* Defines services for interacting with Contact's 
*/
public interface ContactService {

	/**
	* 
	* @param contactIds
	*/
	Set<Contact> fetchContactsById(Set<String> contactIds);
	
}
