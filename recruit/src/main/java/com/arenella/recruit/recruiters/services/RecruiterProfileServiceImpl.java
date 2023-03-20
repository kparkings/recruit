package com.arenella.recruit.recruiters.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.recruiters.beans.RecruiterProfile;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo;
import com.arenella.recruit.recruiters.beans.RecruiterProfileFilter;
import com.arenella.recruit.recruiters.dao.RecruiterProfileDao;
import com.arenella.recruit.recruiters.utils.ImageManipulator;

/**
* Services for Recruiter Profiles
* @author K Parkings
*/
@Service
public class RecruiterProfileServiceImpl implements RecruiterProfileService{

	@Autowired
	private ImageFileSecurityParser imageFileSecruityParser;
	
	@Autowired
	private RecruiterProfileDao 	recruiterProfileDao;
	
	@Autowired
	private ImageManipulator		imageManipulator;
	
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
			
		}  
		
		this.recruiterProfileDao.saveRecruiterProfile(recruiterProfile);
		
	}

	/**
	* Refer to the RecruiterProfileService interface for details 
	*/
	@Override
	public RecruiterProfile fetchRecruiterProfile(String recruiterProfileId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	* Refer to the RecruiterProfileService interface for details 
	*/
	@Override
	public Set<RecruiterProfile> fetchRecruiterProfiles(RecruiterProfileFilter filters) {
		// TODO Auto-generated method stub
		return null;
	}

}