package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterProfile.CONTRACT_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.COUNTRY;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.LANGUAGE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.REC_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.SECTOR;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.TECH;
import com.arenella.recruit.recruiters.beans.RecruiterProfileAPIOutbound.PhotoAPIOutbound;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo.PHOTO_FORMAT;

/**
* Unit tests for the RecruiterProfileAPIOutbound class
* @author K Parkings
*/
class RecruiterProfileAPIOutboundTest {

	private static final String 			RECRUITER_ID				= "kparkingS";
	private static final String				FIRST_NAME 					= "kevin";
	private static final String 			SURNAME						= "parkings";
	private static final Set<COUNTRY> 		RECRUITS_IN 				= Set.of(COUNTRY.BELGIUM, COUNTRY.IRELAND);
	private static final Set<LANGUAGE>		LANGUAGES_SPOKEN 			= Set.of(LANGUAGE.ENGLISH);
	private static final byte[]				FILE_BYTES					= new byte[] {1,22};					
	private static final PhotoAPIOutbound	PROFILE_PHOTO 				= new PhotoAPIOutbound(FILE_BYTES, PHOTO_FORMAT.jpeg);
	private static final boolean 			VISIBLE_TO_RECRUITERS 		= true;
	private static final boolean 			VISIBLE_TO_CANDIDATES 		= true;
	private static final boolean 			VISIBLE_TO_PUBLIC 			= true;
	private static final String				COMPANY_NAME 				= "arenella bv";
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
		
		RecruiterProfileAPIOutbound profile = 
				RecruiterProfileAPIOutbound
					.builder()
						.companyName(COMPANY_NAME)
						.coreTech(CORE_TECH)
						.introduction(INTRODUCTION)
						.jobTitle(JOB_TITLE)
						.languagesSpoken(LANGUAGES_SPOKEN)
						.profilePhoto(PROFILE_PHOTO)
						.recruiterFirstName(FIRST_NAME)
						.recruiterId(RECRUITER_ID)
						.recruiterSurname(SURNAME)
						.recruiterType(RECRUITER_TYPE)
						.recruitsContractTypes(RECRUITER_CONTRACT_TYPES)
						.recruitsIn(RECRUITS_IN)
						.sectors(SECTORS)
						.visibleToCandidates(VISIBLE_TO_CANDIDATES)
						.visibleToPublic(VISIBLE_TO_PUBLIC)
						.visibleToRecruiters(VISIBLE_TO_RECRUITERS)
						.yearsExperience(YEARS_EXPERIENCE)
					.build();
		

		assertEquals(COMPANY_NAME, 			profile.getCompanyName());
		assertEquals(INTRODUCTION, 			profile.getIntroduction());
		assertEquals(JOB_TITLE, 			profile.getJobTitle());
		assertEquals(PROFILE_PHOTO, 		profile.getProfilePhoto());
		assertEquals(FIRST_NAME, 			profile.getRecruiterFirstName());
		assertEquals(RECRUITER_ID, 			profile.getRecruiterId());
		assertEquals(SURNAME, 				profile.getRecruiterSurname());
		assertEquals(RECRUITER_TYPE, 		profile.getRecruiterType());
		assertEquals(VISIBLE_TO_CANDIDATES, profile.isVisibleToCandidates());
		assertEquals(VISIBLE_TO_PUBLIC, 	profile.isVisibleToPublic());
		assertEquals(VISIBLE_TO_RECRUITERS, profile.isVisibleToRecruiters());
		assertEquals(YEARS_EXPERIENCE, 		profile.getYearsExperience());
		assertEquals(FILE_BYTES, 			profile.getProfilePhoto().getImageBytes());
		assertEquals(PHOTO_FORMAT.jpeg, 	profile.getProfilePhoto().getFormat());
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
	* Tests conversion from Domain to APIOutbound representations
	* @throws Exception
	*/
	@Test
	void testConvertFromDomain() {
		
		final Photo photo = new Photo(FILE_BYTES, PHOTO_FORMAT.jpeg);
		
		Recruiter recruiter = Recruiter.builder().firstName(FIRST_NAME).surname(SURNAME).companyName(COMPANY_NAME).build();
		
		RecruiterProfile profile = 
				RecruiterProfile
					.builder()
						.coreTech(CORE_TECH)
						.introduction(INTRODUCTION)
						.jobTitle(JOB_TITLE)
						.languagesSpoken(LANGUAGES_SPOKEN)
						.profilePhoto(photo)
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
		assertEquals(photo,			 		profile.getProfilePhoto().get());
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
		
		RecruiterProfileAPIOutbound apiOutbound = RecruiterProfileAPIOutbound.convertFromDomain(profile, recruiter); 
		
		assertEquals(COMPANY_NAME, 			apiOutbound.getCompanyName());
		assertEquals(INTRODUCTION, 			apiOutbound.getIntroduction());
		assertEquals(JOB_TITLE, 			apiOutbound.getJobTitle());
		assertEquals(FIRST_NAME, 			apiOutbound.getRecruiterFirstName());
		assertEquals(RECRUITER_ID, 			apiOutbound.getRecruiterId());
		assertEquals(SURNAME, 				apiOutbound.getRecruiterSurname());
		assertEquals(RECRUITER_TYPE, 		apiOutbound.getRecruiterType());
		assertEquals(VISIBLE_TO_CANDIDATES, apiOutbound.isVisibleToCandidates());
		assertEquals(VISIBLE_TO_PUBLIC, 	apiOutbound.isVisibleToPublic());
		assertEquals(VISIBLE_TO_RECRUITERS, apiOutbound.isVisibleToRecruiters());
		assertEquals(YEARS_EXPERIENCE, 		apiOutbound.getYearsExperience());
		assertEquals(FILE_BYTES, 			apiOutbound.getProfilePhoto().getImageBytes());
		assertEquals(PHOTO_FORMAT.jpeg, 	apiOutbound.getProfilePhoto().getFormat());
		assertEquals(YEARS_EXPERIENCE, 		apiOutbound.getYearsExperience());
		
		assertEquals(photo.getImageBytes(), apiOutbound.getProfilePhoto().getImageBytes());
		assertEquals(photo.getFormat(), 	apiOutbound.getProfilePhoto().getFormat());
		
		assertTrue(apiOutbound.getCoreTech().contains(TECH.JAVA));
		assertTrue(apiOutbound.getLanguagesSpoken().contains(LANGUAGE.ENGLISH));
		assertTrue(apiOutbound.getRecruitsContractTypes().contains(CONTRACT_TYPE.FREELANCE));
		assertTrue(apiOutbound.getRecruitsContractTypes().contains(CONTRACT_TYPE.PERM));
		assertTrue(apiOutbound.getRecruitsIn().contains(COUNTRY.BELGIUM));
		assertTrue(apiOutbound.getRecruitsIn().contains(COUNTRY.IRELAND));
		assertTrue(apiOutbound.getSectors().contains(SECTOR.FINTECH));
		assertTrue(apiOutbound.getSectors().contains(SECTOR.CYBER_INTEL));
		
	}

}