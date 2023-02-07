package com.arenella.recruit.candidates.utils;

import java.util.Set;

import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Defines functions for determining a Candidate function 
* @author K Parkings
*/
public interface CandidateFunctionExtractor {

	/**
	* Produces a Set of Candidate Functions based upon some 
	* search text
	* @param searchText - Used to extract Candidate Functions
 	* @return Functions found in the search text
	*/
	public Set<FUNCTION> extractFunctions(String searchText);
	
}
