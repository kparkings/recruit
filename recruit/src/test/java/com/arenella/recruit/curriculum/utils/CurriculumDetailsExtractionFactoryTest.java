package com.arenella.recruit.curriculum.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.curriculum.enums.FileType;
import com.arenella.recruit.curriculum.utils.CurriculumDetailsExtractionFactory.PDFCurriculumDetailsExtractor;
import com.arenella.recruit.curriculum.utils.CurriculumDetailsExtractionFactory.WordCurriculumDetailsExtractor;

/**
* Unit tests for the CurriculumDetailsExtractionFactory class
* @author K Parkings
*/
class CurriculumDetailsExtractionFactoryTest {

	/**
	* Tests factory returns correct Extractor for a PDF document
	* @throws Exception
	*/
	@Test
	void testGetPDFExtractor() {
		
		assertTrue(CurriculumDetailsExtractionFactory.getInstance(FileType.pdf) instanceof PDFCurriculumDetailsExtractor);
		
	}

	/**
	* Tests factory returns correct Extractor for a Word document
	* @throws Exception
	*/
	@Test
	void testGetWordExtractor() {
		
		assertTrue(CurriculumDetailsExtractionFactory.getInstance(FileType.doc)  instanceof WordCurriculumDetailsExtractor);
		assertTrue(CurriculumDetailsExtractionFactory.getInstance(FileType.docx) instanceof WordCurriculumDetailsExtractor);
		
	}
}
