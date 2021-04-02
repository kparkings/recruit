package com.arenella.recruit.candidates.controllers;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;
import com.arenella.recruit.candidates.services.CandidateDownloadService;
import com.arenella.recruit.candidates.services.CandidateService;
import com.arenella.recruit.candudates.beans.CandidateFilterOptions;
import com.arenella.recruit.candudates.beans.Language;


/**
* REST API for working with Candidates
* @author K Parkings
*/
@RestController
@CrossOrigin(origins = {"http://127.0.0.1:4200", "http://127.0.0.1:8080"}, allowedHeaders = "*")
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
	public Set<CandidateAPIOutbound> getCandidate(  @PathVariable(required = false) 	String 				orderAttribute,
													@PathVariable(required = false) 	RESULT_ORDER		order,
												 	@PathVariable(required = false) 	Set<String> 		candidateId,
												 	@PathVariable(required = false) 	Set<COUNTRY> 		countries,
												 	@PathVariable(required = false) 	Set<FUNCTION> 		functions,
												 	@PathVariable(required = false) 	Boolean 			freelance,
												 	@PathVariable(required = false) 	Boolean 			perm,
												 	@PathVariable(required = false) 	Integer				yearsExperienceGtEq,
												 	@PathVariable(required = false) 	Language.LEVEL 		dutch,
												 	@PathVariable(required = false) 	Language.LEVEL 		english,
												 	@PathVariable(required = false) 	Language.LEVEL 		french,
												 	@PathVariable(required = false) 	Set<String>			skills
												 	) {
		
		CandidateFilterOptions filterOptions = CandidateFilterOptions
													.builder()
													.orderAttribute(orderAttribute)
													.order(order)
													.candidateIds(candidateId)
													.countries(countries)
													.functions(functions)
													.freelance(freelance)
													.perm(perm)
													.yearsExperienceGtEq(yearsExperienceGtEq)
													.dutch(dutch)
													.english(english)
													.french(french)
													.skills(skills)
													.build();
		
		return candidateService.getCandidates(filterOptions).stream().map(candidate -> CandidateAPIOutbound.convertFromCandidate(candidate)).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	@GetMapping(path="candidate/download")
	public ResponseEntity<ByteArrayResource> downloadCandidates() throws Exception{
		
		ByteArrayOutputStream 	stream 		= new ByteArrayOutputStream();
		XSSFWorkbook 			workbook 	= this.candidateDownloadService.createXLSCandidateDownload(candidateService.getCandidates(CandidateFilterOptions.builder().build()));
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Candidates.xls");
		
		workbook.write(stream);
		workbook.close();
		
		return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), header, HttpStatus.OK);
		
	}
	
}
