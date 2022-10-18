package com.arenella.recruit.recruiters.utils;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import com.arenella.recruit.recruiters.beans.OpenPositionAPIInbound;

/**
* Validator for OfferedCandidates
* @author K Parkings
*/
public class OpenPositionValidator {

	public static int 		OPEN_POSITION_TITLE_MAX_LENGTH 		= 150;
	public static String 	ATTR_POSITION_TITLE 				= "Position Title";
	public static String 	MISSING_POSITION_TITLE	 			= "You must provide the Candidates Function";
	public static String 	POSITION_TITLE_GT_MAX	 			= "Position title must not exceed ";
	
	public static int 		RENUMERATION_MAX_LENGTH 			= 150;
	public static String 	ATTR_RENUMERATION 					= "Renumeration";
	public static String 	RENUMERATION_GT_MAX	 				= "Renumeration must not exceed ";
	
	public static int 		LOCATION_MAX_LENGTH 				= 150;
	public static String 	ATTR_LOCATION 						= "Location";
	public static String 	LOCATION_GT_MAX	 					= "Location must not exceed ";
	
	/**
	* Performs validation of an OpenPosition
	* @param candidate - To be validated
	*/
	public static void validate(OpenPositionAPIInbound openPosition) {
		
		AttributesValidationException exception = new AttributesValidationException();
		
		if (Optional.ofNullable(openPosition.getPositionTitle()).isEmpty() || openPosition.getPositionTitle().isBlank()) {
			exception.addError(ATTR_POSITION_TITLE, MISSING_POSITION_TITLE);
		}
		
		if (Optional.ofNullable(openPosition.getPositionTitle()).isPresent() && openPosition.getPositionTitle().length() > OPEN_POSITION_TITLE_MAX_LENGTH) {
			exception.addError(ATTR_POSITION_TITLE, POSITION_TITLE_GT_MAX + OPEN_POSITION_TITLE_MAX_LENGTH);
		}
		
		if (Optional.ofNullable(openPosition.getRenumeration()).isPresent() && openPosition.getRenumeration().length() > RENUMERATION_MAX_LENGTH) {
			exception.addError(ATTR_RENUMERATION, RENUMERATION_GT_MAX + RENUMERATION_MAX_LENGTH);
		}
		
		if (Optional.ofNullable(openPosition.getLocation()).isPresent() && openPosition.getLocation().length() > LOCATION_MAX_LENGTH) {
			exception.addError(ATTR_LOCATION, LOCATION_GT_MAX + LOCATION_MAX_LENGTH);
		}
		
		if (exception.hasValidationIssues()) {
			throw exception;
		}
		
	} 
	
	/**
	* Validation Exception for an object requiring validation for multiple
	* attributes at once
	* @author K Parkings
	*/
	public static class AttributesValidationException extends RuntimeException{

		private static final long serialVersionUID = 2351352671765825396L;
		
		private Set<AttributeIssue> errors = new LinkedHashSet<>();
		
		@Override
		public String getMessage() {
			return "Offered Candidate Validation Issues";
		}
		
		/**
		* Adds a validation error
		* @param attribute - Attribute that was invalid
		* @param message   - Description of the issue
		*/
		public void addError(String attribute, String message) {
			this.errors.add(new AttributeIssue(attribute, message));
		}
		
		/**
		* Returns details of validation failures
		* @return validation failure details
		*/
		public Set<AttributeIssue> getValidationIssues(){
			return this.errors;
		}
		
		/**
		* Returns whether or not there was at least one validation issue
		* @return Whether or not there was at least one validation issue
		*/
		public boolean hasValidationIssues() {
			return !this.errors.isEmpty();
		}
		
	}
	
	/**
	* Represents an issue of an individual attribute
	* @author K Parkings
	*/
	public static class AttributeIssue{
		
		private final String attribute;
		private final String issue;
		
		/**
		* Constructor
		* @param attribute 	- Attribute with issue
		* @param issue		- issue description
		*/
		public AttributeIssue(String attribute, String issue) {
			this.attribute 	= attribute;
			this.issue 		= issue;
		}

		/**
		* Returns the attribute with the issue
		* @return attribute
		*/
		public String getAttribute(){
			return this.attribute;
		}
		
		/**
		* Returns the issue with the attribute
		* @return issue
		*/
		public String getIssue() {
			return this.issue;
		}
	}
}