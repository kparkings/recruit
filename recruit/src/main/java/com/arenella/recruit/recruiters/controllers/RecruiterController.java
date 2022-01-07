package com.arenella.recruit.recruiters.controllers;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.arenella.recruit.recruiters.services.RecruiterService;

/**
* REST API for interacting with Recruiters
* @author K Parkings
*/
@RestController
public class RecruiterController {

	@Autowired
	private RecruiterService recruiterService;
	
	/**
	* Adds a new Recruiter
	* @param recruiter
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value="/recruiter")
	public ResponseEntity<Void> addRecruiter(@RequestBody RecruiterAPIInbound recruiter) {
		
		recruiterService.addRecruiter(RecruiterAPIInbound.convertToDomin(recruiter));
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	/**
	* Adds a new Recruiter
	* @param recruiter
	*/
	@PostMapping(value="/public/recruiter")
	public ResponseEntity<Void> requestRecruiterAccount(@RequestBody RecruiterAccountRequestAPIInbound recruiter) {
		
		
		recruiterService.addRecruiterAccountRequest(RecruiterAccountRequestAPIInbound.convertToDomin(recruiter));
		
		//TODO: Need service call to create Recruiter but not activate. Instead must set the correct sttus
		
		//TODO: Script to migrate current recruiters to FIRST_GEN accounts
		
		//TODO: Once created GUI needs to show [Account created. Your request will be processed and your account details sent to your provided email address]
		
		//recruiterService.addRecruiter(RecruiterAPIInbound.convertToDomin(recruiter));
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/**
	* Updates an existing recruiter
	* @param recruiter
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value="/recruiter")
	public ResponseEntity<Void> updateRecruiter(RecruiterAPIInbound recruiter) {
		recruiterService.updateRecruiter(RecruiterAPIInbound.convertToDomin(recruiter));
		return ResponseEntity.ok().build();
	}
	
	/**
	* Returns a Collection of Recruiters
	* @return recruiters
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value="/recruiter")
	public Set<RecruiterAPIOutbound> fetchRecruiters(){
		return recruiterService.fetchRecruiters().stream().map(r -> RecruiterAPIOutbound.convertFromDomain(r)).collect(Collectors.toSet());
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
	@PreAuthorize("hasRole('RECRUITER')")
	@GetMapping(value="/recruiter/me")
	public RecruiterAPIOutbound fetchRecruiterOwnAccount() throws IllegalAccessException{
		return RecruiterAPIOutbound.convertFromDomain(recruiterService.fetchRecruiterOwnAccount());
	}
	
	/**
	* Adds a new subscription for the Recruiter
	* @param recruiterId		- Unique Id of the Recruiter owning the Subscription
	* @param subscription		- Type of subscription requested
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@PostMapping(value="/recruiter/{recruiterId}/subscription/")
	public ResponseEntity<Void> addSubscription(@PathVariable("recruiterId") String recruiterId, @RequestBody SubscriptionAPIInbound subscription)  throws IllegalAccessException{
		this.recruiterService.addSubscription(recruiterId, subscription.getType());
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
	public ResponseEntity<Void> performSubscriptionAction(@PathVariable("recruiterId") String recruiterId, @PathVariable("subscriptionId") UUID subscriptionId, @RequestParam("action") subscription_action action)  throws IllegalAccessException{
		this.recruiterService.performSubscriptionAction(recruiterId, subscriptionId, action);
		return ResponseEntity.ok().build();
	}
	
}