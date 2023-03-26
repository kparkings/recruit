package com.arenella.recruit.recruiters.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.RecruiterProfile;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.CONTRACT_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.COUNTRY;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.LANGUAGE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.REC_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.SECTOR;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo.PHOTO_FORMAT;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.TECH;
import com.arenella.recruit.recruiters.beans.RecruiterProfileAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterProfileAPIOutbound;
import com.arenella.recruit.recruiters.beans.RecruiterProfileFilter;
import com.arenella.recruit.recruiters.controllers.RecruiterProfileController.VISIBILITY_TYPE;
import com.arenella.recruit.recruiters.services.RecruiterProfileService;
import com.arenella.recruit.recruiters.services.RecruiterService;

/**
* Unit tests for the RecruiterProfileController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class RecruiterProfileControllerTest {

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
	
	@Mock
	private Principal							mockPrincipal;
	
	@Mock
	private MultipartFile						mockMultipartFile;
	
	@Mock
	private RecruiterProfileService 			mockRPService;
	
	@Mock
	private RecruiterService					mockRecruiterService;
	
	@InjectMocks
	private RecruiterProfileController controller;
	
	/**
	* Test adding new Recruiter Profile
	* @throws Exception
	*/
	@Test
	public void testAddRecruiterProfile() throws Exception{
		
		Mockito.when(this.mockPrincipal.getName()).thenReturn("kparkings");
		Mockito.when(this.mockMultipartFile.getBytes()).thenReturn(new byte[]{});
		
		ResponseEntity<Void> response = controller.addRecruiterProfile(RecruiterProfileAPIInbound.builder().build(), Optional.of(mockMultipartFile), mockPrincipal);
	
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
	}
	
	/**
	* Test updating existing Recruiter Profile
	* @throws Exception
	*/
	@Test
	public void testUpdateRecruiterProfile() throws Exception{
	
		Mockito.when(this.mockPrincipal.getName()).thenReturn("kparkings");
		Mockito.when(this.mockMultipartFile.getBytes()).thenReturn(new byte[]{});
		
		ResponseEntity<Void> response = controller.updateRecruiterProfile(RecruiterProfileAPIInbound.builder().build(), Optional.of(mockMultipartFile), mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Test fetching of authenticated users Recruiter Profile
	* @throws Exception
	*/
	@Test
	public void testFetchRecruiterProfile() throws Exception{
		
		Recruiter recruiter = Recruiter.builder().companyName("arenella bv").firstName("kevin").surname("parkings").build();
		
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
		
		Mockito.when(this.mockRPService.fetchRecruiterProfile(Mockito.any())).thenReturn(Optional.of(profile));
		Mockito.when(this.mockRecruiterService.fetchRecruiterOwnAccount()).thenReturn(recruiter);
		Mockito.when(this.mockPrincipal.getName()).thenReturn("notAdmin");
		
		ResponseEntity<Optional<RecruiterProfileAPIOutbound>> response = controller.fetchRecruiterProfile(mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isPresent());
		
	}
	
	/**
	* Test fetching of authenticated users Recruiter Profile where the profile
	* does not exist
	* @throws Exception
	*/
	@Test
	public void testFetchRecruiterProfile_profile_doesnt_exist() throws Exception{
		
		Recruiter recruiter = Recruiter.builder().companyName("arenella bv").firstName("kevin").surname("parkings").build();
	
		Mockito.when(this.mockRPService.fetchRecruiterProfile(Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(this.mockRecruiterService.fetchRecruiterOwnAccount()).thenReturn(recruiter);
		Mockito.when(this.mockPrincipal.getName()).thenReturn("notAdmin");
		
		ResponseEntity<Optional<RecruiterProfileAPIOutbound>> response = controller.fetchRecruiterProfile(mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNull(response.getBody());
		
	}
	
	/**
	* Test fetching of Recruiter Profiles
	* @throws Exception
	*/
	@Test
	public void testFetchRecruiterProfiles() throws Exception{
		
		Recruiter recruiter1 = Recruiter.builder().userId("bparkings").companyName("arenella bv").firstName("kevin").surname("parkings").build();
		
		RecruiterProfile rp1 = 
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
		
		Recruiter recruiter2 = Recruiter.builder().userId("kparkings").companyName("arenella bv").firstName("kevin").surname("parkings").build();
		
		RecruiterProfile rp2 = 
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
		
		
		ArgumentCaptor<RecruiterProfileFilter> filterCapt = ArgumentCaptor.forClass(RecruiterProfileFilter.class);
		
		Mockito.when(this.mockRPService.fetchRecruiterProfiles(filterCapt.capture())).thenReturn(Set.of(rp1,rp2));
		Mockito.when(this.mockRecruiterService.fetchRecruiters()).thenReturn(Set.of(recruiter1, recruiter2));
		
		ResponseEntity<Set<RecruiterProfileAPIOutbound>> response = controller.fetchRecruiterProfiles(Set.of(COUNTRY.BELGIUM), Set.of(TECH.ARCHITECT), Set.of(CONTRACT_TYPE.FREELANCE), VISIBILITY_TYPE.CANDIDATES, this.mockPrincipal);
		
		RecruiterProfileFilter filters = filterCapt.getValue();
		
		assertTrue(filters.getRecruitsIn().contains(COUNTRY.BELGIUM));
		assertTrue(filters.getCoreTech().contains(TECH.ARCHITECT));
		assertTrue(filters.getRecruitsContractTypes().contains(CONTRACT_TYPE.FREELANCE));
		assertTrue(filters.isVisibleToCandidates().get().booleanValue());
		assertTrue(filters.isVisibleToRecruiters().isEmpty());
		assertTrue(filters.isVisibleToPublic().isEmpty());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Test sending of contact request email
	* @throws Exception
	*/
	@Test
	public void testContactRecruiterForOpenPosition() throws Exception{
		
		final String message = "aMessage";

		ResponseEntity<Void> response = this.controller.contactRecruiterForOpenPosition(RECRUITER_ID, JOB_TITLE, message, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
}