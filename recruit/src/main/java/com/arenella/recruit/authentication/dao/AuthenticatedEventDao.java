package com.arenella.recruit.authentication.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.authentication.beans.AuthenticatedEvent;
import com.arenella.recruit.authentication.entity.AuthenticatedEventEntity;

/**
* Defines Persistence for AuthenticatedEvent's
* @author K Parkings
*/
public interface AuthenticatedEventDao extends CrudRepository<AuthenticatedEventEntity, UUID>{

	/**
	* Persists AuthenticatedEvent 
	* @param authenticatedEvent - Event to persist
	*/
	public default void persitAuthenticatedEvent(AuthenticatedEvent authenticatedEvent) {
		this.save(AuthenticatedEventEntity.convertToEntity(authenticatedEvent, UUID.randomUUID()));
	}
	
}
