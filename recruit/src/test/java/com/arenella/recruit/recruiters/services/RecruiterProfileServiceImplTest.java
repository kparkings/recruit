package com.arenella.recruit.recruiters.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.ContactRequestEvent;
import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
import com.arenella.recruit.recruiters.beans.RecruiterProfile;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo.PHOTO_FORMAT;
import com.arenella.recruit.recruiters.dao.RecruiterProfileDao;
import com.arenella.recruit.recruiters.utils.ImageManipulator;

/**
* Unit tests for the RecruiterProfileServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class RecruiterProfileServiceImplTest {

	@Mock
	private RecruiterProfileDao					mockRecruiterProfileDao;
	
	@Mock
	private ImageFileSecurityParser 			mockImageFileSecruityParser;
	
	@Mock
	private ImageManipulator					mockImageManipulator;
	
	@Mock
	private RecruitersExternalEventPublisher	mockEeventPublisher;
	
	@InjectMocks
	private RecruiterProfileService 			service 						= new RecruiterProfileServiceImpl();
	
	
	/**
	* Test case Profile already exits for the Recruiter
	* @throws Exception
	*/
	@Test
	public void testAddRecruiterProfile_alreadyExists() throws Exception{
		
		Mockito.when(mockRecruiterProfileDao.existsById(Mockito.anyString())).thenReturn(true);
		
		assertThrows(IllegalStateException.class, () -> {
			service.addRecruiterProfile(RecruiterProfile.builder().recruiterId("kparkings").build());
		});
		
	}
	
	/**
	* Tests case the image was provided but an unsafe type
	* was detected
	* @throws Exception
	*/
	@Test
	public void testAddRecruiterProfile_unsafeFileType() throws Exception{
	
		Mockito.when(mockRecruiterProfileDao.existsById(Mockito.anyString())).thenReturn(false);
		Mockito.when(mockImageFileSecruityParser.isSafe(Mockito.any())).thenReturn(false);
		
		assertThrows(RuntimeException.class, () -> {
			service.addRecruiterProfile(RecruiterProfile.builder().recruiterId("kparkings").profilePhoto(new Photo(new byte[]{}, PHOTO_FORMAT.jpeg)).build());
		});
		
	}
	
	/**
	* Tests case that photo is present and is resized
	* @throws Exception
	*/
	@Test
	public void testAddRecruiterProfile_photoPresent() throws Exception{
	
		final byte[] resizedImage = new byte[] {17,18};
		
		ArgumentCaptor<RecruiterProfile> argRecProf = ArgumentCaptor.forClass(RecruiterProfile.class);
		
		Mockito.when(this.mockRecruiterProfileDao.existsById(Mockito.anyString())).thenReturn(false);
		Mockito.when(this.mockImageFileSecruityParser.isSafe(Mockito.any())).thenReturn(true);
		Mockito.when(this.mockImageManipulator.toProfileImage(Mockito.any(), Mockito.any())).thenReturn(resizedImage);
		Mockito.doNothing().when(this.mockRecruiterProfileDao).saveRecruiterProfile(argRecProf.capture());
		
		service.addRecruiterProfile(RecruiterProfile.builder().recruiterId("kparkings").profilePhoto(new Photo(new byte[]{}, PHOTO_FORMAT.jpeg)).build());
		
		assertEquals(resizedImage, argRecProf.getValue().getProfilePhoto().get().getImageBytes());
		
	}
	
	/**
	* Test case Profile does not already exits for the Recruiter
	* @throws Exception
	*/
	@Test
	public void testAddRecruiterProfile_doesNotalreadyExists() throws Exception{
		
		Mockito.when(mockRecruiterProfileDao.existsById(Mockito.anyString())).thenReturn(false);
		
		assertThrows(IllegalStateException.class, () -> {
			service.updateRecruiterProfile(RecruiterProfile.builder().recruiterId("kparkings").build());
		});
		
	}
	
	/**
	* Tests case the image was provided but an unsafe type
	* was detected
	* @throws Exception
	*/
	@Test
	public void testUpdateRecruiterProfile_unsafeFileType() throws Exception{
	
		Mockito.when(mockRecruiterProfileDao.existsById(Mockito.anyString())).thenReturn(true);
		Mockito.when(mockImageFileSecruityParser.isSafe(Mockito.any())).thenReturn(false);
		
		assertThrows(RuntimeException.class, () -> {
			service.updateRecruiterProfile(RecruiterProfile.builder().recruiterId("kparkings").profilePhoto(new Photo(new byte[]{}, PHOTO_FORMAT.jpeg)).build());
		});
		
	}
	
	/**
	* Tests case that photo is present and is resized
	* @throws Exception
	*/
	@Test
	public void testUpdateRecruiterProfile_photoPresent() throws Exception{
	
		final byte[] resizedImage = new byte[] {17,18};
		
		ArgumentCaptor<RecruiterProfile> argRecProf = ArgumentCaptor.forClass(RecruiterProfile.class);
		
		Mockito.when(this.mockRecruiterProfileDao.existsById(Mockito.anyString())).thenReturn(true);
		Mockito.when(this.mockImageFileSecruityParser.isSafe(Mockito.any())).thenReturn(true);
		Mockito.when(this.mockImageManipulator.toProfileImage(Mockito.any(), Mockito.any())).thenReturn(resizedImage);
		Mockito.doNothing().when(this.mockRecruiterProfileDao).saveRecruiterProfile(argRecProf.capture());
		
		service.updateRecruiterProfile(RecruiterProfile.builder().recruiterId("kparkings").profilePhoto(new Photo(new byte[]{}, PHOTO_FORMAT.jpeg)).build());
		
		assertEquals(resizedImage, argRecProf.getValue().getProfilePhoto().get().getImageBytes());
		
	}
	
	/**
	* Test event generated and published
	* @throws Exception
	*/
	@Test
	public void testSendEmailToRecruiter() throws Exception{
		
		final String message 			= "aMessage";
		final String title 				= "aTitle";
		final String recipientId 		= "ref45";
		final String senderId 			= "123";
		
		ArgumentCaptor<ContactRequestEvent> argCapt = ArgumentCaptor.forClass(ContactRequestEvent.class);
		
		Mockito.doNothing().when(this.mockEeventPublisher).publishRecruiterContactRequestEvent(argCapt.capture());
		
		this.service.sendEmailToRecruiter(message, title, recipientId, senderId);
		
		assertEquals(message, 		argCapt.getValue().getMessage());
		assertEquals(recipientId, 	argCapt.getValue().getRecipientId());
		assertEquals(senderId, 		argCapt.getValue().getSenderRecruiterId());
		assertEquals(title, 		argCapt.getValue().getTitle());
		
	}
	
}
