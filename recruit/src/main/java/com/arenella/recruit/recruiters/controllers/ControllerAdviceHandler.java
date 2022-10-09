package com.arenella.recruit.recruiters.controllers;

import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.arenella.recruit.recruiters.utils.OfferedCandidateValidator.AttributeIssue;
import com.arenella.recruit.recruiters.utils.OfferedCandidateValidator.AttributesValidationException;

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
	@ExceptionHandler(value={AttributesValidationException.class})
	public ResponseEntity<Set<AttributeIssue>> handleAttributesValidationException(AttributesValidationException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getValidationIssues());
	}

}
