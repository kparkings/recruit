package com.arenella.recruit.candidates.services;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.CandidateUpdateRequest;
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

	/**
	* Fetches the candidate with the given Id 
	* @param candidateId			- id of the Candidate to fetch 
	* @param authenticatedUserId 	- Id of the Authenticated User
	* @param authorities			- Authorities of the Authenticated User
	* @return
	*/
	public Candidate fetchCandidate(String candidateId, String authernticatedUserId, Collection<GrantedAuthority> authorities);

	/**
	* Updates details of a Candidates profile
	* @param candidateUpdateRequest - Contains details of updates Candidate properties
	*/
	public void updateCandidateProfile(CandidateUpdateRequest candidateUpdateRequest);

	/**
	* Fully deletes Candidate from the System
	* @param candidateId - id of Candidate to delete
	*/
	public void deleteCandidate(String candidateId);

	/**
	* Converts Multiplart file to Candidate Photo 
	* @param profilePhoto - bytes
	* @return Photo
	* @throws IOException 
	*/
	public Optional<Photo> convertToPhoto(Optional<MultipartFile> profilePhoto) throws IOException;

	/**
	* Adds or updates the recruiter's contact record
	* @param recruiterId - Unique identifier of the Recruiter
	* @param email		 - Email address of the Recruiter
	* @param firstName	 - Firstname of the Recruiter
	* @param surname	  -Surname of the Recruiter
	*/
	void updateContact(String recruiterId, String email, String firstName, String surname);

	/**
	* Sends a message to the Candidate
	* @param message		- Message to send
	* @param candidateId	- Id of candidate to receive the message
	* @param title			- Title of the message
	* @param userId			- Id of the currently logged in User
	*/
	public void sendEmailToCandidate(String message, String candidateId, String title, String userId);

	/**
	* Whether or not the User has remaining credits
	*/
	boolean hasCreditsLeft(String userName);

	/**
	* Updates the amount of Credits the Recruiter has
	* @param command - information about credits
	*/
	void updateCreditsForUser(String userId, int availableCredits);

	/**
	* Returns the number of Credits the candidate still has
	* available
	* @param userId - Id of the User to get Credit count for
	* @return Number of remaining credits
	*/
	public int getCreditCountForUser(String userId);
	
}