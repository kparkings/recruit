package com.arenella.recruit.recruiters.controllers;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.recruiters.beans.BlacklistedRecruiterAPIOutbound;
import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIInbound;
import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIOutbound;
import com.arenella.recruit.recruiters.beans.OpenPositionAPIInbound;
import com.arenella.recruit.recruiters.beans.OpenPositionAPIOutbound;
import com.arenella.recruit.recruiters.services.SupplyAndDemandService;
import com.arenella.recruit.recruiters.utils.OfferedCandidateValidator;
import com.arenella.recruit.recruiters.utils.OpenPositionValidator;

/**
* API for recruiters to Offer Candidates and Advertise open positions 
* to other Recruiters
* API is Public and can be used directly by Recruiters
* @author K Parkings
*/
@RestController
public class SupplyAndDemandController {

	@Autowired
	private SupplyAndDemandService supplyAndDemandService;
	
	/**
	* Allows Recruiter to publish an open position they are looking to fill
	* @param openPosition - Open position to be filled
	* @return Status code
	*/
	@PostMapping(value="/v1/open-position")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> addOpenPosition(@RequestBody OpenPositionAPIInbound openPosition) {
		
		OpenPositionValidator.validate(openPosition);
		
		supplyAndDemandService.addOpenPosition(OpenPositionAPIInbound.convertToDomain(openPosition));
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	/**
	* Allows Recruiter to delete an Open Position they previously published
	* @param openPositionId - Unique Id of OpenPosition to be deleted
	* @return Status Code
	 * @throws IllegalAccessException 
	*/
	@DeleteMapping(value="/v1/open-position/{id}")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteOpenPosition(@PathVariable("id") UUID openPositionId) throws IllegalAccessException{
		
		supplyAndDemandService.deleteOpenPosition(openPositionId);
		
		return ResponseEntity.ok().build();
	}
	
	/**
	* Updates an Open position published by the Recruiter
	* @param openPositionId - Unique identifier of the Open position to update
	* @param openPosition	- Details of the Open Position being updated
	* @return Status Code
	* @throws IllegalAccessException 
	*/
	@PutMapping(value="/v1/open-position/{id}")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> updateOpenPosition(@PathVariable("id") UUID openPositionId, @RequestBody OpenPositionAPIInbound openPosition) throws IllegalAccessException{
		
		OpenPositionValidator.validate(openPosition);
		
		supplyAndDemandService.updateOpenPosition(openPositionId, OpenPositionAPIInbound.convertToDomain(openPosition));
		
		return ResponseEntity.ok().build();
	}
	
	/**
	* Allows the Recruiter to Offer a new Candidate to other Recruiters
	* @param offeredCandidate - OfferedCandidate details
	* @return Status Code
	*/
	@PostMapping(path="/v1/offered-candidate", consumes="application/json", produces="application/json")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> addOfferedCandidate(@RequestBody OfferedCandidateAPIInbound offeredCandidate){
		
		OfferedCandidateValidator.validate(offeredCandidate);
		
		supplyAndDemandService.addOfferedCandidate(OfferedCandidateAPIInbound.convertToDomain(offeredCandidate));
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	/**
	* Allows Recruiter to remove a previously offered Candidate
	* @param offeredCandidateId - Unique Id of Candidate to be removed
	* @return Status Code
	*/
	@DeleteMapping(value="/v1/offered-candidate/{id}")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteOfferedCandidate(@PathVariable("id") UUID offeredCandidateId) throws IllegalAccessException{
		
		supplyAndDemandService.deleteOfferedCandidate(offeredCandidateId);
		
		return ResponseEntity.ok().build();
	}
	
	/**
	* Updates an Offered Candidate published by the Recruiter
	* @param offeredCandidateId - Unique Id of Offered Candidate to be updated
	* @param offeredCandidate	- Updated version of Offered Candidate
	* @return Status Code
	* @throws IllegalAccessException 
	*/
	@PutMapping(value="/v1/offered-candidate/{id}")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> updateOfferedCandidate(@PathVariable("id") UUID offeredCandidateId, @RequestBody OfferedCandidateAPIInbound offeredCandidate) throws IllegalAccessException{
		
		OfferedCandidateValidator.validate(offeredCandidate);
		
		this.supplyAndDemandService.updateOfferedCandidate(offeredCandidateId, OfferedCandidateAPIInbound.convertToDomain(offeredCandidate));
		
		return ResponseEntity.ok().build();
	}
	
	/**
	* Allows Recruiter to add a Recruiter to their blacklist so that their Offered Candidates and Open
	* Positions are not visible to that Recruiter
	* @param userId - Unique Id of Recruiter to add to the Blacklist
	* @return Status Code
	*/
	@PutMapping(value="/v1/supply-demand-blacklist/{id}")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> addRecruiterToGlobalBlacklist(@PathVariable("id") String userId){
		return ResponseEntity.ok().build();
	}
	
	/**
	* Allows Recruiter to remove a Recruiter to their blacklist so that their Offered Candidates and Open
	* Positions are again visible to that Recruiter
	* @param userId - Unique Id of Recruiter to add to the Blacklist
	* @return Status Code
	*/
	@DeleteMapping(value="/v1/supply-demand-blacklist/{id}")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteRecruiterFromGlobalBlacklist(@PathVariable("id") String userId){
		return ResponseEntity.ok().build();
	}
	
	/**
	* Returns all Recruiters on the Recruiters Blacklist
	* @param userId - Unique Id of Recruiter who owns the Blacklist
	* @return All recruiters on the Recruiters Blacklist
	*/
	@GetMapping(value="/v1/supply-demand-blacklist/{id}")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Set<BlacklistedRecruiterAPIOutbound>> fetchBlacklistForRecruiter(@PathVariable("id") String recruiterId) {
		return ResponseEntity.ok().body(Set.of());
	}
	
	/**
	* Returns all Candidates offered by Recruiters
	* @return - Offered Candidates
	*/
	@GetMapping(value="/v1/offered-candidate/")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Set<OfferedCandidateAPIOutbound>> fetchOfferedCandidates(){
		return ResponseEntity
				.ok()
				.body(this.supplyAndDemandService.fetchOfferedCandidates()
						.stream()
						.map(c -> OfferedCandidateAPIOutbound.convertFromDomain(c, this.supplyAndDemandService.fetchRecruiterDetails(c.getRecruiterId())))
						.collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Returns all Open Positions from recruiters
	* @return - Open positions posted by Recruiters
	*/
	@GetMapping(value="/v1/open-position")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Set<OpenPositionAPIOutbound>> fetchOpenPositions(){
		return ResponseEntity
				.ok()
				.body(this.supplyAndDemandService.fetchOpenPositions()
						.stream()
						.map(c -> OpenPositionAPIOutbound.convertFromDomain(c, this.supplyAndDemandService.fetchRecruiterDetails(c.getRecruiterId())))
						.collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Returns all Candidates offered by a specific Recruiter
	* @param recruiterId - Unique id of the Recruiter
	* @return Candidates offered by the Recruiter
	*/
	@GetMapping(value="/v1/offered-candidate/rectuiter/{id}")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Set<OfferedCandidateAPIOutbound>> fetchOfferedCandidates(@PathVariable("id") String recruiterId){
		return ResponseEntity
				.ok()
				.body(this.supplyAndDemandService.fetchOfferedCandidates(recruiterId)
						.stream()
						.map(c -> OfferedCandidateAPIOutbound.convertFromDomain(c, this.supplyAndDemandService.fetchRecruiterDetails(c.getRecruiterId())))
						.collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Returns all Positions advertised by a specific Recruiter
	* @param recruiterId - UniqueId of the Recruiter
	* @return Open Positions posted by the Recruiter
	*/
	@GetMapping(value="/v1/open-position/{rectuiterId}/{id}")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Set<OpenPositionAPIOutbound>> fetchOpenPositions(@PathVariable("id") String recruiterId){
		return ResponseEntity
				.ok()
				.body(this.supplyAndDemandService.fetchOpenPositions(recruiterId)
						.stream()
						.map(c -> OpenPositionAPIOutbound.convertFromDomain(c, this.supplyAndDemandService.fetchRecruiterDetails(c.getRecruiterId())))
						.collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	
}