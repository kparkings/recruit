package com.arenella.recruit.emailservice.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;

@Entity
@Table(schema="email", name="email_recipient")
public class EmailRecipientEntity {

	@Id
	@Column(name="id")
	private String 				id;
	
	@Column(name="email_id")
	private UUID 				emailId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="recipient_type")
	private ContactType 		recipientType;
	
	@Column(name="first_name")
	private String 				firstName;
	
	@Column(name="email_address")
	private String 				emailAddress;
	
	@Column(name="viewed")
	private boolean				viewed;
	
	/**
	* Constructor 
	*/
	public EmailRecipientEntity() {}
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public EmailRecipientEntity(EmailRecipientEntityBuilder builder) {
		this.id 			= builder.id;
		this.emailId 		= builder.emailId;
		this.recipientType 	= builder.recipientType;
		this.firstName		= builder.firstName;
		this.emailAddress 	= builder.emailAddress;
		this.viewed			= builder.viewed;
	}
	
	public String getId() {
		return this.id;
	}
	
	public UUID getEmailId() {
		return this.emailId;
	}
	
	public ContactType getRecipientType() {
		return this.recipientType;
	}
	
	public String getEmailAddress() {
		return this.emailAddress;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns whether the recipient has viewed 
	* the email
	* @return if email has been viewed
	*/
	public boolean isViewed() {
		return this.viewed;
	}
	
	/**
	* Returns a builder for the RecipientBuilder class
	* @return Builder
	*/
	public static EmailRecipientEntityBuilder builder() {
		return new EmailRecipientEntityBuilder();
	}
	
	/**
	* Builder class for EmailRecipientEntity
	* @author K Parkings
	*/
	public static class EmailRecipientEntityBuilder{
		
		private String 			id;
		private UUID 			emailId;
		private ContactType 	recipientType;
		private String 			firstName;
		private String 			emailAddress;
		private boolean			viewed;
		
		/**
		* Sets the unique Id of the Recipient
		* @param id - Unique Id of the Recipient
		* @return Builder
		*/
		public EmailRecipientEntityBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the unique id of the Email
		* @param emailId - Unique Id of the Email
		* @return Builder
		*/
		public EmailRecipientEntityBuilder emailId(UUID emailId) {
			this.emailId = emailId;
			return this;
		}
		
		/**
		* Sets the type of the Recipient
		* @param recipientType - Type of the Recipient
		* @return Builder
		*/
		public EmailRecipientEntityBuilder recipientType(ContactType recipientType) {
			this.recipientType = recipientType;
			return this;
		}
		
		/**
		* Sets the first name of the Recipient
		* @param firstName - First name of the Recipient
		* @return Builder
		*/
		public EmailRecipientEntityBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the body of the Email
		* @param email - Email
		* @return Builder
		*/
		public EmailRecipientEntityBuilder emailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
			return this;
		}
		
		/**
		* Sets whether or not the recipient has viewed the email
		* @param viewed - Whether the email has been viewed by the recipient
		* @return Builder
		*/
		public EmailRecipientEntityBuilder viewed(boolean viewed) {
			this.viewed = viewed;
			return this;
		}
		
		/**
		* Returns an initialized RecipientEntity
		* @return Initialized RecipientEntity
		*/
		public EmailRecipientEntity build() {
			return new EmailRecipientEntity(this);
		}
	}
	
	/**
	* Converts Domain representation of a Recipient to the Entity
	* representation
	* @param recipient - Domain representation
	* @return Entity representation
	*/
	public static EmailRecipientEntity convertToEntity(EmailRecipient<?> recipient, Email email) {	
		return EmailRecipientEntity
				.builder()
					.id(String.valueOf(recipient.getId()))
					.recipientType(recipient.getRecipientType())
					.emailAddress(recipient.getEmailAddress())
					.emailId(email.getId())
					.firstName(recipient.getFirstName())
					.viewed(recipient.isViewed())
				.build();
	}
	
	/**
	* Converts from Entity to Domain representation 
	* @param entity - To convert
	* @return converted
	*/
	public static EmailRecipient<?> convertFromEntity(EmailRecipientEntity entity) {
		
		EmailRecipient<?> recipient =  new EmailRecipient<String>(entity.getId(), entity.getRecipientType());
		
		recipient.setEmail(entity.getEmailAddress());
		recipient.setFirstName(entity.getFirstName());
		recipient.setViewed(entity.isViewed());
		
		return recipient;
	}
	
}
