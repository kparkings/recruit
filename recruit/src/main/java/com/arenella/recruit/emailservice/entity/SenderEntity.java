package com.arenella.recruit.emailservice.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;

@Entity
@Table(schema="email", name="email_sender")
public class SenderEntity {

	@Id
	@Column(name="id")
	private String 				id;
	
	@Column(name="email_id")
	private UUID 				emailId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="sender_type")
	private SenderType 			senderType;
	
	@Column(name="email_address")
	private String 				emailAddress;
	
	/**
	* Constructor
	*/
	public SenderEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public SenderEntity(SenderEntityBuilder builder) {
		this.id 			= builder.id;
		this.emailId 		= builder.emailId;
		this.senderType 	= builder.senderType;
		this.emailAddress 	= builder.emailAddress;
	}
	
	/**
	* Returns the unique Id of the Sender
	* @return Senders id
	*/
	public String getId(){
		return this.id;
	}
	
	/**
	* Returns the unique id of the email
	* @return Email Id
	*/
	public UUID getEmailId() {
		return this.emailId;
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
	public String getEmailAddress() {
		return this.emailAddress;
	}
	
	/**
	* Returns a Builder for the SenderEntity class
	* @return Builder
	*/
	public static SenderEntityBuilder builder() {
		return new SenderEntityBuilder();
	}
	
	/**
	* Builder for the SenderEntity class
	* @author K Parkings
	*/
	public static class SenderEntityBuilder{
		
		private String 			id;
		private UUID 			emailId;
		private SenderType 		senderType;
		private String 			emailAddress;
		
		/**
		* Sets the Recipients Id
		* @param id - Unique Id of the Recipient
		* @return Builder
		*/
		public SenderEntityBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the id of the Email the Recipient is associated with
		* @param emailId - Unique Id of Email
		* @return Builder
		*/
		public SenderEntityBuilder emailId(UUID emailId) {
			this.emailId = emailId;
			return this;
		}
		
		/**
		* Sets the type of Sender
		* @param senderType - Senders type
		* @return Builder
		*/
		public SenderEntityBuilder senderType(SenderType senderType) {
			this.senderType = senderType;
			return this;
		}
		
		/**
		* Sets the Recipients email address
		* @param emailAddress - email address
		* @return Builder
		*/
		public SenderEntityBuilder emailAddress(String emailAddress) {
			this.emailAddress = emailAddress; 
			return this;
		}
		
		/**
		* Returns initialized instance of a SenderEntity
		* @return SenderEntity
		*/
		public SenderEntity build() {
			return new SenderEntity(this);
		}
		
	}

	/**
	* Converts a Domain representation of a Sender to an Entity representation
	* @param sender - Domain representation of a Sender
	* @return Entity representation of a Sender
	*/
	public static SenderEntity convertToEntity(Sender<?> sender, Email email) {
		return SenderEntity
				.builder()
					.emailAddress(sender.getEmail())
					.emailId(email.getId())
					.id(String.valueOf(sender.getId()))
					.senderType(sender.getSenderType())
				.build();
	}
	
	/**
	* Converts an Entity representation of an Email to a Domain representation
	* @param sender - Emtity representation
	* @return Domain representation
	*/
	public static Sender<String> convertFromEntity(SenderEntity sender) {
		return new Sender<String>(sender.getId().toString(), sender.getSenderType(), sender.getEmailAddress());
	}
	
}