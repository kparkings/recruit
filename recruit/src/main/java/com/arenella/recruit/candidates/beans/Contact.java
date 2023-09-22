package com.arenella.recruit.candidates.beans;

/**
* Domain represenation of Contact
* @author K Parkings
*/
public class Contact {

	public enum CONTACT_TYPE {RECRUITER}
	
	private CONTACT_TYPE 	contactType;
	private String			userId;
	private String 			email;
	private String 			firstname;
	private String 			surname;

	/**
	* Default constructor 
	*/
	public Contact() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - contains initialization values
	*/
	public Contact(ContactBuilder builder) {
		this.contactType 	= builder.contactType;
		this.userId			= builder.userId;
		this.email 			= builder.email;
		this.firstname 		= builder.firstname;
		this.surname 		= builder.surname;
	}
	
	/**
	* Returns the type of the Contact
	* @return Contact Type
	*/
	public CONTACT_TYPE getContactType() {
		return this.contactType;
	}
	
	/**
	* Returns the unique id of the Contact
	* @return userId
	*/
	public String getUserId() {
		return this.userId;
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
	public static ContactBuilder builder() {
		return new ContactBuilder();
	}
	
	/**
	* Builder for the Class
	* @author K Parkings
	*/
	public static class ContactBuilder{
		
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
		public ContactBuilder contactType(CONTACT_TYPE contactType) {
			this.contactType = contactType;
			return this;
		}
		
		/**
		* Sets the Unique id of the Contact
		* @param userId = user Id
		* @return Builder
		*/
		public ContactBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets the Email of the Contact
		* @param email - email address
		* @return Builder
		*/
		public ContactBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the firstname of the Contract
		* @param firstname - firstname
		* @return Builder
		*/
		public ContactBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the surname of the Contact
		* @param surname - surname of the Contact
		* @return Builder
		*/
		public ContactBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Creates an instance of the class initalized with the values
		* in the Builder
		* @return Initalized Entity
		*/
		public Contact build() {
			return new Contact(this);
		}
	}

}