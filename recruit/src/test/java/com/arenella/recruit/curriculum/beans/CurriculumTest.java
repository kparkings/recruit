package com.arenella.recruit.curriculum.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.curriculum.enums.FileType;

/**
* Unit tests for the Curriculum class
* @author K Parkings
*/
class CurriculumTest {

	private static final String 		id 				= "100";
	private static final FileType 		fileType 		= FileType.doc;
	private static final byte[] 		file 			= new byte[1];
	
	/**
	* Test Getters return values set in the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		Curriculum curriculum = 
				Curriculum.builder()
								.id(id)
								.fileType(fileType)
								.file(file)
							.build();

		assertEquals(id, 		curriculum.getId().get());
		assertEquals(fileType, 	curriculum.getFileType());
		assertEquals(file, 		curriculum.getFile());
		
	}
	
	/**
	* Test id is empty if no id specificed
	* @throws Exception
	*/
	@Test
	void testBuilderNoId() {
		
		Curriculum curriculum = 
				Curriculum.builder()
								.fileType(fileType)
								.file(file)
							.build();
		
		assertTrue(curriculum.getId().isEmpty());
		
	}

	/**
	* Test setters
	* @throws Exception
	*/
	@Test
	void testSetters() {
		
		final String ownerId = "a1";
		
		Curriculum curriculum = 
				Curriculum.builder()
								.fileType(fileType)
								.file(file)
							.build();
		
		curriculum.setOwnerId(ownerId);
		assertEquals(ownerId, curriculum.getOwnerId().get());
		
	}
}