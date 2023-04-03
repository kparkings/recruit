package com.arenella.recruit.adapters.events;

/**
* Event representing a change to the Candidate's
* password
* @author K Parkings
*/
public class CandidatePasswordUpdatedEvent {

	private final String candidateId;
	private final String newPassword;
	
	/**
	* Constructor
	* @param candidateId - Uni1ue id of the Candidate
	* @param newPassword - new password
	*/
	public CandidatePasswordUpdatedEvent(String candidateId, String newPassword) {
		this.candidateId = candidateId;
		this.newPassword = newPassword;
	}
	
	/**
	* Return the id of the Candidate 
	* @return candidates id
	*/
	public String getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns the Candidate's new password
	* @return new password
	*/
	public String getNewPassword() {
		return this.newPassword;
	}
	
}