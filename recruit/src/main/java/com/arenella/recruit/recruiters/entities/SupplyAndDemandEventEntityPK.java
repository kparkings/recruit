package com.arenella.recruit.recruiters.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
* Primary Key definition for SupplyAndDemandEventEntity
* @author K Parkings
*/
@Embeddable
public class SupplyAndDemandEventEntityPK implements Serializable{

	private static final long serialVersionUID = -6697380452424511020L;

	@Column(name="id")
	private UUID 			eventId;
	
	@Column(name="recruiter_id")
	private String 			recruiterId;
	
	/**
	* Default Constructor 
	*/
	public SupplyAndDemandEventEntityPK() {
		//Hibernate
	}
	
	/**
	* Constructor
	* @param contactType - Type of Contacct
	* @param contactId   - Unique ID of contact in scope of ContactType
	*/
	public SupplyAndDemandEventEntityPK(UUID eventId, String recruiterId) {
		this.eventId 		= eventId;
		this.recruiterId 	= recruiterId;
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
	
}