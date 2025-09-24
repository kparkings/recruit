package com.arenella.recruit.curriculum.entities;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.entity.CurriculumEntity;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Unit tests for the CurriculumEntity class
* @author K Parkings
*/
class CurriculumEntityTest {

	/**
	* Tests builder and associated Getters
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		final int 		curriculumId 	= 1;
		final byte[] 	fileBytes 		= new byte[] {};
		final FileType 	fileType 		= FileType.pdf;
		
		CurriculumEntity entity = CurriculumEntity
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
		
		final int 		curriculumId 	= 1;
		final byte[] 	fileBytes 		= new byte[] {};
		final FileType 	fileType 		= FileType.pdf;
		
		CurriculumEntity entity = CurriculumEntity
											.builder()
												.curriculumId(curriculumId)
												.file(fileBytes)
												.fileType(fileType)
											.build();
		
		Curriculum curriculum = CurriculumEntity.convertFromEntity(entity);
		
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
	void testConvertToEntity() {
		
		final String	curriculumId 	= "1";
		final byte[] 	fileBytes 		= new byte[] {};
		final FileType 	fileType 		= FileType.pdf;
		
		Curriculum curriculum = Curriculum
								.builder()
									.id(curriculumId)
									.file(fileBytes)
									.fileType(fileType)
								.build();
		
		CurriculumEntity entity = CurriculumEntity.convertToEntity(curriculum);
		
		assertEquals(curriculumId, 	String.valueOf(entity.getCurriculumId()));
		assertEquals(fileBytes, 	entity.getFile());
		assertEquals(fileType, 		entity.getFileType());
		
	}
	
}