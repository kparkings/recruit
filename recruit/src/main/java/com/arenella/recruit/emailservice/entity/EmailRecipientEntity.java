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
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.RecipientType;

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
	private RecipientType 		recipientType;
	
	@Column(name="email_address")
	private String 				emailAddress;
	
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
		this.emailAddress 	= builder.emailAddress;
	}
	
	public String getId() {
		return this.id;
	}
	
	public UUID getEmailId() {
		return this.emailId;
	}
	
	public RecipientType getRecipientType() {
		return this.recipientType;
	}
	
	public String getEmailAddress() {
		return this.emailAddress;
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
		private RecipientType 	recipientType;
		private String 			emailAddress;
		
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
		public EmailRecipientEntityBuilder recipientType(RecipientType recipientType) {
			this.recipientType = recipientType;
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
				.build();
	}
	
	public static EmailRecipient<?> convertFromEntity(EmailRecipientEntity entity) {
		return new EmailRecipient<String>(entity.getId(), entity.getRecipientType(), entity.getEmailAddress());
	}
	
}
