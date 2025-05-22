package com.arenella.recruit.candidates.entities;

import com.arenella.recruit.candidates.beans.CandidateRoleStats;
import com.arenella.recruit.candidates.beans.CandidateRoleStats.CandidateRoleStatsBuilder;
import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Defines an Interface for mapping group by Candidate Function
* results
* @author K Parkings
*/
public class CandidateRoleStatsView {

	private FUNCTION 		function;
	private long 			availableCandidates;
	private long			unavailableCandidates;
	
	/**
	* Constructor based upon a builder
	* @param builder - contains initialization values
	*/
	public CandidateRoleStatsView(FUNCTION function, long availableCandidates, long unavailableCandidates) {
		this.function 				= function;
		this.availableCandidates 	= availableCandidates;
		this.unavailableCandidates 	= unavailableCandidates;
	}
	
	/**
	* Returns the Function the Candidates perform
	* @return Function the Candidates perform
	*/
	public FUNCTION getFunction() {
		return this.function;
	}
	
	/**
	* Returns the number of available candidates that 
	* perform the function
	* @return available Candidates
	*/
	public long getAvailableCandidates() {
		return this.availableCandidates;
	}
	
	/**
	* Returns the number of unavailable candidates that 
	* perform the function
	* @return unavailable Candidates
	*/
	public long getUnavailableCandidates() {
		return this.unavailableCandidates;
	}
	
	/**
	* Returns a Builder for the CandidateRoleStats class 
	* @return Builder
	*/
	public static CandidateRoleStatsBuilder builder() {
		return new CandidateRoleStatsBuilder();
	}
	
	/**
	* Converts from View to Domain representation
	* @param view - View Representation of Stats
	* @return Domain representation of Stats
	*/
	public static CandidateRoleStats convertFromView(CandidateRoleStatsView view) {
		
		return CandidateRoleStats
							.builder()
								.function(view.getFunction())
								.availableCandidates(view.getAvailableCandidates())
								.unavailableCandidates(view.getUnavailableCandidates())
							.build();
		
	}
	
}
