package com.arenella.recruit.listings.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

/**
* Unit tests for the CandidateListingContactRequest class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateListingContactRequestTest {

	private static final String CANDIDATE_ID 	= "kparkg33";
	private static final UUID	LISTING_ID 		= UUID.randomUUID();
	private static final String	MESSAGE 		= "A Mesage";
	
	@Mock
	private MultipartFile mockAttachment;
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		CandidateListingContactRequest request = 
				CandidateListingContactRequest
				.builder()
					.candidateId(CANDIDATE_ID)
					.attachment(mockAttachment)
					.listingId(LISTING_ID)
					.message(MESSAGE)
				.build();
		
		assertEquals(CANDIDATE_ID, 		request.getCandidateId());
		assertEquals(LISTING_ID, 		request.getListingId());
		assertEquals(MESSAGE, 			request.getMessage());
		assertEquals(mockAttachment, 	request.getAttachment().get());
		
	}
	
	/**
	* Tests construction via the Builder when no File provided
	* @throws Exception
	*/
	@Test
	public void testBuilder_noFile() throws Exception{
		
		CandidateListingContactRequest request = 
				CandidateListingContactRequest
				.builder()
					.candidateId(CANDIDATE_ID)
					.listingId(LISTING_ID)
					.message(MESSAGE)
				.build();
		
		assertEquals(CANDIDATE_ID, 	request.getCandidateId());
		assertEquals(LISTING_ID, 	request.getListingId());
		assertEquals(MESSAGE, 		request.getMessage());
		
		assertTrue(request.getAttachment().isEmpty());
		
	}
	
}