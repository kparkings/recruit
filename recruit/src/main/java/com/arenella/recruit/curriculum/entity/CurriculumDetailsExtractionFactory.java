package com.arenella.recruit.curriculum.entity;

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

/**
* Factory for extracting information out of Curriculum Files
* @author K Parkings
*/
public class CurriculumDetailsExtractionFactory {

	private static Set<String> soughtSkills = Set.of( "java"
													 ,"angular"
													 ,"css"
													 ,"html"
													 ,"c"
													 ,"c++"
													 ,"c#"
													 ,"typescript"
													 ,"jira"
													 ,"git"
													 ,"react");
	
	public static CurriculumDetailsExtractor getInstance(String fileType) {
		
		switch(fileType.toLowerCase()) {
			case "pdf":{
				return new PDFCurriculumDetailsExtractor();
			}
			case "doc":
			case "docx":{
				return new WordCurriculumDetailsExtractor(); 
			}
		}
		
		return null;
	}
	
	public static interface CurriculumDetailsExtractor {
		
		public CurriculumUpdloadDetails extract(String curriculumId, byte[] curriculumFileBytes) throws Exception; 
		
	}
	
	public static class PDFCurriculumDetailsExtractor implements CurriculumDetailsExtractor{

		@Override
		public CurriculumUpdloadDetails extract(String curriculumId,  byte[] curriculumFileBytes) throws Exception{
			
			PDDocument 			doc 			= PDDocument.load(new ByteArrayInputStream(curriculumFileBytes));
			PDFTextStripper 	pdfStripper 	= new PDFTextStripper();
			String 				text 			= pdfStripper.getText(doc);
			
			text = text.toLowerCase();
			doc.close();
			return CurriculumUpdloadDetails.builder().id(curriculumId).emailAddress(extractEmailAddress(text)).skills(extractSkills(text)).build();
			
		}
		
	}
	
	public static class WordCurriculumDetailsExtractor implements CurriculumDetailsExtractor{

		@Override
		public CurriculumUpdloadDetails extract(String curriculumId, byte[] curriculumFileBytes) throws Exception {
			
			InputStream 		is 				= new ByteArrayInputStream(curriculumFileBytes);
			OPCPackage 			docPackage 		= OPCPackage.open(is);
			XWPFWordExtractor 	extractor 		= new XWPFWordExtractor(docPackage);
			String 				text 			= extractor.getText();
			
			extractor.close();
			
			return CurriculumUpdloadDetails.builder().id(curriculumId).emailAddress(extractEmailAddress(text)).skills(extractSkills(text)).build();
		}
		
	}
	
	private static Set<String> extractSkills(String text) {
		
		Set<String> skills = new HashSet<>();
		
		final String baseText = text.toLowerCase();
		
		soughtSkills.stream().forEach(soughtSkill -> {
			
			if (baseText.contains(soughtSkill.trim())) {
				skills.add(soughtSkill);
			}
			
		});
		
		return skills;
		
	}
	
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
