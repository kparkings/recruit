package com.arenella.recruit.curriculum.entities;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.curriculum.beans.PendingCurriculum;
import com.arenella.recruit.curriculum.entity.PendingCurriculumEntity;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Unit tests for the CurriculumEntity class
* @author K Parkings
*/
class PendingCurriculumEntityTest {

	/**
	* Tests builder and associated Getters
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		final UUID 		curriculumId 	= UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
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
	void testConvertFromEntity() {
		
		final UUID 		curriculumId 	= UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
		final byte[] 	fileBytes 		= new byte[] {};
		final FileType 	fileType 		= FileType.pdf;
		
		PendingCurriculumEntity entity = PendingCurriculumEntity
														.builder()
															.curriculumId(curriculumId)
															.file(fileBytes)
															.fileType(fileType)
														.build();
		
		PendingCurriculum curriculum = PendingCurriculumEntity.convertFromEntity(entity);
		
		assertEquals(curriculumId, 	curriculum.getId().get());
		assertEquals(fileBytes, 	curriculum.getFile());
		assertEquals(fileType, 		curriculum.getFileType());
		
	}
	
	/**
	* Tests conversion from the Domain representation to the 
	* Entity representation
	* @throws Exception
	*/
	@Test
	void testConvertToEntity() {
		
		final UUID	curriculumId 	= UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
		final byte[] 	fileBytes 		= new byte[] {};
		final FileType 	fileType 		= FileType.pdf;
		
		PendingCurriculum curriculum = PendingCurriculum
												.builder()
													.id(curriculumId)
													.file(fileBytes)
													.fileType(fileType)
												.build();
		
		PendingCurriculumEntity entity = PendingCurriculumEntity.convertToEntity(curriculum);
		
		assertEquals(curriculumId, 	entity.getCurriculumId());
		assertEquals(fileBytes, 	entity.getFile());
		assertEquals(fileType, 		entity.getFileType());
		
	}
	
}