package com.arenella.recruit.emailservice.adapters;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.Sender;

/**
* Event requesting that an Email is Sent
* @author K Parkings
*/
//TODO: [KP] For now Classes are in shared folder. In MS implementation these will be Json String and 
//			 the class definitions can be declared in each service
public class RequestSendEmailCommand {

	private String 					title;
	private EmailType 				emailType;
	private Sender<?> 				sender; 
	private Set<EmailRecipient<?>> 	recipients					= new LinkedHashSet<>(); 
	private EmailTopic 				topic;
	private Map<String,Object>		model						= new HashMap<>();
	private boolean					persistable					= false;
	private Set<Attachment>			attachments					= new LinkedHashSet<>();
	
	/**
	* Returns the title for the Email
	* @return Email title
	*/
	public String getTitle() {
		return this.title;
	}
	
	/**
	* Returns the type of the Email
	* @return Email type
	*/
	public EmailType getEmailType() {
		return this.emailType;
	}
	
	/**
	* Returns details of the Sender of the Email
	* @return Sender
	*/
	public Sender<?> getSender() {
		return this.sender;
	}
	
	/**
	* Returns details of the Recipient(s) of the Email
	* @return Recipients
	*/
	public Set<EmailRecipient<?>> getRecipients(){
		return this.recipients;
	}
	
	/**
	* Returns the topic of the email
	* @return - determines template to use
	*/
	public EmailTopic getTopic() {
		return this.topic;
	}
	
	/**
	* Returns the Model containing the data to be 
	* used in the email
	* @return Model
	*/
	public Map<String,Object> getModel(){
		return this.model;
	}
	
	/**
	* Returns the emails attachments
	* @return attachments
	*/
	public Set<Attachment> getAttachments(){
		return this.attachments;
	}
	
	/**
	* Returns whether the email should be persisted to the DB
	* - True  - If service is stopped email will not be lost but data stored in email body in DB
	* - False - If service is stopped email is lost bug no senstive data in email stored in DB 
	* @return
	*/
	public boolean isPersistable() {
		return this.persistable;
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder
	*/
	public RequestSendEmailCommand(RequestSendEmailCommandBuilder builder) {
		this.title 			= builder.title;
		this.emailType 		= builder.emailType;
		this.sender 		= builder.sender;
		this.recipients 	= builder.recipients;
		this.topic			= builder.topic;
		this.model			= builder.model;
		this.persistable	= builder.persistable;
		this.attachments	= builder.attachments;
	}
	
	/**
	* Returns a builder for the RequestSendEmailEvent class
	* @return Builder
	*/
	public static RequestSendEmailCommandBuilder builder() {
		return new RequestSendEmailCommandBuilder();
	}
	
	/**
	* Builder for the RequestSendEmailEvent class
	* @author K Parkings
	*/
	public static class RequestSendEmailCommandBuilder{
		
		private String 					title;
		private EmailType 				emailType;
		private Sender<?> 				sender; 
		private Set<EmailRecipient<?>> 	recipients					= new LinkedHashSet<>(); 
		private EmailTopic 				topic;
		private Map<String,Object>		model						= new HashMap<>();
		private boolean					persistable					= false;
		private Set<Attachment>			attachments					= new LinkedHashSet<>();
		
		/**
		* Sets the Title of the Email
		* @param title - Title of the Email
		* @return Builder
		*/
		public RequestSendEmailCommandBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the email type
		* @param emailType - Type of the Email
		* @return Builder
		*/
		public RequestSendEmailCommandBuilder emailType(EmailType emailType) {
			this.emailType = emailType;
			return this;
		}
		
		/**
		* Sets the Sender of the Email
		* @param sender - Sender
		* @return Builder
		*/
		public RequestSendEmailCommandBuilder sender(Sender<?> sender) {
			this.sender = sender;
			return this;
		}
		
		/**
		* Sets the Recipient(s) of the Email
		* @param recipient - Recipients
		* @return Builder
		*/
		public RequestSendEmailCommandBuilder recipients(Set<EmailRecipient<?>> recipient) {
			this.recipients = recipient;
			return this;
		}
		
		/**
		* Sets the topic of the Email (Determines template to use)
		* @param topic - Email topic
		* @return Builder
		*/
		public RequestSendEmailCommandBuilder topic(EmailTopic topic) {
			this.topic = topic;
			return this;
		}
		
		/**
		* Contains data to be used in the email
		* @param model - Data for constructing the email
		* @return Builder
		*/
		public RequestSendEmailCommandBuilder model(Map<String,Object> model) {
			this.model = model;
			return this;
		}
		
		/**
		* Sets whether or not the email can be persisted in case the service is 
		* stopped so that is can be sent once the service is restarted
		* Warning: Persisting of sensitive data is security risk. Only set true if no sensitive data in Email
		* @param persistable - Whether or not the email can be persisted
		* @return Builder
		*/
		public RequestSendEmailCommandBuilder persistable(boolean persistable) {
			this.persistable = persistable;
			return this;
		}
		
		/**
		* Sets the attachments associated with the Email
		* @return Builder
		*/
		public RequestSendEmailCommandBuilder attachments(Set<Attachment> attachments) {
			this.attachments.clear();
			this.attachments.addAll(attachments);
			return this;
		}
		
		/**
		* Returns a new initialized RequestSendEmailCommand 
		* @return command
		*/
		public RequestSendEmailCommand build() {
			return new RequestSendEmailCommand(this);
		}
		
	}
	
	/**
	* Individual Attachment for an Email
	* @author K Parkings
	*/
	public static class Attachment{
	
		private final String 	fileType;
		private final String	name;
		private final byte[]	fileBytes;
		
		/**
		* Constructor
		* @param fileType	- Type of file
		* @param fileBytes	- file as byte array
		*/
		public Attachment(String fileType, String name, byte[] fileBytes) {
			this.fileType 	= fileType;
			this.name		= name;
			this.fileBytes 	= fileBytes;
		}
		
		/**
		* Returns the type of the attachment
		* @return file type
		*/
		public String getFileType() {
			return this.fileType;
		}
		
		/**
		* Returns the name of the Attachment
		* @return name of the Attachment
		*/
		public String getName() {
			return this.name;
		}
		/**
		* Returns the attachment file as bytes
		* @return attachment as bytes
		*/
		public byte[] getFileBytes() {
			return this.fileBytes;
		}
	}
	
}