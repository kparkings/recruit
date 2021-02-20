package com.arenella.recruit.beans;

import java.time.LocalDate;

/**
* Class represents a Recruitment Candidate. A Candidate that 
* can be place on a project
* @author K Parkings
*/
public class Candidate {

	public static enum COUNTRY {NETHERLANDS, BELGIUM, UK}
	
	private String 		candidateId;
	private COUNTRY 	country;
	private String 		city;
	private boolean 	perm;
	private boolean 	freelance;
	private int			yearsExperience;
	private boolean 	available;
	private LocalDate 	registerd;
	private LocalDate 	lastAvailabilityCheck;
	
	/**
	* Returns the unique identifier of the Candidate
	* @return unique Id of the candidate
	*/
	public String getCandidateId() {
		return candidateId;
	}
	
	/**
	* Returns the Country in which the candidate is 
	* located
	* @return Country 
	*/
	public COUNTRY getCountry() {
		return country;
	}
	
	/**
	* Returns the City where the Candidate is located
	* @return Name of City where Candidate is located
	*/
	public String city() {
		return city;
	}
	
	/**
	* 
	* @return
	*/
	public boolean isFreelance() {
		return freelance;
	}
	
	/**
	* 
	* @return
	*/
	public boolean isPerm() {
		return perm;
	}
	
}
