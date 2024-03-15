package com.arenella.recruit.candidates.controllers;

/**
* Infor of candidate availablity counts
* @author K Parkings
*/
public class CandidateAvailabilityCountAPIOutbound {

	private long available;
	private long unavailable;
	
	/**
	* Constructor
	* @param available		- Number of available Candidates
	* @param unavailable	- Number of unavailable Candidates
	*/
	public CandidateAvailabilityCountAPIOutbound(long available, long unavailable) {
		this.available = available;
		this.unavailable = unavailable;
	}
	
	/**
	* Returns total number of available candidates
	* @return available candidates count
	*/
	public long getAvailable() {
		return this.available;
	}
	
	/**
	* Returns total number of unavailable candidates
	* @return unavailable candidate count
	*/
	public long getUnavailable() {
		return this.unavailable;
	}
	
	/**
	* Returns total number of registered candidates 
	* independent of their availability
	* @return total registered candidates
	*/
	public long getTotalRegisteredCandidates() {
		return this.unavailable +  this.available;
	}
	
}
