package com.arenella.recruit.adapters.events;

/**
* Event informing of a Candidate that is no longer available
* in the System
* @author K Parkings
*/
public class CandidateNoLongerAvailableEvent {

	private final long candidateId;
	
	/**
	* Constructor
	* @param candidateId  - Id of candidate no longer available
	*/
	public CandidateNoLongerAvailableEvent(long candidateId){
		this.candidateId = candidateId;
	}
	
	/**
	* Unique Identifier of the Candidate that is no longer available 
	* @return Candidate Id
	*/
	public long getCandidateId() {
		return this.candidateId;
	}
	
}
