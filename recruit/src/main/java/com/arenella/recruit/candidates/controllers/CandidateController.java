package com.arenella.recruit.candidates.controllers;

import java.util.LinkedHashSet;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;
import com.arenella.recruit.candidates.services.CandidateService;

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
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(path="candidate", consumes="application/json", produces="application/json")
	public void addCandidate(@RequestBody CandidateAPIInbound candidate) {
		candidateService.persistCandidate(CandidateAPIInbound.convertToCandidate(candidate));
	}
	
	public static enum CANDIDATE_UPDATE_ACTIONS {enable, disable}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(path="candidate/{candidateId}/")
	public ResponseEntity<Void> updateCandidate(@RequestBody String fakeBody, @PathVariable("candidateId") String candidateId, @RequestParam("action") CANDIDATE_UPDATE_ACTIONS action) {
		
		this.candidateService.updateCandidate(candidateId, action);
		
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
	* @param useSuggestions			- If false returns pages results. If true returns x best match suggestions
	* @return Page of results
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
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
													@RequestParam(required = false)		Boolean				useSuggestions,
													@RequestParam(required = false)		Integer				daysSinceLastAvailabilityCheck,
													Pageable pageable
												 	) {
		
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
																	.build();
		
		if (useSuggestions != null && useSuggestions) {
			return candidateService.getCandidateSuggestions(filterOptions, pageable.getPageSize()).map(candidate -> CandidateSuggestionAPIOutbound.convertFromCandidate(candidate));
		} else {
			return candidateService.getCandidates(filterOptions, pageable).map(candidate -> CandidateStandardAPIOutbound.convertFromCandidate(candidate));
		}
	}
	
	/**
	* Adds a new Candidate
	* @param candidate - Contains candidate details
	* @return id of the candidate
	*/
	@PostMapping(path="pending-candidate", consumes="application/json", produces="application/json")
	public void addPendingCandidate(@RequestBody PendingCandidateAPIInbound candidate) {
		candidateService.persistPendingCandidate(PendingCandidateAPIInbound.convertToPendingCandidate(candidate));
	}
	
	/**
	* Returns a list of all PendingCandidates still to be processed 
	* @return PendingCandidates
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(path="pending-candidate", produces="application/json")
	public ResponseEntity<Set<PendingCandidateAPIOutbound>> getAllPendingCandidates() {
		
		LinkedHashSet<PendingCandidateAPIOutbound> candidates = candidateService.getPendingCandidates().stream().map(p -> PendingCandidateAPIOutbound.convertFromPendingCandidate(p)).collect(Collectors.toCollection(LinkedHashSet::new));
		
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
		this.candidateService.addSearchAlert(CandidateSearchAlertAPIInbound.convertToDomain(alert));
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
				.map(a -> CandidateSearchAlertAPIOutbound.convertFromDomain(a))
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
		
		//TODO: [KP] 1. Define ExtractedFilter object to contain filter values to return to the FE
		//TODO: [KP] 2. Need to retrieve all known skills - Also need to see why duplicates are being added in DB and stop/correct that
		//TODO: [KP] 3. Parse the tokens in the files. Check each skill against contents. Where match add to ExtractedFilters
		//TODO: [KP] 4. Parse the tokens for each synonym and where match add them to the ExtractedFilterObject
		//TODO: [KP] 5. Parse skills in ExtractedFilter for each synonmy group. Where 1 is found remove all other skills in the same synonmy group from extracted filter
		//TODO: [KP] 6. Parse for keyswords Senior / Medior / Junior if found set expereince to [0,2],[3,4],[5]
		//TODO: [KP] 7. Parse for English, Dutch, Engels, French, Francais, Frans and if found set languages
		//TODO: [KP] 7. Parse for perm permantent vast vaste freelance contract set contract type
		
		//TODO: [KP] 9. How to set function type??? Think list of popular job titles and synonyms.
					//	- Java Developer / Java Software Engineer, Java Engineer, Java Software Ontwikkelaar, Java Ontwikkelaar, Fullstack Java, Java Backend Developer
		System.out.println("Uploaded: " + document.getName());
		
		CandidateExtractedFilters filters = CandidateExtractedFilters
				.builder()
					.belgium(true)
					.experienceGTE("2")
					.french(true)
					.jobTitle("java developer")
					.skills(Set.of("java","spring","kakfa","wpf"))
					.freelance(FREELANCE.TRUE)
				.build();
		
		//TO start with just try and return FunctionType and Job title. When that is working add other filters. ?? extract title and/or job title
		return ResponseEntity.ok(filters);
		
		
		
	}
	
}