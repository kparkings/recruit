package com.arenella.recruit.candidates.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.CandidateUpdateRequest;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;
import com.arenella.recruit.candidates.services.CandidateService;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* REST API for working with Candidates
* @author K Parkings
*/
@RestController
public class CandidateController {

	@Autowired
	private CandidateService			candidateService;
	
	/**
	* Adds a new Candidate
	* @param candidate - Contains candidate details
	* @return id of the candidate
	 * @throws IOException 
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PostMapping(path="candidate",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public void addCandidate(@RequestPart("candidate") CandidateAPIInbound candidate, @RequestPart("profileImage")Optional<MultipartFile> profilePhoto, Principal principal) throws IOException {
		
		Optional<Photo> photo = candidateService.convertToPhoto(profilePhoto);
		
		candidateService.persistCandidate(CandidateAPIInbound.convertToCandidate(candidate, photo));
	}
	
	/**
	* Deletes a Candidate account from the System
	* @author K Parkings
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER') or hasRole('ROLE_CANDIDATE')")
	@DeleteMapping(path="candidate/{candidateId}", consumes="application/json", produces="application/json")
	public ResponseEntity<Void> deleteCandidate(@PathVariable("candidateId") String candidateId) {
		candidateService.deleteCandidate(candidateId);
		return ResponseEntity.ok().build();
	}
	
	public static enum CANDIDATE_UPDATE_ACTIONS {enable, disable}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(path="candidate/{candidateId}/")
	public ResponseEntity<Void> updateCandidate(@RequestBody String fakeBody, @PathVariable("candidateId") String candidateId, @RequestParam("action") CANDIDATE_UPDATE_ACTIONS action) {
		
		this.candidateService.updateCandidate(candidateId, action);
		
		return ResponseEntity.ok().build();
	}
	
	/**
	* Updates Candidates Profile
	* @param updateRequest	- New Candidate details
	* @param file			- Optional profile image file
	* @param candidateId	- Id of candidate to update
	* @param principal		- Authorized used
	* @return ResponseEntity
	 * @throws IOException 
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER') OR hasRole('ROLE_CANDIDATE')")
	@PutMapping(path="candidate/{candidateId}/profile",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Void> updateCandidateProfile(@RequestPart("profile") CandidateUpdateRequestAPIInbound updateRequest, @RequestPart("file") Optional<MultipartFile> file, @PathVariable("candidateId") String candidateId, Principal principal) throws IOException {
		
		CandidateUpdateRequest candidateUpdateRequest = CandidateUpdateRequestAPIInbound.convertToDomain(candidateId, updateRequest, file);
		
		this.candidateService.updateCandidateProfile(candidateUpdateRequest);
		
		return ResponseEntity.ok().build();
	
	}
	
	/**
	* Endpoint marks a Candidate as no possibly being no longer available. 
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@PutMapping(path="candidate/{candidateId}/flaggedAsUnavailable/{flaggedAsUnavailable}/")
	public ResponseEntity<Void> updateCandidateflaggedAsUnavailable(@RequestBody String fakeBody, @PathVariable("candidateId") long candidateId, @PathVariable("flaggedAsUnavailable") boolean flaggedAsUnavailable){
		
			this.candidateService.flagCandidateAvailability(candidateId, flaggedAsUnavailable);
		
			return ResponseEntity.ok().build();
	}
	
	/**
	* Endpoint marks a Candidate as being available. In this case we 
	* can update the data that their availability next needs to be checked
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(path="candidate/{candidateId}/updateCandidatesLastAvailabilityCheck")
	public ResponseEntity<Void> updateCandidatesLastAvailabilityCheck(@RequestBody String fakeBody, @PathVariable("candidateId") long candidateId){
		
			this.candidateService.updateCandidatesLastAvailabilityCheck(candidateId);
		
			return ResponseEntity.ok().build();
	}
	
	/**
	* 
	* @param candidateId
	* @param principal
	* @return
	*/
	@PreAuthorize("hasRole('ROLE_CANDIDATE') OR hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER')")
	@GetMapping(path="candidate/{candidateId}")
	public ResponseEntity<CandidateFullProfileAPIOutbound> fetchCandidate(@PathVariable("candidateId")String candidateId, Principal principal){
		
		UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken)principal;
		
		Candidate candidate = this.candidateService.fetchCandidate(candidateId, principal.getName(), user.getAuthorities());
		
