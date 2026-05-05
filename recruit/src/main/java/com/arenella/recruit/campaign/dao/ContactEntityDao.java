package com.arenella.recruit.campaign.dao;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.entities.CampaignContactEntity;

/**
* Repository for Campaign Contacts 
*/
@Repository
public interface ContactEntityDao extends ListCrudRepository<CampaignContactEntity, String>{

	/**
	* If present returns the Contact that is associated with the 
	* given Id
	* @param contactId - Id of the Contact to retrieve
	* @return Contact
	*/
	default Optional<Contact> fetchContact(String contactId){
		return this.findById(contactId).map(CampaignContactEntity::fromEntity);
	}

	/**
	* Persists a new Contact
	* @param contact - Contact to be persisted
	*/
	default void saveContact(Contact contact) {
		this.save(CampaignContactEntity.toEntity(contact));
	};
	
}
