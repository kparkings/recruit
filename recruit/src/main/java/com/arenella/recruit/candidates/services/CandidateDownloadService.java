package com.arenella.recruit.candidates.services;

import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.arenella.recruit.candidates.beans.Candidate;

/**
* Defines services for downloading Candidate Details
* @author K Parkings
*/
public interface CandidateDownloadService {

	/**
	* Generates a XLS file with candidate details
	* @param candidates - Candidates to add to XLS
	* @return XLS containing candidates
	*/
	public XSSFWorkbook createXLSCandidateDownload(Set<Candidate> candidates);
	
}
