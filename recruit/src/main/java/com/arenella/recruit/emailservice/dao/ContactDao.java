package com.arenella.recruit.emailservice.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.emailservice.beans.Contact;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;

import com.arenella.recruit.emailservice.entities.ContactEntity;
import com.arenella.recruit.emailservice.entities.ContactEntityPK;

/**
* Repository for Email Contacts
* @author K Parkings
*/
@Repository
public interface ContactDao extends CrudRepository<ContactEntity, ContactEntityPK>{

	/**
	* Persists the Contact
	* @param contact
	*/
	default void addContact(Contact contact) {
		this.save(ContactEntity.convertToEntity(contact));
	}

	@Query("from ContactEntity e where e.id.contactType = :contactType and e.id.contactId = :contactId")
	public ContactEntity getEntityByIdAndType(ContactType contactType, String contactId);
	
	/**
	* Retrieves a matching Contact where one exists
	* @param contactType  	- Type of Contact
	* @param contactId 		- Id of the Contact
	* @return Where available Contact
	*/
	default Optional<Contact> getByIdAndType(ContactType contactType, String contactId){
		
		Optional<ContactEntity> contactOpt =  Optional.ofNullable(this.getEntityByIdAndType(contactType, contactId));
		
		if (contactOpt.isEmpty()) {
			return Optional.empty();
		}

		return contactOpt.map(f -> ContactEntity.convertFromEntity(f));
		
	}

	/**
	* If Recipient exists updates it to new values
	* @param contact - contains new Contact values
	*/
	default void updateContact(Contact contact) {
		
		ContactEntity entity = getEntityByIdAndType(contact.getContactType(), contact.getId());
		
		if (entity == null) {
			return;
		}
		
		entity.setEmail(contact.getEmail());
		entity.setFirstName(contact.getFirstName());
		
		this.save(entity);
		
		
		
	}
	
}
