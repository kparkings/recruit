package com.arenella.recruit.campaigns.entities;

import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Entity implementation of a Contact 
*/
@Entity
@Table(schema="campaigns", name="contacts")
public class CampaignContactEntity {

	@Id
	@Column(name="id")
	private String 				id; 
	
	@Column(name="first_name")
	private String 				firstName; 
	
	@Column(name="surname")
	private String 				surname; 
	
	@Column(name="email")
	private String 				email; 
	
	@Column(name="subscription_type")
	@Enumerated(EnumType.STRING)
	private SubscriptionType 	subscriptionType;
	
	/**
	* Default constructor 
	*/
	public CampaignContactEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a builder 
	* @param builder - contains initialization values
	*/
	public CampaignContactEntity(CampaignContactEntityBuilder builder) {
		this.id 				= builder.id; 
		this.firstName 			= builder.firstName; 
		this.surname 			= builder.surname; 
		this.email 				= builder.email; 
		this.subscriptionType 	= builder.subscriptionType;
	}
	
	/**
	* Returns the Id of the contact. This is the users name/id
	* @return Id of the contact
	*/
	public String getId() {
		return this.id;
	} 
	
	/**
	* Returns the first name of the contact
	* @return First name
	*/
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns the surname of the contact
	* @return surname
	*/
	public String getSurname() {
		return this.surname;
	} 
	
	/**
	* Returns the contacts email address
	* @return email address of the contact
	*/
	public String getEmail() {
		return this.email;
	} 
	
	/**
	* Returns the type of subscription the contact 
	* currently has
	* @return subscription type
	*/
	public SubscriptionType getSubscriptionType() {
		return this.subscriptionType;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder for the class
	*/
	public static CampaignContactEntityBuilder builder() {
		return new CampaignContactEntityBuilder();
	}
	
	/**
	* Builder for the class
	*/
	public static class CampaignContactEntityBuilder {
		
		private String 				id; 
		private String 				firstName; 
		private String 				surname; 
		private String 				email; 
		private SubscriptionType 	subscriptionType;
		
		/**
		* Sets the Contact's unique Id. This is also their 
		* Username
		* @param id - Unique id of the Contact
		* @return Builder
		*/
		public CampaignContactEntityBuilder id(String id) {
			this.id = id;
			return this;
		} 
		
		/**
		* Sets the firstName of the Contact
		* @param firstName - firstnName
		* @return Builder
		*/
		public CampaignContactEntityBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		} 
		
		/**
		* Sets the surname of the Contact
		* @param surname - Contact's surname
		* @return Builder
		*/
		public CampaignContactEntityBuilder surname(String surname) {
			this.surname = surname;
			return this;
		} 
		
		/**
		* Sets the email of the Contact
		* @param email - Contact's email
		* @return Builder
		*/
		public CampaignContactEntityBuilder email(String email) {
			this.email = email;
			return this;
		} 
		
		/**
		* Sets the contacts active subscription type 
		* @param subscriptionType - type of subscription
		* @return Builder
		*/
		public CampaignContactEntityBuilder subscriptionType(SubscriptionType subscriptionType) {
			this.subscriptionType = subscriptionType;
			return this;
		}
		
		/**
		* Returns an initialized instance
		* @return Initialized instance
		*/
		public CampaignContactEntity build() {
			return new CampaignContactEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - To convert
	* @return Converted
	*/
	public static Contact fromEntity(CampaignContactEntity entity) {
		return new Contact(entity.getId(), entity.getFirstName(), entity.getSurname(), entity.getEmail(), entity.getSubscriptionType());
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param contact - To be converted
	* @return Converted
	*/
	public static CampaignContactEntity toEntity(Contact contact) {
		return CampaignContactEntity
				.builder()
					.id(contact.id())
					.firstName(contact.firstName())
					.surname(contact.surname())
					.email(contact.email())
					.subscriptionType(contact.subscriptionType())
				.build();
	}
	
}