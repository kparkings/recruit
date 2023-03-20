package com.arenella.recruit.recruiters.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo.PHOTO_FORMAT;

/**
* Util's for manipulating Images
* @author K Parkings
*/
@Component
public class ImageManipulatorImpl implements ImageManipulator{

	/**
	* Refer to ImageManipulator interface for details 
	*/
	@Override
	public byte[] toProfileImage(byte[] fileBytes, PHOTO_FORMAT format ) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(fileBytes));
			Graphics2D graphics = image.createGraphics();
			graphics.drawImage(image, 0, 0, 100, 200, null);
			graphics.dispose();
		
			ImageIO.write(image, format.toString() , baos);
		
		}catch(Exception e) {
			return fileBytes;
		}
		
		return baos.toByteArray();
	}

}
