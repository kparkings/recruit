package com.arenella.recruit.recruiters.services;

import java.util.Set;

import com.arenella.recruit.recruiters.beans.Recruiter;

/**
* Defines functionality related to services for Recruiters
* @author K Parkings
*/
public interface RecruiterService {

	/**
	* Adds a new Recruiter 
	* @param recruiter - Recruiter to add
	*/
	public void addRecruiter(Recruiter recruiter);
	
	/**
	* Updates an existing Recruiter 
	* @param recruiter - Recruiter to update
	*/
	public void updateRecruiter(Recruiter recruiter);
	
	/**
	* Returns all Recruiters
	* @return all Recruiters
	*/
	public Set<Recruiter> fetchRecruiters();
	
	/**
	* Returns a specific Recruiter based upon the 
	* associated userId
	* @param recruiterId - Unique id of the Recruiter to return
	* @return UserId of the Recruiter
	* @throws IllegalAccessException
	*/
	public Recruiter fetchRecruiter(String recruiterId) throws IllegalAccessException;
	
	/**
	* Returns the Recruiter details for the currently logged in Recruiter
	* @return Recruiter
	* @throws IllegalAccessException
	*/
	public Recruiter fetchRecruiterOwnAccount()  throws IllegalAccessException;
	
}