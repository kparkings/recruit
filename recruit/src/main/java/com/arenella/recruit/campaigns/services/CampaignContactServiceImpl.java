package com.arenella.recruit.campaigns.services;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.arenella.recruit.campaign.dao.CampaignContactDao;
import com.arenella.recruit.campaigns.beans.Contact;

@Service
public class CampaignContactServiceImpl implements CampaignContactService{

	private CampaignContactDao contactsDao;
	
	public CampaignContactServiceImpl(CampaignContactDao contactDao) {
		this.contactsDao = contactDao;
	}
	
	@Override
	public Set<Contact> fetchContactsById(Set<String> contactIds) {
		// TODO Auto-generated method stub
		return contactsDao.fetchContacts();
	}

}
