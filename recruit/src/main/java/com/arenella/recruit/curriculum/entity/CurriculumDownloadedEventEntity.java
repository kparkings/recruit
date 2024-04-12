package com.arenella.recruit.curriculum.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.arenella.recruit.curriculum.beans.CurriculumDownloadedEvent;

/**
* Entity representation of CurriculumDownloadEvent
* @author K Parkings
*/
@Entity
@Table(schema="curriculum", name="event_curriculum_downloaded")
public class CurriculumDownloadedEventEntity {

	@Id
	private String			id				= UUID.randomUUID().toString();
	private String 			curriculumId;
	private String 			userId;
	private boolean			isAdminUser;
	private LocalDateTime 	timestamp;
	
	/**
	* For Hibernate/JPA
	*/
	@SuppressWarnings("unused")
	private CurriculumDownloadedEventEntity() {
		//For Hibernate
	}
	
	/**
	* Constructor based upon a builder
	* @param builder contains initialization values
	*/
	public CurriculumDownloadedEventEntity(CurriculumDownloadedEventEntityBuilder builder) {
		this.curriculumId 		= builder.curriculumId;
		this.userId 			= builder.userId;
		this.isAdminUser 		= builder.isAdminUser;
		this.timestamp 			= builder.timestamp;
	}
	
	/**
	* Returns the unique identifier of the Curriculum that was downloaded
	* @return Unique Id of the Curriculum
	*/
	public String getCurriculumId() {
		return this.curriculumId;
	}
	
	/**
	* Returns the Unique Identifier of the User that downlaoded 
	* the Curriculum
	* @return Id of the User
	*/
	public String getUserId() {
		return this.userId;
	}
	
	/**
	* Returns whether or not the User that downloaded the Curriculum 
	* was an Admin user
	* @return whether the user has an admin role
	*/
	public boolean isAdminUser() {
		return this.isAdminUser;
	}
	
	/**
	* Returns when the Curriculum was downlaoded
	* @return when the Curriculum was downloaded
	*/
	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}
	
	/**
	* Returns an instance of the Builder for the Entity
	* @return Builder for the Entity
	*/
	public static CurriculumDownloadedEventEntityBuilder builder() {
		return new CurriculumDownloadedEventEntityBuilder();
	}
	
	/**
	* Builder for the Entity
	* @author K Parkings
	*/
	public static class CurriculumDownloadedEventEntityBuilder {
		
		private String 			curriculumId;
		private String 			userId;
		private boolean			isAdminUser;
		private LocalDateTime 	timestamp;
		
		/**
		* Sets the Unique Identifier of the Curriculum that was downloaded
		* @param curriculumId - Id of downloaded curriculum
		* @return Builder
		*/
		public CurriculumDownloadedEventEntityBuilder curriculumId(String curriculumId) {
			this.curriculumId = curriculumId;
			return this;
		}
		
		/**
		* Sets the Unique Identifier of the User that downloaded the Curriculum
		* @param userId		- Unique id of the User
		* @return Builder
		*/
		public CurriculumDownloadedEventEntityBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets whether the User who downloaded the Curriculum was an Admin user
		* @param isAdminUser - Whether download was done by an Admin user
		* @return Builder
		*/
		public CurriculumDownloadedEventEntityBuilder isAdminUser(boolean isAdminUser) {
			this.isAdminUser = isAdminUser;
			return this;
		}
		
		/**
		* Sets the time when the download took place
		* @param timestamp - Time download took place
		* @return Builder
		*/
		public CurriculumDownloadedEventEntityBuilder timestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
			return this;
		}
		
		/**
		* Returns an instance of CurriculumDownloadedEventEntity initialized with 
		* the values in the builder
		* @return Initialized instance of CurriculumDownloadedEventEntity
		*/
		public CurriculumDownloadedEventEntity build() {
			return new CurriculumDownloadedEventEntity(this);
		}
		
	}
	
	/**
	* Converts a CurriculumDownloadedEventEntity to an Entity representation. Not here we are 
	* working with events. This creates a new Event with a new PrimryKey
	* @param event - Domain representation of an event
	* @return Entity representation of an event
	*/
	public static CurriculumDownloadedEventEntity toEntity(CurriculumDownloadedEvent event) {
		return CurriculumDownloadedEventEntity
										.builder()
											.curriculumId(event.getCurriculumId())
											.userId(event.getUserId())
											.isAdminUser(event.isAdminUser())
											.timestamp(event.getTimestamp())
										.build();
	}

	/**
	* Converts an Entity representation of the Event to a Domain representation
	* @param entity - Entity representatin of Event
	* @return Domain representation of Event
	*/
	public static CurriculumDownloadedEvent fromEntity(CurriculumDownloadedEventEntity entity) {
		return CurriculumDownloadedEvent
								.manualBuilder()
									.curriculumId(entity.getCurriculumId())
									.userId(entity.getUserId())
									.isAdminUser(entity.isAdminUser())
									.timestamp(entity.getTimestamp())
								.build();
	}
	
}