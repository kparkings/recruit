package com.arenella.recruit.adapters.events;

/**
* Event to inform that a Cadidate was Deleted from 
* the System
* @author K Parkings
*/
public class CandidateDeletedEvent {

	private final String candidateId;
	
	/**
	* Constructor
	* @param candidateId - id of deleted Candidate
	*/
	public CandidateDeletedEvent(String candidateId) {
		this.candidateId = candidateId;
	}
	
	/**
	* Returns the Id of the deleted Candidate
	* @return id of the deleted Candidate
	*/
	public String getCandidateId() {
		return this.candidateId;
	}
	
}
