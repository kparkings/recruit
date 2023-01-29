package com.arenella.recruit.candidates.extractors;

/**
* Defines the behavior of a CurriculumDetailsExtractor. That is 
* an extractor that can extract details from a Curriculum file
* @author K Parkings
*/
public interface DocumentFilterExtractor {
		
	/**
	* Extracts text from a fle
	* @param fileBytes - The file
	* @return String contents of file
	* @throws Exception
	*/
	public String extract(byte[] fileBytes) throws Exception; 
	
}
