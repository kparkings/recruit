package com.arenella.recruit.curriculum.services;

import org.springframework.web.multipart.MultipartFile;

/**
* Defines functions for checking the safety of 
* a file 
* @author K Parkings
*/
public interface CurriculumFileSecurityParser {

	/**
	* Returns whether or not the file is considered safe 
	* in the context of the system
	* @param file - File to check
	* @return whether to consider the file safe or not
	*/
	public boolean isSafe(MultipartFile file);
	
}
