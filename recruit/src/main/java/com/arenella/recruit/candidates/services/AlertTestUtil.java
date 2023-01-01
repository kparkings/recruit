package com.arenella.recruit.candidates.services;

import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;

/**
* Defines Util for performing tests of CandidateSearchEvents against 
* newly uploaded Candidates
* @author K Parkings
*/
public interface AlertTestUtil {

	/**
	* Carries out test of event against existing CandidateSearchAlerts to see
	* if there is a match. Matches will be persisted/
	* @param event - Event containing new Candidate details
	*/
	public void testAgainstCandidateSearchAlerts(CandidateCreatedEvent event);
	
}
