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
public class PendingCurriculumTest {

	private static final UUID	 		id 				= UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
	private static final FileType 		fileType 		= FileType.doc;
	private static final byte[] 		file 			= new byte[1];
	
	/**
	* Test Getters return values set in the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		PendingCurriculum curriculum = PendingCurriculum
												.builder()
													.id(id)
													.fileType(fileType)
													.file(file)
												.build();
		
		assertEquals(curriculum.getId().get(), 	id);
		assertEquals(curriculum.getFileType(), 	fileType);
		assertEquals(curriculum.getFile(), 		file);
		
	}
	
	/**
	* Test id is empty if no id specificed
	* @throws Exception
	*/
	@Test
	public void testBuilderNoId() throws Exception{
		
		PendingCurriculum curriculum = PendingCurriculum
												.builder()
													.fileType(fileType)
													.file(file)
												.build();
		
		assertTrue(curriculum.getId().isEmpty());
		
	}
	
}