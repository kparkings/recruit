package com.arenella.recruit.emailservice.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.arenella.recruit.emailservice.beans.Recipient;

/**
* Entity representation of Recipient
* @author K Parkings
*/
@Entity
@Table(schema="email", name="recipient")
public class RecipientEntity {

	@EmbeddedId
	private RecipientEntityPK id;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="email")
	private String email;
	
	/**
	* Default Constructor 
	*/
	public RecipientEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - contains initialization values
	*/
	public RecipientEntity(RecipientEntityBuilder builder) {
		this.id 		= builder.id;
		this.firstName 	= builder.firstName;
		this.email 		= builder.email;
	}	
	
	/**
	* Returns the unique id of the Recipient
	* @return id of the Recipient
	*/
	public RecipientEntityPK getId(){
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
	* Returns a Builder for the class
	* @return Builder
	*/
	public static RecipientEntityBuilder builder() {
		return new RecipientEntityBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class RecipientEntityBuilder{
		
		private RecipientEntityPK id;
		private String firstName;
		private String email;
		
		/**
		* Sets the unique id of the Recipient
		* @param id - id of the Recipient
		* @return Builder
		*/
		public RecipientEntityBuilder id(RecipientEntityPK id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the firstName of the Recipient
		* @param firstName - name of the Recipient
		* @return Builder
		*/
		public RecipientEntityBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the email address of the Recipient
		* @param email - Email address of the Recipient
		* @return Builder
		*/
		public RecipientEntityBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Instantiates and instance of the Entity
		* populated with the Builder values
		* @return Entity
		*/
		public RecipientEntity build() {
			return new RecipientEntity(this);
		}
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param recipient - Domain representation
	* @return Entity representation
	*/
	public static RecipientEntity convertToEntity(Recipient recipient) {
		return RecipientEntity
				.builder()
					.email(recipient.getEmail())
					.firstName(recipient.getFirstName())
					.id(new RecipientEntityPK(recipient.getRecipientType(), recipient.getId()))
				.build();
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - Entity representation
	* @return Domain representation
	*/
	public static Recipient convertFromEntity(RecipientEntity entity) {
		return new Recipient(entity.getId().getRecipientId(), entity.getId().getRecipientType(), entity.getFirstName(), entity.getEmail());
	}
	
}