package com.arenella.recruit.candidates.controllers;

/**
* Exception specific to Canddate Validation issues
* @author K Parkings
*/
public class CandidateValidationException extends RuntimeException{

	private static final long serialVersionUID = -1881534918834785174L;

	/**
	* Constructor
	* @param message
	*/
	public CandidateValidationException(String message) {
			super(message);
	}
	
}
