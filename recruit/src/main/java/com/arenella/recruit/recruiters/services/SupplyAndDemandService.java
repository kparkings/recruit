package com.arenella.recruit.recruiters.services;

import java.util.UUID;

import com.arenella.recruit.recruiters.beans.OfferedCandidate;
import com.arenella.recruit.recruiters.beans.OpenPosition;

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
}
