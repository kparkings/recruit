package com.arenella.recruit.adapters.events;

/**
* Event to inform that a Recruiter has been deleted from the System
* @author K Parkings
*/
public class RecruiterDeletedEvent {

	private final String recruiterId;
	
	/**
	* Constructor
	* @param recruiterId - Id of recruiter that was deleted
	*/
	public RecruiterDeletedEvent(String recruiterId) {
		this.recruiterId = recruiterId;
	}
	
	/**
	* Returns the Id of the Recruitr that was deleted
	* @return Id of Recruiter that was deleted
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
}
