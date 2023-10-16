package com.arenella.recruit.curriculum.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.Principal;
import java.util.UUID;

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

import com.arenella.recruit.curriculum.beans.PendingCurriculum;
import com.arenella.recruit.curriculum.enums.FileType;
import com.arenella.recruit.curriculum.services.CurriculumFileSecurityParser;
import com.arenella.recruit.curriculum.services.CurriculumService;

/**
* Unit tests for the CurriculumController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CurriculumControllerTest {

	@InjectMocks
	private CurriculumController 			curriculumController		= new CurriculumController();
	
	@Mock
	private CurriculumService 				mockCurriculumService;
	
	@Mock
	private MultipartFile					mockMultipartFile;
	
	@Mock
	private CurriculumFileSecurityParser	mockFileSecurityParser;
	
	@Mock
	private Principal						mockPrincipal;
	
	
	/**
	* Tests happy path
	* @throws Exception
	*/
	@Test
	public void testUploadPendingCurriculum() throws Exception {
		
		final byte[] curriculumBytes 		= new byte[] {};
		final String originalFileName		= "cv.PDF";
		
		ArgumentCaptor<PendingCurriculum> captor = ArgumentCaptor.forClass(PendingCurriculum.class);
		
		Mockito.when(this.mockFileSecurityParser.isSafe(Mockito.any())).thenReturn(true);
		
		Mockito.when(mockMultipartFile.getBytes()).thenReturn(curriculumBytes);
		Mockito.when(mockMultipartFile.getOriginalFilename()).thenReturn(originalFileName);
		Mockito.doNothing().when(mockCurriculumService).persistPendingCurriculum(captor.capture());
		
		ResponseEntity<UUID> response = curriculumController.uploadPendingCurriculum(mockMultipartFile);
		
		Mockito.verify(mockCurriculumService).persistPendingCurriculum(Mockito.any(PendingCurriculum.class));
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(captor.getValue().getId().get());
		assertEquals(FileType.pdf, captor.getValue().getFileType());
		assertEquals(curriculumBytes, captor.getValue().getFile());
		
	}
	
	/**
	* Tests case that file is deemed unsafe to upload
	* @throws Exception
	*/
	@Test
	public void testUploadPendingCurriculum_unsafe_file() throws Exception {
		
		Mockito.when(this.mockFileSecurityParser.isSafe(Mockito.any())).thenReturn(false);
		
		assertThrows(RuntimeException.class, () -> {
			curriculumController.uploadPendingCurriculum(mockMultipartFile);
		
		});
	}
	
	/**
	* Tests case that file is deemed unsafe to upload
	* @throws Exception
	*/
	@Test
	public void testUploadCurriculum_unsafe_file() throws Exception {
		
		Mockito.when(this.mockFileSecurityParser.isSafe(Mockito.any())).thenReturn(false);
		
		assertThrows(RuntimeException.class, () -> {
			curriculumController.uploadCurriculum(mockMultipartFile, mockPrincipal);
		
		});
	}
	
	/**
	* Tests case that file is deemed unsafe to upload
	* @throws Exception
	*/
	@Test
	public void testUpdateCurriculum_unsafe_file() throws Exception {
		
		Mockito.when(this.mockFileSecurityParser.isSafe(Mockito.any())).thenReturn(false);
		
		assertThrows(RuntimeException.class, () -> {
			curriculumController.updateCurriculum(1L, mockMultipartFile, mockPrincipal);
		
		});
	}
	
	/**
	* Test calling of endpoint to get bytes for PDF version of 
	* curriculum
	* @throws Exception
	*/
	@Test
	public void testGetCurriculumAsPDF() throws Exception {
		
		final String 	curriculumId 	= "1";
		final byte[] 	pdfBytes 		=  new byte[]{};
		
		ArgumentCaptor<String> argCaptId = ArgumentCaptor.forClass(String.class);
		
		Mockito.when(this.mockCurriculumService.getCurriculamAsPdfBytes(argCaptId.capture())).thenReturn(pdfBytes);
		byte[] response = this.curriculumController.getCurriculumAsPDF(curriculumId);
		
		assertEquals(pdfBytes, response);
		assertEquals(curriculumId, argCaptId.getValue());
		
	}
	
	/**
	* Tests deletion of a Pending Curriculum
	* @throws Exception
	*/
	@Test
	public void testDeletePendingCurriculum() throws Exception{
		
		ResponseEntity<Void> response = this.curriculumController.deletePendingCurriculum(UUID.randomUUID());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		Mockito.verify(this.mockCurriculumService).deletePendingCurriculum(Mockito.any());
		
	}
	
}