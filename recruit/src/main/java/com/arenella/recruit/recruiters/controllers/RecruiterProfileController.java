package com.arenella.recruit.recruiters.controllers;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.arenella.recruit.recruiters.beans.RecruiterProfileFilter;
import com.arenella.recruit.recruiters.beans.RecruiterProfileFilter.RecruiterProfileFilterBuilder;
import com.arenella.recruit.recruiters.services.RecruiterProfileService;
import com.arenella.recruit.recruiters.services.RecruiterService;
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

	public static enum VISIBILITY_TYPE {PUBLIC, CANDIDATES, RECRUITERS}
	
	@Autowired
	private RecruiterProfileService rpService;
	
	@Autowired
	private RecruiterService		recruiterService;
	
	/**
	* Creates a new Recruiter profile
	* @param profile			- Details of Profile to create
	* @param authenticatedUser  - authenticated User
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@PostMapping(path="recruiter-profile")
	public ResponseEntity<Void> addRecruiterProfile(@RequestPart("profile") RecruiterProfileAPIInbound profile, @RequestPart("file") Optional<MultipartFile> file, Principal authenticatedUser) throws Exception{
		
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
	public ResponseEntity<Void> updateRecruiterProfile(@RequestPart("profile") RecruiterProfileAPIInbound profile, @RequestPart("file") Optional<MultipartFile> file, Principal authenticatedUser) throws Exception{
		
		Optional<PhotoAPIInbound> photo = file.isEmpty() ? Optional.empty() : Optional.of(new PhotoAPIInbound(file.get().getBytes(), PHOTO_FORMAT.jpeg));
		
		this.rpService.updateRecruiterProfile(RecruiterProfileAPIInbound.convertToDomain(profile, photo, authenticatedUser.getName()));
		
		return ResponseEntity.ok().build();
	}
	
	/**
	* Returns the authenticated users Recruiter profile
	* @return Recruiter profile
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@GetMapping(path="recruiter-profile/own")
	public ResponseEntity<Optional<RecruiterProfileAPIOutbound>> fetchRecruiterProfile(Principal authenticatedUser) throws IllegalAccessException{
		
		Recruiter 					recruiter 	= this.recruiterService.fetchRecruiterOwnAccount();
		Optional<RecruiterProfile> 	profile 	= this.rpService.fetchRecruiterProfile(recruiter);
		
		if (profile.isEmpty()) {
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.ok().body(Optional.of(RecruiterProfileAPIOutbound.convertFromDomain(profile.get(), recruiter)));
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
																					@RequestParam(required = true)  VISIBILITY_TYPE 	visibilityType) {
		
		RecruiterProfileFilterBuilder filters = RecruiterProfileFilter.builder();
		
		if (Optional.ofNullable(recruitsIn).isPresent()  && !recruitsIn.isEmpty()) {
			filters.recruitsIn(recruitsIn);
		}
		
		if (Optional.ofNullable(coreTech).isPresent() && !coreTech.isEmpty()) {
			filters.coreTech(coreTech);
		}
		
		if (Optional.ofNullable(recruitsContractTypes).isPresent() && !recruitsContractTypes.isEmpty()) {
			filters.recruitsContractTypes(recruitsContractTypes);
		}
		
		switch(visibilityType) {
			case PUBLIC:{
				filters.visibleToPublic(true);
				break;
			}
			case CANDIDATES:{
				filters.visibleToCandidates(true);
				break;
			}
			case RECRUITERS:{
				filters.visibleToRecruiters(true);
				break;
			}
			default:{
				throw new IllegalStateException("Unsupported Visibility Type");
			}
		}	
		
		/**
		* TODO: [KP] Will need to re-think this if there are large numbers of Recuiter's but for now 
		* 			 this will work well enough to launch the feature
		*/
		Set<Recruiter> recruiters = this.recruiterService.fetchRecruiters();
		
		
		Set<RecruiterProfile> 				profiles = this.rpService.fetchRecruiterProfiles(filters.build());
		Set<RecruiterProfileAPIOutbound> 	outbound = profiles.stream().map(p -> RecruiterProfileAPIOutbound.convertFromDomain(p, getAssociatedRecruiter(recruiters, p.getRecruiterId()))).collect(Collectors.toCollection(LinkedHashSet::new));
		
		return ResponseEntity.ok().body(outbound);
	}
	
	/**
	* Will attempt to find the corresponding Recruiter for the profile but if it cannot 
	* will provide a default recruiter
	* @param recruiters		- all available recruiters
	* @param recruiterId	- unique id of the recruiter
	* @return
	*/
	private Recruiter getAssociatedRecruiter(Set<Recruiter> recruiters, String recruiterId) {
		return recruiters.stream().filter(r -> r.getUserId().equals(recruiterId)).findFirst().orElse(Recruiter.builder().companyName("n\\a").surname("n\\a").firstName("n\\a").build());
	}
	
}