
package com.arenella.recruit.recruiters.beans;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
* Class represents a Recruiter 
* @author K Parkings
*/
public class Recruiter {

	public static enum language {ENGLISH, DUTCH, FRENCH}
	
	private String 						userId;
	private String 						firstName;
	private String 						surname;
	private String 						email;
	private String						companyName;
	private boolean 					active;
	private language 					language;
	private LocalDate					accountCreated;
	private Set<RecruiterSubscription> 	subscriptions 		= new LinkedHashSet<>();
	
	/**
	* Constuctor based upon a builder
	* @param builder - Contains initialization details
	*/
	public Recruiter(RecruiterBuilder builder) {
		
		this.userId 			= builder.userId;
		this.firstName 			= builder.firstName;
		this.surname 			= builder.surname;
		this.email 				= builder.email;
		this.companyName 		= builder.companyName;
		this.active 			= builder.active;
		this.language 			= builder.language;
		this.accountCreated 	= builder.accountCreated;
		
		this.subscriptions.addAll(builder.subscriptions);
		
	}
	
	/**
	* Returns the recruiters loginId
	* @return Unique login for the Recruiter
	*/
	public String getUserId() {
		return userId;
	}
	
	/**
	* Returns the firstname of the Recruiter
	* @return Recruiters first name
	*/
	public String getFirstName() {
		return firstName;
	}
	
	/**
	* Returns the surname of the Recruiter
	* @return Recruiters Surname
	*/
	public String getSurname() {
		return surname;
	}
	
	/**
	* Returns the Recruiters email address
	* @return Email address of the Recruiter
	*/
	public String getEmail() {
		return email;
	}
	
	/**
	* Returns the name of the recruitment company the 
	* Recruiter works for
	* @return
	*/
	public String getCompanyName() {
		return companyName;
	}
	
	/**
	* Returns whether or not the Recruiter account is 
	* active
	* @return whether the account is active
	*/
	public boolean isActive(){
		return active;
	}
	
	/**
	* Returns the language spoken by the Recruiter
	* @return language spoken by the recruiter
	*/
	public language getLanguage(){
		return language;
	}

	/**
	* Returns the date the recruiters account was created
	* @return
	*/
	public LocalDate getAccountCreated() {
		return accountCreated;
	}
	
	/**
	* Returns a Builder for the Recruiter class
	* @return Builder for the Recruiter class
	*/
	public static RecruiterBuilder builder() {
		return new RecruiterBuilder();
	}
	
	/**
	* Disables the Recruiters account 
	*/
	public void disableAccount() {
		this.active = false;
	}
	
	/**
	* Sets the uniqueId of the User
	* @param userId - Unique Id of the User
	*/
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	* Returns the Subscriptions associated with the Recruiter
	* @return Recruiters subscriptions
	*/
	public Set<RecruiterSubscription> getSubscriptions(){
		return this.subscriptions;
	}
	
	/**
	* Activated the Recruiters account. If this is the 
	* first time the account has been activated (I.e a new Recruiter) 
	* it will also set the accountCreated to the current date
	*/
	public void activateAccount() {
		
		if (this.accountCreated == null) {
			this.accountCreated = LocalDate.now();
		}
		
		this.active = true;
		
	}
	
	/**
	* Builder for the Recruiter class 
	* @author K Parkings
	*/
	public static class RecruiterBuilder{
		
		private String 						userId;
		private String 						firstName;
		private String 						surname;
		private String 						email;
		private String						companyName;
		private boolean 					active;
		private language 					language;
		private LocalDate					accountCreated;
		private Set<RecruiterSubscription> 	subscriptions 		= new LinkedHashSet<>();
		
		/**
		* Sets the userId associated with the Recruiter
		* @param userId - Unique userId for the Recruiter
		* @return Builder
		*/
		public RecruiterBuilder userId(String userId) {
			this.userId = userId.toLowerCase().trim();
			return this;
		}
		
		/**
		* Sets the firstname of the Recruiter
		* @param firstName - Firstname of the Recruiter
		* @return Builder
		*/
		public RecruiterBuilder firstName(String firstName) {
			this.firstName = firstName.toLowerCase().trim();
			return this;
		}
		
		/**
		* Sets the Surname of the Recruiter
		* @param surname - Surname of the Recruiter
		* @return Builder
		*/
		public RecruiterBuilder surname(String surname) {
			this.surname = surname.toLowerCase().trim();
			return this;
		}
		
		/**
		* Sets the Email address for the Recruiter
		* @param email - Email address of the Recruiter
		* @return Builder
		*/
		public RecruiterBuilder email(String email) {
			this.email = email.toLowerCase().trim();
			return this;
		}
		
		/**
		* Sets the name of the Company the Recruiter works for 
		* @param companyName - Name of the Company the Recruiter works for
		* @return Builder
		*/
		public RecruiterBuilder companyName(String companyName) {
			this.companyName = companyName.toLowerCase().trim();
			return this;
		}
		
		/**
		* Sets whether or not the account for the Recruiter is active
		* @param active - Whether or not the account is active
		* @return Builder
		*/
		public RecruiterBuilder active(boolean active) {
			this.active = active;
			return this;
		}
		
		/**
		* Sets the Language spoken by the Recruiter 
		* @param language - Language spoken by the Recruiter
		* @return Builder
		*/
		public RecruiterBuilder language(language language) {
			this.language = language;
			return this;
		}
		
		/**
		* Sets the Data the Recruiter was created
		* @param accountCreated - Date account was created
		* @return Builder
		*/
		public RecruiterBuilder accountCreated(LocalDate accountCreated) {
			this.accountCreated = accountCreated;
			return this;
		}
		
		/**
		* Sets the subscriptions associated with the Recruiter
		* @param subscriptions - Recruiters subscriptions
		* @return Builder
		*/
		public RecruiterBuilder subscriptions(Set<RecruiterSubscription> subscriptions) {
			this.subscriptions.clear();
			this.subscriptions.addAll(subscriptions);
			return this;
		}
		
		/**
		* Returns an instance of Recruiter initialized with the 
		* values in the builder
		* @return Initalized instance of Recruiter
		*/
		public Recruiter build() {
			return new Recruiter(this);
		}
		
	}
	
}