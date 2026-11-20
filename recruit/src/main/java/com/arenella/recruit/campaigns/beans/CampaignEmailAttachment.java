package com.arenella.recruit.campaigns.beans;

import java.util.UUID;

/**
* Represents an Email attachemnt 
*/
public class CampaignEmailAttachment {

	public static enum FileType {doc, docx, pdf, odt}
	
	private UUID 		attachmentId;
	private UUID 		emailId;
	private String 		name;
	private FileType 	fileType;
	private byte[] 		fileBytes;
	
	/**
	* Default Constructor 
	*/
	public CampaignEmailAttachment() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - contains initialization values
	*/
	public CampaignEmailAttachment(CampaignEmailAttachmentBuilder builder) {
		this.attachmentId 	= builder.attachmentId;
		this.emailId 		= builder.emailId;
		this.name			= builder.name;
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
	* Returns the name of the Attachment 
	* @return
	*/
	public String getName() {
		return this.name;
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
	public static CampaignEmailAttachmentBuilder builder() {
		return new CampaignEmailAttachmentBuilder();
	}
	
	/**
	* Builder for the EmailAttachmentEntity class
	* @author K Parkings
	*/
	public static class CampaignEmailAttachmentBuilder{
		
		private UUID 		attachmentId;
		private UUID 		emailId;
		private String		name;
		private FileType 	fileType;
		private byte[] 		fileBytes;
		
		/**
		* Sets the Unique id of the attachment
		* @param attachmentId - id of the attahment
		* @return Builder
		*/
		public CampaignEmailAttachmentBuilder attachmentId(UUID attachmentId) {
			this.attachmentId = attachmentId;
			return this;
		}
		
		/**
		* Sets the Unique id of the Email the attachment is 
		* associated with 
		* @param emailId - id of the email
		* @return Builder
		*/
		public CampaignEmailAttachmentBuilder emailId(UUID emailId) {
			this.emailId = emailId;
			return this;
		}
		
		/**
		* Sets the name of the Attachment
		* @param name - attachment name
		* @return Builder
		*/
		public CampaignEmailAttachmentBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		* Sets the type of the attachment file
		* @param fileType
		* @return Builder
		*/
		public CampaignEmailAttachmentBuilder fileType(FileType fileType) {
			this.fileType = fileType;
			return this;
		}
		
		/**
		* Sets the bytes that constitutes the attachment file
		* @param fileBytes - bytes of file
		* @return Builder
		*/
		public CampaignEmailAttachmentBuilder fileBytes(byte[] fileBytes) {
			this.fileBytes = fileBytes;
			return this;
		}
		
		/**
		* Returns an initialized instance of the
		* EmailAttachmentEntity class
		* @return instance
		*/
		public CampaignEmailAttachment build() {
			return new CampaignEmailAttachment(this);
		}
	}
	
}