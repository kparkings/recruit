package com.arenella.recruit.authentication.services;

import java.util.Set;

import com.arenella.recruit.authentication.beans.User;
import com.arenella.recruit.authentication.beans.User.USER_ROLE;
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
	
	/**
	* Creates a new account for a User where and encrypted password has already been 
	* provided
	* @param username			- Users id
	* @param encryptedPassword  - Encrypted version of password for the User
	* @return
	*/
	public void createAccount(String username, String encryptedPassword, AccountType accountType);
	
	/**
	* Switches the type of Account a User is associated with.
	* @param userId - Unique Id of the User 
	* @param roles 	- Replacement roles for the uSer
	*/
	public void replaceRolesForUser(String userId, Set<USER_ROLE> roles);
	
}
