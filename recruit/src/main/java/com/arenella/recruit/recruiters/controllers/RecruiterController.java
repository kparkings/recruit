package com.arenella.recruit.recruiters.controllers;

import java.util.Set;
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
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.recruiters.beans.RecruiterAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterAPIOutbound;
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
	//@PostMapping(value="/public/recruiter")
	//public ResponseEntity<Void> requestRecruiterAccount(@RequestBody RecruiterAPIInbound recruiter) {
		
		//TODO: Enable url in seurity for non auth access 
		
		//TODO: Here we need to do the reverse of how an admin sets up an acount. That is first we create the recruiter. Then if the trial is accepted 
		//		we create the User. That way only the admin user provides authenticated access to the recruiter. This we do by sending an event to the authentication service and 
		//		expect an event to say account created. thereupon we enable the recruiter and provide the login details.
		
		//recruiterService.addRecruiter(RecruiterAPIInbound.convertToDomin(recruiter));
		
	//	return ResponseEntity.status(HttpStatus.CREATED).build();
	//}
	
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
	
}