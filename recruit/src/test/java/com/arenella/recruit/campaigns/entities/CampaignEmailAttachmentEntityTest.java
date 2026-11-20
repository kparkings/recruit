package com.arenella.recruit.campaigns.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.CampaignEmailAttachment;
import com.arenella.recruit.campaigns.beans.CampaignEmailAttachment.FileType;

/**
* Unit tests for the CampaignEmailAttachmentEntity class 
*/
class CampaignEmailAttachmentEntityTest {

	private static final UUID 		ATTACHMENT_ID 	= UUID.randomUUID();
	private static final UUID 		EMAIL_ID 		= UUID.randomUUID();
	private static final String		NAME 			= "my cv";
	private static final FileType 	FILE_TYPE 		= FileType.pdf;
	private static final byte[] 	FILE_BYTES 		= new byte[] {};
	
	/**
	* Tests construction
	*/
	@Test
	void testConstruction() {
		
		CampaignEmailAttachmentEntity entity = CampaignEmailAttachmentEntity
				.builder()
					.attachmentId(ATTACHMENT_ID)
					.emailId(EMAIL_ID)
					.name(NAME)
					.fileType(FILE_TYPE)
					.fileBytes(FILE_BYTES)
				.build();
		
		assertEquals(ATTACHMENT_ID, entity.getAttachmentId());
		assertEquals(EMAIL_ID, 		entity.getEmailId());
		assertEquals(NAME, 			entity.getName());
		assertEquals(FILE_TYPE, 	entity.getFileType());
		assertEquals(FILE_BYTES, 	entity.getFileBytes());
	}
	
	/**
	* Tests conversion from Domain to Entity representation of 
	* an Attachment  
	*/
	@Test
	void testToEntity() {
		
		CampaignEmailAttachment attachment = CampaignEmailAttachment
				.builder()
					.attachmentId(ATTACHMENT_ID)
					.emailId(EMAIL_ID)
					.name(NAME)
					.fileType(FILE_TYPE)
					.fileBytes(FILE_BYTES)
				.build();
		
		assertEquals(ATTACHMENT_ID, attachment.getAttachmentId());
		assertEquals(EMAIL_ID, 		attachment.getEmailId());
		assertEquals(NAME, 			attachment.getName());
		assertEquals(FILE_TYPE, 	attachment.getFileType());
		assertEquals(FILE_BYTES, 	attachment.getFileBytes());
		
		
		CampaignEmailAttachmentEntity entity = CampaignEmailAttachmentEntity.toEntity(attachment);
		
		assertEquals(ATTACHMENT_ID, entity.getAttachmentId());
		assertEquals(EMAIL_ID, 		entity.getEmailId());
		assertEquals(NAME, 			entity.getName());
		assertEquals(FILE_TYPE, 	entity.getFileType());
		assertEquals(FILE_BYTES, 	entity.getFileBytes());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation of 
	* an Attachment  
	*/
	@Test
	void testfromEntity() {
		
		CampaignEmailAttachmentEntity entity = CampaignEmailAttachmentEntity
				.builder()
					.attachmentId(ATTACHMENT_ID)
					.emailId(EMAIL_ID)
					.name(NAME)
					.fileType(FILE_TYPE)
					.fileBytes(FILE_BYTES)
				.build();
		
		assertEquals(ATTACHMENT_ID, entity.getAttachmentId());
		assertEquals(EMAIL_ID, 		entity.getEmailId());
		assertEquals(NAME, 			entity.getName());
		assertEquals(FILE_TYPE, 	entity.getFileType());
		assertEquals(FILE_BYTES, 	entity.getFileBytes());
		
		
		CampaignEmailAttachment attachment = CampaignEmailAttachmentEntity.fromEntity(entity);
		
		assertEquals(ATTACHMENT_ID, attachment.getAttachmentId());
		assertEquals(EMAIL_ID, 		attachment.getEmailId());
		assertEquals(NAME, 			attachment.getName());
		assertEquals(FILE_TYPE, 	attachment.getFileType());
		assertEquals(FILE_BYTES, 	attachment.getFileBytes());
		
	}
	
	
}
