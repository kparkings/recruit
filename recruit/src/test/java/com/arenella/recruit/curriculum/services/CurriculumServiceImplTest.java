package com.arenella.recruit.curriculum.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.dao.CurriculumDao;
import com.arenella.recruit.curriculum.entity.CurriculumEntity;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Unit tests for the CurriculumServiceImpl class
* @author K Parkings
*/
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CurriculumServiceImplTest {

	@Mock
	private 				CurriculumDao			 mockCurriculumDao;
	
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
	
}
