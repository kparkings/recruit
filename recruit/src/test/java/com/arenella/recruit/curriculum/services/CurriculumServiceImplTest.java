package com.arenella.recruit.curriculum.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.controllers.CurriculumUpdloadDetails;
import com.arenella.recruit.curriculum.dao.CurriculumDao;
import com.arenella.recruit.curriculum.dao.CurriculumDownloadedEventDao;
import com.arenella.recruit.curriculum.entity.CurriculumDownloadedEventEntity;
import com.arenella.recruit.curriculum.entity.CurriculumEntity;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Unit tests for the CurriculumServiceImpl class
* @author K Parkings
*/
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class CurriculumServiceImplTest {

	@Mock
	private 				CurriculumDao			 		mockCurriculumDao;
	
	@Mock
	private					CurriculumDownloadedEventDao	mockCurriculumDownloadedEventDao;
	
	@Mock
	private					Authentication					mockAuthentication;
	
	@InjectMocks
	private static final 	CurriculumServiceImpl 	service 			= new CurriculumServiceImpl();
	
	final 	String 		curriculumId 	= "501";
	final 	FileType 	fileType 		= FileType.doc;
	final 	byte[] 		file 			= new byte[] {};
	
	/**
	* Happy Path test for persisting a new Curriculum
	* @throws Exception
	*/
	@Test
	public void testPersistCurriculum() throws Exception{
		
		ArgumentCaptor<CurriculumEntity> captorEntity = ArgumentCaptor.forClass(CurriculumEntity.class);
		
		Curriculum curriculum = Curriculum
										.builder()
											.id(curriculumId)
											.fileType(fileType)
											.file(file)
										.build();
		
		String returnedId = service.persistCurriculum(curriculum);
		
		Mockito.verify(mockCurriculumDao).save(captorEntity.capture());
		
		assertEquals(curriculumId, 					returnedId);
		assertEquals(Long.valueOf(curriculumId), 	captorEntity.getValue().getCurriculumId());
		assertEquals(fileType, 						captorEntity.getValue().getFileType());
		
		assertNotNull(captorEntity.getValue().getFile());
		
	}
	
	/**
	* Tests happy path for fetching an existing Curriculum
	* @throws Exception
	*/
	@Test
	public void testFetchCurriculum() throws Exception {
		
		CurriculumEntity curriculumEntity = CurriculumEntity
				.builder()
					.curriculumId(Long.valueOf(curriculumId))
					.fileType(fileType)
					.file(file)
				.build();
		
		Mockito.when(this.mockCurriculumDao.findById(Long.valueOf(curriculumId))).thenReturn(Optional.of(curriculumEntity));
		
		Curriculum curriculum = service.fetchCurriculum(curriculumId);
		
		assertEquals(curriculumId, 					curriculum.getId().get());
		assertEquals(fileType, 						curriculum.getFileType());
		
		assertNotNull(curriculum.getFile());
		
	}
	
	/**
	* Tests case where no Curriculum entities are in the DB. Should return
	* 1
	* @throws Exception
	*/
	@Test
	public void testGetNextCurriculumId_noCurriculums() throws Exception{
		
		Mockito.when(mockCurriculumDao.findTopByOrderByCurriculumIdDesc()).thenReturn(Optional.empty());
		
		assertEquals(1, service.getNextCurriculumId());
		
	}
	
	/**
	* Tests case that Curriculums are in the DB. Should return the id of the 
	* latest Curriculum + 1
	* @throws Exception
	*/
	@Test
	public void testGetNextCurriculumId_existingCurriculums() throws Exception{
		
		final long lastCurriculumId = 500;
		
		Mockito.when(mockCurriculumDao.findTopByOrderByCurriculumIdDesc()).thenReturn(Optional.of(CurriculumEntity.builder().curriculumId(lastCurriculumId).build()));
		
		assertEquals(lastCurriculumId + 1, service.getNextCurriculumId());
		
	}
	
	/**
	* Tests that method leads to an event being persisted for 
	* the curriculum 
	* @throws Exception
	*/
	@Test
	public void testLogCurriculumDownloadedEvent() throws Exception{
		
		final String curriculumId = "100";
		
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
		
		ArgumentCaptor<CurriculumDownloadedEventEntity> captor = ArgumentCaptor.forClass(CurriculumDownloadedEventEntity.class);
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("kparkings");
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(List.of());
		Mockito.when(mockCurriculumDownloadedEventDao.save(captor.capture())).thenReturn(null);
		
		service.logCurriculumDownloadedEvent(curriculumId);
		
		Mockito.verify(mockCurriculumDownloadedEventDao).save(Mockito.any(CurriculumDownloadedEventEntity.class));
		
		assertEquals(curriculumId, captor.getValue().getCurriculumId());
		
	}
	
	/**
	* Tests if there is a Exception the response is still returned with the id
	* for the Curriculum
	* @throws Exception
	*/
	@Test
	public void testExtractDetails_failure_returnsMinumum() throws Exception {
		
		final String 		curriculumId 	= "897";
		final FileType 		fileType 		= FileType.pdf;
		final byte[] 		file 			= new byte[] {};
		
		CurriculumUpdloadDetails extractedDetails = service.extractDetails(curriculumId, fileType, file);
		
		assertEquals(curriculumId, extractedDetails.getId());
		
	}

}