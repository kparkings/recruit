package com.arenella.recruit.adapters.events;

/**
* Event to inform that a User has used a Credit
* @author K Parkings
*/
public class CreditsUsedEvent {

	private String 	userId;
	private int 	credits;
	
	/**
	* Constructor
	* @param userId 	- Id of user who used the Credit
	* @param credits 	- remainingCredits;
	*/
	public CreditsUsedEvent(String userId, int credits) {
		this.userId = userId;
		this.credits = credits;
	} 
	
	/**
	* Returns the id of the User who used a credit
	* @return id of the User
	*/
	public String getUserId() {
		return this.userId;
	}
	
	/**
	* Returns the number of credits
	* @return remaining credits
	*/
	public int getCredits() {
		return this.credits;
	}
	
}