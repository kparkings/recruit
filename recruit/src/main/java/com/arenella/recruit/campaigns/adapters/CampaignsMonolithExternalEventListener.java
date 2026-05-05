package com.arenella.recruit.campaigns.adapters;

import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterDeletedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.campaign.dao.ContactEntityDao;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;
import com.arenella.recruit.campaigns.beans.Contact;

/**
* Listener handles incoming events form external services
*/
@Service
public class CampaignsMonolithExternalEventListener implements CampaignsExternalEventListener{

	private final ContactEntityDao contactDao;
	
	/**
	* Constructor
	* @param contactDao - Repository for interacting with Contacts
	*/
	public CampaignsMonolithExternalEventListener(ContactEntityDao contactDao) {
		this.contactDao = contactDao;
	}
	
	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event) {
		
		if (this.contactDao.fetchContact(event.getRecruiterId()).isPresent()) {
			return ;
		}
		
		Contact contact = new Contact (event.getRecruiterId(), event.getFirstName(), event.getSurname(), event.getEmail(), SubscriptionType.CREDIT);
		
		this.contactDao.saveContact(contact);
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterUpdatedEvent(RecruiterUpdatedEvent event) {
		this.contactDao.fetchContact(event.getRecruiterId()).ifPresent(_ ->{
			Contact contact = new Contact (event.getRecruiterId(), event.getFirstName(), event.getSurname(), event.getEmail(), SubscriptionType.CREDIT);
			this.contactDao.saveContact(contact);
		});
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenForSubscriptionAddedEvent(SubscriptionAddedEvent event) {
		
		this.contactDao.fetchContact(event.getRecruiterId()).ifPresent(c ->{
			Contact contact = new Contact (c.id(), c.firstName(), c.surname(), c.email(), event.getSubscriptionType().toString().equals("CREDIT_BASED_SUBSCRIPTION") ? SubscriptionType.CREDIT : SubscriptionType.PAID);
			this.contactDao.saveContact(contact);
		});
		
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent event) {
		this.contactDao.fetchContact(event.geRecruiterId()).ifPresent(c ->{
			Contact contact = new Contact (c.id(), c.firstName(), c.surname(), c.email(), SubscriptionType.CREDIT);
			this.contactDao.saveContact(contact);
		});
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterAccountDeletedEvent(RecruiterDeletedEvent event) {
		this.contactDao.fetchContact(event.getRecruiterId()).ifPresent(_ ->
			this.contactDao.deleteById(event.getRecruiterId())
		);
	}

	/**
	* Refer to the interface CampaignsExternalEventListener for details 
	*/
	@Override
	public void listenForRecruiterDeletedEvent(RecruiterDeletedEvent event) {
		this.contactDao.fetchContact(event.getRecruiterId()).ifPresent(_ -> this.contactDao.deleteById(event.getRecruiterId()));
	}

}