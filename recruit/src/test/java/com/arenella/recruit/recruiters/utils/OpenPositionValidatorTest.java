package com.arenella.recruit.recruiters.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.OpenPositionAPIInbound;
import com.arenella.recruit.recruiters.utils.OpenPositionValidator.AttributesValidationException;

/**
* Unit tests for the OfferedCandidateValidator class
* @author K Parkings
*/
public class OpenPositionValidatorTest {

	/**
	* Tests no Exception thrown when no issues found
	*/
	@Test
	public void testValidate_noIssues() {
		
		OpenPositionAPIInbound openPosition = OpenPositionAPIInbound.builder().positionTitle("Java Developer").build(); 
		
		OpenPositionValidator.validate(openPosition);
		
	}
	
	/**
	* Tests Exception thrown when an empty positionTitle is  provided
	*/
	@Test
	public void testValidate_emptyPositionTitle() {
		
		OpenPositionAPIInbound openPosition = OpenPositionAPIInbound.builder().positionTitle(" ").build(); 
		
		assertThrows(AttributesValidationException.class, () ->{
			OpenPositionValidator.validate(openPosition);
		});
		
		
	}

	/**
	* Tests Exception thrown when a positionTitle is over 150 chars
	*/
	@Test
	public void testValidate_tooLongPositionTitle() {
		
		OpenPositionAPIInbound openPosition = OpenPositionAPIInbound.builder().positionTitle("12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254AB").build(); 
		
		assertThrows(AttributesValidationException.class, () ->{
			OpenPositionValidator.validate(openPosition);
		});
		
		
	}
	
	/**
	* Tests Exception thrown when an Renumeration is over 150 chars
	*/
	@Test
	public void testValidate_tooLongRenumeration() {
		
		OpenPositionAPIInbound openPosition = OpenPositionAPIInbound.builder().renumeration("12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254AB").build(); 
		
		assertThrows(AttributesValidationException.class, () ->{
			OpenPositionValidator.validate(openPosition);
		});
		
		
	}
	
	/**
	* Tests Exception thrown when a Location is over 150 chars
	*/
	@Test
	public void testValidate_tooLongLocation() {
		
		OpenPositionAPIInbound openPosition = OpenPositionAPIInbound.builder().location("12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254AB").build(); 
		
		assertThrows(AttributesValidationException.class, () ->{
			OpenPositionValidator.validate(openPosition);
		});
		
		
	}
	
	/**
	* Tests Exception thrown when positionTitle is not provided
	*/
	@Test
	public void testValidate_noCandidateRoleTitle() {
		
		OpenPositionAPIInbound openPosition = OpenPositionAPIInbound.builder().build(); 
		
		assertThrows(AttributesValidationException.class, () ->{
			OpenPositionValidator.validate(openPosition);
		});
		
	}
	
}
