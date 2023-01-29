package com.arenella.recruit.candidates.utils;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.extractors.DocumentFilterExtractionUtil;
import com.arenella.recruit.candidates.extractors.PDFExtractor;
import com.arenella.recruit.candidates.extractors.WordExtractor;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Unit tests for the DocumentFilterExtractionFactory class
* @author K Parkings
*/
public class DocumentFilterExtractionFactoryTest {

	/**
	* Tests correct extractor returned for file type
	* @throws Exception
	*/
	@Test
	public void testGetInstance() throws Exception{
		
		DocumentFilterExtractionUtil util = new DocumentFilterExtractionUtil();
		assertTrue(util.getInstance(FileType.doc) instanceof WordExtractor);
		assertTrue(util.getInstance(FileType.docx) instanceof WordExtractor);
		assertTrue(util.getInstance(FileType.pdf) instanceof PDFExtractor);
		assertNull(util.getInstance(null));
		
	}
	
}
