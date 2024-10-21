package com.arenella.recruit.recruiters.services;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.core.io.ByteArrayResource;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.SubscriptionActionFeedback;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.INVOICE_TYPE;
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
	* @param invoiceType - Type of Invoice to send to the Recruiter for the subscription
	* @throws  IllegalAccessException
	*/
	public void addSubscription(String recruiterId, subscription_type type, INVOICE_TYPE invoiceType)  throws IllegalAccessException;

	/**
	* Resets the users password and emails them the new password
	* @param emailAddress - Email address of recruiter
	*/
	public void resetPassword(String emailAddress);
	
	/**
	* Returns whether the user has a paid subscription
	* @param userId - unique if of the user
	* @return whether the user has a paid subscription
	*/
	boolean hasPaidSubscription(String userId);

	/**
	* Deletes recruiter and sends events to trigger removal of data relating 
	* to the recruiter
	* @param recruiterId - Unique identifier of the Recruiter
	*/
	public void deleteRecruiter(String recruiterId);

	/**
	* Generates Invoice for given Subscription 
	* @param subscriptionId	 - Unique id of the subscription
	* @param invoiceNumber	 - Unique invoice number
	* @param btwApplies		 - Whether BTW needs to be applied to invoice
	* @param invoiceDate     - Date to use for the invoice
	* @param unitDescription - description of unit of service provided
	* @return Invoice as File
	*/
	public ByteArrayResource generateInvoiceForSubscription(UUID subscriptionId, String invoiceNumber, Optional<Boolean> btwApplies, Optional<LocalDate> invoiceDate, Optional<String> unitDescription);
	
}