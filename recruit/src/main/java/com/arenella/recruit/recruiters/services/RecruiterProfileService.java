package com.arenella.recruit.recruiters.services;

import java.util.Optional;
import java.util.Set;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.RecruiterProfile;
import com.arenella.recruit.recruiters.beans.RecruiterProfileFilter;

/**
* Defines Services for interaction with Recruiter Profiles
* @author K Parkings
*/
public interface RecruiterProfileService {

	/**
	* Adds a new Recruiter Profile 
	* @param recruiterProfile
	*/
	public void addRecruiterProfile(RecruiterProfile recruiterProfile);
	
	/**
	* Updates an existing Recruiter Profile
	* @param recruiterProfile
	*/
	public void updateRecruiterProfile(RecruiterProfile recruiterProfile);
	
	/***
	* Returns RecruiterProfile matching the id
	* @param recruiter - Recruiter the profile belongs to
	* @return Recruiter Profile
	*/
	public Optional<RecruiterProfile> fetchRecruiterProfile(Recruiter recruiter);
	
	/**
	* Returns Recruiter Profiles matching the filters
	* @param filters - Filter to apply
	* @return matching Recruiter Profiles
	*/
	public Set<RecruiterProfile> fetchRecruiterProfiles(RecruiterProfileFilter filters);

	/**
	* Sends an email from the authorized user to the recruiter
	* @param message		- Message to send
	* @param title			- title of the messsage
	* @param recruiterId	- Id of recipient recruiter
	* @param name			- Id of sender recruiter
	*/
	public void sendEmailToRecruiter(String message, String title, String recruiterId, String authorizedUserId);
	
}
