package com.arenella.recruit.messaging.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.arenella.recruit.messaging.beans.Photo;

import net.coobird.thumbnailator.Thumbnails;

/**
* Util's for manipulating Images
* @author K Parkings
*/
@Component
public class MessaginCandidateImageManipulatorImpl implements MessagingCandidateImageManipulator{

	/**
	* Refer to ImageManipulator interface for details 
	*/
	@Override
	public Photo resizeToThumbnail(Photo photo) {
		
		if (Optional.ofNullable(photo).isEmpty()) {
			return null;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			//52*52
			BufferedImage inputImage 	= ImageIO.read(new ByteArrayInputStream(photo.imageBytes()));
			BufferedImage outputImage 	= Thumbnails.of(inputImage).size(52, 52).asBufferedImage();	
		
			ImageIO.write(outputImage, photo.format().toString().toLowerCase() , baos);
		
		}catch(Exception e) {
			return null;
		}
		
		return new Photo(baos.toByteArray(), photo.format());
		
	}

}
