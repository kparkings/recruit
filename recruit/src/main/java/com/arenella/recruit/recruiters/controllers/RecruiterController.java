package com.arenella.recruit.recruiters.controllers;

import java.util.Set;

import com.arenella.recruit.recruiters.beans.RecruiterAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterAPIOutbound;

/**
* REST API for interacting with Recruiters
* @author K Parkings
*/
public class RecruiterController {

	/**
	* Adds a new Recruiter
	* @param recruiter
	*/
	public void addRecruiter(RecruiterAPIInbound recruiter) {
		
	}
	
	/**
	* Updates an existing recruiter
	* @param recruiter
	*/
	public void updateRecruiter(RecruiterAPIInbound recruiter) {
		
	}
	
	/**
	* Returns a Collection of Recruiters
	* @return
	*/
	public Set<RecruiterAPIOutbound> fetchRecruiters(){
		return Set.of();
	}
	
}