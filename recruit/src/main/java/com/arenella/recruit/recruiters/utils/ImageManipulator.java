package com.arenella.recruit.recruiters.utils;

import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo.PHOTO_FORMAT;

/**
* Defines services relating to Image manipulation
* @author K Parkings
*/
public interface ImageManipulator {

	/**
	* Resizes an image to fit the dimensions for a 
	* Profile Image
	* @param imageBytes - Image file byes
	* @return bytes of resized image
	*/
	public byte[] toProfileImage(byte[] imageBytes, PHOTO_FORMAT format);
	
}
