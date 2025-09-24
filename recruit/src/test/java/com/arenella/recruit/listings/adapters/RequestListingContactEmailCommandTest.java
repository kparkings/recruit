package com.arenella.recruit.listings.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the RequestListingContactEmailCommand class
* @author K Parkings
*/
class RequestListingContactEmailCommandTest {

	private static final String	RECRUITER_ID	= "acb124";
	private static final String	LISTING_NAME	= "Java Developer";
	private static final String	SENDER_NAME		= "kevin";
	private static final String	SENDER_EMAIL	= "kparkings@boop.com";
	private static final String	MESSAGE			= "a message";
	private static final byte[]	FILE			= new byte[]{};
	private static final String	FILE_TYPE		= "pdf";
	
	/**
	* Tests construction via the builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		RequestListingContactEmailCommand command = 
				RequestListingContactEmailCommand
					.builder()
						.file(FILE)
						.fileType(FILE_TYPE)
						.listingName(LISTING_NAME)
						.message(MESSAGE)
						.recruiterId(RECRUITER_ID)
						.senderEmail(SENDER_EMAIL)
						.senderName(SENDER_NAME)
					.build();
		
		assertEquals(RECRUITER_ID, 	command.getRecruiterId());
		assertEquals(LISTING_NAME, 	command.getListingName());
		assertEquals(SENDER_NAME, 	command.getSenderName());
		assertEquals(SENDER_EMAIL, 	command.getSenderEmail());
		assertEquals(MESSAGE, 		command.getMessage());
		assertEquals(FILE, 			command.getFile());
		assertEquals(FILE_TYPE, 	command.getFileType());
	}
	
}