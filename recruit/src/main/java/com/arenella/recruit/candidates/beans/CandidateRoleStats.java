package com.arenella.recruit.candidates.beans;

import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Statistics for the number of available candidates
* per function
* @author K Parkings
*/
public class CandidateRoleStats {

	private FUNCTION 		function;
	private long 			availableCandidates;
	
	/**
	* Constructor based upon a builder
	* @param builder - contains initialization values
	*/
	public CandidateRoleStats(CandidateRoleStatsBuilder builder) {
		this.function 				= builder.function;
		this.availableCandidates 	= builder.availableCandidates;
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
	* Returns a Builder for the CandidateRoleStats class 
	* @return Builder
	*/
	public static CandidateRoleStatsBuilder builder() {
		return new CandidateRoleStatsBuilder();
	}
	
	/**
	* Builder for the CandidateRoleStats class
	* @author K Parkings
	*/
	public static class CandidateRoleStatsBuilder {
	
		private FUNCTION 		function;
		private long 			availableCandidates;
		
		/**
		* Sets the function
		* @param function - type of function the user performs
		* @return Builder
		*/
		public CandidateRoleStatsBuilder function(FUNCTION function) {
			this.function = function;
			return this;
		}
		
		/**
		* Sets the number of candidates available for the function
		* @param availableCandidates
		* @return Builder
		*/
		public CandidateRoleStatsBuilder availableCandidates(long availableCandidates) {
			this.availableCandidates  = availableCandidates;
			return this;
		}
		
		/**
		* Return an initialized instance of CandidateRoleStats
		* @return Initialized instance of the class
		*/
		public CandidateRoleStats build() {
			return new CandidateRoleStats(this);
		}
		
	}
	
}