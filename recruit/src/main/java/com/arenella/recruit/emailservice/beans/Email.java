package com.arenella.recruit.emailservice.beans;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
* Class represents an Email that has been been created in the System
* @author K Parkings
*/
public class Email {

	public static enum EmailTopic 	{FORGOTTEN_PWD, ACCOUNT_CREATED, WEEKLY_UPDATE, ALERT_MATCHES};
	public static enum EmailType 	{INTERN, EXTERN, SYSTEM_INTERN, SYSTEM_EXTERN};
	public static enum Status 		{DRAFT, TO_OUTBOX, SENT_INTERN, SENT_EXTERN, FAILURE};
	
	private UUID 					id;
	private String 					title;
	private EmailType 				emailType;
	private Sender<?> 				sender; 
	private LocalDateTime 			created;
	private LocalDateTime 			scheduledToBeSentAfter;
	private LocalDateTime 			sent;
	private Set<EmailRecipient<?>> 	recipients					= new LinkedHashSet<>(); 
	private String 					body;
	private Status 					status						= Status.DRAFT;
	private boolean					persistable					= false;
		
	/**
	* Returns the unique Id of the email
	* @return email id
	*/
	public UUID getId(){
		return this.id;
	}
	
	/**
	* Returns the title of the email
	* @return email title
	*/
	public String getTitle(){
		return this.title;
	}
	
	/**
	* Returns the type of the Email
	* @return email type
	*/
	public EmailType getEmailType(){
		return this.emailType;
	}
	
	/**
	* Returns the Entity that is Sending the Email
	* @return Sender
	*/
	public Sender<?> getSender(){
		return this.sender;
	}
	
	/**
	* Returns when the Email was created
	* @return Email creation date/time
	*/
	public LocalDateTime getCreated(){
		return this.created;
	}
	
	/**
	* Returns the earliest time the Email can be sent to the 
	* Recpient(s)
	* @return earliest moment the Email can be sent
	*/
	public LocalDateTime getScheduledToBeSentAfter(){
		return this.scheduledToBeSentAfter;
	}
	
	/**
	* Returns the data/time when the email was Sent to the Recipient(s)
	* @return When the Email was sent
	*/
	public LocalDateTime getSent(){
		return this.sent;
	}
	
	/**
	* Returns the Recipients that are to receive the Email
	* @return Recipients of the Email
	*/
	public Set<EmailRecipient<?>> getRecipients(){
		return this.recipients;
	} 
	
	/**
	* Returns the Body of the Email
	* @return email body
	*/
	public String getBody(){
		return this.body;
	}
	
	/**
	* Returns the Status of the Email
	* @return Email Status
	*/
	public Status getStatus(){
		return this.status;
	}
	
	/**
	* Whether or not the Email can be persisted to the DB
	* @return Whether or not the Email can be peristed to the DB
	*/
	public boolean isPersistable() {
		return this.persistable;
	}
	
	/**
	* Marks an Email as having been sent to 
	* an external email address
	*/
	public void markSentExternal() {
		this.status = Status.SENT_EXTERN;
	}
	
