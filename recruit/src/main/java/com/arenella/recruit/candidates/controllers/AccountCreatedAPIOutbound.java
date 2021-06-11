package com.arenella.recruit.candidates.controllers;

import com.arenella.recruit.authentication.beans.User;

/**
* Returns the username and password 
* of a newly created account
* @author K Parkings
*/
public class AccountCreatedAPIOutbound {

	private String username;
	private String password;
	
	/**
	* Constructor based upon a Builder
	* @param builder Contains initalization values
	*/
	public AccountCreatedAPIOutbound(AccountCreatedAPIOutboundBuilder builder) {
		this.username = builder.username;
		this.password = builder.password;
	}
	
	/**
	* Returns the username for the new account
	* @return Username
	*/
	public String getUsername() {
		return username;
	}
	
	/**
	* Returns the password for the account
	* @return Password for the account
	*/
	public String getPassword() {
		return password;
	}
	
	/**
	* Returns a Builder for the AccountCreatedAPIOutbound Class
	* @return builder
	*/
	public static AccountCreatedAPIOutboundBuilder builder() {
		return new AccountCreatedAPIOutboundBuilder();
	}
	
	/**
	* Builder for the AccountCreatedAPIOutbound class
	* @author K Parkings
	*/
	public static class AccountCreatedAPIOutboundBuilder {
		
		private String username;
		private String password;
		
		/**
		* Sets the username for the new account
		* @param username Username for the new account
		* @return Builder
		*/
		public AccountCreatedAPIOutboundBuilder username(String username) {
			this.username = username;
			return this;
		}
		
		/**
		* Sets the password for the new account
		* @param password Password for the new account
		* @return Builder
		*/
		public AccountCreatedAPIOutboundBuilder password(String password) {
			this.password = password;
			return this;
		}
		
		/**
		* Returns an instance of AccountCreatedAPIOutbound initialized 
		* with the values in the Builder
		* @return AccountCreatedAPIOutbound
		*/
		public AccountCreatedAPIOutbound build() {
			return new AccountCreatedAPIOutbound(this);
		}
		
	}
	
	/**
	* Converts the domain representation of a User to 
	* the AccountCreatedAPIOutbound representation
	* @param user domain version of user to convert
	* @return API Outbound representation of the user
	*/
	public static AccountCreatedAPIOutbound convertFromUser(User user) {
		
		return AccountCreatedAPIOutbound
									.builder()
										.username(user.getUsername())
										.password(user.getPassword())
									.build();
		
	}
	
}