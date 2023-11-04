package com.arenella.recruit.adapters.events;

/**
* Event to inform that Credits have been assigned to a User
* @author K Parkings
*/
public class CreditsAssignedEvent {

	private String 	userId;
	private int 	currentCreditCount;
	
	/**
	* Constructor
	* @param userId				- Id of the user credits assigned t 
	* @param currentCreditCount - Current number of credits available to the User
	*/
	public CreditsAssignedEvent(String userId, int currentCreditCount) {
		this.userId 			= userId;
		this.currentCreditCount = currentCreditCount;
	}
	
	/**
	* Returns the Id of the user assigned Credits
	* @return user id
	*/
	public String getUserId() {
		return this.userId;
	}
	
	/**
	* Returns the number of credits available to the user
	* @return number of credits available ot the user
	*/
	public int getCurrentCreditCount() {
		return this.currentCreditCount;
	}
	
}
