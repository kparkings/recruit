package com.arenella.recruit.recruiters.services;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.adapters.actions.GrantCreditCommand;
import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIOutbound.RecruiterDetails;
import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent.EventType;
import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent;

/**
* Defines Services relating to SupplyAndDomand
* @author K Parkings
*/
public interface SupplyAndDemandService {

	/**
	* Adds a new OpenPoistion that a Recruiter wants
	* to advertise to other Recruiters
	* @param openPosition - New OpenPosition
	*/
	public void addOpenPosition(OpenPosition openPosition);
	
	/**
	* Deletes an Open Position
	* @param openPositionId - Unique Id of the Open Position to delete
	* @throws IllegalAccessException 
	*/
	public void deleteOpenPosition(UUID openPositionId) throws IllegalAccessException;
	
	/**
	* Updates an existing OpenPosition
	* @param UUID 			- OpenPosition id 
	* @param openPosition 	- OpenPosition to be updated
	* @throws IllegalAccessException
	*/
	public void updateOpenPosition(UUID openPositionId, OpenPosition openPosition) throws IllegalAccessException;
	
	/**
	* Returns details of a Recruiter based upon their unique identifier
	* @param recruiterId - Unique identifier of the recruiter
	* @return Details of the Recruiter
	 */
	public RecruiterDetails fetchRecruiterDetails(String recruiterId);

	/**
	* Returns available OpenPositions
	* @return OpenPositions 
	*/
	public Set<OpenPosition> fetchOpenPositions();

	/**
	* Returns openPositions belonging to a specific Recruiter
	* @param recruiterId - unique id of the Recruiter
	* @return OpenPositions
	*/
	public Set<OpenPosition> fetchOpenPositions(String recruiterId);

	/**
	* Logs an event stating that an open position was viewed
	* @param id - Unique id of the Open Position viewed
	*/
	public void registerOpenPositionViewedEvent(UUID id);
	
	/**
	* Logs an event stating that an offered candidate was viewed
	* @param id - Unique id of the Offered Candidate viewed
	*/
	public void registerOfferedCandidateViewedEvent(UUID id);

	/**
	* Fetches Status relating to views of Open 
	* Positions by recruiters
	* @return Stats
	*/
	public Set<SupplyAndDemandEvent> fetchOpenPositionViewStats();
	
	/**
	* 
	* @param openPositionId			- Id of the open position
	* @param message				- message to be sent to the owner
	* @param authenticatedUserId	- userId of currently authenticated user	
	*/
	public void sendOpenPositionContactEmail(UUID openPositionId, String message, String authenticatedUserId);
	
	/**
	* Returns all the viewed events for the recruiter. That is the id's of the posts on 
	* the Market place that the recruiter has viewed
	* @param type			- Type of Event
	* @param recruiterId	- Id of the Recruiter
	* @return -Posts viewed
	*/
	public Set<UUID> fetchViewedEventsByRecruiter(EventType type, String recruiterId);

	/**
	* Disables all the SupplyAndDemand posts owned by a specific Recruiter
	* @param geRecruiterId - Unique id of the Recruiter
	*/
	public void disableSupplyAndDemandPostsForRecruiter(String geRecruiterId);

	/**
	* Enables all the SupplyAndDemand posts owned by a specific Recruiter
	* @param geRecruiterId - Unique id of the Recruiter
	*/
	public void enableSupplyAndDemandPostsForRecruiter(String geRecruiterId);

	/**
	* Updaes the Credits available to the Recruiters
	* @param command - GrantCredit command details
	*/
	public void updateCredits(GrantCreditCommand command);
	
	/**
	* Returns whether the User either does not have Credit based access or does 
	* have credit based access to curriculums but has remaining credits
	* @param name - id of Authorized User
	* @return whether User can access Curriculums
	*/
	public Boolean doCreditsCheck(String name);

	/**
	* Updates a Recruiters credits. Decrementing them by 1
	* @param userId - id of User who used a Credit
	*/
	public void useCredit(String userId);

	/**
	* Updates the amount of Credits the Recruiter has
	* @param command - information about credits
	*/
	public void updateCreditsForUser(String recruiterId, int disabledCredits);
	
	/**
	* Updates the amount of Credits the Recruiter has
	* @param command - information about credits
	*/
	void updateCreditsForUser(String userId, int availableCredits, Optional<Boolean> hasPaidSubscription);
	
	/**
	* Returns the number of Credits the candidate still has
	* available
	* @param userId - Id of the User to get Credit count for
	* @return Number of remaining credits
	*/
	public int getCreditCountForUser(String userId);

	/**
	* Adds the initial record to the Credit table 
	* for the User
	*/
	void addCreditsRecordForUser(String userId);

	
}