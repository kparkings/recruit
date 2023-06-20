package com.arenella.recruit.authentication.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.authentication.beans.AuthenticatedEvent;

/**
* Entity representation of a successful login attempt
* @author K Parkings
*/
@Entity
@Table(schema="users", name="login_events")
public class AuthenticatedEventEntity {

	@Id
	@Column(name="id")
	private UUID			id;
	
	@Column(name="user_id")
	private String 			userId;
	
	@Column(name="recruiter")
	private boolean 		recruiter;
	
	@Column(name="candidate")
	private	boolean 		candidate;
	
	@Column(name="logged_in_at")
	private LocalDateTime 	loggedInAt;
	
	/**
	* Default Constructor 
	*/
	public AuthenticatedEventEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon Builder
	* @param builder - Contains initialization values
	*/
	public AuthenticatedEventEntity(AuthenticatedEventEntityBuilder builder) {
		this.id 		= builder.id;
		this.userId 	= builder.userId;
		this.recruiter 	= builder.recruiter;
		this.candidate 	= builder.candidate;
		this.loggedInAt = builder.loggedInAt;
	}
	
	/**
	* Returns the id of the Event
	* @return id of the Event
	*/
	public UUID getId(){
		return this.id;
	}
	
	/**
	* Returns id of User that logged in
	* @return id of the User
	*/
	public String  getUserId() {
		return this.userId;
	}
	
	/**
	* Returns if the User is a Recruiter
	* @return if the User is a Recruiter
	*/
	public boolean isRecruiter() {
		return this.recruiter;
	}
	
	/**
	* Returns if the User is a Candidate
	* @return if the User is a Candidate
	*/
	public boolean isCandidate() {
		return this.candidate;
	}
	
	/**
	* Returns when the User logged In
	* @return when the User logged In
	*/
	public LocalDateTime getLoggedInAt() {
		return this.loggedInAt;
	}
	
	/**
	* Returns a Builder
	* @return builder
	*/
	public static AuthenticatedEventEntityBuilder builder() {
		return new AuthenticatedEventEntityBuilder();
	}
	
	/**
	* Builder
	* @author K Parkings
	*/
	public static class AuthenticatedEventEntityBuilder {
	
		private UUID			id;
		private String 			userId;
		private boolean 		recruiter;
		private	boolean 		candidate;
		private LocalDateTime 	loggedInAt;
		
		/**
		* Sets the unique id of the Event
		* @param id - Unique id of the Event
		* @return Builder
		*/
		public AuthenticatedEventEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the unique Id of the user
		* @param userId - Id of the User
		* @return Builder
		*/
		public AuthenticatedEventEntityBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets whether the user is a Recruiter
		* @param recruiter - whether the user is a Recruiter
		* @return Builder
		*/
		public AuthenticatedEventEntityBuilder recruiter(boolean recruiter) {
			this.recruiter = recruiter;
			return this;
		}
		
		/**
		* Sets whether the user is a Candidate
		* @param candidate - whether the user is a Candidate
		* @return Builder
		*/
		public	AuthenticatedEventEntityBuilder candidate(boolean candidate) {
			this.candidate = candidate;
			return this;
		}
		
		/**
		* Sets when the Event occurred
		* @param loggedInAt - when the Event occurred
		* @return Builder
		*/
		public AuthenticatedEventEntityBuilder loggedInAt(LocalDateTime loggedInAt) {
			this.loggedInAt = loggedInAt;
			return this;
		}
		
		/**
		* Returns Initialized instance
		* @return Initialized instance
		*/
		public AuthenticatedEventEntity build() {
			return new AuthenticatedEventEntity(this);
		}
		
	}
	
	/**
	* Converts from the Domain to Entity Representation
	* @param event		- Domain representation
	* @param eventId	- Id of the Event
	* @return Entity representation
	*/
	public static AuthenticatedEventEntity convertToEntity(AuthenticatedEvent event, UUID eventId) {
		return AuthenticatedEventEntity
				.builder()
					.id(eventId)
					.userId(event.getUserId())
					.recruiter(event.isRecruiter())
					.candidate(event.isCandidate())
					.loggedInAt(event.getLoggedInAt())
				.build();
	}
	
	/**
	* Converts from the Entity to Domain Representation
	* @param enitty		- Domain representation
	* @return Domain representation
	*/
	public static AuthenticatedEvent convertFromEntity(AuthenticatedEventEntity entity) {
		return new AuthenticatedEvent(entity.getUserId(), entity.isRecruiter(), entity.isCandidate(), entity.getLoggedInAt());
	}
	
}