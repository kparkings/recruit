package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterProfile.CONTRACT_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.COUNTRY;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.LANGUAGE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.REC_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.SECTOR;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.TECH;
import com.arenella.recruit.recruiters.beans.RecruiterProfileAPIInbound.PhotoAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo.PHOTO_FORMAT;

/**
* Unit tests for the RecruiterProfileAPIInbound class
* @author K Parkings
*/
public class RecruiterProfileAPIInboundTest {

	private static final Set<COUNTRY> 		RECRUITS_IN 				= Set.of(COUNTRY.BELGIUM, COUNTRY.IRELAND);
	private static final Set<LANGUAGE>		LANGUAGES_SPOKEN 			= Set.of(LANGUAGE.ENGLISH);
	private static final byte[]				FILE_BYTES					= new byte[] {1,22};					
	private static final PhotoAPIInbound	PROFILE_PHOTO 				= new PhotoAPIInbound(FILE_BYTES, PHOTO_FORMAT.jpeg);
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
	public void testBuilder() throws Exception{
		
		RecruiterProfileAPIInbound profile = 
				RecruiterProfileAPIInbound
					.builder()
						.coreTech(CORE_TECH)
						.introduction(INTRODUCTION)
						.jobTitle(JOB_TITLE)
						.languagesSpoken(LANGUAGES_SPOKEN)
						.profilePhoto(PROFILE_PHOTO)
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
		assertEquals(PROFILE_PHOTO, 		profile.getProfilePhoto());
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
	public void testConvertToDomain() throws Exception{
		
		final PhotoAPIInbound photo = new PhotoAPIInbound(FILE_BYTES, PHOTO_FORMAT.jpeg);
		
		RecruiterProfileAPIInbound profile = 
				RecruiterProfileAPIInbound
					.builder()
						.coreTech(CORE_TECH)
						.introduction(INTRODUCTION)
						.jobTitle(JOB_TITLE)
						.languagesSpoken(LANGUAGES_SPOKEN)
						.profilePhoto(photo)
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
		assertEquals(photo,			 		profile.getProfilePhoto());
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
		
		RecruiterProfile domain = RecruiterProfileAPIInbound.convertToDomain(profile, Optional.of(new PhotoAPIInbound(FILE_BYTES, PHOTO_FORMAT.jpeg)), "kparkings"); 
		
		assertEquals(INTRODUCTION, 			domain.getIntroduction());
		assertEquals(JOB_TITLE, 			domain.getJobTitle());
		assertEquals(RECRUITER_TYPE, 		domain.getRecruiterType());
		assertEquals(VISIBLE_TO_CANDIDATES, domain.isVisibleToCandidates());
		assertEquals(VISIBLE_TO_PUBLIC, 	domain.isVisibleToPublic());
		assertEquals(VISIBLE_TO_RECRUITERS, domain.isVisibleToRecruiters());
		assertEquals(YEARS_EXPERIENCE, 		domain.getYearsExperience());
		assertEquals(FILE_BYTES, 			domain.getProfilePhoto().get().getImageBytes());
		assertEquals(PHOTO_FORMAT.jpeg, 	domain.getProfilePhoto().get().getFormat());
		assertEquals(YEARS_EXPERIENCE, 		domain.getYearsExperience());
		
		assertEquals(photo.getImageBytes(), domain.getProfilePhoto().get().getImageBytes());
		assertEquals(photo.getFormat(), 	domain.getProfilePhoto().get().getFormat());
		
		assertTrue(domain.getCoreTech().contains(TECH.JAVA));
		assertTrue(domain.getLanguagesSpoken().contains(LANGUAGE.ENGLISH));
		assertTrue(domain.getRecruitsContractTypes().contains(CONTRACT_TYPE.FREELANCE));
		assertTrue(domain.getRecruitsContractTypes().contains(CONTRACT_TYPE.PERM));
		assertTrue(domain.getRecruitsIn().contains(COUNTRY.BELGIUM));
		assertTrue(domain.getRecruitsIn().contains(COUNTRY.IRELAND));
		assertTrue(domain.getSectors().contains(SECTOR.FINTECH));
		assertTrue(domain.getSectors().contains(SECTOR.CYBER_INTEL));

	}
	
}