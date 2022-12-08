package com.arenella.recruit.recruiters.beans;

import java.time.LocalDateTime;
import java.util.UUID;

/**
* Represents a view of wither an Open Position or Offered Candidate 
* by a Recruiter
*/
public class SupplyAndDemandEvent {

	public static enum EventType {OPEN_POSITION, OFFERED_CANDIDATE}
	
	private UUID 			eventId;
	private String 			recruiterId;
	private EventType 		type;
	private LocalDateTime 	created;
	
	/**
	* Constructor based upon a Builder
	* @param builder - contains initialization values
	*/
	public SupplyAndDemandEvent(SupplyAndDemandEventBuilder builder) {
		this.eventId 		= builder.eventId;
		this.recruiterId 	= builder.recruiterId;
		this.type 			= builder.type;
		this.created 		= builder.created;
	}
	
	/**
	* Returns the unique Id of the Event
	* @return unique Id of the Event
	*/
	public UUID getEventId() {
		return this.eventId;
	}
	
	/**
	* Returns the unique Id of the Recruiter who performed
	* the view action
	* @return unique id of the Recruiter
	*/
	public String getRecruiterId() {
		return this.recruiterId;
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
	public static SupplyAndDemandEventBuilder builder() {
		return new SupplyAndDemandEventBuilder();
	}
	
	/**
	* Builder for SupplyAndDemandEvent
	* @author K Parkings
	*/
	public static class SupplyAndDemandEventBuilder{
		
		private UUID 			eventId;
		private String 			recruiterId;
		private EventType 		type;
		private LocalDateTime 	created;

		/**
		* Sets the Unique ID of the Event 
		* @param eventId - Unique ID
		* @return Builder
		*/
		public SupplyAndDemandEventBuilder eventId(UUID eventId) {
			this.eventId = eventId;
			return this;
		}
		
		/**
		* Sets the Unique Id of the Recruiter
		* @param recruiterId - Unique id
		* @return Builder
		*/
		public SupplyAndDemandEventBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the type of the View event
		* @param type - What was viewed
		* @return Builder
		*/
		public SupplyAndDemandEventBuilder type(EventType type){
			this.type = type;
			return this;
		}
		
		/**
		* Sets when the event took place
		* @param created - When the event took place
		* @return Builder
		*/
		public SupplyAndDemandEventBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Returns an initialized instance of SupplyAndDemandEvent
		* @return SupplyAndDemandEvent
		*/
		public SupplyAndDemandEvent build() {
			return new SupplyAndDemandEvent(this);
		} 
		
	}
	
}