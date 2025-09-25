package com.arenella.recruit.recruiters.controllers;

import java.security.Principal;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.authentication.spring.filters.ClaimsUsernamePasswordAuthenticationToken;
import com.arenella.recruit.recruiters.beans.BlacklistedRecruiterAPIOutbound;
import com.arenella.recruit.recruiters.beans.OpenPositionAPIInbound;
import com.arenella.recruit.recruiters.beans.OpenPositionAPIOutbound;
import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent.EventType;
import com.arenella.recruit.recruiters.services.SupplyAndDemandService;
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
	public ResponseEntity<Void> addOpenPosition(@RequestBody OpenPositionAPIInbound openPosition, Principal principal) {
		
		OpenPositionValidator.validate(openPosition);

		//START
		this.performCreditCheck(principal);
		//END
				
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
	* Returns all Open Positions from recruiters
	* @return - Open positions posted by Recruiters
	*/
	@GetMapping(value="/v1/open-position")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Set<OpenPositionAPIOutbound>> fetchOpenPositions(Principal principal){
		
		Set<UUID> viewedPosts = this.supplyAndDemandService.fetchViewedEventsByRecruiter(EventType.OPEN_POSITION, principal.getName());
		
		return ResponseEntity
				.ok()
				.body(this.supplyAndDemandService.fetchOpenPositions()
						.stream()
						.map(c -> OpenPositionAPIOutbound.convertFromDomain(c, this.supplyAndDemandService.fetchRecruiterDetails(c.getRecruiterId()), viewedPosts))
						.sorted(Comparator.comparing(OpenPositionAPIOutbound::getCreated).reversed())
						.collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Returns the number of unseen open positions for the user
	* @param principal - AuthenticatedUser
	* @return umber of unseen open positions
	*/
	@GetMapping(value="/v1/open-position/count")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Long> fetchOpenPositionsCount(Principal principal){
		
		Set<UUID> viewedPosts = this.supplyAndDemandService.fetchViewedEventsByRecruiter(EventType.OPEN_POSITION, principal.getName());
	
		return ResponseEntity.ok(this.supplyAndDemandService.fetchOpenPositions().stream().filter(op -> !viewedPosts.contains(op.getId())).count());
	}
	
	
	/**
	* Returns all Positions advertised by a specific Recruiter
	* @param recruiterId - UniqueId of the Recruiter
	* @return Open Positions posted by the Recruiter
	*/
	@GetMapping(value="/v1/open-position/{rectuiterId}/{id}")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Set<OpenPositionAPIOutbound>> fetchOpenPositions(@PathVariable("id") String recruiterId, Principal principal){
		
		Set<UUID> viewedPosts = this.supplyAndDemandService.fetchViewedEventsByRecruiter(EventType.OPEN_POSITION, principal.getName());
		
		return ResponseEntity
				.ok()
				.body(this.supplyAndDemandService.fetchOpenPositions(recruiterId)
						.stream()
						.map(c -> OpenPositionAPIOutbound.convertFromDomain(c, this.supplyAndDemandService.fetchRecruiterDetails(c.getRecruiterId()), viewedPosts))
						.collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Logs that a Recruiter has viewed an Open Position
	* @return ResponseEntity
	*/
	@PostMapping("/v1/open-position/viewed/{id}")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> registerOpenPositionViewedEvent(@PathVariable("id") UUID id) {
		
		this.supplyAndDemandService.registerOpenPositionViewedEvent(id);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	/**
	* Returns the number of credits the Candidate has left
	* @return
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@GetMapping(path="v1/open-position/_credits")
	public ResponseEntity<Integer> fetchRemainingCreditCount(Principal principal){
		return ResponseEntity.ok(supplyAndDemandService.getCreditCountForUser(principal.getName()));
	}
	
	/**
	* Logs that a Recruiter has viewed an offered candidate
	* @return ResponseEntity
	*/
	@PostMapping("/v1/offered-candidate/viewed/{id}")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> registerOfferedCandidateViewedEvent(@PathVariable("id") UUID id) {
		
		this.supplyAndDemandService.registerOfferedCandidateViewedEvent(id);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	
	/**
	* Logs that a Recruiter has viewed an offered candidate
	* @return ResponseEntity
	*/
	@PutMapping("/v1/open-position/{id}/_message")
	@PreAuthorize("hasRole('ROLE_RECRUITER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> contactRecruiterForOpenPosition(@PathVariable("id") UUID openPositionId, @RequestPart("message") String message, Principal principal) {
		
		this.supplyAndDemandService.sendOpenPositionContactEmail(openPositionId, message, principal.getName());
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/**
	* Performs a check to see if the User passes the credit check. That is. If the users access to 
	* curriculums is via credits does the User have remaining credits. If the Users access is not 
	* dependent upon credits or the User has remaining credits then returns true else false
	* @param principal - Authorized User
	* @return whether the creditCheck passed for the User
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER')")
	@GetMapping(value="/v1/open-position/creditCheck")
	public ResponseEntity<Boolean> passesCreditCheck(Principal principal){
		
		ClaimsUsernamePasswordAuthenticationToken 	user 			= (ClaimsUsernamePasswordAuthenticationToken)principal;
		boolean 									isRecruiter 	= user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"));
		boolean 									useCredits 		= (Boolean)user.getClaim("useCredits").get();
		
		if (isRecruiter && useCredits) {
			return ResponseEntity.ok(this.supplyAndDemandService.doCreditsCheck(principal.getName()));
		}
		
		return ResponseEntity.ok(true);
	}
	
	/**
	* Performs check to ensure user either doesnt use credit based access or 
	* has enough credits to perform an operation
	* @param principal - currently logged in user
	*/
	private void performCreditCheck(Principal principal) {
		
		ClaimsUsernamePasswordAuthenticationToken 	user 			= (ClaimsUsernamePasswordAuthenticationToken)principal;
		boolean 									isRecruiter 	= user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"));
		boolean 									useCredits 		= (Boolean)user.getClaim("useCredits").get();
		
		if (isRecruiter && useCredits) {
			this.supplyAndDemandService.useCredit(user.getName());
		}
		
	}
}