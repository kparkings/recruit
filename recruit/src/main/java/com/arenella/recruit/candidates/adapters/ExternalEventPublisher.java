package com.arenella.recruit.candidates.adapters;

import java.util.Set;

/**
* Defines functionality for publishing Events to external services 
* @author K Parkings
*/
public interface ExternalEventPublisher {

	/**
	* Posts an event containing Skills that where searched on when
	* searching for Candidates
	* @param skills - Skills used in Candidate Search
	*/
	public void publishSearchedSkillsEvent(Set<String> skills);
	
}
