package com.arenella.recruit.authentication.dao;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.authentication.beans.AuthenticatedEvent;
import com.arenella.recruit.authentication.entity.AuthenticatedEventEntity;

/**
* Defines Persistence for AuthenticatedEvent's
* @author K Parkings
*/
public interface AuthenticatedEventDao extends CrudRepository<AuthenticatedEventEntity, UUID>{

	@Query("from AuthenticatedEventEntity where loggedInAt >= :since")
	public Set<AuthenticatedEventEntity> fetchInternalEventsSince(LocalDateTime since);
	
	@Query("from AuthenticatedEventEntity where recruiter = true")
	public Set<AuthenticatedEventEntity> fetchLoginEventsForRecruiters();
	
	/**
	* Returns all the login events for a given User
	* @param userId - Id of User to fetch Login events for
	* @return Login Events for the User
	*/
	public default Set<AuthenticatedEvent> fetchLoginEventsForAllRecruiter() {
		return this.fetchLoginEventsForRecruiters().stream().map(AuthenticatedEventEntity::convertFromEntity).collect(Collectors.toSet());
	}
	
	/**
	* Persists AuthenticatedEvent 
	* @param authenticatedEvent - Event to persist
	*/
	public default void persitAuthenticatedEvent(AuthenticatedEvent authenticatedEvent) {
		this.save(AuthenticatedEventEntity.convertToEntity(authenticatedEvent, UUID.randomUUID()));
	}
	
	
	/**
	* Returns the Events that occurred after a certain moment in tim
	* @param since - Moment from when to include events
	* @return Events since the moment in time stipulate
	*/
	public default Set<AuthenticatedEvent> fetchEventsSince(LocalDateTime since){
		return this.fetchInternalEventsSince(since).stream().map(AuthenticatedEventEntity::convertFromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
}
