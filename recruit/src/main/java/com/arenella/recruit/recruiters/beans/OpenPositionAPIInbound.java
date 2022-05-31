package com.arenella.recruit.recruiters.beans;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Represents an incoming request from a Recruiter to 
* create a new OpenPosition that needs to be filled and 
* is being offered to other Recruiters
* @author K Parkings
*/
@JsonDeserialize(builder=OpenPositionAPIInbound.OpenPositionAPIInboundBuilder.class)
public class OpenPositionAPIInbound {

	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	private OpenPositionAPIInbound(OpenPositionAPIInboundBuilder builder) {
		
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder for the Class
	*/
	public static OpenPositionAPIInboundBuilder builder() {
		return new OpenPositionAPIInboundBuilder();
	} 
	
	/**
	* Builder for the OpenPositionAPIInbound class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class OpenPositionAPIInboundBuilder{
		
		/**
		* Returns an instance of OpenPositionAPIInbound initalized with 
		* the values in the Builder
		* @return Initialzied instance of OpenPositionAPIInbound
		*/
		public OpenPositionAPIInbound build() {
			return new OpenPositionAPIInbound(this);
		}
		
	}
	
}
