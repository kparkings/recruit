package com.arenella.recruit.candidates.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;

import net.coobird.thumbnailator.Thumbnails;

/**
* Util's for manipulating Images
* @author K Parkings
*/
@Component
public class CandidateImageManipulatorImpl implements CandidateImageManipulator{

	/**
	* Refer to ImageManipulator interface for details 
	*/
	@Override
	public byte[] toProfileImage(byte[] fileBytes, PHOTO_FORMAT format ) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			
			BufferedImage inputImage 	= ImageIO.read(new ByteArrayInputStream(fileBytes));
			BufferedImage outputImage 	= Thumbnails.of(inputImage).size(300, 350).asBufferedImage();	
		
			ImageIO.write(outputImage, format.toString() , baos);
		
		}catch(Exception e) {
			return fileBytes;
		}
		
		return baos.toByteArray();
	}

}
