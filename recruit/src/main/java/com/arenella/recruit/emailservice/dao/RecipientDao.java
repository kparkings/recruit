package com.arenella.recruit.emailservice.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.emailservice.beans.Recipient;
import com.arenella.recruit.emailservice.entities.RecipientEntity;
import com.arenella.recruit.emailservice.entities.RecipientEntityPK;


/**
* Repository for Email Recipients
* @author K Parkings
*/
@Repository
public interface RecipientDao extends CrudRepository<RecipientEntity, RecipientEntityPK>{

	/**
	* Persists the Recipient
	* @param recipient
	*/
	default void addRecipient(Recipient recipient) {
		this.save(RecipientEntity.convertToEntity(recipient));
	}

}
