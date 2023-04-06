package com.arenella.recruit.listings.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CandidateRequestListingContactEmailCommand class
* @author K Parkings
*/
public class CandidateRequestListingContactEmailCommandTest {

	private static final String	RECRUITER_ID	= "acb124";
	private static final String	LISTING_NAME	= "Java Developer";
	private static final String	CANDIDATE_ID	= "128822";
	private static final String	MESSAGE			= "a message";
	private static final byte[]	FILE			= new byte[]{};
	private static final String	FILE_TYPE		= "pdf";
	
	/**
	* Tests construction via the builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		CandidateRequestListingContactEmailCommand command = 
				CandidateRequestListingContactEmailCommand
					.builder()
						.file(FILE)
						.fileType(FILE_TYPE)
						.listingName(LISTING_NAME)
						.message(MESSAGE)
						.recruiterId(RECRUITER_ID)
						.candidateId(CANDIDATE_ID)
					.build();
		
		assertEquals(RECRUITER_ID, 	command.getRecruiterId());
		assertEquals(LISTING_NAME, 	command.getListingName());
		assertEquals(CANDIDATE_ID, 	command.getCandidateId());
		assertEquals(MESSAGE, 		command.getMessage());
		assertEquals(FILE, 			command.getFile());
		assertEquals(FILE_TYPE, 	command.getFileType());
	}
	
}