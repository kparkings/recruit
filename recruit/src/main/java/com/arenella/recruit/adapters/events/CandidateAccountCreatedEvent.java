package com.arenella.recruit.adapters.events;

/**
* Event representing the creation of a new
* Candidate Account
* @author K Parkings
*/
public class CandidateAccountCreatedEvent {

	final String candidateId;
	final String encryptedPassword;
	
	/**
	* Constructor
	* @param candidateId		- Unique id of the Candidate
	* @param encryptedPassword	- Encrypted version of Candidates password
	*/
	public CandidateAccountCreatedEvent(String candidateId, String encryptedPassword) {
		this.candidateId 		= candidateId;
		this.encryptedPassword 	= encryptedPassword;
	}
	
	/**
	* Returns the unique id of the Candidate
	* @return id of the Candidate
	*/
	public String getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns the encrypted version of the Candidates Password
	* @return encrypted Password
	*/
	public String getEncryptedPassword() {
		return this.encryptedPassword;
	}
	
}
