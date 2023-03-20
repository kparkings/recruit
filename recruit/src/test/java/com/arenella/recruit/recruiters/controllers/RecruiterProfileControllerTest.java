package com.arenella.recruit.recruiters.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.recruiters.beans.RecruiterProfile;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo.PHOTO_FORMAT;
import com.arenella.recruit.recruiters.beans.RecruiterProfileAPIInbound;
import com.arenella.recruit.recruiters.beans.RecruiterProfileAPIOutbound;
import com.arenella.recruit.recruiters.controllers.RecruiterProfileController.VISIBILITY_TYPE;
import com.arenella.recruit.recruiters.services.RecruiterProfileService;

/**
* Unit tests for the RecruiterProfileController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class RecruiterProfileControllerTest {

	@Mock
	private Principal							mockPrincipal;
	
	@Mock
	private MultipartFile						mockMultipartFile;
	
	@Mock
	private RecruiterProfileService 			mockRPService;
	
	
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
		
		Photo photo = new Photo(new byte[] {}, PHOTO_FORMAT.jpeg);
		
		Mockito.when(this.mockPrincipal.getName()).thenReturn("kparkings");
		Mockito.when(this.mockRPService.fetchRecruiterProfile(Mockito.anyString())).thenReturn(RecruiterProfile.builder().profilePhoto(photo).build());
		
		ResponseEntity<RecruiterProfileAPIOutbound> response = controller.fetchRecruiterProfile(mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Test fetching of Recruiter Profiles
	* @throws Exception
	*/
	@Test
	public void testFetchRecruiterProfiles() throws Exception{
		
		ResponseEntity<Set<RecruiterProfileAPIOutbound>> response = controller.fetchRecruiterProfiles(Set.of(), Set.of(), Set.of(), VISIBILITY_TYPE.CANDIATES);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
}