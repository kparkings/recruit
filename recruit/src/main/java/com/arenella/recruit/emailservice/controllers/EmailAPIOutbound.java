package com.arenella.recruit.emailservice.controllers;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.arenella.recruit.emailservice.beans.Contact;
import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.emailservice.beans.EmailAttachment;
import com.arenella.recruit.emailservice.beans.EmailAttachment.FileType;

/**
* External representation of an Email for viewing
* by the email recipient
* @author K Parkings
*/
public class EmailAPIOutbound {

	private UUID 							id;
	private String 							title;
	private SenderAPIOutbound 				sender; 
	private LocalDateTime 					created;
	private String 							body;
	private Set<EmailAttachmentAPIOutbound>	attachments		= new LinkedHashSet<>();
	private boolean							viewed			= false;
	
	/**
	* Constructor based upon Builder
	* @param builder Contains initalization values
	*/
	public EmailAPIOutbound(EmailAPIOutboundBuilder builder) {
		this.id				= builder.id;
		this.title			= builder.title;
		this.sender			= builder.sender; 
		this.created		= builder.created;
		this.body			= builder.body;
		this.attachments	= builder.attachments;
		this.viewed			= builder.viewed;
	}
	
	/**
	* Returns the id of the email
	* @return email id
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns the title of the email
	* @return email title
	*/
	public String getTitle() {
		return this.title;
	}
	
	/**
	* Returns the Sender of the Email
	* @return sender
	*/
	public SenderAPIOutbound getSender() {
		return this.sender;
	} 
	
	/**
	* Returns the date the email was created
	* @return when the email was created
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Returns the body of the email
	* @return - content of email
	*/
	public String getBody() {
		return this.body;
	}
	
	/**
	* Returns the attachments associated with the email
	* @return email attachments
	*/
	public Set<EmailAttachmentAPIOutbound> getAttachments() {
		return this.attachments;
	}
	
	/**
	* Returns whether the recipient has viewed the email
	* @return whether the email has been viewed
	*/
	public boolean isViewed() {
		return this.viewed;
	}
	
	/**
	* Returns a builder for the class
	* @return
	*/
	public static EmailAPIOutboundBuilder builder() {
		return new EmailAPIOutboundBuilder();
	}
	
	/**
	* Builder for the EmailAPIOutbound class
	* @author K Parkings
	*/
	public static class EmailAPIOutboundBuilder{
		
		private UUID 							id;
		private String 							title;
		private SenderAPIOutbound				sender; 
		private LocalDateTime 					created;
		private String 							body;
		private Set<EmailAttachmentAPIOutbound>	attachments		= new LinkedHashSet<>();
		private boolean							viewed			= false;
		
		/**
		* Sets the unique id of the Email
		* @param id - id of the Email
		* @return Builder
		*/
		public EmailAPIOutboundBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the title of the email
		* @param title - Email's title
		* @return Builder
		*/
		public EmailAPIOutboundBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the Sender of the Email
		* @param sender - The sender
		* @return Builder
		*/
		public EmailAPIOutboundBuilder sender(SenderAPIOutbound sender) {
			this.sender = sender;
			return this;
		} 
		
		/**
		* Sets when the Email was created
		* @param created - creation date/time
		* @return Builder
		*/
		public EmailAPIOutboundBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the body of the Email
		* @param body - Email contents/body
		* @return Builder
		*/
		public EmailAPIOutboundBuilder body(String body) {
			this.body = body;
			return this;
		}
		
		/**
		* Sets the Attachments associated with the Email
		* @param attachments - Email attachments
		* @return Builder
		*/
		public EmailAPIOutboundBuilder attachments(Set<EmailAttachmentAPIOutbound> attachments){
			this.attachments.clear();
			this.attachments.addAll(attachments);
			return this;
		}
		
		/**
		* Sets whether the Recipient has viewed the Email
		* @param viewed - Whether email has been viewed by the Recipient
		* @return Builder
		*/
		public EmailAPIOutboundBuilder viewed(boolean viewed) {
			this.viewed = viewed;
			return this;
		}
		
