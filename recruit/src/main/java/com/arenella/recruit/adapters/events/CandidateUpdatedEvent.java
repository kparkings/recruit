package com.arenella.recruit.adapters.events;

public class CandidateUpdatedEvent {

	private final String candidateId;
	private final String firstName;
	private final String surname;
	private final String email;
	
	/**
	* Constructor
	* @param candidateId - id of deleted Candidate
	*/
	public CandidateUpdatedEvent(String candidateId, String firstName, String surname, String email) {
		this.candidateId 	= candidateId;
		this.firstName 		= firstName;
		this.surname 		= surname;
		this.email 			= email;
	}
	
	/**
	* Returns the Id of the deleted Candidate
	* @return id of the deleted Candidate
	*/
	public String getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns the Candidates first name
	* @return first name
	*/
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns the candidates Surname
	* @return candidates surname
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns the Candidates email
	* @return email address
	*/
	public String getEmail() {
		return this.email;
	}
	
}