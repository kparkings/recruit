package com.arenella.recruit.candidates.controllers;

import com.arenella.recruit.candidates.beans.CandidateRoleStats;
import com.arenella.recruit.candidates.enums.FUNCTION;

public class CandidateRoleStatsAPIOutbound {

	private String	 		function;
	private long 			availableCandidates;
	
	/**
	* Constructor based upon a builder
	* @param builder - contains initialization values
	*/
	public CandidateRoleStatsAPIOutbound(CandidateRoleStatsAPIOutboundBuilder builder) {
		this.function 				= builder.function.getDesc();
		this.availableCandidates 	= builder.availableCandidates;
	}
	
	/**
	* Returns the Function the Candidates perform
	* @return Function the Candidates perform
	*/
	public String getFunction() {
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
	public static CandidateRoleStatsAPIOutboundBuilder builder() {
		return new CandidateRoleStatsAPIOutboundBuilder();
	}
	
	/**
	* Builder for the CandidateRoleStats class
	* @author K Parkings
	*/
	public static class CandidateRoleStatsAPIOutboundBuilder {
	
		private FUNCTION 		function;
		private long 			availableCandidates;
		
		/**
		* Sets the function
		* @param function - type of function the user performs
		* @return Builder
		*/
		public CandidateRoleStatsAPIOutboundBuilder function(FUNCTION function) {
			this.function = function;
			return this;
		}
		
		/**
		* Sets the number of candidates available for the function
		* @param availableCandidates
		* @return Builder
		*/
		public CandidateRoleStatsAPIOutboundBuilder availableCandidates(long availableCandidates) {
			this.availableCandidates  = availableCandidates;
			return this;
		}
		
		/**
		* Return an initialized instance of CandidateRoleStats
		* @return Initialized instance of the class
		*/
		public CandidateRoleStatsAPIOutbound build() {
			return new CandidateRoleStatsAPIOutbound(this);
		}
		
	}
	
	/**
	* Converts from Domain to API representation
	* @param stat - Domain Representation of Stats
	* @return API representation of Stats
	*/
	public static CandidateRoleStatsAPIOutbound convertFromDomain(CandidateRoleStats stat) {
		
		return CandidateRoleStatsAPIOutbound
							.builder()
								.function(stat.getFunction())
								.availableCandidates(stat.getAvailableCandidates())
							.build();
		
	}
}
