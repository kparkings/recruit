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
import org.springframework.web.bind.annotation.RequestParam;
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
	* Fetches Candidates
	* @param orderAttribute			- Optional attribute to order the results on
	* @param order					- Optional direction of ordering
	* @param candidateId			- Optional candidates ids to filter on
	* @param countries				- Optional countries to filter on
	* @param functions				- Optional functions to filter on
	* @param freelance				- Optional freelance value to filter on
	* @param perm					- Optional perm value to filter on
	* @param yearsExperienceGtEq	- Optional years experience value to filter on
	* @param dutch					- Optional Dutch language proficiency value to filter on 
	* @param english				- Optional English language proficiency value to filter on
	* @param french					- Optional French language proficiency value to filter on
	* @param skills					- Optional Skills to filter on
	* @return
	*/
	@GetMapping(path="candidate")
	public Set<CandidateAPIOutbound> getCandidate(  @RequestParam("orderAttribute") 	String 				orderAttribute,
													@RequestParam("order") 				RESULT_ORDER		order,
													@RequestParam(required = false) 	Set<String> 		candidateId,
													@RequestParam(required = false) 	Set<COUNTRY> 		countries,
													@RequestParam(required = false) 	Set<FUNCTION> 		functions,
													@RequestParam(required = false) 	Boolean 			freelance,
													@RequestParam(required = false) 	Boolean 			perm,
													@RequestParam(required = false) 	Integer				yearsExperienceGtEq,
													@RequestParam(required = false) 	Language.LEVEL 		dutch,
													@RequestParam(required = false) 	Language.LEVEL 		english,
													@RequestParam(required = false) 	Language.LEVEL 		french,
													@RequestParam(required = false) 	Set<String>			skills
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
	
	/**
	* Downloads Candidates
	* @param orderAttribute			- Optional attribute to order the results on
	* @param order					- Optional direction of ordering
	* @param candidateId			- Optional candidates ids to filter on
	* @param countries				- Optional countries to filter on
	* @param functions				- Optional functions to filter on
	* @param freelance				- Optional freelance value to filter on
	* @param perm					- Optional perm value to filter on
	* @param yearsExperienceGtEq	- Optional years experience value to filter on
	* @param dutch					- Optional Dutch language proficiency value to filter on 
	* @param english				- Optional English language proficiency value to filter on
	* @param french					- Optional French language proficiency value to filter on
	* @param skills					- Optional Skills to filter on
	* @return Xls download of candidates
	*/
	@GetMapping(path="candidate/download")
	public ResponseEntity<ByteArrayResource> downloadCandidates(@RequestParam("orderAttribute") 	String 				orderAttribute,
																@RequestParam("order") 				RESULT_ORDER		order,
																@RequestParam(required = false) 	Set<String> 		candidateId,
																@RequestParam(required = false) 	Set<COUNTRY> 		countries,
																@RequestParam(required = false) 	Set<FUNCTION> 		functions,
																@RequestParam(required = false) 	Boolean 			freelance,
																@RequestParam(required = false) 	Boolean 			perm,
																@RequestParam(required = false) 	Integer				yearsExperienceGtEq,
																@RequestParam(required = false) 	Language.LEVEL 		dutch,
																@RequestParam(required = false) 	Language.LEVEL 		english,
																@RequestParam(required = false) 	Language.LEVEL 		french,
																@RequestParam(required = false) 	Set<String>			skills) throws Exception{
		
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
		
		ByteArrayOutputStream 	stream 		= new ByteArrayOutputStream();
		XSSFWorkbook 			workbook 	= this.candidateDownloadService.createXLSCandidateDownload(candidateService.getCandidates(filterOptions));
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Candidates.xls");
		
		workbook.write(stream);
		workbook.close();
		
		return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), header, HttpStatus.OK);
		
	}
	
}