package com.arenella.recruit.campaigns.services;

import java.io.ByteArrayInputStream;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.springframework.stereotype.Component;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

/**
* Implements functionality for checking whether a File is able
* to be considered sage in the context of the system
* @author K Parkings
*/
@Component
public class CampaignFileSecurityParserImpl implements CampaignFileSecurityParser{

	/**
	* Refer to FileSecurityParser interface for details 
	*/
	@Override
	public boolean isSafe(byte[] file) {
		
		try {
			
			Detector 	detector 	= new DefaultDetector();
		    Metadata 	metadata 	= new Metadata();
		    MediaType 	mediaType 	= detector.detect(new ByteArrayInputStream(file), metadata);
		    
		    //TODO: [KP] See what we need to do to check the ooxml when we have no file
		    //TODO: [KP] Add png
			return switch (mediaType.toString()) {
				case "image/jpeg" ->  true;
			 	case "application/vnd.oasis.opendocument.text", "application/msword", "application/pdf" -> true;
				//case "application/x-tika-ooxml" -> file.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
				default -> false;
				
			};
			
		} catch (Exception e) {
			return false;
		}
		
	}

}
