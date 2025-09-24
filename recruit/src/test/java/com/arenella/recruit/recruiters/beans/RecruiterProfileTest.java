package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterProfile.CONTRACT_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.COUNTRY;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.LANGUAGE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo.PHOTO_FORMAT;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.REC_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.SECTOR;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.TECH;

/**
* Unit tests for the RecruiterProfile class
* @author K Parkings
*/
class RecruiterProfileTest {

	private static final String 			RECRUITER_ID				= "kparkingS";
	private static final Set<COUNTRY> 		RECRUITS_IN 				= Set.of(COUNTRY.BELGIUM, COUNTRY.IRELAND);
	private static final Set<LANGUAGE>		LANGUAGES_SPOKEN 			= Set.of(LANGUAGE.ENGLISH);
	private static final byte[]				FILE_BYTES					= new byte[] {1,22};					
	private static final Photo				PROFILE_PHOTO 				= new Photo(FILE_BYTES, PHOTO_FORMAT.jpeg);
	private static final boolean 			VISIBLE_TO_RECRUITERS 		= true;
	private static final boolean 			VISIBLE_TO_CANDIDATES 		= true;
	private static final boolean 			VISIBLE_TO_PUBLIC 			= true;
	private static final String				JOB_TITLE 					="Java Recruiter";
	private static final int				YEARS_EXPERIENCE 			= 2;
	private static final String				INTRODUCTION 				= "I am not really a recruiter";
	private static final Set<SECTOR>		SECTORS 					= Set.of(SECTOR.FINTECH, SECTOR.CYBER_INTEL);
	private static final Set<TECH>			CORE_TECH 					= Set.of(TECH.JAVA);
	private static final Set<CONTRACT_TYPE> RECRUITER_CONTRACT_TYPES 	= Set.of(CONTRACT_TYPE.FREELANCE, CONTRACT_TYPE.PERM);
	private static final REC_TYPE	 		RECRUITER_TYPE 				= REC_TYPE.COMMERCIAL;
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		RecruiterProfile profile = 
				RecruiterProfile
					.builder()
						.coreTech(CORE_TECH)
						.introduction(INTRODUCTION)
						.jobTitle(JOB_TITLE)
						.languagesSpoken(LANGUAGES_SPOKEN)
						.profilePhoto(PROFILE_PHOTO)
						.recruiterId(RECRUITER_ID)
						.recruiterType(RECRUITER_TYPE)
						.recruitsContractTypes(RECRUITER_CONTRACT_TYPES)
						.recruitsIn(RECRUITS_IN)
						.sectors(SECTORS)
						.visibleToCandidates(VISIBLE_TO_CANDIDATES)
						.visibleToPublic(VISIBLE_TO_PUBLIC)
						.visibleToRecruiters(VISIBLE_TO_RECRUITERS)
						.yearsExperience(YEARS_EXPERIENCE)
					.build();
		
		assertEquals(INTRODUCTION, 			profile.getIntroduction());
		assertEquals(JOB_TITLE, 			profile.getJobTitle());
		assertEquals(PROFILE_PHOTO, 		profile.getProfilePhoto().get());
		assertEquals(RECRUITER_ID, 			profile.getRecruiterId());
		assertEquals(RECRUITER_TYPE, 		profile.getRecruiterType());
		assertEquals(VISIBLE_TO_CANDIDATES, profile.isVisibleToCandidates());
		assertEquals(VISIBLE_TO_PUBLIC, 	profile.isVisibleToPublic());
		assertEquals(VISIBLE_TO_RECRUITERS, profile.isVisibleToRecruiters());
		assertEquals(YEARS_EXPERIENCE, 		profile.getYearsExperience());
		assertEquals(FILE_BYTES, 			profile.getProfilePhoto().get().getImageBytes());
		assertEquals(PHOTO_FORMAT.jpeg, 	profile.getProfilePhoto().get().getFormat());
		assertEquals(YEARS_EXPERIENCE, 		profile.getYearsExperience());
		
		assertTrue(profile.getCoreTech().contains(TECH.JAVA));
		assertTrue(profile.getLanguagesSpoken().contains(LANGUAGE.ENGLISH));
		assertTrue(profile.getRecruitsContractTypes().contains(CONTRACT_TYPE.FREELANCE));
		assertTrue(profile.getRecruitsContractTypes().contains(CONTRACT_TYPE.PERM));
		assertTrue(profile.getRecruitsIn().contains(COUNTRY.BELGIUM));
		assertTrue(profile.getRecruitsIn().contains(COUNTRY.IRELAND));
		assertTrue(profile.getSectors().contains(SECTOR.FINTECH));
		assertTrue(profile.getSectors().contains(SECTOR.CYBER_INTEL));
		
	}
	
	/**
	* Tests Setters
	* @throws Exception
	*/
	@Test
	void testSetters() {
		
		RecruiterProfile profile = 
				RecruiterProfile
					.builder()
					.build();
	
		Photo profilePhoto = new Photo(new byte[] {}, PHOTO_FORMAT.jpeg);
		
		profile.setProfilePhoto(profilePhoto);
		
		assertEquals(profilePhoto, profile.getProfilePhoto().get());
		
	}
	
}