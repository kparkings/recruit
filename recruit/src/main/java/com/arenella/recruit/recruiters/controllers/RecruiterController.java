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
	* @return
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value="/recruiter")
	public Set<RecruiterAPIOutbound> fetchRecruiters(){
		return recruiterService.fetchRecruiters().stream().map(r -> RecruiterAPIOutbound.convertFromDomain(r)).collect(Collectors.toSet());
	}
	
	/**
	* Returns a Collection of Recruiters
	* @return
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@PostMapping(value="/recruiter/{id}")
	public RecruiterAPIOutbound fetchRecruiter(@PathVariable("id") String recruiterId) throws IllegalAccessException{
		return RecruiterAPIOutbound.convertFromDomain(recruiterService.fetchRecruiter(recruiterId));
	}
	
}