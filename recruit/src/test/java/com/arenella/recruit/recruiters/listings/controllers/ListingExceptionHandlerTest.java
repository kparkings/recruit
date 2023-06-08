package com.arenella.recruit.recruiters.listings.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.listings.controllers.ListingExceptionHandler;
import com.arenella.recruit.listings.exceptions.ListingValidationException;
import com.arenella.recruit.listings.exceptions.ListingValidationException.FailedField;

/**
* Unit tests for the ListingExceptionHandler class
* @author K Parkings
*/
public class ListingExceptionHandlerTest {

	private ListingExceptionHandler handler = new ListingExceptionHandler();
	
	/**
	* Tests the expected ResponseEntity is returned if a ListingValidationException
	* is thrown
	* @throws Exception
	*/
	@Test
	public void testHandleListingValidationException() throws Exception{
		
		final String field1 				= "f1";
		final String field1MessageOrKey 	= "f1Key";
		final String field2 				= "f2";
		final String field2MessageOrKey 	= "f2Key";
		
		ListingValidationException exception = ListingValidationException
																	.builder()
																		.addFailedValidationField(field1, field1MessageOrKey)
																		.addFailedValidationField(field2, field2MessageOrKey)
																	.build();
		
		ResponseEntity<Set<FailedField>> response = handler.handleListingValidationException(exception);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(exception.getFailedFields().stream().filter(f -> f.getFieldName().equals(field1)).findAny().isPresent());
		assertTrue(exception.getFailedFields().stream().filter(f -> f.getFieldName().equals(field2)).findAny().isPresent());
		assertEquals(exception.getFailedFields().stream().filter(f -> f.getFieldName().equals(field1)).findAny().get().getFieldMessageOrKey(), field1MessageOrKey);
		assertEquals(exception.getFailedFields().stream().filter(f -> f.getFieldName().equals(field2)).findAny().get().getFieldMessageOrKey(), field2MessageOrKey);
		
		
	}
	
}
