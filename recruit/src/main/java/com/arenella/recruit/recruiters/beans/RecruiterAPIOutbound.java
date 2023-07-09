package com.arenella.recruit.recruiters.beans;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.arenella.recruit.recruiters.beans.Recruiter.language;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;

/**
* API Outbound representation of a Recruiter
* @author K Parkings
*/
public class RecruiterAPIOutbound {

	private String 									userId;
	private String 									firstName;
	private String 									surname;
	private String 									email;
	private String									companyName;
	private String 									companyAddress;
	private String 									companyCountry;
	private String 									companyVatNumber;
	private String 									companyRegistrationNumber;
	private boolean 								active					= true;
	private language 								language;
	private Set<RecruiterSubscriptionAPIOutbound> 	subscriptions			= new LinkedHashSet<>();
	private boolean 								hasActiveSubscription 	= false;
	
	/**
	* Constuctor based upon a builder
	* @param builder - Contains initialization details
	*/
	public RecruiterAPIOutbound(RecruiterAPIOutboundBuilder builder) {
		
		this.userId 					= builder.userId;
		this.firstName 					= builder.firstName;
		this.surname 					= builder.surname;
		this.email 						= builder.email;
		this.companyName 				= builder.companyName;
		this.companyAddress				= builder.companyAddress;
		this.companyCountry				= builder.companyCountry;
		this.companyVatNumber			= builder.companyVatNumber;
		this.companyRegistrationNumber 	= builder.companyRegistrationNumber;
		this.active 					= builder.active;
		this.language 					= builder.language;
		
		this.subscriptions.clear();
		this.subscriptions.addAll(builder.subscriptions);
		
		this.hasActiveSubscription = this.subscriptions
			.stream()
			.filter(s -> s.isCurrentSubscription() 
						&& s.getStatus() != subscription_status.DISABLED_PENDING_PAYMENT 
						&& 	s.getStatus() != subscription_status.SUBSCRIPTION_ENDED 
						&& 	s.getStatus() != subscription_status.AWAITING_ACTIVATION)
			.count() > 0;
		
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
	* Returns the address of the Company
	* @return address
	*/
	public String getCompanyAddress() {
		return this.companyAddress;
	}
	
	/**
	* Returns the country where the Company is base
	* @return country
	*/
	public String getCompanyCountry() {
		return this.companyCountry;
	}
	
	/**
	* Returns the Companies VAT number
	* @return vat number
	*/
	public String getCompanyVatNumber() {
		return this.companyVatNumber;
	}
	
	/**
	* Returns the companies registration number
	* @return company registration number
	*/
	public String getCompanyRegistrationNumber() {
		return this.companyRegistrationNumber;
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
	* Returnss the subscriptions belonging to the recruiter
	* @return Recruiters subscriptions
	*/
	public Set<RecruiterSubscriptionAPIOutbound> getSubscriptions() {
		return this.subscriptions;
	}
	
	/**
	* Whether a recruiter has an active subscription. This will be false if 
	* the in one of the two situations
	* - Their subscription has been cancelled
	* - Their subscription has ended and not been renewed
	* @return
	*/
	public boolean getHasActiveSubscription() {
		return this.hasActiveSubscription;
	}

	/**
	* Returns a Builder for the Recruiter class
	* @return Builder for the Recruiter class
	*/
	public static RecruiterAPIOutboundBuilder builder() {
		return new RecruiterAPIOutboundBuilder();
	}
	
	/**
	* Builder for the Recruiter class 
	* @author K Parkings
	*/
	public static class RecruiterAPIOutboundBuilder{
		
		private String 									userId;
		private String 									firstName;
		private String 									surname;
		private String 									email;
		private String									companyName;
		private String 									companyAddress;
		private String 									companyCountry;
		private String 									companyVatNumber;
		private String 									companyRegistrationNumber;
		private boolean 								active			= true;
		private language 								language;
		private Set<RecruiterSubscriptionAPIOutbound> 	subscriptions	= new LinkedHashSet<>();
		
		/**
		* Sets the userId associated with the Recruiter
		* @param userId - Unique userId for the Recruiter
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets the firstname of the Recruiter
		* @param firstName - Firstname of the Recruiter
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the Surname of the Recruiter
		* @param surname - Surname of the Recruiter
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address for the Recruiter
		* @param email - Email address of the Recruiter
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the name of the Company the Recruiter works for 
		* @param companyName - Name of the Company the Recruiter works for
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder companyName(String companyName) {
			this.companyName = companyName;
			return this;
		}
		
		/**
		* Sets the address of the Company
		* @param companyAddress - Address of the Company
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder companyAddress(String companyAddress) {
			this.companyAddress = companyAddress;
			return this;
		}
		
		/**
		* Sets the Country where the Company is registered
		* @param companyCountry - Country
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder companyCountry(String companyCountry) {
			this.companyCountry = companyCountry;
			return this;
		}
		
		/**
		* Sets the VAT Number of the Compamy
		* @param companyVatNumber - VAT Number
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder companyVatNumber(String companyVatNumber) {
			this.companyVatNumber = companyVatNumber;
			return this;
		}
		
		/**
		* Sets the companies registration Number
		* @param companyRegistrationNumber - Registration Number 
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder companyRegistrationNumber(String companyRegistrationNumber) {
			this.companyRegistrationNumber = companyRegistrationNumber;
			return this;
		}
		
		/**
		* Sets whether or not the account for the Recruiter is active
		* @param active - Whether or not the account is active
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder active(boolean active) {
			this.active = active;
			return this;
		}
		
		/**
		* Sets the Language spoken by the Recruiter 
		* @param language - Language spoken by the Recruiter
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder language(language language) {
			this.language = language;
			return this;
		}
		
		/**
		* Sets the subscriptions associated with the Recruiter
		* @param subscriptions - Recruiters subscriptions
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder subscriptions(Set<RecruiterSubscriptionAPIOutbound> subscriptions) {
			this.subscriptions.clear();
			this.subscriptions.addAll(subscriptions);
			return this;
		}
		
		/**
		* Returns an instance of Recruiter initialized with the 
		* values in the builder
		* @return Initalized instance of Recruiter
		*/
		public RecruiterAPIOutbound build() {
			return new RecruiterAPIOutbound(this);
		}
		
	}
	
	/**
	* Converts a Domain representation of a Recruiter to a 
	* API Outbound representation
	* @param recruiter - Domain representation to convert
	* @return API Outbound version representation of the Recruiter
	*/
	public static RecruiterAPIOutbound convertFromDomain(Recruiter recruiter) {
		
		return RecruiterAPIOutbound
								.builder()
									.companyName(recruiter.getCompanyName())
									.companyAddress(recruiter.getCompanyAddress())
									.companyCountry(recruiter.getCompanyCountry())
									.companyVatNumber(recruiter.getCompanyVatNumber())
									.companyRegistrationNumber(recruiter.getCompanyRegistrationNumber())
									.email(recruiter.getEmail())
									.firstName(recruiter.getFirstName())
									.active(recruiter.isActive())
									.language(recruiter.getLanguage())
									.surname(recruiter.getSurname())
									.userId(recruiter.getUserId())
									.subscriptions(recruiter.getSubscriptions().stream().map(RecruiterSubscriptionAPIOutbound::convertFromSubscription).collect(Collectors.toCollection(LinkedHashSet::new)))
								.build();
	}
	
}