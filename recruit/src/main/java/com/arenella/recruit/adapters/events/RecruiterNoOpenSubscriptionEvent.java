package com.arenella.recruit.adapters.events;

/**
* Event to inform network that a Recruiter has 
* no open subscriptions
* @author K Parkings
*/
public class RecruiterNoOpenSubscriptionEvent {

	private final  String recruiterId;
	
	/**
	* Constructor
	* @param recruiterId - Unique Id of the Recruiter
	*/
	public RecruiterNoOpenSubscriptionEvent(String recruiterId) {
		this.recruiterId = recruiterId;
	}
	
	/**
	* Returns the unique identifier of the Recruiter
	* @return id of Recruiter
	*/
	public String geRecruiterId() {
		return this.recruiterId;
	}
	
}