	/**
	* Marks an email as having failed to be sent
	*/
	public void markSentFailure() {
		this.status = Status.FAILURE;
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization details
	*/
	public Email(EmailBuilder builder) {
		this.id 					= builder.id;
		this.title 					= builder.title;
		this.emailType 				= builder.emailType;
		this.sender 				= builder.sender;
		this.created 				= builder.created;
		this.scheduledToBeSentAfter = builder.scheduledToBeSentAfter;
		this.sent 					= builder.sent;
		this.recipients 			= builder.recipients;
		this.body 					= builder.body;
		this.status 				= builder.status;
		this.persistable			= builder.persistable;
	}
	
	/**
	* Returns a Builder for the Email class
	* @return Builder
	*/
	public static EmailBuilder builder() {
		return new EmailBuilder();
	}
	
	/**
	* Builder for the Email class
	* @author K Parkings
	*/
	public static class EmailBuilder{
	
		private UUID 					id;
		private String 					title;
		private EmailType 				emailType;
		private Sender<?> 				sender; 
		private LocalDateTime 			created;
		private LocalDateTime 			scheduledToBeSentAfter;
		private LocalDateTime 			sent;
		private Set<EmailRecipient<?>> 	recipients					= new LinkedHashSet<>(); 
		private String 					body;
		private Status 					status						= Status.DRAFT;
		private boolean					persistable					= false;
		
		/**
		* Sets the unique Id of the email
		* @param id - Sets the unique ID of the email
		* @return Builder
		*/
		public EmailBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the title of the Email
		* @param title - Title of the Email
		* @return Builder
		*/
		public EmailBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the type of the Email
		* @param emailType - Email Type
		* @return Builder
		*/
		public EmailBuilder emailType(EmailType emailType) {
			this.emailType = emailType;
			return this;
		}
		
		/**
		* Sets the Object representing the Entity sending the Email
		* @param sender - Who is sending the email
		* @return Builder
		*/
		public EmailBuilder sender(Sender<?> sender) {
			this.sender = sender;
			return this;
		} 
		
		/**
		* Sets when the Email was created
		* @param created - When the Email was created
		* @return Builder
		*/
		public EmailBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets when the Email is scheduled to run at the earliest. There is no guarantee
		* when the email will be send but it will not be before this time
		* @param scheduledToBeSentAfter - Time from which email can be sent to recipent(s)
		* @return Builder
		*/
		public EmailBuilder scheduledToBeSentAfter(LocalDateTime scheduledToBeSentAfter) {
			this.scheduledToBeSentAfter = scheduledToBeSentAfter;
			return this;
		}
		
		/**
		* Sets when the Email was sent to the Recipient(s)
		* @param sent - When the Email was sent
		* @return Builder
		*/
		public EmailBuilder sent(LocalDateTime sent) {
			this.sent = sent;
			return this;
		}
		
		/**
		* Sets the Entities representing the recripients of the Email
		* @param recipients - Email recipients
		* @return Builder
		*/
		public EmailBuilder recipients(Set<EmailRecipient<?>> recipients) {
			this.recipients.clear();
			this.recipients.addAll(recipients);
			return this;
		} 
		
		/**
		* Sets the body of the Email
		* @param body - Body of the Email
		* @return Builder
		*/
		public EmailBuilder body(String body) {
			this.body = body;
			return this;
		}
		
		/**
		* Sets the status of the Email
		* @param status - Email Status
		* @return Builder
		*/
		public EmailBuilder status(Status status) {
			this.status = status;
			return this;
		}
		
		/**
		* Sets whether or not the Email can be persisted to DB. Should not
		* be persisted in sensitive details are in the body
		* @param persistable - Whether to persist email : default false
		* @return Builder
		*/
		public EmailBuilder persistable(boolean persistable) {
			this.persistable = persistable;
			return this;
		}
		
		/**
		* Returns an initialized Email object
		* @return Initialized Email object
		*/
		public Email build() {
			return new Email(this);
		}
		
	}
	
	/**
	* Sender of an Email
	* @author K Parkings
	* @param <T> Type of the object used to for the Id of the 
	* 			 specific Recipient type	
	*/
	public static class Sender<T>{
		
		public static enum SenderType {SYSTEM, RECRUITER};
		
		private final T				id;
		private final SenderType 	senderType;
		private final String 		email;
		
		/**
		* Constructor
		* @param id				- Unique Id of the Sender
		* @param senderType		- Type of the Sender
		* @param email			- Senders Email Address
		*/
		public Sender(T id, SenderType senderType, String email) {
			this.id 			= id;
			this.senderType 	= senderType;
			this.email 			= email;
		}
		
		/**
		* Returns the unique Identifier of the Sender
		* @return Id of the Sender
		*/
		public T getId() {
			return this.id;
		}
		
		/**
		* Returns the type of the Sender
		* @return Senders type
		*/
		public SenderType getSenderType() {
			return this.senderType;
		}
		
		/**
		* Returns the email address of the Sender
		* @return Senders email address
		*/
		public String getEmail() {
			return this.email;
		}
		
	}
	
	/**
	* Recipient of an Email
	* @author K Parkings
	* @param <T> Type of the object used to for the Id of the 
	* 			 specific Recipient type	
	*/
	public static class EmailRecipient<T>{
		
		public static enum RecipientType {SYSTEM, RECRUITER};
		
		private final T 				id;
		private final RecipientType 	recipientType;
		private final String 			emailAddress;
	
		/**
		* Constructor
		* @param id				- Unique id of the Recipient
		* @param recipientType	- Type of the Recipient
		* @param emailAddress	- Recipients Email address
		*/
		public EmailRecipient(T id, RecipientType recipientType, String emailAddress) {
			this.id  			= id;
			this.recipientType 	= recipientType;
			this.emailAddress 	= emailAddress;
		}
		
		/**
		* Returns the unique Identifier of the Recipient
		* @return Id of the Sender
		*/
		public T getId() {
			return this.id;
		}
		
		/**
		* Returns the type of the Recipient
		* @return Recipient type
		*/
		public RecipientType getRecipientType() {
			return this.recipientType;
		}
		
		/**
		* Returns the email address of the Recipient
		* @return Recipient email address
		*/
		public String getEmailAddress() {
			return this.emailAddress;
		}
		
	}
	
}