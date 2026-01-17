package com.arenella.recruit.messaging.utils;

import com.arenella.recruit.messaging.beans.Photo;

/**
* Defines services relating to Image manipulation
* @author K Parkings
*/
public interface MessagingCandidateImageManipulator {

	/**
	* Resizes an image to fit the dimensions for a 
	* Profile Image
	* @param imageBytes - Image file byes
	* @return bytes of resized image
	*/
	public Photo resizeToThumbnail(Photo photo);
	
}
