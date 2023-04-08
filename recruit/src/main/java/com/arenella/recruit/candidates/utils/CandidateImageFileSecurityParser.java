package com.arenella.recruit.candidates.utils;

import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;

/**
* Defines functions for checking the safety of 
* a file 
* @author K Parkings
*/
public interface CandidateImageFileSecurityParser {

	/**
	* Returns whether or not the file is considered safe 
	* in the context of the system
	* @param file - File to check
	* @return whether to consider the file safe or not
	*/
	public boolean isSafe(byte[] file);
	
	/**
	* Returns the type of file 
	*  @param file - File to retrieve type from
	* @return best guess type of file
	*/
	public PHOTO_FORMAT getFileType(byte[] file); 
	
}
