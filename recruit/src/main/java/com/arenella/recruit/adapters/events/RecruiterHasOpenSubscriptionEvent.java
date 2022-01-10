package com.arenella.recruit.adapters.events;

/**
* Event to inform network that a Recruiter has 
* an open subscriptions where previously it had 
* no open subscription
* @author K Parkings
*/
public class RecruiterHasOpenSubscriptionEvent {

	private final  String recruiterId;
	
	/**
	* Constructor
	* @param recruiterId - Unique Id of the Recruiter
	*/
	public RecruiterHasOpenSubscriptionEvent(String recruiterId) {
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
