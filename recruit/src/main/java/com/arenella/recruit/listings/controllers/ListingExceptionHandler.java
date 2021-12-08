package com.arenella.recruit.listings.controllers;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.arenella.recruit.listings.exceptions.ListingValidationException;
import com.arenella.recruit.listings.exceptions.ListingValidationException.FailedField;

/**
* Exception Handlers specific to Listings
* @author K Parkings
*/
@ControllerAdvice
public class ListingExceptionHandler {

	/**
	* Catches and returns ListingValidationException to the API CLient 
	* @param exception - Contains info about the fields that failed validation
	* @return Exception
	*/
	@ExceptionHandler
	public ResponseEntity<Set<FailedField>> handleListingValidationException(ListingValidationException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getFailedFields());
	}
}
