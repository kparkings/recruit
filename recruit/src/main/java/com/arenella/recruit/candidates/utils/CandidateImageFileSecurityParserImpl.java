package com.arenella.recruit.candidates.utils;

import java.io.ByteArrayInputStream;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
//import com.mchange.v1.db.sql.UnsupportedTypeException;
import com.arenella.recruit.candidates.exceptions.UnsupportedTypeException;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

/**
* Implements functionality for checking whether a File is able
* to be considered sage in the context of the system
* @author K Parkings
*/
@Component
public class CandidateImageFileSecurityParserImpl implements CandidateImageFileSecurityParser{

	/**
	* Refer to FileSecurityParser interface for details 
	*/
	@Override
	public boolean isSafe(byte[] file) {
		
		try {
			
			Detector 	detector 	= new DefaultDetector();
		    Metadata 	metadata 	= new Metadata();
		    MediaType 	mediaType 	= detector.detect(new ByteArrayInputStream(file), metadata);
		    
			switch (mediaType.toString()) {
				case "image/jpeg":{
					return true;
				}
				
				default:{
					return false;
				}
			}
			
		} catch (Exception e) {
			return false;
		}
		
	}

	/**
	* Refer to FileSecurityParser interface for details 
	*/
	@Override
	public PHOTO_FORMAT getFileType(byte[] file) {
		
		try {
			
			Detector 	detector 	= new DefaultDetector();
		    Metadata 	metadata 	= new Metadata();
		    MediaType 	mediaType 	= detector.detect(new ByteArrayInputStream(file), metadata);
		    
			switch (mediaType.toString()) {
				case "image/jpeg":{
					return PHOTO_FORMAT.jpeg;
				}
				case "image/png":{
					return PHOTO_FORMAT.png;
				}default:{
					throw new UnsupportedTypeException("Unsupported Image Type");
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
