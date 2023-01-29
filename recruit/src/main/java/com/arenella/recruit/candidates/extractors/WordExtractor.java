package com.arenella.recruit.candidates.extractors;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

/**
* Word (doc/docx) specific implementation of a DocumentFilterExtractionExtractor
* @author K Parkings
*/
public class WordExtractor implements DocumentFilterExtractor{

	/**
	* Refer to the DocumentFilterExtractionExtractor interface for details
	*/
	@Override
	public String extract(byte[] fileBytes) throws Exception {
		
		InputStream 		is 				= new ByteArrayInputStream(fileBytes);
		OPCPackage 			docPackage 		= OPCPackage.open(is);
		XWPFWordExtractor 	extractor 		= new XWPFWordExtractor(docPackage);
		String 				text 			= extractor.getText();
		
		extractor.close();
		
		return text;
	}
	
}