package com.arenella.recruit.recruiters.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent;
import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent.EventType;

@Entity
@Table(schema="recruiter", name="event_supply_or_demand_viewed")
public class SupplyAndDemandEventEntity{

	@EmbeddedId
	private SupplyAndDemandEventEntityPK id;
	
	@Column(name="type")
	@Enumerated(EnumType.STRING)
	private EventType 		type;
	
	@Column(name="created")
	private LocalDateTime 	created;
	
	/**
	* Default Constructor 
	*/
	public SupplyAndDemandEventEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - contains initialization values
	*/
	public SupplyAndDemandEventEntity(SupplyAndDemandEventEntityBuilder builder) {
		this.id = new SupplyAndDemandEventEntityPK(builder.eventId, builder.recruiterId);
		this.type 			= builder.type;
		this.created 		= builder.created;
	}
	
	/**
	* Returns the unique id of the Contact
	* @return id of the Recipient
	*/
	public SupplyAndDemandEventEntityPK getId(){
		return this.id;
	}
	
	/**
	* Returns the type of the View
	* @return type
	*/
	public EventType getType() {
		return this.type;
	}
	
	/**
	* Returns when the Event was created
	* @return when the Event was created
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Returns a Builder for the SupplyAndDemandEvent class
	* @return Builder
	*/
	public static SupplyAndDemandEventEntityBuilder builder() {
		return new SupplyAndDemandEventEntityBuilder();
	}
	
	/**
	* Builder for SupplyAndDemandEvent
	* @author K Parkings
	*/
	public static class SupplyAndDemandEventEntityBuilder{
		
		private UUID 			eventId;
		private String 			recruiterId;
		private EventType 		type;
		private LocalDateTime 	created;

		/**
		* Sets the Unique ID of the Event 
		* @param eventId - Unique ID
		* @return Builder
		*/
		public SupplyAndDemandEventEntityBuilder eventId(UUID eventId) {
			this.eventId = eventId;
			return this;
		}
		
		/**
		* Sets the Unique Id of the Recruiter
		* @param recruiterId - Unique id
		* @return Builder
		*/
		public SupplyAndDemandEventEntityBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the type of the View event
		* @param type - What was viewed
		* @return Builder
		*/
		public SupplyAndDemandEventEntityBuilder type(EventType type){
			this.type = type;
			return this;
		}
		
		/**
		* Sets when the event took place
		* @param created - When the event took place
		* @return Builder
		*/
		public SupplyAndDemandEventEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Returns an initialized instance of SupplyAndDemandEvent
		* @return SupplyAndDemandEvent
		*/
		public SupplyAndDemandEventEntity build() {
			return new SupplyAndDemandEventEntity(this);
		} 
		
	}
	
	/**
	* Converts from Domain representation to Entity representation
	* @param event - Domain representation
	* @return Entity representation
	*/
	public static SupplyAndDemandEventEntity convertToEntity(SupplyAndDemandEvent event) {
		return SupplyAndDemandEventEntity
				.builder()
					.created(event.getCreated())
					.eventId(event.getEventId())
					.recruiterId(event.getRecruiterId())
					.type(event.getType())
				.build();
	}
	
	/**
	* Converts from Entity representation to Domain representation
	* @param entity - Entity representation
	* @return Domain representation
	*/
	public static SupplyAndDemandEvent convertFromEntity(SupplyAndDemandEventEntity entity) {
		return SupplyAndDemandEvent
				.builder()
					.created(entity.getCreated())
					.eventId(entity.id.getEventId())
					.recruiterId(entity.id.getRecruiterId())
					.type(entity.getType())
				.build();
	}
	
}
