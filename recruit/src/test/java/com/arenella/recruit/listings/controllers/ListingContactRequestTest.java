package com.arenella.recruit.listings.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

/**
* Unit tests for the ListingContactRequest class
* @author K Parkings
*/
public class ListingContactRequestTest {

	private static final UUID			LISTING_ID		= UUID.randomUUID();
	private static final MultipartFile 	ATTACHMENT		= Mockito.mock(MultipartFile.class);
	private static final String 		SENDER_NAME		= "Kevin Parkings";
	private static final String 		SENDER_EMAIL	= "kparkings@gmail.com";
	private static final String 		MESSAGE			= "some message";
	
	/**
	* Tests construction via builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		ListingContactRequest request = 
				ListingContactRequest
				.builder()
					.listingId(LISTING_ID)
					.attachment(ATTACHMENT)
					.message(MESSAGE)
					.senderEmail(SENDER_EMAIL)
					.senderName(SENDER_NAME)
				.build();
		
		assertEquals(LISTING_ID, 	request.getListingId());
		assertEquals(ATTACHMENT, 	request.getAttachment());
		assertEquals(SENDER_NAME, 	request.getSenderName());
		assertEquals(SENDER_EMAIL, 	request.getSenderEmail());
		assertEquals(MESSAGE, 		request.getMessage());
		
	}
	
}