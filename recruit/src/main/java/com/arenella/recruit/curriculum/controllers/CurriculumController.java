package com.arenella.recruit.curriculum.controllers;

import java.io.ByteArrayOutputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CurriculumController {

	private static byte[] testCurriculum = null;
	
	@PostMapping(value="/curriculum",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadCurriculum(@RequestParam("file") MultipartFile curriculum) throws Exception{
		
		byte[] fileBytes = curriculum.getBytes();
		
		testCurriculum = fileBytes;
		
		return "{'id':'1'}";
		
	}
	
	@GetMapping(value="/curriculum")
	public ResponseEntity<ByteArrayResource> getCurriculum() throws Exception{
		
		ByteArrayOutputStream 	stream 		= new ByteArrayOutputStream(testCurriculum.length);
		stream.write(testCurriculum);
		
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=curriculum.pdf");
		
		return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), header, HttpStatus.OK);
		
	}
}
