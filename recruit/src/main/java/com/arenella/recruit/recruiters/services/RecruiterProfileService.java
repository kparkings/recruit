package com.arenella.recruit.recruiters.services;

import java.util.Set;

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
	* @param recruiterProfileId - Unique id of recruiter profile to return
	* @return Recruiter Profile
	*/
	public RecruiterProfile fetchRecruiterProfile(String recruiterProfileId);
	
	/**
	* Returns Recruiter Profiles matching the filters
	* @param filters - Filter to apply
	* @return matching Recruiter Profiles
	*/
	public Set<RecruiterProfile> fetchRecruiterProfiles(RecruiterProfileFilter filters);
}
