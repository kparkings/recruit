package com.arenella.recruit.campaigns.controllers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Command to create a new Role for a Campaign 
*/
@JsonDeserialize(builder=NewRoleAPIInbound.NewRoleAPIInboundBuilder.class)
public class NewRoleAPIInbound {

	private String 				name;
	private String 				description;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains the initialization values
	*/
	public NewRoleAPIInbound(NewRoleAPIInboundBuilder builder) {
		this.name 			= builder.name;
		this.description 	= builder.description;
	}
	
	/**
	* Returns the name of the Campaign
	* @return name of the Campaign
	*/
	public String getName() {
		return this.name;
	}
	
	/**
	* Returns the Campaign description
	* @return description
	*/
	public String getDescription() {
		return this.description;
	}
	
	/**
	* Returns a Builder for the class
	* @return Builder
	*/
	public static NewRoleAPIInboundBuilder builder() {
		return new NewRoleAPIInboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class NewRoleAPIInboundBuilder {
		
		private String 				name;
		private String 				description;
		
		/**
		* Sets the name of the Campaign
		* @param name - Name of the Campaign
		* @return Builder
		*/
		public NewRoleAPIInboundBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		* Sets the Campaign description
		* @param description - Description of the Campaign 
		* @return Builder
		*/
		public NewRoleAPIInboundBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Returns a new initialized instance of the class
		* @return Initialized instance
		*/
		public NewRoleAPIInbound build() {
			return new NewRoleAPIInbound(this);
		}
		
	}
	
}