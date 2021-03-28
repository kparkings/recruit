package com.arenella.recruit.candidates.controllers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.candidates.services.CandidateDownloadService;
import com.arenella.recruit.candidates.services.CandidateService;


/**
* REST API for working with Candidates
* @author K Parkings
*/
@RestController
@CrossOrigin(origins = "http://127.0.0.1:4200", allowedHeaders = "*")
public class CandidateController {

	@Autowired
	private CandidateService			candidateService;
	
	@Autowired
	private CandidateDownloadService 	candidateDownloadService;
	
	/**
	* Adds a new Candidate
	* @param candidate - Contains candidate details
	* @return id of the candidate
	*/
	@PostMapping(path="candidate", consumes="application/json", produces="application/json")
	public void addCandidate(@RequestBody CandidateAPIInbound candidate) {
		candidateService.persistCandidate(CandidateAPIInbound.convertToCandidate(candidate));
	}
	
	/**
	* Returns all available Candidates
	* @return Available Candidates
	*/
	@GetMapping(path="candidate")
	public Set<CandidateAPIOutbound> getCandidate() {
		return candidateService.getCandidates().stream().map(candidate -> CandidateAPIOutbound.convertFromCandidate(candidate)).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	@GetMapping(path="candidate/download")
	public ResponseEntity<ByteArrayResource> downloadCandidates() throws Exception{
		
		ByteArrayOutputStream 	stream 		= new ByteArrayOutputStream();
		XSSFWorkbook 			workbook 	= this.candidateDownloadService.createXLSCandidateDownload(candidateService.getCandidates());
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Candidates.xls");
		
		workbook.write(stream);
		workbook.close();
		
		return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), header, HttpStatus.OK);
		
	}
	
}
