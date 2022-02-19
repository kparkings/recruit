package com.arenella.recruit.curriculum.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import com.arenella.recruit.curriculum.controllers.CurriculumUpdloadDetails;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Factory for extracting information out of Curriculum Files
* @author K Parkings
*/
public class CurriculumDetailsExtractionFactory {

	/**
	* Returns an implementation of the Extractor suitable for
	* the FileType of the Curriculum
	* @param fileType - FileType of the Curriculum
	* @return Extractor
	*/
	public static CurriculumDetailsExtractor getInstance(FileType fileType) {
		
		switch(fileType) {
			case pdf:{
				return new PDFCurriculumDetailsExtractor();
			}
			case doc:
			case docx:{
				return new WordCurriculumDetailsExtractor(); 
			}
		}
		
		return null;
	}
	
	/**
	* Defines the behaviour of a CurriculumDetailsExtractor. That is 
	* an extractor that can extract details from a Curriculum file
	* @author K Parkings
	*/
	public static interface CurriculumDetailsExtractor {
		
		public CurriculumUpdloadDetails extract(Set<String> skills, String curriculumId, byte[] curriculumFileBytes) throws Exception; 
		
	}

	/**
	* PDF specific implementation of a CurriculumDetailsExtractor
	* @author K Parkings
	*/
	public static class PDFCurriculumDetailsExtractor implements CurriculumDetailsExtractor{

		/**
		* Refer to the CurriculumDetailsExtractor interface for details
		*/
		@Override
		public CurriculumUpdloadDetails extract(Set<String> skills, String curriculumId,  byte[] curriculumFileBytes) throws Exception{
			
			PDDocument 			doc 			= PDDocument.load(new ByteArrayInputStream(curriculumFileBytes));
			PDFTextStripper 	pdfStripper 	= new PDFTextStripper();
			String 				text 			= pdfStripper.getText(doc);
			
			text = text.toLowerCase();
			doc.close();
			
			return CurriculumUpdloadDetails.builder().id(curriculumId).emailAddress(extractEmailAddress(text)).skills(extractSkills(skills, text)).build();
			
		}
		
	}
	
	/**
	* Word (doc/docx) specific implementation of a CurriculumDetailsExtractor
	* @author K Parkings
	*/
	public static class WordCurriculumDetailsExtractor implements CurriculumDetailsExtractor{

		/**
		* Refer to the CurriculumDetailsExtractor interface for details
		*/
		@Override
		public CurriculumUpdloadDetails extract(Set<String> skills, String curriculumId, byte[] curriculumFileBytes) throws Exception {
			
			InputStream 		is 				= new ByteArrayInputStream(curriculumFileBytes);
			OPCPackage 			docPackage 		= OPCPackage.open(is);
			XWPFWordExtractor 	extractor 		= new XWPFWordExtractor(docPackage);
			String 				text 			= extractor.getText();
			
			extractor.close();
			
			return CurriculumUpdloadDetails.builder().id(curriculumId).emailAddress(extractEmailAddress(text)).skills(extractSkills(skills, text)).build();
		}
		
	}
	
	/**
	* Extracts the skills of interest where they are present
	* in the Curriculum file
	* @param text - text content from the Curriculum file
	* @return Collection of skills found in the Curriculum
	*/
	private static Set<String> extractSkills(Set<String> soughtSkills, String text) {
		
		Set<String> skills = new HashSet<>();
		
		final String baseText = text.toLowerCase();
		
		soughtSkills.stream().forEach(soughtSkill -> {
			
			String skillPatternSpace 	= soughtSkill.trim() + " ";
			String skillPatternPeriod 	= soughtSkill.trim() + ".";
			String skillPatternNewLine 	= soughtSkill.trim() + "\n";
			
			if (baseText.contains(skillPatternSpace) || baseText.contains(skillPatternPeriod) || baseText.contains(skillPatternNewLine)) {
				skills.add(soughtSkill);
			}
			
		});
		
		return skills;
		
	}
	
	/**
	* Extracts the email address from the Curriculum file
	* @param text - Text from the Curriculum file
	* @return email address in the Curriculum
	*/
	private static String extractEmailAddress(String text) {
		
		Set<String> email = new HashSet<>();
		
		Arrays.asList(text.split(" ")).forEach(word -> {
			boolean check1 = word.contains("@");
			boolean check2 = word.contains(".");
			
			if (check1 && check2) {
				email.add(word.trim());
			}
			
		});
		
		String emailString = email.isEmpty() ? "" : email.stream().findFirst().get();
		
		return emailString;
		
	}
	
}
