package com.arenella.recruit.recruiters.controllers;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.arenella.recruit.recruiters.utils.OfferedCandidateValidator;
import com.arenella.recruit.recruiters.utils.OpenPositionValidator;

/**
* Exception Handler from Spring. 
* @author K Parkings
*/
@ControllerAdvice
public class ControllerAdviceHandler {
	
	/**
	* Handles the AttributesValidationException exception
	* @param ex - actual Exception
	* @return ResponseEntity
	*/
	@ExceptionHandler(value={OfferedCandidateValidator.AttributesValidationException.class})
	public ResponseEntity<Set<OfferedCandidateValidator.AttributeIssue>> handleAttributesValidationExceptionOfferedCandidateValidatorAttributesValidationException(OfferedCandidateValidator.AttributesValidationException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getValidationIssues());
	}

	/**
	* Handles the AttributesValidationException exception
	* @param ex - actual Exception
	* @return ResponseEntity
	*/
	@ExceptionHandler(value={OpenPositionValidator.AttributesValidationException.class})
	public ResponseEntity<Set<OpenPositionValidator.AttributeIssue>> handleAttributesValidationExceptionOpenPositionValidatorAttributesValidationException(OpenPositionValidator.AttributesValidationException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getValidationIssues());
	}
}
