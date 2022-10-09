package com.arenella.recruit.recruiters.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIInbound;
import com.arenella.recruit.recruiters.utils.OfferedCandidateValidator.AttributesValidationException;

/**
* Unit tests for the OfferedCandidateValidator class
* @author K Parkings
*/
public class OfferedCandidateValidatorTest {

	/**
	* Tests no Exception thrown when no issues found
	*/
	@Test
	public void testValidate_noIssues() {
		
		OfferedCandidateAPIInbound candidate = OfferedCandidateAPIInbound.builder().candidateRoleTitle("Java Developer").build(); 
		
		OfferedCandidateValidator.validate(candidate);
		
	}
	
	/**
	* Tests Exception thrown when an empty candidateRoleTitle is  provided
	*/
	@Test
	public void testValidate_emptyCandidateRoleTitle() {
		
		OfferedCandidateAPIInbound candidate = OfferedCandidateAPIInbound.builder().candidateRoleTitle(" ").build(); 
		
		assertThrows(AttributesValidationException.class, () ->{
			OfferedCandidateValidator.validate(candidate);
		});
		
		
	}

	/**
	* Tests Exception thrown when an candidateRoleTitle is over 150 chars
	*/
	@Test
	public void testValidate_tooLongCandidateRoleTitle() {
		
		OfferedCandidateAPIInbound candidate = OfferedCandidateAPIInbound.builder().candidateRoleTitle("12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254A12345678901254AB").build(); 
		
		assertThrows(AttributesValidationException.class, () ->{
			OfferedCandidateValidator.validate(candidate);
		});
		
		
	}
	
	/**
	* Tests Exception thrown when candidateRoleTitle is not provided
	*/
	@Test
	public void testValidate_noCandidateRoleTitle() {
		
		OfferedCandidateAPIInbound candidate = OfferedCandidateAPIInbound.builder().build(); 
		
		assertThrows(AttributesValidationException.class, () ->{
			OfferedCandidateValidator.validate(candidate);
		});
		
	}
	
}