		/**
		* Returns an instance initailized with the values
		* set in the Builder
		* @return initialized instance
		*/
		public EmailAPIOutbound build() {
			return new EmailAPIOutbound(this);
		}
		
	}
	
	/**
	* API Outbound representation of Sender
	* @author K Parkings
	*/
	public static class SenderAPIOutbound{
		
		private final String 		id;
		private final SenderType 	senderType;
		private final String 		senderName;
		private final String		email;
		
		/**
		* Constructor
		* @param id			- Id of the Sender
		* @param senderType	- Type of the Sender
		* @param senderName	- Name of the Sender
		* @param email		- External Email of Sender
		*/
		public SenderAPIOutbound(String id, SenderType senderType, String senderName, String email) {
			this.id = id;
			this.senderType = senderType;
			this.senderName = senderName;
			this.email		= email;
		}
		
		/**
		* Returns the id of the Sender
		* @return
		*/
		public String getId() {
			return this.id;
		}
		
		/**
		* Returns the name of the Sender
		* @return
		*/
		public String getSenderName() {
			return this.senderName;
		}
		
		/**
		* Returns the Senders external Email address
		* @return external email address
		*/
		public String getEmail() {
			return this.email;
		}
		
		/**
		* Returns the type of User that is sending the email
		* @return tye of Sender
		*/
		public SenderType getSenderType() {
			return this.senderType;
		}
		
		/**
		* Returns whether or not the Sender is the System
		* @return if sender is System
		*/
		public boolean isSystem() {
			return this.senderType == SenderType.SYSTEM;
		}
		
	}
	
	/**
	* APIOutbound representation of an Email Attachment
	* @author K Parkings
	*/
	public static class EmailAttachmentAPIOutbound{
		
		private final UUID 		id;
		private final String 	name;
		
		/**
		* Constructor
		* @param id			- Unique id of the Attachment
		* @param name		- name of the attachment
		* @param fileType	- File type of the attachment
		*/
		public EmailAttachmentAPIOutbound(UUID id, String name, FileType fileType) {
			this.id = id;
			this.name = name.concat(".".concat(fileType.toString()));
		}
		
		/**
		* Returns the id of the Attachment
		* @return id of the Attachment
		*/
		public UUID getId() {
			return this.id;
		}
		
		/**
		* Returns the name of the Attachment
		* @return name of the Attachment
		*/
		public String getName() {
			return this.name.toLowerCase();
		}
	}
	
	/**
	* Converts from Domain representation of Sender to API Outbound representation
	* @param sender - To convert
	* @return converted
	*/
	private static SenderAPIOutbound convertFromDomain(Sender<?> sender, Set<Contact> contacts) {
		
		Optional<Contact> contact = contacts.stream().filter(c -> c.getId().equals(sender.getContactId()) && c.getContactType() == ContactType.RECRUITER).findAny();
		
		return new SenderAPIOutbound(sender.getId().toString()
				, sender.getContactType()
				, contact.isPresent() ? contact.get().getFirstName() + " " + contact.get().getSurname() : sender.getContactId()
				, sender.getEmail());
	}
	
	/**
	* Converts from Domain representation of EmailAttachemnt to API Outbound representation
	* @param attachment - to be converted
	* @return converted
	*/
	private static EmailAttachmentAPIOutbound convertFromDomain(EmailAttachment attachment) {
		return new EmailAttachmentAPIOutbound(attachment.getAttachmentId(), attachment.getName(), attachment.getFileType());
	}
	
	/**
	* Converts from Domain representation to API Outbound
	* representation of the Email
	* @param email - To convert
	* @return converted
	*/
	public static EmailAPIOutbound convertFromDomain(Email email, Set<Contact> contacts) {
		return EmailAPIOutbound
				.builder()
					.attachments(email.getAttachments().stream().map(a -> convertFromDomain(a)).collect(Collectors.toCollection(LinkedHashSet::new)))
					.body(email.getBody())
					.created(email.getCreated())
					.id(email.getId())
					.sender(convertFromDomain(email.getSender(), contacts)) //?? How to get sender name
					.title(email.getTitle())
					.viewed(email.getRecipients().stream().findFirst().get().isViewed()) //Will only work if we only return single recipient
				.build();
	}
}