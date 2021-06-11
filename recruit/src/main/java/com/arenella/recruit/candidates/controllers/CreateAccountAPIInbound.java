package com.arenella.recruit.candidates.controllers;

/**
* Contains details of the new account to be created
* @author K Parkings
*/
public class CreateAccountAPIInbound {

	public static enum AccountType {RECRUITER} //TODO: Move to domain version

	private String 			proposedUsername;
	private AccountType 	accountType;

	//TODO: use Jackson builder annotations and remove this constructor
	@SuppressWarnings("unused")
	private CreateAccountAPIInbound() {}
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initalization values
	*/
	public CreateAccountAPIInbound(CreateAccountAPIInboundBuilder builder) {
		this.proposedUsername 	= builder.proposedUsername;
		this.accountType 		= builder.accountType;
	}
	
	/**
	* Returns the name requested for the Username. This may be different that the 
	* name used if the name requested already exists or is unsuitable
	* @return prefered username
	*/
	public String getProposedUsername() {
		return this.proposedUsername;
	}
	
	/**
	* Returns the type of account to create
	* @return  type of account
	*/
	public AccountType getAccountType() {
		return this.accountType;
	}
	
	/**
	* Returns a builder for the CreateAccountAPIInbound class
	* @return Builder
	*/
	public static CreateAccountAPIInboundBuilder builder() {
		return new CreateAccountAPIInboundBuilder();
	}
	
	/**
	* Builder for the CreateAccountAPIInbound class
	* @author K Parkings
	*/
	public static class CreateAccountAPIInboundBuilder{
	
		private String 			proposedUsername;
		private AccountType 	accountType;
		
		/**
		* Sets the proposed username. This may be different that the actual username 
		* for the account if the username is already in use or unsuitable
		* @param proposedUsername - requested username
		* @return Builder
		*/
		public CreateAccountAPIInboundBuilder proposedUsername(String proposedUsername) {
			this.proposedUsername = proposedUsername;
			return this;
		}
		
		/**
		* Sets the type of Account to create
		* @param accountType - Type of account to create
		* @return Builder
		*/
		public CreateAccountAPIInboundBuilder accountType(AccountType accountType) {
			this.accountType = accountType;
			return this;
		}
		
		/**
		* Returns an instance of CreateAccountAPIInbound initialized with the values
		* in the builder
		* @return new initialized instance
		*/
		public CreateAccountAPIInbound build() {
			return new CreateAccountAPIInbound(this);
		}
		
	}
	
}