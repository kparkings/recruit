package com.arenella.recruit.candidates.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.beans.Contact;
import com.arenella.recruit.candidates.beans.Contact.CONTACT_TYPE;
import com.arenella.recruit.candidates.entities.RecruiterContactEntity;
import com.arenella.recruit.candidates.entities.RecruiterContactEntity.RecruiterContactEntityPK;

/**
* Repository for ContactEntity objects 
* @author K Parkings
*/
public interface RecruiterContactDao extends CrudRepository<RecruiterContactEntity, RecruiterContactEntityPK>{

	@Query("from RecruiterContactEntity where id.contactType = :contactType and id.userId = :userId")
	Optional<RecruiterContactEntity> getEntityByTypeAndId(CONTACT_TYPE contactType, String userId);
	
	/**
	* If found returns the Contact
	* @param contactType
	* @param userId
	* @return If found the Contact
	*/
	default Optional<Contact> getByTypeAndId(CONTACT_TYPE contactType, String userId) {
		
		Optional<RecruiterContactEntity> entity = getEntityByTypeAndId(contactType, userId);
		
		if (entity.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(RecruiterContactEntity.convertFromEntity(entity.get()));
		
	}

}