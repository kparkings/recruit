package com.arenella.recruit.emailservice.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.emailservice.beans.EmailAttachment;
import com.arenella.recruit.emailservice.beans.EmailAttachment.FileType;

/**
* Entity represenations of an Email attachement
* @author K Parkings
*/
@Entity
@Table(schema="email", name="email_attachment")
public class EmailAttachmentEntity {

	@Id
	@Column(name="attachment_id")
	private UUID attachmentId;
	
	@Column(name="email_id")
	private UUID emailId;
	
	@Column(name="file_type")
	@Enumerated(EnumType.STRING)
	private FileType fileType;
	
	@Column(name="file_bytes")
	private byte[] fileBytes;
	
	/**
	* Default Constructor 
	*/
	public EmailAttachmentEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - contains initialization values
	*/
	public EmailAttachmentEntity(EmailAttachmentEntityBuilder builder) {
		this.attachmentId 	= builder.attachmentId;
		this.emailId 		= builder.emailId;
		this.fileType 		= builder.fileType;
		this.fileBytes 		= builder.fileBytes;
	}
	
	/**
	* Returns the Unique id of the attachment
	* @return uinuqeId/PK
	*/
	public UUID getAttachmentId() {
		return this.attachmentId;
	}
	
	/**
	* Returns the unique Id of the Email the attachment
	* belongs to
	* @return Id of the email
	*/
	public UUID getEmailId() {
		return this.emailId;
	}
	
	/**
	* Returns the type of file of the attachment
	* @return fileType
	*/
	public FileType getFileType() {
		return this.fileType;
	}
	
	/**
	* Returns the bytes that constitute the 
	* attachment file
	* @return file bytes
	*/
	public byte[] getFileBytes() {
		return this.fileBytes;
	}
	
	/**
	* Returns a Builder for the class
	* @return Builder for the class
	*/
	public static EmailAttachmentEntityBuilder builder() {
		return new EmailAttachmentEntityBuilder();
	}
	
	/**
	* Builder for the EmailAttachmentEntity class
	* @author K Parkings
	*/
	public static class EmailAttachmentEntityBuilder{
		
		public  UUID 		attachmentId;
		private UUID 		emailId;
		private FileType 	fileType;
		private byte[] 		fileBytes;
		
		/**
		* Sets the Unique id of the attachment
		* @param attachmentId - id of the attahment
		* @return Builder
		*/
		public EmailAttachmentEntityBuilder attachmentId(UUID attachmentId) {
			this.attachmentId = attachmentId;
			return this;
		}
		
		/**
		* Sets the Unique id of the Email the attachment is 
		* associated with 
		* @param emailId - id of the email
		* @return Builder
		*/
		public EmailAttachmentEntityBuilder emailId(UUID emailId) {
			this.emailId = emailId;
			return this;
		}
		
		/**
		* Sets the type of the attachment file
		* @param fileType
		* @return Builder
		*/
		public EmailAttachmentEntityBuilder fileType(FileType fileType) {
			this.fileType = fileType;
			return this;
		}
		
		/**
		* Sets the bytes that constitutes the attachment file
		* @param fileBytes - bytes of file
		* @return Builder
		*/
		public EmailAttachmentEntityBuilder fileBytes(byte[] fileBytes) {
			this.fileBytes = fileBytes;
			return this;
		}
		
		/**
		* Returns an initialized instance of the
		* EmailAttachmentEntity class
		* @return instance
		*/
		public EmailAttachmentEntity build() {
			return new EmailAttachmentEntity(this);
		}
	}
	
	/**
	* Converts from Domain representation of Attachment to 
	* Entity representation
	* @param attachment - domain representation
	* @return entity representation
	*/
	public static EmailAttachmentEntity convertFromDomain(EmailAttachment attachment) {
		return EmailAttachmentEntity
				.builder()
					.attachmentId(attachment.getAttachmentId())
					.emailId(attachment.getEmailId())
					.fileBytes(attachment.getFileBytes())
					.fileType(attachment.getFileType())
				.build();
	}
	
	/**
	* Converts from Entity representation of Attachment to 
	* Domain representation
	* @param entity - Entity representation
	* @return domain representation
	*/
	public static EmailAttachment convertToDomain(EmailAttachmentEntity entity) {
		return EmailAttachment
				.builder()
					.attachmentId(entity.getAttachmentId())
					.emailId(entity.getEmailId())
					.fileBytes(entity.getFileBytes())
					.fileType(entity.getFileType())
				.build();
	}
	
}