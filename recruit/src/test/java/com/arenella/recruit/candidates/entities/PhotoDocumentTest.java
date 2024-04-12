package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;

/**
* Unit tests for the PhotoDocument class
* @author K Parkings
*/
public class PhotoDocumentTest {

	private static final byte[] 		IMAGE_BYTES = new byte[] {};
	private static final PHOTO_FORMAT 	FORMAT		= PHOTO_FORMAT.jpeg;
	
	/**
	* Tests PhotoDocument construction
	* @throws Exception
	*/
	@Test
	public void testConstruction() throws Exception{
		
		PhotoDocument doc = new PhotoDocument(IMAGE_BYTES, FORMAT);
		
		assertEquals(IMAGE_BYTES, 	doc.getImageBytes());
		assertEquals(FORMAT, 		doc.getFormat());
		
	}
	
	/**
	* Tests conversion from Persistence to Domain representation
	* @throws Exception
	*/
	public void testConvertFromDomain() throws Exception{
		
		Candidate.Photo photo 	= new Candidate.Photo(IMAGE_BYTES, FORMAT);
		PhotoDocument 	doc	 	= PhotoDocument.convertFromDomain(photo);
		
		assertEquals(IMAGE_BYTES, 	doc.getImageBytes());
		assertEquals(FORMAT, 		doc.getFormat());
		
	}
	
	/**
	* Tests conversion from Domain to Persistence representation
	* @throws Exception
	*/
	public void testConvertToDomain() throws Exception{
		
		PhotoDocument 	doc 	= new PhotoDocument(IMAGE_BYTES, FORMAT);
		Candidate.Photo photo 	= PhotoDocument.convertToDomain(doc);
		
		assertEquals(IMAGE_BYTES, 	photo.getImageBytes());
		assertEquals(FORMAT, 		photo.getFormat());
		
	}

}