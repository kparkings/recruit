package com.arenella.recruit.emailservice.beans;

import java.util.UUID;

/**
* Class represents an attachment to an email
* @author K Parkings
*/
public class EmailAttachment {

	public static enum FileType {doc, docx, pdf, odt}
	
	private UUID 		attachmentId;
	private UUID 		emailId;
	private FileType 	fileType;
	private byte[] 		fileBytes;
	
	/**
	* Constructor based upon a Builder
	* @param builder - contains initialization values
	*/
	public EmailAttachment(EmailAttachmentBuilder builder) {
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
	public static EmailAttachmentBuilder builder() {
		return new EmailAttachmentBuilder();
	}
	
	/**
	* Builder for the EmailAttachment class
	* @author K Parkings
	*/
	public static class EmailAttachmentBuilder{
		
		public  UUID 		attachmentId;
		private UUID 		emailId;
		private FileType 	fileType;
		private byte[] 		fileBytes;
		
		/**
		* Sets the Unique id of the attachment
		* @param attachmentId - id of the attahment
		* @return Builder
		*/
		public EmailAttachmentBuilder attachmentId(UUID attachmentId) {
			this.attachmentId = attachmentId;
			return this;
		}
		
		/**
		* Sets the Unique id of the Email the attachment is 
		* associated with 
		* @param emailId - id of the email
		* @return Builder
		*/
		public EmailAttachmentBuilder emailId(UUID emailId) {
			this.emailId = emailId;
			return this;
		}
		
		/**
		* Sets the type of the attachment file
		* @param fileType
		* @return Builder
		*/
		public EmailAttachmentBuilder fileType(FileType fileType) {
			this.fileType = fileType;
			return this;
		}
		
		/**
		* Sets the bytes that constitutes the attachment file
		* @param fileBytes - bytes of file
		* @return Builder
		*/
		public EmailAttachmentBuilder fileBytes(byte[] fileBytes) {
			this.fileBytes = fileBytes;
			return this;
		}
		
		/**
		* Returns an initialized instance of the
		* EmailAttachmentEntity class
		* @return instance
		*/
		public EmailAttachment build() {
			return new EmailAttachment(this);
		}
	}
}
