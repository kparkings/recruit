package com.arenella.recruit.authentication.controllers;

import java.io.Serializable;

/**
* Login details of user requesting a JWT token
* @author K Parkings
*/
public class JwtRequest implements Serializable{

	private static final long serialVersionUID = 7717344088587393400L;

	private String username;
	private String password;
	
	/**
	* Default constructor 
	*/
	public JwtRequest() {
		//Jackson
	}
	
	/**
	* Constructor
	* @param username - Username provided by the User
	* @param password - Password provided by the User
	*/
	public JwtRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	* Returns the username provided by the user for the login attempt
	* @return username provided by the User
	*/
	public String getUsername() {
		return username;
	}

	/**
	* Sets the username provided by the User for the login attempt
	* @param username provided by the User
	*/
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	* Returns the password provided by the User for the login attempt 
	* @return Password provided by the User
	*/
	public String getPassword() {
		return password;
	}

	/**
	* Sets the password provided by the User for the login attempt
	* @param password provided by the User
	*/
	public void setPassword(String password) {
		this.password = password;
	}
	
}