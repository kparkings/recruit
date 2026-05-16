package com.arenella.recruit.campaign.dao;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.entities.CampaignContactEntity;

/**
* Repository for CampaingContacts
*/
@Repository
public interface CampaignContactDao extends ListCrudRepository<CampaignContactEntity, String> {

	/**
	* Returns all contacts 
	* @return contacts
	*/
	default Set<Contact> fetchContacts(){
		return this.findAll().stream().map(CampaignContactEntity::fromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
}
