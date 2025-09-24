package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
* Unit tests for the CandidateExceptionHandler class
* @author K Parkings
*/
public class CandidateExceptionHandlerTest {

	private CandidateExceptionHandler handler = new CandidateExceptionHandler();
	
	/**
	* Tests response for CandidateValidationException
	* @throws Exception
	*/
	@Test
	public void testHandleCandidateValidationException() {
		
		final String msg = "Exception details messaeg";
		
		ResponseEntity<String> res = handler.handleCandidateValidationException(new CandidateValidationException(msg));
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, res.getStatusCode());
		assertEquals(msg, res.getBody());
	}
	
}
