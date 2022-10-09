package com.arenella.recruit.recruiters.utils;

import java.util.LinkedHashSet;
import java.util.Set;

import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIInbound;

/**
* Validator for OfferedCandidates
* @author K Parkings
*/
public class OfferedCandidateValidator {

	public static int 		CANDIDATE_ROLE_MAX_LENGTH 		= 150;
	public static String 	ATTR_CANDODIDATE_FUNC 			= "Candidates Function";
	public static String 	MISSING_CANDIDATE_FUNCTION 		= "You must provide the Candidates Function";
	public static String 	EMPTY_CANDIDATE_FUNCTION 		= "Candidate Function must not exceed";
	
	/**
	* Performs validation of an Offered Candidate
	* @param candidate - To be validated
	*/
	public static void validate(OfferedCandidateAPIInbound candidate) {
		
		AttributesValidationException exception = new AttributesValidationException();
		
		if (candidate.getcandidateRoleTitle().isBlank()) {
			exception.addError(ATTR_CANDODIDATE_FUNC, MISSING_CANDIDATE_FUNCTION);
		}
		
		if (!candidate.getcandidateRoleTitle().isBlank() && candidate.getcandidateRoleTitle().length() > CANDIDATE_ROLE_MAX_LENGTH) {
			exception.addError(ATTR_CANDODIDATE_FUNC, EMPTY_CANDIDATE_FUNCTION + CANDIDATE_ROLE_MAX_LENGTH);
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