package com.arenella.recruit.curriculum.controllers;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.beans.PendingCurriculum;
import com.arenella.recruit.curriculum.enums.FileType;
import com.arenella.recruit.curriculum.services.CurriculumFileSecurityParser;
import com.arenella.recruit.curriculum.services.CurriculumService;

/**
* REST API for working with Curriculums of Candidates
* @author K Parkings
*/
@RestController
public class CurriculumController {

	@Autowired
	private CurriculumService curriculumService;
	
	@Autowired
	private CurriculumFileSecurityParser fileSecurityParser;
	
	/**
	* EndPoint for uploading a Curriculum
	* @param curriculum		- MultiPart representation of file
	* @return Id of the Curriculum
	* @throws Exception
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value="/curriculum",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CurriculumUpdloadDetails uploadCurriculum(@RequestParam("file") MultipartFile curriculumFile) throws Exception{
		
		if (!fileSecurityParser.isSafe(curriculumFile)) {
			throw new RuntimeException("Unacceptable File");
		}
		
		String 			postFix						= curriculumFile.getOriginalFilename().substring(curriculumFile.getOriginalFilename().lastIndexOf('.')+1).toLowerCase();
		long 			nextAvailableCurriculumId	= curriculumService.getNextCurriculumId();
		Curriculum 		curriculum					= Curriculum.builder().fileType(FileType.valueOf(postFix)).file(curriculumFile.getBytes()).id(String.valueOf(nextAvailableCurriculumId)).build();
		
		return curriculumService.extractDetails(curriculumService.persistCurriculum(curriculum), FileType.valueOf(postFix), curriculumFile.getBytes());
		
	}
	
	/**
	* EndPoint for updating a Curriculum
	* @param curriculum		- MultiPart representation of file
	* @return Id of the Curriculum
	* @throws Exception
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_CANDIDATE')")
	@PutMapping(value="/curriculum/{curriculumId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CurriculumUpdloadDetails updateCurriculum(@PathVariable("curriculumId") long curriculumId, @RequestParam("file") MultipartFile curriculumFile) throws Exception{
		
		if (!fileSecurityParser.isSafe(curriculumFile)) {
			throw new RuntimeException("Unacceptable File");
		}
		
		String 			postFix						= curriculumFile.getOriginalFilename().substring(curriculumFile.getOriginalFilename().lastIndexOf('.')+1).toLowerCase();
		Curriculum 		curriculum					= Curriculum.builder().fileType(FileType.valueOf(postFix)).file(curriculumFile.getBytes()).id(String.valueOf(curriculumId)).build();
		
		return curriculumService.extractDetails(curriculumService.updateCurriculum(curriculumId, curriculum), FileType.valueOf(postFix), curriculumFile.getBytes());
		
	}
	
	/**
	* Returns the Curriculum file 
	* @param  - curriculumId - Unique Id of the curriculum to download
	* @param  - principal	 - Contains id of User who made the request
	* @return - Requested curriculum file
	* @throws Exception
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@GetMapping(value="/curriculum/{curriculumId}")
	public ResponseEntity<ByteArrayResource> getCurriculum(@PathVariable("curriculumId")String curriculumId) throws Exception{
		
		Curriculum 				curriculum 	= curriculumService.fetchCurriculum(curriculumId);
		ByteArrayOutputStream 	stream 		= new ByteArrayOutputStream(curriculum.getFile().length);
		
		stream.write(curriculum.getFile());
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=C"+curriculum.getId().get()+"." + curriculum.getFileType());
		
		curriculumService.logCurriculumDownloadedEvent(curriculumId);
		
		return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), header, HttpStatus.OK);
		
	}
	
	//TODO: [KP] If conversion is slow it may be worth converting on upload and just retrieving the bytes from the DB. THerefore for doc/docx there would be 2 cvs ( word + pdf)
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@GetMapping(
			  value = "/curriculum-test/{curriculumId}.pdf",
			  produces = MediaType.APPLICATION_PDF_VALUE
			)
	public @ResponseBody byte[] getCurriculumAsPDF(@PathVariable("curriculumId")String curriculumId) throws Exception{
		return this.curriculumService.getCurriculamAsPdfBytes(curriculumId);
	}
	
	/**
	* Uploads a Curriculum sent direct by a candidate. This is a Curriculum that needs to be 
	* processed and accepted before it is available to the recruiters 
	* @param curriculumFile - Candidates curriculum
	* @return ResponseEntity
	* @throws Exception
	*/
	@PostMapping(value="/pending-curriculum")
	public ResponseEntity<UUID> uploadPendingCurriculum(@RequestParam("file") MultipartFile curriculumFile) throws Exception {
		
		if (!fileSecurityParser.isSafe(curriculumFile)) {
			throw new RuntimeException("Unacceptable File");
		}
		
		String 			postFix						= curriculumFile.getOriginalFilename().substring(curriculumFile.getOriginalFilename().lastIndexOf('.')+1).toLowerCase();
		UUID			curriculumId				= UUID.randomUUID();		
		
		this.curriculumService.persistPendingCurriculum(PendingCurriculum.builder().file(curriculumFile.getBytes()).fileType(FileType.valueOf(postFix)).id(curriculumId).build());
		
		return ResponseEntity.ok().body(curriculumId);
		
	}	
	
