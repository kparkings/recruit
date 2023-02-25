package com.arenella.recruit.curriculum.services;

import java.io.ByteArrayInputStream;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

/**
* Implements functionality for checking whether a File is able
* to be considered sage in the context of the system
* @author K Parkings
*/
@Component
public class CurriculumFileSecurityParserImpl implements CurriculumFileSecurityParser{

	/**
	* Refer to FileSecurityParser interface for details 
	*/
	@Override
	public boolean isSafe(MultipartFile file) {
		
		try {
			
			Detector 	detector 	= new DefaultDetector();
		    Metadata 	metadata 	= new Metadata();
		    MediaType 	mediaType 	= detector.detect(new ByteArrayInputStream(file.getBytes()), metadata);
		    
			switch (mediaType.toString()) {
				case "application/vnd.oasis.opendocument.text":
				case "application/msword":
				case "application/pdf":{
					return true;
				}
				case "application/x-tika-ooxml":{
					return file.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
				}
				default:{
					return false;
				}
			}
			
		} catch (Exception e) {
			return false;
		}
		
	}

}
