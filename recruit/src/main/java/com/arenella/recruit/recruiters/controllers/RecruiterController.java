package com.arenella.recruit.recruiters.controllers;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
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

import com.arenella.recruit.recruiters.beans.RecruiterAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterAPIOutbound;
import com.arenella.recruit.recruiters.beans.RecruiterAccountRequestAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.SubscriptionAPIInbound;
import com.arenella.recruit.recruiters.beans.SubscriptionActionFeedback;
import com.arenella.recruit.recruiters.services.RecruiterService;

/**
* REST API for interacting with Recruiters
* @author K Parkings
*/
@RestController
public class RecruiterController {

	private RecruiterService recruiterService;
	
	/**
	* Constructor
	* @param recruiterService
	*/
	public RecruiterController(RecruiterService recruiterService) {
		this.recruiterService = recruiterService;
	}
	
	/**
	* Adds a new Recruiter
	* @param recruiter
	*/
	@PostMapping(value="/public/recruiter")
	public ResponseEntity<Void> requestRecruiterAccount(@RequestBody RecruiterAccountRequestAPIInbound recruiter) {
		
		recruiterService.addRecruiterAccountRequest(RecruiterAccountRequestAPIInbound.convertToDomin(recruiter));
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/**
	* Updates an existing recruiter
	* @param recruiter
	* @throws IllegalAccessException 
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER') OR hasRole('ROLE_RECRUITERNOSUBSCRITION')")
	@PutMapping(value="/recruiter", consumes="application/json", produces="application/json")
	public ResponseEntity<Void> updateRecruiter(@RequestBody RecruiterAPIInbound recruiter) throws IllegalAccessException {
		recruiterService.updateRecruiter(RecruiterAPIInbound.convertToDomain(recruiter));
		return ResponseEntity.ok().build();
	}
	
	/**
	* Returns a Collection of Recruiters
	* @return recruiters
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value="/recruiter")
	public Set<RecruiterAPIOutbound> fetchRecruiters(){
		return recruiterService
				.fetchRecruiters()
				.stream()
				.map(RecruiterAPIOutbound::convertFromDomain)
				.collect(Collectors.toSet());
	}
	
	/**
	* Returns basic information about requested recruiters. This is available 
	* to candidates, intended to be used with the chats and therefore should 
	* only contain the most basic, non personal data.
	* @param ids - Id's of recruiters to return details for
	* @return Basic details of selected recruiters
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_CANDIDATE')")
	@GetMapping(value="/recruiter/ids/")
	public ResponseEntity<Set<RecruiterBasicInfoAPIOutbound>> fetchRecruiters(@RequestParam("ids") Set<String> ids) {
		return ResponseEntity.ok(this.recruiterService.fetchRecruitersByIds(ids).stream().map(RecruiterBasicInfoAPIOutbound::fromDomain).collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Returns a Recruiter matching the Id
	* @return recruiter
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@GetMapping(value="/recruiter/{id}")
	public RecruiterAPIOutbound fetchRecruiter(@PathVariable("id") String recruiterId) throws IllegalAccessException{
		return RecruiterAPIOutbound.convertFromDomain(recruiterService.fetchRecruiter(recruiterId));
	}
	
	/**
	* Returns the details of the currently logged in Recruiter
	* @return recruiter
	*/
	@PreAuthorize("hasRole('RECRUITER') OR hasRole('ROLE_RECRUITERNOSUBSCRITION')")
	@GetMapping(value="/recruiter/me")
	public RecruiterAPIOutbound fetchRecruiterOwnAccount() throws IllegalAccessException{
		return RecruiterAPIOutbound.convertFromDomain(recruiterService.fetchRecruiterOwnAccount());
	}
	
	/**
	* Adds a new subscription for the Recruiter
	* @param recruiterId		- Unique Id of the Recruiter owning the Subscription
	* @param subscription		- Type of subscription requested
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')  OR hasRole('ROLE_RECRUITERNOSUBSCRITION')")
	@PostMapping(value="/recruiter/{recruiterId}/subscription/")
	public ResponseEntity<Void> addSubscription(@PathVariable("recruiterId") String recruiterId, @RequestBody SubscriptionAPIInbound subscription)  throws IllegalAccessException{
		this.recruiterService.addSubscription(recruiterId, subscription.getType(), subscription.getInvoiceType());
		return ResponseEntity.ok().build();
	}
	
	
	/**
	* Performs an action on a Recruiters subscription
	* @param recruiterId		- Unique Id of the Recruiter owning the Subscription
	* @param subscriptionId		- Unique Id of the subscription to be amended
	* @param action				- Action to perform on the Subscription
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@PutMapping(value="/recruiter/{recruiterId}/subscription/{subscriptionId}/")
	public ResponseEntity<Optional<SubscriptionActionFeedback>> performSubscriptionAction(@PathVariable("recruiterId") String recruiterId, @PathVariable("subscriptionId") UUID subscriptionId, @RequestParam("action") subscription_action action)  throws IllegalAccessException{
		return ResponseEntity.ok().body(this.recruiterService.performSubscriptionAction(recruiterId, subscriptionId, action));
	}
	
	/**
	* Endpoint to handle the password reset of an existing user
	* @param emailAddress - Users email address
	*/
	@PutMapping(path="recruiter/reset-password/{email}", consumes="application/json", produces="application/json")
	public ResponseEntity<Void> resetPassword(@PathVariable("email") String emailAddress) {
		
		this.recruiterService.resetPassword(emailAddress);
		
		return ResponseEntity.ok().build();
	}
	
	//TOD): [My are the credits which are recruiter based in Candodate service. Need to move the whole lot to for now I will add this hear to keep it consistent
	/**
	* Returns if the recruiter has paid subscription
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('RECRUITER')")
	@GetMapping(path="recruiter/_paid_subscription")
	public ResponseEntity<Boolean> hasPaidSubscription(Principal principal){
		return ResponseEntity.ok(recruiterService.hasPaidSubscription(principal.getName()));
	}
	
	/**
	* Deletes a recruiter and initiates process of removing data related to the 
	* Recruiter from the system
	* @param recruiterId - Id of recruiter to be deleted
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('RECRUITER') OR hasRole('ADMIN')")
	@DeleteMapping(path="recruiter/{recruiterId}")
	public ResponseEntity<Void> deleteRecruiter(@PathVariable("recruiterId") String recruiterId){
		
		this.recruiterService.deleteRecruiter(recruiterId);
		
		return ResponseEntity.ok().build();
	}
	
}