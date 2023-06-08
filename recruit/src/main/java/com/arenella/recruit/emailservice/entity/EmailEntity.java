package com.arenella.recruit.emailservice.entity;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Status;

/**
* Entity representation of an Email 
* @author K Parkings
*/
@Entity
@Table(schema="email", name="email")
public class EmailEntity {

	@Id
	@Column(name="id")
	private UUID 					id;
	
	@Column(name="title")
	private String 					title;
	
	@Column(name="email_type")
	@Enumerated(EnumType.STRING)
	private EmailType 				emailType;
	
	@Column(name="created")
	private LocalDateTime 			created;
	
	@Column(name="scheduled_to_be_sent_after")
	private LocalDateTime 			scheduledToBeSentAfter;
	
	@Column(name="sent")
	private LocalDateTime 			sent;
	
	@Column(name="body")
	private String 					body;
	
	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private Status 					status;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumns({
        @JoinColumn(name="sender_id", referencedColumnName="id")
    })
	private SenderEntity 			sender; 
	
	@OneToMany(mappedBy = "emailId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private Set<EmailRecipientEntity> 	recipients					= new LinkedHashSet<>(); 
	
	@OneToMany(mappedBy = "emailId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private Set<EmailAttachmentEntity> 	attachments					= new LinkedHashSet<>(); 
	
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
	public SenderEntity getSender(){
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
	* Recipient(s)
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
	public Set<EmailRecipientEntity> getRecipients(){
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
	* Returns the attachments associated
	* with the email
	* @return attachments
	*/
	public Set<EmailAttachmentEntity> getAttachments(){
		return this.attachments;
	}
	
	/**
	* Constructor 
	*/
	public EmailEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization details
	*/
	public EmailEntity(EmailEntityBuilder builder) {
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
		this.attachments			= builder.attachments;
	}
	
	/**
	* Returns a Builder for the EmailEntity class
	* @return Builder
	*/
	public static EmailEntityBuilder builder() {
		return new EmailEntityBuilder();
	}
	
	/**
	* Builder for the Email class
	* @author K Parkings
	*/
	public static class EmailEntityBuilder{
	
		private UUID 						id;
		private String 						title;
		private EmailType 					emailType;
		private SenderEntity 				sender; 
		private LocalDateTime 				created;
		private LocalDateTime 				scheduledToBeSentAfter;
		private LocalDateTime 				sent;
		private Set<EmailRecipientEntity> 	recipients					= new LinkedHashSet<>(); 
		private String 						body;
		private Status 						status;
		private Set<EmailAttachmentEntity> 	attachments					= new LinkedHashSet<>(); 
		
		/**
		* Sets the unique Id of the email
		* @param id - Sets the unique ID of the email
		* @return Builder
		*/
		public EmailEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the title of the Email
		* @param title - Title of the Email
		* @return Builder
		*/
		public EmailEntityBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the type of the Email
		* @param emailType - Email Type
		* @return Builder
		*/
		public EmailEntityBuilder emailType(EmailType emailType) {
			this.emailType = emailType;
			return this;
		}
		
		/**
		* Sets the Object representing the Entity sending the Email
		* @param sender - Who is sending the email
		* @return Builder
		*/
		public EmailEntityBuilder sender(SenderEntity sender) {
			this.sender = sender;
			return this;
		} 
		
		/**
		* Sets when the Email was created
		* @param created - When the Email was created
		* @return Builder
		*/
		public EmailEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets when the Email is scheduled to run at the earliest. There is no guarantee
		* when the email will be send but it will not be before this time
		* @param scheduledToBeSentAfter - Time from which email can be sent to recipient(s)
		* @return Builder
		*/
		public EmailEntityBuilder scheduledToBeSentAfter(LocalDateTime scheduledToBeSentAfter) {
			this.scheduledToBeSentAfter = scheduledToBeSentAfter;
			return this;
		}
		
		/**
		* Sets when the Email was sent to the Recipient(s)
		* @param sent - When the Email was sent
		* @return Builder
		*/
		public EmailEntityBuilder sent(LocalDateTime sent) {
			this.sent = sent;
			return this;
		}
		
		/**
		* Sets the Entities representing the recipients of the Email
		* @param recipients - Email recipients
		* @return Builder
		*/
		public EmailEntityBuilder recipients(Set<EmailRecipientEntity> recipients) {
			this.recipients.clear();
			this.recipients.addAll(recipients);
			return this;
		} 
		
		/**
		* Sets the body of the Email
		* @param body - Body of the Email
		* @return Builder
		*/
		public EmailEntityBuilder body(String body) {
			this.body = body;
			return this;
		}
		
		/**
		* Sets the status of the Email
		* @param status - Email Status
		* @return Builder
		*/
		public EmailEntityBuilder status(Status status) {
			this.status = status;
			return this;
		}
		
		/**
		* Sets the attachments associated with the Entity
		* @param attachments - attachments
		* @return Builder
		*/
		public EmailEntityBuilder attachments(Set<EmailAttachmentEntity> attachments) {
			this.attachments = attachments;
			return this;
		}
		
		/**
		* Returns an initialized Email object
		* @return Initialized Email object
		*/
		public EmailEntity build() {
			return new EmailEntity(this);
		}
		
	}
	
	/**
	* Converts domain version of Email to Entity version
	* @param email - Domain representation
	* @return Entity representation
	*/
	public static EmailEntity convertToEntity(Email email) {
		
		return EmailEntity
				.builder()
					.body(email.getBody())
					.created(email.getCreated())
					.emailType(email.getEmailType())
					.id(email.getId())
					.recipients(email.getRecipients().stream().map(r -> EmailRecipientEntity.convertToEntity(r, email)).collect(Collectors.toSet()))
					.scheduledToBeSentAfter(email.getScheduledToBeSentAfter())
					.sender(SenderEntity.convertToEntity(email.getSender(), email))
					.sent(email.getSent())
					.status(email.getStatus())
					.title(email.getTitle())
					.attachments(email.getAttachments().stream().map(EmailAttachmentEntity::convertFromDomain).collect(Collectors.toCollection(LinkedHashSet::new)))
				.build();
		
	}
	
	/**
	* Converts from an Entity representation of an Email to a Domain representation
	* @param entity - Entity representation
	* @return Domain representation
	*/
	public static Email convertFromEntity(EmailEntity entity) {
		
		return Email
				.builder()
					.body(entity.getBody())
					.created(entity.getCreated())
					.emailType(entity.getEmailType())
					.id(entity.getId())
					.recipients(entity.getRecipients().stream().map(EmailRecipientEntity::convertFromEntity).collect(Collectors.toSet()))
					.scheduledToBeSentAfter(entity.getScheduledToBeSentAfter())
					.sender(SenderEntity.convertFromEntity(entity.getSender())) //TODO: T saved to DB String returned
					.sent(entity.getSent())
					.status(entity.getStatus())
					.title(entity.getTitle())
					.attachments(entity.getAttachments().stream().map(EmailAttachmentEntity::convertToDomain).collect(Collectors.toCollection(LinkedHashSet::new)))
				.build();
	}
	
}