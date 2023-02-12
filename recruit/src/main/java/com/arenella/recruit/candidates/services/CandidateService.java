package com.arenella.recruit.candidates.services;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.PendingCandidate;
import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;
import com.arenella.recruit.candidates.controllers.SavedCandidate;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Defines services available for interacting with Candidates
* @author K Parkings
*/
public interface CandidateService {

	/**
	* Persists a Candidate. 
	* @param candidate - Candidate to persist
	* @return CandidateId
	*/
	public void persistCandidate(Candidate candidate);
	
	/**
	* Performs an update on a specific Candidate
	* @param candidateId	- Unique identifier of the Candidate to be updated
	* @param updateAction	- Update action to be performed
	*/
	public void updateCandidate(String candidateId, CANDIDATE_UPDATE_ACTIONS updateAction);
	
	/**
	* Flags a Candidate as being unavailable. This does not deactivate the Candidate as 
	* the availability needs to be confirmed by an admin user.
	* @param candidateId - Unique Id of the candidate
	* @param available 		- Whether or not the candidate is available
	*/
	public void flagCandidateAvailability(long candidateId, boolean available);
	
	/**
	* Retrieves a list of Candidates
	* @param filterOptions  - filters to apply to the results
	* @return Candidates
	*/
	public Page<Candidate> getCandidates(CandidateFilterOptions filterOptions, Pageable pageable);
	
	/**
	* Retrieves a list of Candidates
	* @param filterOptions  - filters to apply to the results
	* @param maxSuggestions - maximum number of suggestions to return
	* @return Candidates
	*/
	public Page<CandidateSearchAccuracyWrapper> getCandidateSuggestions(CandidateFilterOptions filterOptions, Integer maxSuggestions);
	
	
	/**
	* Retrieves a list of Candidates
	* @param filterOptions - filters to apply to the results
	* @return Candidates
	*/
	//public Set<Candidate> getCandidates(CandidateFilterOptions filterOptions);

	/**
	* Adds a Pending Candidate to the System
	* @param convertToPendingCandidate
	*/
	public void persistPendingCandidate(PendingCandidate pendingCandidate);

	/**
	* Return a Set of all PendingCandidats still to be processed
	* @return
	*/
	public Set<PendingCandidate> getPendingCandidates();
	
	/**
	* Updates a when candidates availability was last checked 
	* @param canidateId - Id of the Candidate to update
	*/
	public void updateCandidatesLastAvailabilityCheck(long candidateId);

	/**
	* Saves a new Candidate Search Alert
	* @param alert 		- Domain representation of an Alert
	* @param searchText - Search text to filter on
	*/
	public void addSearchAlert(CandidateSearchAlert alert, String searchText);

	/**
	* Returns the Alerts for the currently logged in User
	* @return Alerts
	*/
	public Set<CandidateSearchAlert> getAlertsForCurrentUser();

	/**
	* Deletes SearchAlert providing the Alert belongs to the Authenticated
	* User
	* @param id - Unique Id of the SearchAlert
	*/
	public void deleteSearchAlert(UUID id);

	/**
	* Performs Test of candidate against filter options for accuracy
	*/
	public suggestion_accuracy doTestCandidateAlert(long candidateId, CandidateFilterOptions filterOptions);
	
	/**
	* Extracts Candidate search filters from a document
	* @param fileType 	- .pdf or .doc
	* @param fileBytes 	- Bytes of actual document
	* @return extracted filters
	*/
	public CandidateExtractedFilters extractFiltersFromDocument(FileType fileType, byte[] fileBytes) throws IOException;

	/**
	* Persists a new Saved Candidate 
	* @param convertToDomain
	*/
	public void addSavedCanidate(SavedCandidate savedCandidate);
	
	/**
	* Fetches all the SaveCandidates belonging to the authenticated User
	* @return SavedCandidates
	*/
	public Map<SavedCandidate, Candidate> fetchSavedCandidatesForUser();

	/**
	* Removes a Saved candidate for the authenticated user
	* @param candidateId - Id of candidate to remove from Users saved candidates
	* @param principal   - Auth for current user
	*/
	public void removeSavedCandidate(long candidateId, Principal principal);

	/**
	* Updates an existing SavedCandidate
	* @param savedCandidate - new version of existing SavedCandidate 
	*/
	public void updateSavedCandidate(SavedCandidate savedCandidate);
	
}