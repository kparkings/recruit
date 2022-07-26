package com.arenella.recruit.recruiters.services;

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
	
}
