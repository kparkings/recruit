package com.arenella.recruit.recruiters.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterContactRequestEvent;
import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.RecruiterProfile;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo;
import com.arenella.recruit.recruiters.beans.RecruiterProfileFilter;
import com.arenella.recruit.recruiters.dao.RecruiterDao;
import com.arenella.recruit.recruiters.dao.RecruiterProfileDao;
import com.arenella.recruit.recruiters.utils.ImageManipulator;

/**
* Services for Recruiter Profiles
* @author K Parkings
*/
@Service
public class RecruiterProfileServiceImpl implements RecruiterProfileService{

	@Autowired
	private ImageFileSecurityParser 			imageFileSecruityParser;
	
	@Autowired
	private RecruiterProfileDao 				recruiterProfileDao;
	
	@Autowired
	private ImageManipulator					imageManipulator;
	
	@Autowired
	private RecruiterDao						recruiterDao;
	
	@Autowired
	private RecruitersExternalEventPublisher	eventPublisher;
	
	/**
	* Refer to the RecruiterProfileService interface for details 
	*/
	@Override
	public void addRecruiterProfile(RecruiterProfile recruiterProfile) {
		
		if (this.recruiterProfileDao.existsById(recruiterProfile.getRecruiterId())) {
			throw new IllegalStateException("Cannot add an already existing Profile");
		}
		
		if (recruiterProfile.getProfilePhoto().isPresent()) {
			
			if (!imageFileSecruityParser.isSafe(recruiterProfile.getProfilePhoto().get().getImageBytes())) {
				throw new RuntimeException("Invalid file type detected"); 
			}
		
			byte[] photoBytes = this.imageManipulator.toProfileImage(recruiterProfile.getProfilePhoto().get().getImageBytes(), recruiterProfile.getProfilePhoto().get().getFormat());
					
			Photo photo = new Photo(photoBytes, recruiterProfile.getProfilePhoto().get().getFormat());
			
			recruiterProfile.setProfilePhoto(photo);
			
		}  
		
		this.recruiterProfileDao.saveRecruiterProfile(recruiterProfile);
		
	}

	/**
	* Refer to the RecruiterProfileService interface for details 
	*/
	@Override
	public void updateRecruiterProfile(RecruiterProfile recruiterProfile) {
		
		if (!this.recruiterProfileDao.existsById(recruiterProfile.getRecruiterId())) {
			throw new IllegalStateException("Cannot update a non exintent Profile");
		}
		
		if (recruiterProfile.getProfilePhoto().isPresent()) {
			
			if (!imageFileSecruityParser.isSafe(recruiterProfile.getProfilePhoto().get().getImageBytes())) {
				throw new RuntimeException("Invalid file type detected"); 
			}
		
			byte[] photoBytes = this.imageManipulator.toProfileImage(recruiterProfile.getProfilePhoto().get().getImageBytes(), recruiterProfile.getProfilePhoto().get().getFormat());
					
			Photo photo = new Photo(photoBytes, recruiterProfile.getProfilePhoto().get().getFormat());
			
			recruiterProfile.setProfilePhoto(photo);
			
		} else {
			
			Recruiter 			recruiter;
			
			/**
			* [KP]  I know its ugly but for the admin user there is no recruiter object and 
			* 		this means I can concentrate on the real functionality until I have time 
			* 		to do something prettier
			*/
			if (recruiterProfile.getRecruiterId().equals("kparkings")) {
				recruiter = Recruiter.builder().userId("kparkings").companyName("Arenella BV").firstName("kevin").surname("parkings").build();
			} else {
				recruiter = this.recruiterDao.findRecruiterById(recruiterProfile.getRecruiterId()).get();
			}
			
			RecruiterProfile 	existing 	= this.recruiterProfileDao.fetchRecruiterProfileById(recruiter).get();
			
			if (existing.getProfilePhoto().isPresent()) {
				recruiterProfile.setProfilePhoto(existing.getProfilePhoto().get());
			}
		}
		
		this.recruiterProfileDao.saveRecruiterProfile(recruiterProfile);
		
	}

	/**
	* Refer to the RecruiterProfileService interface for details 
	*/
	@Override
	public Optional<RecruiterProfile> fetchRecruiterProfile(Recruiter recruiter) {
		return this.recruiterProfileDao.fetchRecruiterProfileById(recruiter);
	}

	/**
	* Refer to the RecruiterProfileService interface for details 
	*/
	@Override
	public Set<RecruiterProfile> fetchRecruiterProfiles(RecruiterProfileFilter filters) {
		return this.recruiterProfileDao.fetchByFilters(filters);
	}

	/**
	* Refer to the RecruiterProfileService interface for details 
	*/
	@Override
	public void sendEmailToRecruiter(String message, String recruiterId, String title, String authorizedUserId) {
		
		this.eventPublisher
			.publishRecruiterContactRequestEvent(new RecruiterContactRequestEvent(authorizedUserId, recruiterId, title, message));
		
	}

}