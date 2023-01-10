package com.arenella.recruit.adapters.events;

/**
* Event for updating Recruiters password
* @author K Parkings
*/
public class RecruiterPasswordUpdatedEvent {

	private final String recruiterId;
	private final String newPassword;
	
	/**
	* Construtor
	* @param recruiterId - Id of Recruiter with new Password
	* @param newPassword - new Password
	*/
	public RecruiterPasswordUpdatedEvent(String recruiterId, String newPassword){
		this.recruiterId = recruiterId;
		this.newPassword = newPassword;
	}

	/**
	* Returns the id of the Recruiter who has a new 
	* Password
	* @return - Unique id of the Recruiter
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns the new Password
	* @return password
	*/
	public String getNewPassword() {
		return this.newPassword;
	}
	
}
