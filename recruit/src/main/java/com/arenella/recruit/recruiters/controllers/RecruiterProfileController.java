package com.arenella.recruit.recruiters.controllers;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.recruiters.beans.RecruiterProfileAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterProfileAPIInbound.PhotoAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterProfileAPIOutbound;
import com.arenella.recruit.recruiters.services.RecruiterProfileService;
import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.RecruiterProfile;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.CONTRACT_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.COUNTRY;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.TECH;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo.PHOTO_FORMAT;

/**
* API for Recruiter Profiles. A Recruiter profile is the public
* image of the Recruiter. It is what other Recruiters and Candidates
* can view to gain insight into the Recruiter
* @author K Parkings
*/
@RestController
public class RecruiterProfileController {

	public static enum VISIBILITY_TYPE {PUBLIC, CANDIATES, RECRUITERS}
	
	@Autowired
	private RecruiterProfileService rpService;
	
	/**
	* Creates a new Recruiter profile
	* @param profile			- Details of Profile to create
	* @param authenticatedUser  - authenticated User
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@PostMapping(path="recruiter-profile")
	public ResponseEntity<Void> addRecruiterProfile(@RequestPart("profile") RecruiterProfileAPIInbound profile, @RequestPart("attachment") Optional<MultipartFile> file, Principal authenticatedUser) throws Exception{
		
		Optional<PhotoAPIInbound> photo = file.isEmpty() ? Optional.empty() : Optional.of(new PhotoAPIInbound(file.get().getBytes(), PHOTO_FORMAT.jpeg));
		
		this.rpService.addRecruiterProfile(RecruiterProfileAPIInbound.convertToDomain(profile, photo, authenticatedUser.getName()));
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	/**
	* Creates an existing Recruiter profile
	* @param profile			- Details of Profile to update
	* @param authenticatedUser
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@PutMapping(path="recruiter-profile")
	public ResponseEntity<Void> updateRecruiterProfile(@RequestPart("profile") RecruiterProfileAPIInbound profile, @RequestPart("attachment") Optional<MultipartFile> file, Principal authenticatedUser) throws Exception{
		
		Optional<PhotoAPIInbound> photo = file.isEmpty() ? Optional.empty() : Optional.of(new PhotoAPIInbound(file.get().getBytes(), PHOTO_FORMAT.jpeg));
		
		this.rpService.updateRecruiterProfile(RecruiterProfileAPIInbound.convertToDomain(profile, photo, authenticatedUser.getName()));
		
		return ResponseEntity.ok().build();
	}
	
	/**
	* Returns the authenticated users Recruiter profile
	* @return Recruiter profile
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@GetMapping(path="recruiter-profile")
	public ResponseEntity<RecruiterProfileAPIOutbound> fetchRecruiterProfile(Principal authenticatedUser) {
		
		RecruiterProfile profile = this.rpService.fetchRecruiterProfile(authenticatedUser.getName());
		
		return ResponseEntity.ok().body(RecruiterProfileAPIOutbound.convertFromDomain(profile, Recruiter.builder().build()));
	}
	
	/**
	* Returns Recruiter Profiles that are visible for Recruiters
	* @param recruitsIn				- Countries Recruiter recruits for
	* @param coreTech				- Technologies Recruiter recruits for
	* @param recruitsContractTypes	- Contract types recruiter recruits for
	* @return Recruiter Profiles
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@GetMapping(path="recruiter-profile")
	public ResponseEntity<Set<RecruiterProfileAPIOutbound>> fetchRecruiterProfiles(	@RequestParam(required = false) Set<COUNTRY> 		recruitsIn,
																					@RequestParam(required = false) Set<TECH>			coreTech,
																					@RequestParam(required = false) Set<CONTRACT_TYPE> 	recruitsContractTypes,
																					@RequestParam(required = true)  VISIBILITY_TYPE 	visibilityTYpe) {
		
		Set<RecruiterProfile> profiles = this.rpService.fetchRecruiterProfiles(null);
		
		
		return ResponseEntity.ok().build();
	}
	
}