	/**
	* Adds a Pending Curriculum as a standard Curriculum and deletes it from the PendingCurriculums
	* @param pendingCurriculumId - PendingCurriculum to make active
	* @return curriculumId
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value="/pending-curriculum/{pendingCurriculumId}")
	public CurriculumUpdloadDetails makePendingCurriculumActive(@PathVariable("pendingCurriculumId") UUID pendingCurriculumId) throws Exception{
		
		PendingCurriculum 	pendingCurriculum 			= this.curriculumService.fetchPendingCurriculum(pendingCurriculumId);
		long 				nextAvailableCurriculumId	= curriculumService.getNextCurriculumId();
		
		Curriculum curriculum = Curriculum
									.builder()
									.file(pendingCurriculum.getFile())
									.fileType(pendingCurriculum.getFileType())
									.id(String.valueOf(nextAvailableCurriculumId))
									.build();
		
		//this.curriculumService.deletePendingCurriculum(pendingCurriculumId);
		
		return curriculumService.extractDetails(curriculumService.persistCurriculum(curriculum), curriculum.getFileType(), curriculum.getFile());
	}
	
	/**
	* Deletes an existing Pending curriculum
	* @param pendingCurriculumId - Id of Curriculum to delete
	* @return ResponseEntity
	* @throws Exception
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value="/pending-curriculum/{pendingCurriculumId}")
	public ResponseEntity<Void> deletePendingCurriculum(@PathVariable("pendingCurriculumId") UUID pendingCurriculumId) throws Exception{
		
		this.curriculumService.deletePendingCurriculum(pendingCurriculumId);
		
		return ResponseEntity.ok().build();
		
	}
	
	/**
	* Downloads a PendingCurriculum
	* @param pendingCurriculumId - Unique Id of the PendingCurriculum
	* @return Bytes for pending Curriculum
	* @throws Exception
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value="/pending-curriculum/{pendingCurriculumId}")
	public ResponseEntity<ByteArrayResource> getCurriculum(@PathVariable("pendingCurriculumId")UUID pendingCurriculumId) throws Exception{
		
		PendingCurriculum 		pendingCurriculum 	= curriculumService.fetchPendingCurriculum(pendingCurriculumId);
		ByteArrayOutputStream 	stream 				= new ByteArrayOutputStream(pendingCurriculum.getFile().length);
		
		stream.write(pendingCurriculum.getFile());
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=C"+pendingCurriculum.getId().get()+"." + pendingCurriculum.getFileType());
		
		return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), header, HttpStatus.OK);
		
	} 
	
}