		return ResponseEntity.ok(CandidateFullProfileAPIOutbound.convertFromDomain(candidate));
	}
	
	/**
	* Fetches Candidates
	* @param orderAttribute			- Optional attribute to order the results on
	* @param order					- Optional direction of ordering
	* @param candidateId			- Optional candidates ids to filter on
	* @param countries				- Optional countries to filter on
	* @param functions				- Optional functions to filter on
	* @param freelance				- Optional freelance value to filter on
	* @param perm					- Optional perm value to filter on
	* @param yearsExperienceGtEq	- Optional years experience value to filter on
	* @param yearsExperienceGtEq	- Optional years experience value to filter on
	* @param dutch					- Optional Dutch language proficiency value to filter on 
	* @param english				- Optional English language proficiency value to filter on
	* @param french					- Optional French language proficiency value to filter on
	* @param skills					- Optional Skills to filter on
	* @return Page of results
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@GetMapping(path="candidate")
	public Page<CandidateAPIOutbound> getCandidate( @RequestParam("orderAttribute") 	String 				orderAttribute,
													@RequestParam("order") 				RESULT_ORDER		order,
													@RequestParam(required = false) 	Set<String> 		candidateId,
													@RequestParam(required = false) 	Set<COUNTRY> 		countries,
													@RequestParam(required = false) 	Set<FUNCTION> 		functions,
													@RequestParam(required = false) 	Boolean 			freelance,
													@RequestParam(required = false) 	Boolean 			perm,
													@RequestParam(required = false) 	Integer				yearsExperienceGtEq,
													@RequestParam(required = false) 	Integer				yearsExperienceLtEq,
													@RequestParam(required = false) 	Language.LEVEL 		dutch,
													@RequestParam(required = false) 	Language.LEVEL 		english,
													@RequestParam(required = false) 	Language.LEVEL 		french,
													@RequestParam(required = false) 	Set<String>			skills,
													@RequestParam(required = false) 	String				firstname,
													@RequestParam(required = false) 	String				surname,
													@RequestParam(required = false) 	String				email,
													@RequestParam(required = false) 	Boolean				flaggedAsUnavailable,
													@RequestParam(required = false)		Integer				daysSinceLastAvailabilityCheck,
													@RequestParam(required = false)		String				searchText,
													@RequestParam(required = false)		Boolean				available,
													@RequestParam(required = false)		String				ownerId,
													Pageable pageable,
													Principal principal
													) {
		
		
		
		
		UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken)principal;
		
		boolean isCandidate = user.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_CANDIDATE")).findAny().isPresent();
		if (isCandidate) {
			candidateId.clear();
			candidateId.add(user.getName());
		}
		
		CandidateFilterOptions filterOptions = CandidateFilterOptions
																	.builder()
																		.orderAttribute(orderAttribute)
																		.order(order)
																		.candidateIds(candidateId)
																		.countries(countries)
																		.functions(functions)
																		.freelance(freelance)
																		.perm(perm)
																		.yearsExperienceGtEq(yearsExperienceGtEq)
																		.yearsExperienceLtEq(yearsExperienceLtEq)
																		.dutch(dutch)
																		.english(english)
																		.french(french)
																		.skills(skills)
																		.flaggedAsUnavailable(flaggedAsUnavailable)
																		.firstname(firstname)
																		.surname(surname)
																		.email(email)
																		.daysSinceLastAvailabilityCheck(daysSinceLastAvailabilityCheck)
																		.searchText(searchText)
																		.available(available)
																		.ownerId(ownerId)
																	.build();
		
		return candidateService.getCandidateSuggestions(filterOptions, pageable.getPageSize()).map(CandidateSuggestionAPIOutbound::convertFromCandidate);
		
	}
	
	/**
	* Adds a new Candidate
	* @param candidate 		- Contains candidate details
	* @param profilePhoto	- Optional Profile Photo0
	* @return id of the candidate
	 * @throws IOException 
	*/
	@PostMapping(path="pending-candidate",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public void addPendingCandidate(@RequestPart PendingCandidateAPIInbound candidate, @RequestPart("file") Optional<MultipartFile> profilePhoto) throws IOException {
		candidateService.persistPendingCandidate(PendingCandidateAPIInbound.convertToPendingCandidate(candidate, profilePhoto));
	}
	
	/**
	* Returns a list of all PendingCandidates still to be processed 
	* @return PendingCandidates
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(path="pending-candidate", produces="application/json")
	public ResponseEntity<Set<PendingCandidateAPIOutbound>> getAllPendingCandidates() {
		
		LinkedHashSet<PendingCandidateAPIOutbound> candidates = candidateService.getPendingCandidates().stream().map(PendingCandidateAPIOutbound::convertFromPendingCandidate).collect(Collectors.toCollection(LinkedHashSet::new));
		
		return new ResponseEntity<>(candidates, HttpStatus.OK);
	}
	
	/**
	* Adds a new Candidate Search Alert
	* @param alert
	* @return
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@PostMapping(path="candidate/alert")
	public ResponseEntity<Void> addSearchAlert(@RequestBody CandidateSearchAlertAPIInbound alert){
		this.candidateService.addSearchAlert(CandidateSearchAlertAPIInbound.convertToDomain(alert), alert.getSearchText());
		return ResponseEntity.ok().build();
	}
	
	/**
	* Deletes a Candidate Search Alert
	* @param id
	* @return
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@DeleteMapping(path="candidate/alert/{id}")
	public ResponseEntity<Void> deleteSearchAlert(@PathVariable UUID id){
		this.candidateService.deleteSearchAlert(id);
		return ResponseEntity.ok().build();
	}
	
	/**
	* Returns Alerts for currently logged in Recruiter
	* @return Alerts for Recruiter
	*/
	@PreAuthorize("hasRole('RECRUITER')")
	@GetMapping(path="candidate/alert")
	public ResponseEntity<Set<CandidateSearchAlertAPIOutbound>> getRecruiterAlerts(){
		return ResponseEntity.ok(candidateService
				.getAlertsForCurrentUser()
				.stream()
				.map(CandidateSearchAlertAPIOutbound::convertFromDomain)
				.collect(Collectors.toCollection(LinkedHashSet::new))
				);
	}
	
	/**
	* Processes a file and returns potential candidate filters based upon the contents of the
	* file. The purpose is to parse a job specification and return the filter values to 
	* allow the FE to make a request for candidates matching the job spec
	* @param document - Contains data to extract filter values from
	* @throws Exception
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER')")
	@PostMapping(value="/extract-filters",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CandidateExtractedFilters> extractSearchFiltersFromDocument(@RequestParam("file") MultipartFile document) throws Exception{
		
		String postFix = document.getOriginalFilename().substring(document.getOriginalFilename().lastIndexOf('.')+1).toLowerCase();
		
		CandidateExtractedFilters extractedFilters = this.candidateService.extractFiltersFromDocument(FileType.valueOf(postFix),document.getBytes());
		
		return ResponseEntity.ok(extractedFilters);
		
	}
	
	/**
	* Adds a Candidate to the Users list of saved Candidates
	* @param savedCandidate - Candidate to add to the Users saved candidate list
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER')")
	@PostMapping(value="saved-candidate")
	public ResponseEntity<Void> addSavedCandidate(@RequestBody SavedCandidateAPIInbound savedCandidate, Principal principal) {
		
		this.candidateService.addSavedCanidate(SavedCandidateAPIInbound.convertToDomain(savedCandidate, principal.getName()));
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	/**
	* Returns a list of candidates previously saved by the current User
	* @param userId - Unique id of the User
	* @return Saved candidates for the User
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER')")
	@GetMapping(value="saved-candidate")
	public ResponseEntity<Set<SavedCandidateAPIOutbound>> fetchSavedCandidates() {
		
		Set<SavedCandidateAPIOutbound> candidates = new LinkedHashSet<>();
			
		this.candidateService.fetchSavedCandidatesForUser().entrySet().forEach(es -> 
			candidates.add(SavedCandidateAPIOutbound.convertFromDomain(es.getKey(), new CandidateSearchAccuracyWrapper(es.getValue())))
		);
		
		return ResponseEntity.status(HttpStatus.OK).body(candidates);
	}
	
	/**
	* Deletes a Saved Candidate from the users saved Candidates
	* @param userId			- Unique id of the User
	* @param candidateId	- Unique id of the Candidate
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER')")
	@DeleteMapping(value="saved-candidate/{candidateId}")
	public ResponseEntity<Void> removeSavedCandidate(@PathVariable long candidateId, Principal principal) {
		
		this.candidateService.removeSavedCandidate(candidateId, principal);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/**
	* Updates the Users saved Candidate
	* @param savedCandidate - Latest version of the Saved Candidate
	* @return ResponseEntity 
	*/ 
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER')")
	@PutMapping(value="saved-candidate")
	public ResponseEntity<Void> updateSavedCandidate(@RequestBody SavedCandidateAPIInbound savedCandidate, Principal principal) {
		
		this.candidateService.updateSavedCandidate(SavedCandidateAPIInbound.convertToDomain(savedCandidate, principal.getName()));
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/**
	* Sends email to recruiter
	* @return ResponseEntity
	*/
	@PutMapping("/v1/candidate/{id}/_message")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> contactRecruiterForOpenPosition(@PathVariable("id") String candidateId, @RequestPart("title") String title, @RequestPart("message") String message, Principal principal) {
		
		this.candidateService.sendEmailToCandidate(message, candidateId, title, principal.getName());
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
}