package com.arenella.recruit.emailservice.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.RecipientType;
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

	@Query("from RecipientEntity e where e.id.recipientType = :recipientType and e.id.recipientId = :recipientId")
	public RecipientEntity getEntityByIdAndType(RecipientType recipientType, String recipientId);
	
	/**
	* Retrieves a matching Recipeint where one exists
	* @param recipientType  - Type of recipient
	* @param recipientId 	- Id of the Recipient
	* @return Where available Recipient
	*/
	default Optional<Recipient> getByIdAndType(RecipientType recipientType, String recipientId){
		
		Optional<RecipientEntity> recipientOpt =  Optional.ofNullable(this.getEntityByIdAndType(recipientType, recipientId));
		
		if (recipientOpt.isEmpty()) {
			return Optional.empty();
		}

		return recipientOpt.map(f -> RecipientEntity.convertFromEntity(f));
		
	}

	/**
	* If Recipient exists updates it to new values
	* @param recipient - contains new Recipient values
	*/
	default void updateRecipient(Recipient recipient) {
		
		RecipientEntity entity = getEntityByIdAndType(recipient.getRecipientType(), recipient.getId());
		
		if (entity == null) {
			return;
		}
		
		entity.setEmail(recipient.getEmail());
		entity.setFirstName(recipient.getFirstName());
		
		this.save(entity);
		
		
		
	}
	
}
