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
class ListingValidationExceptionTest {

	/**
	* Tests creation of Exception via Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		final String field1 				= "f1";
		final String field1MessageOrKey 	= "f1Key";
		final String field2 				= "f2";
		final String field2MessageOrKey 	= "f2Key";
		
		ListingValidationException exception = ListingValidationException
																	.builder()
																		.addFailedValidationField(field1, field1MessageOrKey)
																		.addFailedValidationField(field2, field2MessageOrKey)
																	.build();
		
		assertTrue(exception.getFailedFields().stream().filter(f -> f.getFieldName().equals(field1)).findAny().isPresent());
		assertTrue(exception.getFailedFields().stream().filter(f -> f.getFieldName().equals(field2)).findAny().isPresent());
		assertEquals(exception.getFailedFields().stream().filter(f -> f.getFieldName().equals(field1)).findAny().get().getFieldMessageOrKey(), field1MessageOrKey);
		assertEquals(exception.getFailedFields().stream().filter(f -> f.getFieldName().equals(field2)).findAny().get().getFieldMessageOrKey(), field2MessageOrKey);
		
	}
	
	/**
	* Tests hasFailedFields method returns true if failed fields
	* exist
	* @throws Exception
	*/
	@Test
	void testHasFailedFields() {
		
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