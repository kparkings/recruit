package com.arenella.recruit.candidates.extractors;

import java.io.ByteArrayInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
* PDF specific implementation of a DocumentFilterExtractionExtractor
* @author K Parkings
*/
public class PDFExtractor implements DocumentFilterExtractor{

	/**
	* Refer to the DocumentFilterExtractionExtractor interface for details
	*/
	@Override
	public String extract(byte[] fileBytes) throws Exception{
		
		PDDocument 			doc 			= PDDocument.load(new ByteArrayInputStream(fileBytes));
		PDFTextStripper 	pdfStripper 	= new PDFTextStripper();
		String 				text 			= pdfStripper.getText(doc);
		
		text = text.toLowerCase();
		doc.close();
		
		return text;
		
	}
	
}