package com.arenella.recruit.recruiters.listings.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.listings.controllers.ListingExceptionHandler;
import com.arenella.recruit.listings.exceptions.ListingValidationException;

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
		
		ResponseEntity<Map<String,String>> response = handler.handleListingValidationException(exception);
		
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
		assertTrue(response.getBody().containsKey(field1));
		assertTrue(response.getBody().containsKey(field2));
		assertEquals(response.getBody().get(field1), field1MessageOrKey);
		assertEquals(response.getBody().get(field2), field2MessageOrKey);
		
		
	}
	
}
