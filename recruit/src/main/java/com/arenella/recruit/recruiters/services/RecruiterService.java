package com.arenella.recruit.recruiters.services;

import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;

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

	/**
	* Creates a Recruiter account as a request on behalf of a Recruiter. This 
	* differs from the standard addRecruiter method as it needs to be 
	* confirmed and a user account created and userId assigned..
	* @param convertToDomin
	*/
	public void addRecruiterAccountRequest(Recruiter convertToDomin);

	/**
	* Performs an action on a Recruiters subscription
	* @param recruiterId		- Unique Id of the Recruiter owning the Subscription
	* @param subscriptionId		- Unique Id of the subscription to be amended
	* @param action				- Action to perform on the Subscription
	* @throws  IllegalAccessException
	*/
	public void performSubscriptionAction(String recruiterId, UUID subscriptionId, subscription_action action)  throws IllegalAccessException;
	
}