package com.arenella.recruit.authentication.services;

import com.arenella.recruit.authentication.beans.User;
import com.arenella.recruit.authentication.enums.AccountType;

/**
* Defines Services for interaction with User Accounts
* @author K Parkings
*/
public interface AccountService {

	/**
	* Creates a new account for a user
	* @param proposedUsername - username if available
	* @return New user account
	*/
	public User createAccount(String proposedUsername, AccountType accountType);
	
}