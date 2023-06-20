package com.arenella.recruit.authentication.beans;

import java.time.LocalDateTime;

/**
* Event for a successfully authenticated User
* @author Hp
*/
public class AuthenticatedEvent {
	
	private final String 		userId;
	private final boolean 		recruiter;
	private final boolean 		candidate;
	private final LocalDateTime loggedInAt;
	
	/**
	* Constructor
	* @param userId		- Id of user logged in
	* @param recruiter	- If User is a recruiter
	* @param candidate	- If User is a candidate
	* @param loggedInAt - Date/Time User logged in
	*/
	public AuthenticatedEvent(String userId, boolean recruiter, boolean candidate, LocalDateTime loggedInAt) {
		this.userId 		= userId;
		this.recruiter 		= recruiter;
		this.candidate 		= candidate;
		this.loggedInAt 	= loggedInAt;
	}
	
	/**
	* Returns the Id of the User that logged in 
	* @return Unique user Id
	*/
	public String getUserId() {
		return this.userId;
	}
	
	/**
	* Returns whether the User is a Recruiter
	* @return whether the User is a Recruiter
	*/
	public boolean isRecruiter() {
		return this.recruiter;
	}
	
	/**
	* Returns whether the User is a Candidate
	* @return whether the User is a Candidate
	*/
	public boolean isCandidate() {
		return this.candidate;
	}

	/**
	* Returns the moment the User loggedIn
	* @return when the User logged in
	*/
	public LocalDateTime getLoggedInAt() {
		return this.loggedInAt;
	}
	
}