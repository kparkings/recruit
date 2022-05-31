package com.arenella.recruit.recruiters.beans;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Represents an Open Position that needs to be filled and 
* is being offered to other Recruiters
* @author K Parkings
*/
public class OpenPositionAPIOutbound {

	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	private OpenPositionAPIOutbound(OpenPositionAPIOutboundBuilder builder) {
		
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder for the Class
	*/
	public static OpenPositionAPIOutboundBuilder builder() {
		return new OpenPositionAPIOutboundBuilder();
	} 
	
	/**
	* Builder for the OpenPositionAPIOutbound class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class OpenPositionAPIOutboundBuilder{
		
		/**
		* Returns an instance of OpenPositionAPIOutbound initalized with 
		* the values in the Builder
		* @return Initialzied instance of OpenPositionAPIOutbound
		*/
		public OpenPositionAPIOutbound build() {
			return new OpenPositionAPIOutbound(this);
		}
		
	}
	
}
