package com.arenella.recruit.emailservice.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.EmailAttachment;
import com.arenella.recruit.emailservice.beans.EmailAttachment.FileType;
import com.arenella.recruit.emailservice.entity.EmailAttachmentEntity;

/**
* Unit tests for the EmailAttachmentEntity class
* @author K Parkings
*/
class EmailAttachmentEntityTest {

	/**
	* Tests construction via Builder 
	*/
	@Test
	void testBuilder() {
		
		final UUID 		attachmentId 	= UUID.randomUUID();
		final UUID 		emailId 		= UUID.randomUUID();
		final String 	name			= "cv";
		final FileType 	fileType 		= FileType.odt;
		final byte[] 	fileBytes 		= new byte[] {1,66,4};
		
		EmailAttachmentEntity attachment = 
				EmailAttachmentEntity
					.builder()
						.attachmentId(attachmentId)
						.emailId(emailId)
						.name(name)
						.fileBytes(fileBytes)
						.fileType(fileType)
					.build();
		
		assertEquals(attachmentId, 	attachment.getAttachmentId());
		assertEquals(emailId, 		attachment.getEmailId());
		assertEquals(name, 			attachment.getName());
		assertEquals(fileType, 		attachment.getFileType());
		assertEquals(fileBytes, 	attachment.getFileBytes());
		
	}
	
	/**
	* Tests conversion from Entity representation
	* to Domain representation
	* @throws Exception
	*/
	@Test
	void testConvertToDomain() {
		
		final UUID 		attachmentId 	= UUID.randomUUID();
		final UUID 		emailId 		= UUID.randomUUID();
		final String 	name			= "cv";
		final FileType 	fileType 		= FileType.odt;
		final byte[] 	fileBytes 		= new byte[] {1,66,4};
		
		EmailAttachmentEntity entity = 
				EmailAttachmentEntity
					.builder()
						.attachmentId(attachmentId)
						.emailId(emailId)
						.name(name)
						.fileBytes(fileBytes)
						.fileType(fileType)
					.build();
		
		EmailAttachment attachment = EmailAttachmentEntity.convertToDomain(entity);
		
		assertEquals(attachmentId, 	attachment.getAttachmentId());
		assertEquals(emailId, 		attachment.getEmailId());
		assertEquals(name, 			attachment.getName());
		assertEquals(fileType, 		attachment.getFileType());
		assertEquals(fileBytes, 	attachment.getFileBytes());
		
	}
	
}