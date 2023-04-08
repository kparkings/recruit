package com.arenella.recruit.candidates.utils;

import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;

/**
* Defines services relating to Image manipulation
* @author K Parkings
*/
public interface CandidateImageManipulator {

	/**
	* Resizes an image to fit the dimensions for a 
	* Profile Image
	* @param imageBytes - Image file byes
	* @return bytes of resized image
	*/
	public byte[] toProfileImage(byte[] imageBytes, PHOTO_FORMAT format);
	
}
