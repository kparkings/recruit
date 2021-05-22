package com.arenella.recruit.curriculum.controllers;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.enums.FileType;
import com.arenella.recruit.curriculum.services.CurriculumService;

/**
* REST API for working with Curriculums of Candidates
* @author K Parkings
*/
@RestController
public class CurriculumController {

	private static byte[] testCurriculum = null;
	
	@Autowired
	private CurriculumService curriculumService;
	
	/**
	* Endoint for uploading a Curriculum
	* @param curriculum		- Multipart representation of file
	* @return Id of the Curriculum
	* @throws Exception
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value="/curriculum",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadCurriculum(@RequestParam("file") MultipartFile curriculumFile) throws Exception{
		
		String 			postFix						= curriculumFile.getOriginalFilename().substring(curriculumFile.getOriginalFilename().lastIndexOf('.')+1);
		long 			nextAvailableCurriculumId	= curriculumService.getNextCurriculumId();
		Curriculum 		curriculum					= Curriculum.builder().fileType(FileType.valueOf(postFix)).file(curriculumFile.getBytes()).id(String.valueOf(nextAvailableCurriculumId)).build();
		
		return "{\"id\":\""+curriculumService.persistCurriculum(curriculum)+"\"}";
		
	}
	
	/**
	* Returns the Curriculum file 
	* @return - Requested curriculum file
	* @throws Exception
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@GetMapping(value="/curriculum/{curriculumId}")
	public ResponseEntity<ByteArrayResource> getCurriculum(@PathVariable("curriculumId")String curriculumId) throws Exception{
		
		Curriculum 				curriculum = curriculumService.fetchCurriculum(curriculumId);
		ByteArrayOutputStream 	stream 		= new ByteArrayOutputStream(curriculum.getFile().length);
		
		stream.write(curriculum.getFile());
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=curriculum.pdf");
		
		return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), header, HttpStatus.OK);
		
	}
}