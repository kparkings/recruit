package com.arenella.recruit.curriculum.entities;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.curriculum.beans.PendingCurriculum;
import com.arenella.recruit.curriculum.entity.PendingCurriculumEntity;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Unit tests for the CurriculumEntity class
* @author K Parkings
*/
public class PendingCurriculumEntityTest {

	/**
	* Tests builder and associated Getters
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		final int 		curriculumId 	= 1;
		final byte[] 	fileBytes 		= new byte[] {};
		final FileType 	fileType 		= FileType.pdf;
		
		PendingCurriculumEntity entity = PendingCurriculumEntity
														.builder()
															.curriculumId(curriculumId)
															.file(fileBytes)
															.fileType(fileType)
														.build();
		
		assertEquals(curriculumId, 	entity.getCurriculumId());
		assertEquals(fileBytes, 	entity.getFile());
		assertEquals(fileType, 		entity.getFileType());
		
	}
	
	/**
	* Tests conversion from the Entity representation to the 
	* Domain representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception {
		
		final int 		curriculumId 	= 1;
		final byte[] 	fileBytes 		= new byte[] {};
		final FileType 	fileType 		= FileType.pdf;
		
		PendingCurriculumEntity entity = PendingCurriculumEntity
														.builder()
															.curriculumId(curriculumId)
															.file(fileBytes)
															.fileType(fileType)
														.build();
		
		PendingCurriculum curriculum = PendingCurriculumEntity.convertFromEntity(entity);
		
		assertEquals(curriculumId, 	Long.valueOf(curriculum.getId().get()).longValue());
		assertEquals(fileBytes, 	curriculum.getFile());
		assertEquals(fileType, 		curriculum.getFileType());
		
	}
	
	/**
	* Tests conversion from the Domain representation to the 
	* Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception {
		
		final String	curriculumId 	= "1";
		final byte[] 	fileBytes 		= new byte[] {};
		final FileType 	fileType 		= FileType.pdf;
		
		PendingCurriculum curriculum = PendingCurriculum
												.builder()
													.id(curriculumId)
													.file(fileBytes)
													.fileType(fileType)
												.build();
		
		PendingCurriculumEntity entity = PendingCurriculumEntity.convertToEntity(curriculum);
		
		assertEquals(curriculumId, 	String.valueOf(entity.getCurriculumId()));
		assertEquals(fileBytes, 	entity.getFile());
		assertEquals(fileType, 		entity.getFileType());
		
	}
	
}