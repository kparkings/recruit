package com.arenella.recruit.emailservice.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.arenella.recruit.emailservice.beans.Contact;

/**
* Entity representation of Recipient
* @author K Parkings
*/
@Entity
@Table(schema="email", name="contact")
public class ContactEntity {

	@EmbeddedId
	private ContactEntityPK id;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="email")
	private String email;
	
	/**
	* Default Constructor 
	*/
	public ContactEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - contains initialization values
	*/
	public ContactEntity(ContactEntityBuilder builder) {
		this.id 		= builder.id;
		this.firstName 	= builder.firstName;
		this.email 		= builder.email;
	}	
	
	/**
	* Returns the unique id of the Recipient
	* @return id of the Recipient
	*/
	public ContactEntityPK getId(){
		return this.id;
	}
	
	/**
	* Returns the firstName of the Recipient
	* @return name of the Recipient
	*/
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns the email address of the Recipient
	* @return email of the Recipient
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* Sets the Recipients First name
	* @param firstName - First name of the Recipient
	*/
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	* Sets the Recipients email
	* @param email - email of the Recipient
	*/
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	* Returns a Builder for the class
	* @return Builder
	*/
	public static ContactEntityBuilder builder() {
		return new ContactEntityBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class ContactEntityBuilder{
		
		private ContactEntityPK id;
		private String firstName;
		private String email;
		
		/**
		* Sets the unique id of the Recipient
		* @param id - id of the Recipient
		* @return Builder
		*/
		public ContactEntityBuilder id(ContactEntityPK id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the firstName of the Recipient
		* @param firstName - name of the Recipient
		* @return Builder
		*/
		public ContactEntityBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the email address of the Recipient
		* @param email - Email address of the Recipient
		* @return Builder
		*/
		public ContactEntityBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Instantiates and instance of the Entity
		* populated with the Builder values
		* @return Entity
		*/
		public ContactEntity build() {
			return new ContactEntity(this);
		}
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param contact - Domain representation
	* @return Entity representation
	*/
	public static ContactEntity convertToEntity(Contact contact) {
		return ContactEntity
				.builder()
					.email(contact.getEmail())
					.firstName(contact.getFirstName())
					.id(new ContactEntityPK(contact.getContactType(), contact.getId()))
				.build();
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - Entity representation
	* @return Domain representation
	*/
	public static Contact convertFromEntity(ContactEntity entity) {
		return new Contact(entity.getId().getContactId(), entity.getId().getContactType(), entity.getFirstName(), entity.getEmail());
	}
	
}