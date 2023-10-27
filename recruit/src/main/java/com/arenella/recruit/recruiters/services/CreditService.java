package com.arenella.recruit.recruiters.services;

/**
* Defines functins relating to Recruiter credits
* @author K Parkings
*/
public interface CreditService {

	/**
	* Each month Recruiters receive credits to perform certain actions in the 
	* system. This method initiates that process
	*/
	public void grantMonthlyCreditsToRecruiters();
	
}
