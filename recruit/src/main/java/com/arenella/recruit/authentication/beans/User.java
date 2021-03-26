package com.arenella.recruit.authentication.beans;

import java.util.LinkedHashSet;
import java.util.Set;

/**
* User for Authentication
* @author K Parkings
*/
public class User {

	public static enum USER_ROLE{admin, recruiter}
	
	private String			username;
	private String			password;
	private boolean			enabled;
	private Set<USER_ROLE> 	roles		= new LinkedHashSet<>();
	
	/**
	* Constructor based upon a builder
	* @param builder - contains initialization values
	*/
	public User(UserBuilder builder) {
		
		this.username 	= builder.username;
		this.password 	= builder.password;
		this.enabled	= builder.enabled;
		this.roles.addAll(builder.roles);
	
	}
	
	/**
	* Returns the username of the User
	* @return username of the User
	*/
	public String getUsername() {
		return this.username;
	}

	/**
	* returns the users password
	* @return users password
	*/
	public String getPassword() {
		return this.password;
	}
	
	/**
	* Returns whether or not the users account is
	* enabled 
	* @return whether the account is enabled
	*/
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	* Returns the roles the user has within the system
	* @return users roles
	*/
	public Set<USER_ROLE> getRoles(){
		return this.roles;
	}
	
	/**
	* Returns an instance of a builder for the User class
	* @return Builder for the User class
	*/
	public static UserBuilder builder() {
		return new UserBuilder();
	}
	
	/**
	* Builder for the User class
	* @author K Parkings
	*/
	public static class UserBuilder {
		
		private String			username;
		private String			password;
		private boolean			enabled;
		private Set<USER_ROLE> 	roles		= new LinkedHashSet<>();
		
		/**
		* Sets the username which is the unique id of the User 
		* @param username - users unsername
		* @return Builder
		*/
		public UserBuilder username(String username) {
			this.username = username;
			return this;
		}
		
		/**
		* Sets the password of the User
		* @param password - Users password
		* @return Builder
		*/
		public UserBuilder password(String password) {
			this.password = password;
			return this;
		}
		
		/**
		* Sets whether or not the User is enabled in the System
		* @param enabled - Whether the User is enabled
		* @return Builder
		*/
		public UserBuilder enabled(boolean enabled) {
			this.enabled = enabled;
			return this;
		}
		
		/**
		* Adds the Users roles
		* @param roles - Roles the User has been granted
		* @return Builder
		*/
		public UserBuilder roles(Set<USER_ROLE> roles) {
			this.roles.clear();
			this.roles.addAll(roles);
			return this;
		}
		
		/**
		* Initialized instance of a User based upon 
		* the values in the Builder
		* @return User
		*/
		public User build() {
			return new User(this);
		}
	}
	
}