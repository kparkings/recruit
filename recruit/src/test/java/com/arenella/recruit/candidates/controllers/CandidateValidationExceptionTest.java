package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CandidateValidationException class 
* @author K Parkings
*/
public class CandidateValidationExceptionTest {

	/**
	* Tests construction of message
	* @throws Exception
	*/
	@Test
	public void testMessage() throws Exception{
		
		final String message = "exeption message";
		
		CandidateValidationException ex = new CandidateValidationException(message);
		
		assertEquals(message , ex.getMessage());
		
	}
	
}
