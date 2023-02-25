package com.arenella.recruit.listings.services;

import org.springframework.web.multipart.MultipartFile;

/**
* Defines functions for checking the safety of 
* a file 
* @author K Parkings
*/
public interface FileSecurityParser {

	public static enum FileType {doc, docx, pdf, odt}
	
	/**
	* Returns whether or not the file is considered safe 
	* in the context of the system
	* @param file - File to check
	* @return whether to consider the file safe or not
	*/
	public boolean isSafe(MultipartFile file);
	
	/**
	* Returns the type of file 
	*  @param file - File to retrieve type from
	* @return best guess type of file
	*/
	public FileType getFileType(MultipartFile file); 
	
}
