package com.arenella.recruit.candidates.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.arenella.recruit.candidates.beans.Contact;
import com.arenella.recruit.candidates.beans.Contact.CONTACT_TYPE;

/**
* Contact represents a contact other than the Candidate themselves. 
* This could be for example a Recruiter associated with a Candidate
* who is offering the Candidate
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="contacts")
public class RecruiterContactEntity {

	@EmbeddedId
	RecruiterContactEntityPK id = new RecruiterContactEntityPK();
	
	@Column(name="email")
	private String 			email;
	
	@Column(name="firstname")
	private String 			firstname;
	
	@Column(name="surname")
	private String 			surname;
	
	public RecruiterContactEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - contains initialization values
	*/
	public RecruiterContactEntity(RecruiterContactEntityBuilder builder) {
		this.id.contactType 	= builder.contactType;
		this.id.userId			= builder.userId;
		this.email 				= builder.email;
		this.firstname 			= builder.firstname;
		this.surname 			= builder.surname;
	}
	
	/**
	* Returns the type of the Contact
	* @return Contact Type
	*/
	public CONTACT_TYPE getContactType() {
		return this.id.getContactType();
	}
	
	/**
	* Returns the unique id of the Contact
	* @return userId
	*/
	public String getUserId() {
		return this.id.getUserId();
	}
	
	/**
	* Returns the Contact's email address
	* @return email address
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* Returns the Contact's firstname
	* @return firstname
	*/
	public String getFirstname() {
		return this.firstname;
	}
	
	/**
	* Returns the Contact's surname
	* @return surname
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* 
	* @return Builder
	*/
	public static RecruiterContactEntityBuilder builder() {
		return new RecruiterContactEntityBuilder();
	}
	
	/**
	* Builder for the Class
	* @author K Parkings
	*/
	public static class RecruiterContactEntityBuilder{
		
		private CONTACT_TYPE 	contactType;
		private String 			userId;
		private String 			email;
		private String 			firstname;
		private String 			surname;
		
		/**
		* Sets the type of the Contact
		* @param contactType - type
		* @return Builder
		*/
		public RecruiterContactEntityBuilder contactType(CONTACT_TYPE contactType) {
			this.contactType = contactType;
			return this;
		}
		
		/**
		* Sets the Unique id of the Contact
		* @param userId = user Id
		* @return Builder
		*/
		public RecruiterContactEntityBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets the Email of the Contact
		* @param email - email address
		* @return Builder
		*/
		public RecruiterContactEntityBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the firstname of the Contract
		* @param firstname - firstname
		* @return Builder
		*/
		public RecruiterContactEntityBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the surname of the Contact
		* @param surname - surname of the Contact
		* @return Builder
		*/
		public RecruiterContactEntityBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Creates an instance of the class initalized with the values
		* in the Builder
		* @return Initalized Entity
		*/
		public RecruiterContactEntity build() {
			return new RecruiterContactEntity(this);
		}
	}
	
	/**
	* PK Class
	* @author K Parkings
	*/
	public static class RecruiterContactEntityPK implements Serializable{

		private static final long serialVersionUID = -2158286902074105361L;
		
		@Enumerated(EnumType.STRING)
		@Column(name="contact_type")
		private CONTACT_TYPE 	contactType;
		private String			userId;
		
		/**
		* Default Constructor 
		*/
		public RecruiterContactEntityPK() {
			//Hibernate
		}
		
		/**
		* Constructor
		* @param contactType - Type of Contact
		* @param userId   - Unique ID of contact in scope of ContactType
		*/
		public RecruiterContactEntityPK(CONTACT_TYPE contactType, String userId) {
			this.contactType 	= contactType;
			this.userId 		= userId;
		}
		
		/**
		* Returns type ContactType
		* @return type of Contact
		*/
		public CONTACT_TYPE getContactType() {
			return this.contactType;
		}
		
		/**
		* Returns id of Contact
		* @return id of Contact
		*/
		public String getUserId() {
			return this.userId;
		}
		
	}

	/**
	* Converts from Entity to Domain representation
	* @param contactEntity - Entity representation
	* @return Domain representation
	*/
	public static Contact convertFromEntity(RecruiterContactEntity contactEntity) {
		return Contact
				.builder()
					.contactType(contactEntity.getContactType())
					.userId(contactEntity.getUserId())
					.email(contactEntity.getEmail())
					.firstname(contactEntity.getFirstname())
					.surname(contactEntity.getSurname())
				.build();
	}
	
}