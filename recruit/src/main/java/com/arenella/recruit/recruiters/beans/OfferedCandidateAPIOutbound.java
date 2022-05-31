package com.arenella.recruit.recruiters.beans;

/**
* Represents an Offered Candidate that needs to be placed and 
* is being offered to other Recruiters
* @author K Parkings
*/
public class OfferedCandidateAPIOutbound {

	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	private OfferedCandidateAPIOutbound(OfferedCandidateAPIOutboundBuilder builder) {
		
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder for the Class
	*/
	public static OfferedCandidateAPIOutboundBuilder builder() {
		return new OfferedCandidateAPIOutboundBuilder();
	} 
	
	/**
	* Builder for the OfferedCandidateAPIOutbound class
	* @author K Parkings
	*/
	public static class OfferedCandidateAPIOutboundBuilder{
		
		/**
		* Returns an instance of OfferedCandidateAPIOutbound initalized with 
		* the values in the Builder
		* @return Initialized instance of OfferedCandidateAPIOutbound
		*/
		public OfferedCandidateAPIOutbound build() {
			return new OfferedCandidateAPIOutbound(this);
		}
		
	}
	
}
