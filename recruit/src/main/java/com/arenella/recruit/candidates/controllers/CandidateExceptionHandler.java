package com.arenella.recruit.candidates.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
* Exception Handling for Candidate specific exception
* @author K Parkings
*/
@RestControllerAdvice
public class CandidateExceptionHandler {
	
	/**
	* Validation Exception for Candidates
	* @param ex - Exception
	* @return ResponseEntity
	*/
	@ExceptionHandler(CandidateValidationException.class)
	public ResponseEntity<String> handleCandidateValidationException(CandidateValidationException ex){
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
	}
	
}
