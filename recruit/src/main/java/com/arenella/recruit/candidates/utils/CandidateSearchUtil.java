package com.arenella.recruit.candidates.utils;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.controllers.CandidateAPIOutbound;
import com.arenella.recruit.candidates.controllers.CandidateSuggestionAPIOutbound;
import com.arenella.recruit.candidates.services.CandidateService;

/**
* Component for fetching Candidate search results and applying appropriate views of the candidates 
* information based upon the User and their rights / subscriptions
* @author K Parkings
*/
@Component
public class CandidateSearchUtil {

	@Autowired
	private CandidateService candidateService;
	
	/**
	* Fetches Candidates based upon filters and then applies masking to details of results based upon
	* the current User
	* @param isRecruiter			- Whether or not the User is a Recruiter
	* @param isUseCreditUser		- Whether or not the User has a Credit based subscription
	* @param isUserCreditsExpired	- Whether or not the User has a Credit based Subscription but used all their credits
	* @param filterOptions			- Filter options to apply
	* @param pageable				- Pageable object for results size
	* @param unfiltered				- Whether the Search request is unfiltered ( no filters = faster processing )
	* @return Matching Candidates with data masking applied
	* @throws Exception
	*/
	public Page<CandidateAPIOutbound> searchAndPackageForAPIOutput(boolean isRecruiter, boolean isUseCreditUser, boolean isUserCreditsExpired, CandidateFilterOptions filterOptions, Pageable pageable, Boolean unfiltered) throws Exception{
		return this.searchAndPackageForAPIOutput(isRecruiter, isUseCreditUser, isUserCreditsExpired, filterOptions, pageable, unfiltered, false);
	}
	
	/**
	* Fetches Candidates based upon filters and then applies masking to details of results based upon
	* the current User
	* @param isRecruiter			- Whether or not the User is a Recruiter
	* @param isUseCreditUser		- Whether or not the User has a Credit based subscription
	* @param isUserCreditsExpired	- Whether or not the User has a Credit based Subscription but used all their credits
	* @param filterOptions			- Filter options to apply
	* @param pageable				- Pageable object for results size
	* @param unfiltered				- Whether the Search request is unfiltered ( no filters = faster processing )
	* @param isSystemRequest		- Identified that request was from system and not an authenticated user of the system
	* @return Matching Candidates with data masking applied
	* @throws Exception
	*/
	public Page<CandidateAPIOutbound> searchAndPackageForAPIOutput(boolean isRecruiter, boolean isUseCreditUser, boolean isUserCreditsExpired, CandidateFilterOptions filterOptions, Pageable pageable, Boolean unfiltered, boolean isSystemRequest) throws Exception{
		
		boolean isUnfilteredRequest = Optional.ofNullable(unfiltered).isEmpty() ? false :  unfiltered;
		
		/**
		* For Credit users and users we want to show them the Candidates but we later mask them 
		* so they can only see the details if they have a paid subscription
		*/
		if (isUseCreditUser) {
			filterOptions.setAvailable(null);
		}
		
		/**
		* Scenario 1 - User is recruiter with a credit subscription whose credits for the week have expired 
		*/
		if (isRecruiter && isUseCreditUser && isUserCreditsExpired) {
			return candidateService.getCandidateSuggestions(filterOptions, pageable.getPageSize(), isUnfilteredRequest).map(CandidateSuggestionAPIOutbound::convertFromCandidateAsCensored);
		}
		
		/**
		* Scenario 2 - User is recruiter with a credit subscription and has remaining credits 
		*/
		if (isRecruiter && isUseCreditUser) {
			return candidateService
					.getCandidateSuggestions(filterOptions, pageable.getPageSize(), isUnfilteredRequest)
					.map(c -> c.get().isAvailable() ? CandidateSuggestionAPIOutbound.convertFromCandidateAsCensoredForActiveCredits(c) : CandidateSuggestionAPIOutbound.convertFromCandidateAsCensored(c));
		}
		
		/**
		* Scenario 4 - All remaining scenarios
		*/
		return candidateService.getCandidateSuggestions(filterOptions, pageable.getPageSize(), isUnfilteredRequest, isSystemRequest).map(CandidateSuggestionAPIOutbound::convertFromCandidate);
		
	}
	
}