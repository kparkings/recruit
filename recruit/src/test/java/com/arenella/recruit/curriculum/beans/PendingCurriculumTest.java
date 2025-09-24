package com.arenella.recruit.curriculum.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.curriculum.enums.FileType;

/**
* Unit tests for the PendingCurriculum class
* @author K Parkings
*/
class PendingCurriculumTest {

	private static final UUID	 		ID 				= UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
	private static final FileType 		FILE_TYPE 		= FileType.doc;
	private static final byte[] 		FILE 			= new byte[1];
	
	/**
	* Test Getters return values set in the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		PendingCurriculum curriculum = PendingCurriculum
												.builder()
													.id(ID)
													.fileType(FILE_TYPE)
													.file(FILE)
												.build();
		
		assertEquals(ID, 		curriculum.getId().get());
		assertEquals(FILE_TYPE, curriculum.getFileType());
		assertEquals(FILE, 		curriculum.getFile());
		
	}
	
	/**
	* Test id is empty if no id specified
	* @throws Exception
	*/
	@Test
	void testBuilderNoId() {
		
		PendingCurriculum curriculum = PendingCurriculum
												.builder()
													.fileType(FILE_TYPE)
													.file(FILE)
												.build();
		
		assertTrue(curriculum.getId().isEmpty());
		
	}
	
}