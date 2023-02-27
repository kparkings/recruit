package com.arenella.recruit.emailservice.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.EmailAttachment.FileType;

/**
* Unit tests for the EmailAttachment class
* @author K Parkings
*/
public class EmailAttachmentTest {

	/**
	* Tests construction via Builder 
	*/
	@Test
	public void testBuilder() throws Exception{
		
		final UUID 		attachmentId 	= UUID.randomUUID();
		final UUID 		emailId 		= UUID.randomUUID();
		final String	name			= "cv";
		final FileType 	fileType 		= FileType.odt;
		final byte[] 	fileBytes 		= new byte[] {1,66,4};
		
		EmailAttachment attachment = 
				EmailAttachment
					.builder()
						.attachmentId(attachmentId)
						.emailId(emailId)
						.name(name)
						.fileBytes(fileBytes)
						.fileType(fileType)
					.build();
		
		assertEquals(attachmentId, 	attachment.getAttachmentId());
		assertEquals(emailId, 		attachment.getEmailId());
		assertEquals(name,	 		attachment.getName());
		assertEquals(fileType, 		attachment.getFileType());
		assertEquals(fileBytes, 	attachment.getFileBytes());
				
		
	}
	
}