package com.arenella.recruit.recruiters.services;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.SubscriptionActionFeedback;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Defines functionality related to services for Recruiters
* @author K Parkings
*/
public interface RecruiterService {

	/**
	* Updates an existing Recruiter 
	* @param recruiter - Recruiter to update
	* @throws IllegalAccessException 
	*/
	public void updateRecruiter(Recruiter recruiter) throws IllegalAccessException;
	
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
	* @returns Optional feedback specific to the Action performed
	* @throws  IllegalAccessException
	*/
	public Optional<SubscriptionActionFeedback> performSubscriptionAction(String recruiterId, UUID subscriptionId, subscription_action action)  throws IllegalAccessException;

	/**
	* Adds / switches the Recruiters subscription type. 
	* @param recruiterId - Unique identifier of the Recruiter the Subscription is for
	* @param type 		 - type of the Subscription
	* @throws  IllegalAccessException
	*/
	public void addSubscription(String recruiterId, subscription_type type)  throws IllegalAccessException;

	/**
	* Resets the users password and emails them the new password
	* @param emailAddress - Email address of recruiter
	*/
	public void resetPassword(String emailAddress);
	
}