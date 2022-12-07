package com.arenella.recruit.emailservice.dao;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Repository;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.Status;
import com.arenella.recruit.emailservice.entity.EmailEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
* Repository for EmailEntities
* @author K Parkings
*/
@Repository
public interface EmailServiceDao extends CrudRepository<EmailEntity, UUID> {

	/**
	* Persists an email 
	* @param email - Email to persist
	*/
	default void saveEmail(Email email) {
		if (email.isPersistable()) {
			this.save(EmailEntity.convertToEntity(email));
		}
	}
	
	/**
	* Fetches email's that have not yet been sent
	* @return
	*/
	default Set<Email> fetchEmailsByStatus(Status status){
		return StreamSupport
			.stream(this.findEmailEntitiessByStatus(status).spliterator(),false)
			.map(e -> EmailEntity.convertFromEntity(e)).collect(Collectors.toSet());
		
	}
	
	@Query("FROM EmailEntity where status = :status ")
	Set<EmailEntity> findEmailEntitiessByStatus(@Param("status") Status status);
	
}
