package com.arenella.recruit.recruiters.services;

import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.recruiters.beans.OfferedCandidate;
import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIOutbound.RecruiterDetails;
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
	public void updateOpenPosition(UUID OpenPositionId, OpenPosition openPosition) throws IllegalAccessException;
	
	/**
	* Adds a new OfferedCandidate
	* @param offeredCandidate - Details of the Candidate being offered
	*/
	public void addOfferedCandidate(OfferedCandidate offeredCandidate);

	/**
	* Deletes an existing offeredCandidateId
	* @param offeredCandidateId - Unique Id of the candidate to delete
	* @throws IllegalAccessException 
	*/
	public void deleteOfferedCandidate(UUID offeredCandidateId) throws IllegalAccessException;

	/**
	* Updates an existing OfferedCandidate
	* @param offeredCandidateId			- Unique Id of the OfferedCandidate
	* @param offeredCandidate			- OfferedCandidate to update
	* @throws IllegalAccessException
	*/
	public void updateOfferedCandidate(UUID offeredCandidateId, OfferedCandidate offeredCandidate) throws IllegalAccessException;

	/**
	* Returns available OfferedCandidates
	* @return offeredCandidates
	*/
	public Set<OfferedCandidate> fetchOfferedCandidates();
	
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
	* Returns OfferedCandidates for a specificRecruiter
	* @param recruiterId - Unique Id of the Recruiter
	*/
	public Set<OfferedCandidate> fetchOfferedCandidates(String recruiterId);
	
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
	* Fetches Status relating to views of Offered 
	* Candidates by recruiters
	* @return Stats
	*/
	public Set<SupplyAndDemandEvent> fetchOfferedCandidateViewStats();
	
}