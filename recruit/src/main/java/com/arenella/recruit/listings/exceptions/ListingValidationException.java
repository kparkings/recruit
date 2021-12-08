package com.arenella.recruit.listings.exceptions;

import java.util.LinkedHashSet;
import java.util.Set;

/**
* Exception class for validation Exceptions relating to Listings
* @author K Parkings
*/
public class ListingValidationException extends RuntimeException{

	private static final long serialVersionUID = 1835770554635514392L;
	
	private Set<FailedField> failedFields = new LinkedHashSet<>();
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains the initialiaztion values
	*/
	public ListingValidationException(ListingValidationExceptionBuilder builder) {
		this.failedFields = new LinkedHashSet<>();
		this.failedFields.addAll(builder.failedFields);
	}
	
	/**
	* Returns whether or not at least one Failed field was logged
	* @return Whether of not failed fields where logged
	*/
	public boolean hasFailedFields() {
		return !this.failedFields.isEmpty();
	}
	
	/**
	* Returns the failed fields
	* @return failed fields
	*/
	public Set<FailedField> getFailedFields(){
		return this.failedFields;
	}
	
	/**
	* Returns a Builder for the Exception
	* @return
	*/
	public static ListingValidationExceptionBuilder builder() {
		return new ListingValidationExceptionBuilder();
	}
	
	/**
	* Builder class for the ListingValidationException 
	* @author K Parkings
	*/
	public static class ListingValidationExceptionBuilder{
		
		private Set<FailedField> failedFields = new LinkedHashSet<>();
		
		/**
		* Adds details of the Field with the failed validation
		* @param fieldName		- Name of the field that failed validation
		* @param keyOrMessage	- Key or message indication the reason the validation failed
		* @return Builder
		*/
		public ListingValidationExceptionBuilder addFailedValidationField(String fieldName, String keyOrMessage) {
			failedFields.add(new FailedField(fieldName, keyOrMessage));
			return this;
		}
		
		/**
		* Returns an initialized instance of ListingValidationException
		* @return instance of ListingValidationException
		*/
		public ListingValidationException build() {
			return new ListingValidationException(this);
		}
		
	}
	
	/**
	* Represents a single Field validation failure
	* @author K Parkings
	*/
	public static class FailedField{
		
		final String fieldName;
		final String fieldMessageOrKey;
		
		/**
		* Constructor
		* @param fieldName			- Name of Field with validation error
		* @param fieldMessageOrKey	- Message or Key description validation issue
		*/
		public FailedField(String fieldName, String fieldMessageOrKey) {
			this.fieldName 			= fieldName;
			this.fieldMessageOrKey 	= fieldMessageOrKey;
		}
		
		/**
		* Returns the Name of Field with validation error
		* @return Name of Field with validation error
		*/
		public String getFieldName() {
			return this.fieldName;
		}
		
		/**
		* Returns the Message or Key description validation issue
		* @return Message or Key description validation issue
		*/
		public String getFieldMessageOrKey() {
			return this.fieldMessageOrKey;
		}

		
	}
	
}