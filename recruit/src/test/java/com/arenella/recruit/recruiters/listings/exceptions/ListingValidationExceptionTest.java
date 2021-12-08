package com.arenella.recruit.recruiters.listings.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.exceptions.ListingValidationException;

/**
* Unit tests for the ListingValidationException class
* @author K Parkings
*/
public class ListingValidationExceptionTest {

	/**
	* Tests creation of Exception via Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		final String field1 				= "f1";
		final String field1MessageOrKey 	= "f1Key";
		final String field2 				= "f2";
		final String field2MessageOrKey 	= "f2Key";
		
		
		
		ListingValidationException exception = ListingValidationException
																	.builder()
																		.addFailedValidationField(field1, field1MessageOrKey)
																		.addFailedValidationField(field2, field2MessageOrKey)
																	.build();
		
		assertTrue(exception.getFailedFields().containsKey(field1));
		assertTrue(exception.getFailedFields().containsKey(field2));
		assertEquals(exception.getFailedFields().get(field1), field1MessageOrKey);
		assertEquals(exception.getFailedFields().get(field2), field2MessageOrKey);
		
	}
	
	/**
	* Tests hasFailedFields method returns true if failed fields
	* exist
	* @throws Exception
	*/
	@Test
	public void testHasFailedFields() throws Exception {
		
		final String field1 				= "f1";
		final String field1MessageOrKey 	= "f1Key";
		final String field2 				= "f2";
		final String field2MessageOrKey 	= "f2Key";
		
		
		
		ListingValidationException exception = ListingValidationException
																	.builder()
																		.addFailedValidationField(field1, field1MessageOrKey)
																		.addFailedValidationField(field2, field2MessageOrKey)
																	.build();
		
		assertTrue(exception.hasFailedFields());
		assertFalse(ListingValidationException.builder().build().hasFailedFields());
		
	}
	
}