package com.arenella.recruit.candidates.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;

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

import com.arenella.recruit.authentication.spring.filters.ClaimsUsernamePasswordAuthenticationToken;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.CandidateUpdateRequest;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.controllers.CandidateSearchRequest.RequestFilters;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;
import com.arenella.recruit.candidates.services.CandidateService;
import com.arenella.recruit.candidates.utils.CandidateSearchUtil;
import com.arenella.recruit.candidates.utils.GeoZoneSearchUtil.GEO_ZONE;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* REST API for working with Candidates
* @author K Parkings
*/
@RestController
public class CandidateController {

	@Autowired
	private CandidateService			candidateService;
	
	@Autowired
	private CandidateSearchUtil			candidateSearchUtil;
	
	/**
	* Adds a new Candidate
	* @param candidate - Contains candidate details
	* @return id of the candidate
	 * @throws IOException 
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PostMapping(path="candidate",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public void addCandidate(@RequestPart("candidate") CandidateAPIInbound candidate, @RequestPart("profileImage")Optional<MultipartFile> profilePhoto, Principal principal) throws Exception {
		
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
	* Updates Candidates 	
	* @param updateRequest	- New Candidate details	
	* @param file			- Optional profile image file
	* @param candidateId	- Id of candidate to update
	* @param principal		- Authorized used
	* @return ResponseEntity
	 * @throws IOException 
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER') OR hasRole('ROLE_CANDIDATE')")
	@PutMapping(path="candidate/{candidateId}/profile",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Void> updateCandidateProfile(@RequestPart("profile") CandidateUpdateRequestAPIInbound updateRequest, @RequestPart("file") Optional<MultipartFile> file, @PathVariable("candidateId") String candidateId, Principal principal) throws Exception {
		
		CandidateUpdateRequest candidateUpdateRequest = CandidateUpdateRequestAPIInbound.convertToDomain(candidateId, updateRequest, file);
		
		this.candidateService.updateCandidateProfile(candidateUpdateRequest);
		
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
		
		if (this.isRecruiter(principal) && isUseCredits(principal) && this.userCreditsExpired(user.getName())) {
			return ResponseEntity.ok(CandidateFullProfileAPIOutbound.convertFromCandidateAsCensored(candidate));
		} else {
			return ResponseEntity.ok(CandidateFullProfileAPIOutbound.convertFromDomain(candidate));
		}
		
		
	}
	
	/**
	* Returns Candidates matching the filters
	* @param searchRequest - Contains filter information
	* @return Candidates matching the filters
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PostMapping(path="submitCandidateSearchRequest/")
	public Page<CandidateAPIOutbound> getCandidates(
			@RequestBody CandidateSearchRequest 	searchRequest, 
			@RequestParam("orderAttribute") 		String 				orderAttribute,
			@RequestParam("order") 					RESULT_ORDER		order,
						 Pageable 					pageable,
						 Principal 					principal,
						 HttpServletResponse	 	response) throws Exception{
		
			Set<String> candidateIdFilters = new LinkedHashSet<>();
			
			if (this.isCandidate(principal)) {
				candidateIdFilters.add(getLoggedInUserName(principal));
			} else  {
				searchRequest.termFilters().ifPresent(tf ->
					tf.getCandidateId().ifPresent(candidateId -> candidateIdFilters.add(tf.getCandidateId().get()))
				);
			}
		
			CandidateFilterOptions filterOptions = 
				CandidateSearchRequest
					.convertToCandidateFilterOptions(
							searchRequest, 
							orderAttribute, 
							order);
		
		Boolean 	unfiltered 			= searchRequest.requestFilters().map(RequestFilters::getUnfiltered).orElse(Optional.empty()).orElse(null);
		
		/**
		* Set headers to avoid older requests overriding 
		* older requests responses. Implementation carries 
		* out in front end  
		*/
		searchRequest.requestFilters().ifPresent(f -> {
			f.getBackendRequestId().ifPresent(id ->{
				int backendRequestId = searchRequest.requestFilters().map(RequestFilters::getBackendRequestId).orElse(Optional.of(0)).get();
				response.setHeader("X-Arenella-Request-Id", ""+backendRequestId);
			});
		});
		
		
		return candidateSearchUtil.searchAndPackageForAPIOutput(isRecruiter(principal), isUseCredits(principal), userCreditsExpired(getLoggedInUserName(principal)), filterOptions, pageable, unfiltered);
		
	}
	
	//TODO: THis is getting to big. Change to body and separate out the GeoPosFilter that should not be being referenced
	//      from the service layer. Allowing for now as I want to refactor this entire requestParam list
	/**
	* Fetches Candidates
	* @param orderAttribute			- Optional attribute to order the results on
	* @param order					- Optional direction of ordering
	* @param candidateId			- Optional candidates ids to filter on
	* @param geoZones				- Optional geoZones to filter on
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
	//@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	//@GetMapping(path="candidate")
	//public Page<CandidateAPIOutbound> getCandidate( @RequestParam("orderAttribute") 	String 				orderAttribute,
	//												@RequestParam("order") 				RESULT_ORDER		order,
	//												@RequestParam(required = false) 	Set<String> 		candidateId,
	//												@RequestParam(required = false) 	Set<GEO_ZONE>		geoZones,
	//												@RequestParam(required = false) 	Set<COUNTRY> 		countries,
	//												@RequestParam(required = false) 	Set<FUNCTION> 		functions,
	//												@RequestParam(required = false) 	Boolean 			freelance,
	//												@RequestParam(required = false) 	Boolean 			perm,
	//												@RequestParam(required = false) 	Integer				yearsExperienceGtEq,
	//												@RequestParam(required = false) 	Integer				yearsExperienceLtEq,
	//												@RequestParam(required = false) 	Language.LEVEL 		dutch,
	//												@RequestParam(required = false) 	Language.LEVEL 		english,
	//												@RequestParam(required = false) 	Language.LEVEL 		french,
	//												@RequestParam(required = false) 	Language.LEVEL 		german,
	//												@RequestParam(required = false) 	Language.LEVEL 		italian,
	//												@RequestParam(required = false) 	Language.LEVEL 		polish,
	//												@RequestParam(required = false) 	Language.LEVEL 		portuguese,
	//												@RequestParam(required = false) 	Language.LEVEL 		spanish,
	//												@RequestParam(required = false) 	Set<String>			skills,
	//												@RequestParam(required = false) 	String				firstname,
	//												@RequestParam(required = false) 	String				surname,
	//												@RequestParam(required = false) 	String				email,
	//												@RequestParam(required = false) 	Boolean				flaggedAsUnavailable,
	//												@RequestParam(required = false)		Integer				daysSinceLastAvailabilityCheck,
	//												@RequestParam(required = false)		String				searchText,
	//												@RequestParam(required = false)		Boolean				available,
	//												@RequestParam(required = false)		String				ownerId,
	//												@RequestParam(required = false)		Boolean				includeRequiresSponsorship,
	//												@RequestParam(required = false)		Boolean				unfiltered,
	//												@RequestParam(required = false)		COUNTRY				locCountry,
	//												@RequestParam(required = false)		String				locCity,
	//												@RequestParam(required = false)		Integer				locDistance,
	//												
	//												Integer				backendRequestId,
	//												Pageable 			pageable,
	//												Principal 			principal,
	//												HttpServletResponse response
	//												) throws Exception{
	//	
	//	Set<String> candidateIdFilters = new LinkedHashSet<>();
	//	
	//	if (this.isCandidate(principal)) {
	//		candidateIdFilters.add(getLoggedInUserName(principal));
	//	} else if(Optional.ofNullable(candidateId).isPresent()) {
	//		candidateIdFilters.addAll(candidateId);
	//	}
	//	
	//	//REFACTOR - SHOULD COME AS SET FROM FE REQUEST 
	//	Set<Language> languages = new HashSet<>();
	//	
	//	if (Optional.ofNullable(dutch).isPresent()){
	//		languages.add(Language.builder().language(LANGUAGE.DUTCH).level(dutch).build());
	//	}
	//	
	//	if (Optional.ofNullable(english).isPresent()){
	//		languages.add(Language.builder().language(LANGUAGE.ENGLISH).level(english).build());
	//	}
	//	
	//	if (Optional.ofNullable(french).isPresent()){
	//		languages.add(Language.builder().language(LANGUAGE.FRENCH).level(french).build());
	//	}
	//	
	//	if (Optional.ofNullable(german).isPresent()){
	//		languages.add(Language.builder().language(LANGUAGE.GERMAN).level(german).build());
	//	}
	//	
	//	if (Optional.ofNullable(italian).isPresent()){
	//		languages.add(Language.builder().language(LANGUAGE.ITALIAN).level(italian).build());
	//	}
	//	
	//	if (Optional.ofNullable(polish).isPresent()){
	//		languages.add(Language.builder().language(LANGUAGE.POLISH).level(polish).build());
	//	}
	//	
	//	if (Optional.ofNullable(portuguese).isPresent()){
	//		languages.add(Language.builder().language(LANGUAGE.PORTUGUESE).level(portuguese).build());
	//	}
	//	
	//	if (Optional.ofNullable(spanish).isPresent()){
	//		languages.add(Language.builder().language(LANGUAGE.SPANISH).level(spanish).build());
	//	}
		
	//	//END REFACTOR
		
	//	CandidateFilterOptions filterOptions = CandidateFilterOptions
	//																.builder()
	//																	.orderAttribute(orderAttribute)
	//																	.order(order)
	//																	.candidateIds(candidateIdFilters)
	//																	.geoZones(geoZones)
	//																	.countries(countries)
	//																	.functions(functions)
	//																	.freelance(freelance)
	//																	.perm(perm)
	//																	.yearsExperienceGtEq(yearsExperienceGtEq)
	//																	.yearsExperienceLtEq(yearsExperienceLtEq)
	//																	.languages(languages)
	//																	.skills(skills)
	//																	.flaggedAsUnavailable(flaggedAsUnavailable)
	//																	.firstname(firstname)
	//																	.surname(surname)
	//																	.email(email)
	//																	.daysSinceLastAvailabilityCheck(daysSinceLastAvailabilityCheck)
	//																	.searchText(searchText)
	//																	.available(available)
	//																	.ownerId(ownerId)
	//																	.includeRequiresSponsorship(includeRequiresSponsorship)
	//																	.geoPosFilter(locCountry, locCity, locDistance)
	//																.build();
	//	
	//	response.setHeader("X-Arenella-Request-Id", ""+(backendRequestId == null ? 0 : backendRequestId.intValue()));
	//	
	//	return candidateSearchUtil.searchAndPackageForAPIOutput(isRecruiter(principal), isUseCredits(principal), userCreditsExpired(getLoggedInUserName(principal)), filterOptions, pageable, unfiltered);
	//	
	//}
	
	/**
	* Returns the number of credits the Candidate has left
	* @return
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@GetMapping(path="candidate/_credits")
	public ResponseEntity<Integer> fetchRemainingCreditCount(Principal principal){
		return ResponseEntity.ok(candidateService.getCreditCountForUser(principal.getName()));
	}
	
	/**
	* Returns whether the User has credits left
	* @param userName - Name of the user
	* @return Whether user has credits left
	*/
	private boolean userCreditsExpired(String userName) {
		return !this.candidateService.hasCreditsLeft(userName);
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
	@PreAuthorize("hasRole('RECRUITER') OR hasRole('ADMIN')")
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
	* Returns filters based upon the content of a job specification
	* @param jobspec - Text from a job spec to extract filters from
	* @throws Exception
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER')")
	@PostMapping(value="/extract-filters-text")
	public ResponseEntity<CandidateExtractedFilters> extractSearchFiltersFromText(@RequestBody String jobspec) throws Exception{
		
		CandidateExtractedFilters extractedFilters = this.candidateService.extractFiltersFromText(jobspec);
		
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
	
	/**
	* Returns Skills that need verification 
	* @return Skills
	*/
	@GetMapping("/candidate/skills/pending")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Set<CandidateSkillAPIOutbound>> fetchValidationPendingCandidateSkills(){
		return ResponseEntity.status(HttpStatus.OK)
				.body(this.candidateService
						.fetchPendingCandidateSkills()
						.stream()
						.map(CandidateSkillAPIOutbound::convertFromDomain)
						.collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Returns Skills that need verification 
	* @return Skills
	*/
	@PutMapping("/candidate/skills")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> updateCandidateSkills(@RequestBody Set<CandidateSkillAPIInbound> skills){
		this.candidateService.updateCandidateSkills(skills.stream().map(CandidateSkillAPIInbound::convertToDomain).collect(Collectors.toCollection(LinkedHashSet::new)));
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/**
	* Returns counts of Candiates in the system
	* @return Count of candidate info
	*/
	@GetMapping(path="/public/candidate/_counts")
	public ResponseEntity<CandidateAvailabilityCountAPIOutbound> getCandidateCounts() {
	
		long available 		= this.candidateService.getCountByAvailable(true);
		long unavailable 	= this.candidateService.getCountByAvailable(false);
		
		return ResponseEntity.ok(new CandidateAvailabilityCountAPIOutbound(available, unavailable));
		
	}

	/**
	* Endpoint allows Canidates to update their own availabilty using a token sent 
	* to them via an Email from the system. The update will only take place if the 
	* token is a valid token. There is no other security applied as the request is 
	* triggered from an email. We rely on the fact we are using a clientId in combination with a 
	* UUID's and the period the token is valid to protect against attacks. The scope of the update is 
	* also low risk. The most an attacker can do is change the candidates availability 
	* flag.
	* @param requestToken - Token sent to Candidate to allow them to confirm their availability
	* @param isAvailable  - Whether or not the candidate is available
	* @return Message confirming update
	*/
	@GetMapping(path="/public/candidate/{candidateId}/availabilitytoken/{requestToken}")
	public ResponseEntity<String> confirmOwnAvailability(@PathVariable("candidateId") String candidateId, @PathVariable("requestToken") UUID requestToken, @RequestParam(required=true) Boolean isAvailable) {
		
		this.candidateService.performConfirmCandidateAvailability(candidateId, requestToken, isAvailable);
		//		[KP] Service to check token exists for that candidate and request was sent in past week and candidate not already used token
		//		[KP] Update Candidate fields and persist
		//		[KP] Add url to email
		return ResponseEntity.ok().body("Thanks you. Your'e availability has been updated in the system.");
	}
	
	/**
	* Returns a list of Supported languages that a candidate can possibly speak
	* @return supported languages
	*/
	@GetMapping(path="/candidate/languages")
	public ResponseEntity<Set<LANGUAGE>> fetchSupportedLanguages(){
		return ResponseEntity.ok(Arrays.stream(LANGUAGE.values()).collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Returns list of Geo Zones that a candidate can be available for work in
	* @return Supported GeoZones
	*/
	@GetMapping(path="/candidate/geo-zone")
	public ResponseEntity<Set<GEO_ZONE>> fetchGeoZones(){
		return ResponseEntity.ok(Arrays.stream(GEO_ZONE.values()).collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Returns list of Countries that a candidate can be available for work in
	* @return Supported Countries
	*/
	@GetMapping(path="/candidate/countries")
	public ResponseEntity<Set<CountryEnumAPIOutbound>> fetchSupportedCountries(){
		return ResponseEntity.ok(Arrays.stream(COUNTRY.values()).map(c -> new CountryEnumAPIOutbound(c)).collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Endpoint to handle the password reset of an existing user
	* @param emailAddress - Users email address
	*/
	@PutMapping(path="candidate/reset-password/{email}", consumes="application/json", produces="application/json")
	public ResponseEntity<Void> resetPassword(@PathVariable("email") String emailAddress) {
		
		this.candidateService.resetPassword(emailAddress);
		
		return ResponseEntity.ok().build();
	}
	
	/**
	* Returns whether or not the user is a Candidate
	* @return whether or not the user is a Candidate
	*/
	private boolean isCandidate(Principal principal) {
		return this.hasRole(principal, "ROLE_CANDIDATE");
	}
	
	/**
	* Returns whether or not the user is a Recruiter
	* @return whether or not the user is a Recruiter
	*/
	private boolean isRecruiter(Principal principal) {
		return this.hasRole(principal, "ROLE_RECRUITER");
	}
	
	/**
	* Returns whether or not the user has Credit based access
	* @return whether or not the user has Credit based access
	*/
	private boolean isUseCredits(Principal principal) {
		
		ClaimsUsernamePasswordAuthenticationToken user = (ClaimsUsernamePasswordAuthenticationToken)principal;
		
		return (Boolean)user.getClaim("useCredits").get();
	}
	
	/**
	* Returns whether or not the user has Credit based access
	* @return whether or not the user has Credit based access
	*/
	private String getLoggedInUserName(Principal principal) {
		
		ClaimsUsernamePasswordAuthenticationToken user = (ClaimsUsernamePasswordAuthenticationToken)principal;
		
		return user.getName();
	}
	
	/**
	* Returns whether or not the user has a given Role
	* @return whether or not the user has a given Role
	*/
	private boolean hasRole(Principal principal, String role) {
		
		ClaimsUsernamePasswordAuthenticationToken user = (ClaimsUsernamePasswordAuthenticationToken)principal;
		
		return user.getAuthorities().stream().filter(a -> a.getAuthority().equals(role)).findAny().isPresent();
		
	}
	
}