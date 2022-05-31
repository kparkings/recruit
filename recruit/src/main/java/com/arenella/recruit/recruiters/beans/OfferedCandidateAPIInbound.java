package com.arenella.recruit.recruiters.beans;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Represents an incoming request from a Recruiter to 
* create a new Offered Candidate that needs to be placed and 
* is being offered to other Recruiters
* @author K Parkings
*/
@JsonDeserialize(builder=OfferedCandidateAPIInbound.OfferedCandidateAPIInboundBuilder.class)
public class OfferedCandidateAPIInbound {

	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	private OfferedCandidateAPIInbound(OfferedCandidateAPIInboundBuilder builder) {
		
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder for the Class
	*/
	public static OfferedCandidateAPIInboundBuilder builder() {
		return new OfferedCandidateAPIInboundBuilder();
	} 
	
	/**
	* Builder for the OfferedCandidateAPIInbound class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class OfferedCandidateAPIInboundBuilder{
		
		/**
		* Returns an instance of OfferedCandidateAPIInbound initalized with 
		* the values in the Builder
		* @return Initialzied instance of OfferedCandidateAPIInbound
		*/
		public OfferedCandidateAPIInbound build() {
			return new OfferedCandidateAPIInbound(this);
		}
		
	}
}
