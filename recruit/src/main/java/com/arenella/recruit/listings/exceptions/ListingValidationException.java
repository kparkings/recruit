package com.arenella.recruit.listings.exceptions;

import java.util.LinkedHashMap;
import java.util.Map;

/**
* Exception class for validation Exceptions relating to Listings
* @author K Parkings
*/
public class ListingValidationException extends RuntimeException{

	private static final long serialVersionUID = 1835770554635514392L;
	
	private Map<String,String> failedFields = new LinkedHashMap<>();
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains the initialiaztion values
	*/
	public ListingValidationException(ListingValidationExceptionBuilder builder) {
		this.failedFields = new LinkedHashMap<>();
		this.failedFields.putAll(builder.failedFields);
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
	public Map<String,String> getFailedFields(){
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
		
		private Map<String,String> failedFields = new LinkedHashMap<>();
		
		/**
		* Adds details of the Field with the failed validation
		* @param fieldName		- Name of the field that failed validation
		* @param keyOrMessage	- Key or message indication the reason the validation failed
		* @return Builder
		*/
		public ListingValidationExceptionBuilder addFailedValidationField(String fieldName, String keyOrMessage) {
			failedFields.put(fieldName, keyOrMessage);
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
	
}